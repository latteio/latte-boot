package io.latte.boot.security.customizer;

import io.latte.boot.web.http.login.LoginCommand;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * LoginAuthenticationToken
 *
 * @author : wugz
 * @since : 2022/9/19
 */
public final class LoginAuthenticationToken extends UsernamePasswordAuthenticationToken {
  private LoginCommand loginCommand;

  public LoginAuthenticationToken(LoginCommand cmd) {
    super(cmd.getUsername(), cmd.getPassword());
    this.loginCommand = cmd;
  }

  public LoginAuthenticationToken(Object principal, Object credentials) {
    super(principal, credentials);
  }

  public LoginAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }

  public LoginCommand getLoginCommand() {
    return loginCommand;
  }

}