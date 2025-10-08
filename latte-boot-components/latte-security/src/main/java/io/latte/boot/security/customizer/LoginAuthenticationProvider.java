package io.latte.boot.security.customizer;

import io.latte.boot.security.AuthenticationService;
import io.latte.boot.security.exception.ValidateCodeException;
import io.latte.boot.web.http.response.ApiResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * LoginAuthenticationProvider
 *
 * @author : wugz
 * @since : 2022/9/20
 */
public final class LoginAuthenticationProvider extends DaoAuthenticationProvider {
  private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
  private final PasswordEncoder passwordEncoder;
  private volatile String userNotFoundEncodedPassword;

  /**
   * 构造函数
   *
   * @param authenticationService authenticationService
   */
  public LoginAuthenticationProvider(AuthenticationService authenticationService) {
    super(authenticationService);
    this.passwordEncoder = authenticationService.passwordEncoder();
    setPasswordEncoder(this.passwordEncoder);
  }

  @Override
  public final Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Assert.isInstanceOf(LoginAuthenticationToken.class, authentication,
        () -> this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
            "Only LoginAuthenticationToken is supported"));
    String username = determineUsername(authentication);
    boolean cacheWasUsed = true;
    UserDetails user = this.getUserCache().getUserFromCache(username);
    if (user == null) {
      cacheWasUsed = false;
      try {
        user = retrieveUser((LoginAuthenticationToken) authentication);
      } catch (UsernameNotFoundException ex) {
        this.logger.debug("Failed to find user '" + username + "'");
        if (!this.hideUserNotFoundExceptions) {
          throw ex;
        }
        throw new BadCredentialsException(this.messages
            .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
      }
      Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
    }
    try {
      getPreAuthenticationChecks().check(user);
      additionalAuthenticationChecks(user, (LoginAuthenticationToken) authentication);
    } catch (AuthenticationException ex) {
      if (!cacheWasUsed) {
        throw ex;
      }
      // There was a problem, so try again after checking
      // we're using latest data (i.e. not from the cache)
      cacheWasUsed = false;
      user = retrieveUser(username, (LoginAuthenticationToken) authentication);
      getPreAuthenticationChecks().check(user);
      additionalAuthenticationChecks(user, (LoginAuthenticationToken) authentication);
    }
    getPostAuthenticationChecks().check(user);
    if (!cacheWasUsed) {
      getUserCache().putUserInCache(user);
    }
    Object principalToReturn = user;
    if (isForcePrincipalAsString()) {
      principalToReturn = user.getUsername();
    }
    return createSuccessAuthentication(principalToReturn, authentication, user);
  }

  private UserDetails retrieveUser(LoginAuthenticationToken authentication)
      throws AuthenticationException {
    prepareTimingAttackProtection();
    try {
      UserDetails loadedUser = ((AuthenticationService) this.getUserDetailsService()).loadUser(authentication.getLoginCommand());
      if (loadedUser == null) {
        throw new InternalAuthenticationServiceException(
            "UserDetailsService returned null, which is an interface contract violation");
      }
      return loadedUser;
    } catch (UsernameNotFoundException ex) {
      mitigateAgainstTimingAttack(authentication);
      throw ex;
    } catch (InternalAuthenticationServiceException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
    }
  }

  private void additionalAuthenticationChecks(UserDetails userDetails, LoginAuthenticationToken authentication) throws AuthenticationException {
    super.additionalAuthenticationChecks(userDetails, authentication);

    /* 校验码校验等 */
    final ApiResponse<Boolean> apiResponse = ((AuthenticationService) this.getUserDetailsService())
        .loginAuthenticationAdditionalChecks(authentication.getLoginCommand());
    if (!apiResponse.isSuccess()) {
      throw new InternalAuthenticationServiceException(
          apiResponse.getMsg(),
          new ValidateCodeException(apiResponse.getStatus(), apiResponse.getMsg()));
    }
  }

  private String determineUsername(Authentication authentication) {
    return (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
  }

  private void prepareTimingAttackProtection() {
    if (this.userNotFoundEncodedPassword == null) {
      this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
    }
  }

  private void mitigateAgainstTimingAttack(LoginAuthenticationToken authentication) {
    if (authentication.getCredentials() != null) {
      String presentedPassword = authentication.getCredentials().toString();
      this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
    }
  }
}
