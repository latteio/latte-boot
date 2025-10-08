package io.latte.boot.security;

import io.latte.boot.security.captcha.CaptchaCode;
import io.latte.boot.security.captcha.CaptchaStrategy;
import io.latte.boot.security.captcha.CaptchaStrategyFactory;
import io.latte.boot.security.customizer.LoginAuthenticationProvider;
import io.latte.boot.security.customizer.LoginAuthenticationToken;
import io.latte.boot.security.token.OpaqueToken;
import io.latte.boot.security.token.OpaqueTokenSimplified;
import io.latte.boot.security.token.OpaqueTokenTemplate;
import io.latte.boot.security.util.EncryptUtils;
import io.latte.boot.support.validate.Validate;
import io.latte.boot.web.context.UserContext;
import io.latte.boot.web.http.login.LoginCodeCommand;
import io.latte.boot.web.http.login.LoginCommand;
import io.latte.boot.web.http.message.Constants;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.message.MessageUtils;
import io.latte.boot.web.http.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

/**
 * AuthenticationService
 *
 * @author : wugz
 * @since : 2021/8/3
 */
public abstract class AuthenticationService implements UserDetailsService, InitializingBean {
  private static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
  private final PasswordEncoder passwordEncoder;
  private OpaqueTokenTemplate opaqueTokenTemplate;

  /**
   * 构造函数
   *
   * @param opaqueTokenTemplate
   */
  public AuthenticationService(OpaqueTokenTemplate opaqueTokenTemplate) {
    this.opaqueTokenTemplate = opaqueTokenTemplate;
    this.passwordEncoder = EncryptUtils.passwordEncoder();
  }

  /**
   * afterPropertiesSet
   *
   * @throws Exception
   */
  public void afterPropertiesSet() throws Exception {
  }

  /**
   * getPasswordEncoder
   *
   * @return
   */
  public PasswordEncoder passwordEncoder() {
    return passwordEncoder;
  }

  /**
   * 获取用户信息
   *
   * @param username 用户名
   * @return 用户信息
   * @throws UsernameNotFoundException
   */
  public AuthenticationUser loadUserByUsername(String username) throws UsernameNotFoundException {
    throw new RuntimeException(MessageUtils.getMessage(MessageCodec.NOT_APPLICABLE));
  }

  /**
   * 通过登录信息加载用户
   *
   * @param cmd 登录参数
   * @return 用户信息
   * @throws UsernameNotFoundException
   */
  public abstract AuthenticationUser loadUser(LoginCommand cmd) throws UsernameNotFoundException;

  /**
   * 登录额外校验
   *
   * @param cmd 登录参数
   * @return 反馈
   */
  public final ApiResponse<Boolean> loginAuthenticationAdditionalChecks(LoginCommand cmd) {
    final CaptchaStrategy strategy = CaptchaStrategyFactory.getStrategy(cmd.getType());
    return strategy.validateCode(cmd);
  }

  /**
   * 登录认证完成回调
   *
   * @param request            认证请求
   * @param loginCommand       认证参数
   * @param authenticationUser 认证成功的用户
   * @param description        描述信息
   * @param authenticated      是否认证成功
   */
  public abstract void loginAuthenticationCompleted(HttpServletRequest request,
                                                    LoginCommand loginCommand,
                                                    AuthenticationUser authenticationUser,
                                                    String description,
                                                    boolean authenticated);

  /**
   * 登录
   *
   * @param request  请求参数
   * @param response 请求反馈
   * @param cmd      登录参数
   * @return 反馈
   */
  public ApiResponse<Object> login(HttpServletRequest request, HttpServletResponse response, LoginCommand cmd) {
    /*
     * Auth by username, password and other custom details
     */
    LoginAuthenticationToken authenticationToken = new LoginAuthenticationToken(cmd);
    LoginAuthenticationProvider authenticationProvider = new LoginAuthenticationProvider(this);
    try {
      Authentication authentication = authenticationProvider.authenticate(authenticationToken);
      /* 认证成功 */
      if (null != authentication && authentication.isAuthenticated()) {
        if (null != request && null != response) {
          DefaultAuthenticationSuccessHandler authenticationSuccessHandler = new DefaultAuthenticationSuccessHandler(this);
          authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        }

        /* 返回token等 */
        OpaqueToken token = opaqueTokenTemplate.createToken((AuthenticationUser) authentication.getPrincipal());
        return ApiResponse.success(token);
      }
    } catch (AuthenticationException exception) {
      logger.error(exception.getMessage());
      try {
        /* 认证失败 */
        SecurityContextHolder.clearContext();
        if (null != request && null != response) {
          DefaultAuthenticationFailureHandler authenticationFailureHandler = new DefaultAuthenticationFailureHandler(this);
          authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
          return ApiResponse.failure(MessageCodec.statusOf(authenticationFailureHandler.getStatus()));
        }

        return ApiResponse.failure(MessageCodec.SESSION_AUTH_FAILURE);
      } catch (ServletException | IOException e) {
        logger.error(e.getMessage());
      }
    } catch (ServletException | IOException e) {
      logger.error(e.getMessage());
    }

    SecurityContextHolder.clearContext();
    return ApiResponse.failure(MessageCodec.DEFAULT_FAILURE);
  }

  /**
   * 登出
   *
   * @param context 用户上下文
   * @return 反馈
   */
  public ApiResponse<Object> logout(UserContext context) {
    Validate.requireNonNull(context);
    opaqueTokenTemplate.removeTokenByUUID(context.getUser().uuid());
    return ApiResponse.success();
  }

  /**
   * 获取验证码
   *
   * @return 反馈
   */
  public ApiResponse<CaptchaCode> getCode(LoginCodeCommand cmd) {
    try {
      final CaptchaStrategy strategy = CaptchaStrategyFactory.getStrategy(cmd.getType());
      return ApiResponse.success(strategy.getCode(cmd.getPrincipal()));
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    return ApiResponse.failure(MessageCodec.CODE_GENERATED_FAILURE);
  }

  /**
   * 校验token的有效性
   *
   * @param request 请求参数
   * @return 用户信息
   */
  public ApiResponse<AuthenticationUser> validateToken(HttpServletRequest request) {
    OpaqueToken opaqueToken = opaqueTokenTemplate.parseToken(request);
    AuthenticationUser autUser = opaqueToken.isValid()
        ? opaqueTokenTemplate.getAuthenticationUser(opaqueToken.getAccessToken())
        : AnonymousUser.INSTANCE;
    autUser.setPassword(Constants.PROTECTED);

    return ApiResponse.success(autUser);
  }

  /**
   * 刷新AccessToken
   *
   * @param request     请求参数
   * @param opaqueToken 简单令牌
   * @return 反馈
   */
  public ApiResponse<OpaqueToken> refreshAccessToken(HttpServletRequest request, OpaqueTokenSimplified opaqueToken) {
    Validate.requireNonNull(opaqueToken);
    Validate.requireNonEmpty(opaqueToken.getRefreshToken());

    request.setAttribute(AuthenticationType.AUTH_TOKEN_KEY, opaqueToken.getType() + " " + opaqueToken.getRefreshToken());
    request.setAttribute(AuthenticationType.AUTH_TOKEN_REFRESH_ACCESS_TOKEN, true);

    return ApiResponse.success(opaqueTokenTemplate.refreshToken(opaqueToken.getType(), opaqueToken.getRefreshToken()));
  }

}
