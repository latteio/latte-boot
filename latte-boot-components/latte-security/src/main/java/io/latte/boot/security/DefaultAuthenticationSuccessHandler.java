package io.latte.boot.security;

import io.latte.boot.security.customizer.LoginAuthenticationParameters;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.message.MessageUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * DefaultAuthenticationSuccessHandler
 *
 * @author : wugz
 * @since : 2022/11/25
 */
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  private final AuthenticationService authenticationService;

  /**
   * 构造函数
   *
   * @param authenticationService
   */
  public DefaultAuthenticationSuccessHandler(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    LoginAuthenticationParameters loginAuthenticationParameters = new LoginAuthenticationParameters(request);
    this.authenticationService.loginAuthenticationCompleted(
        request,
        loginAuthenticationParameters.obtainLoginCommand(),
        (AuthenticationUser) authentication.getPrincipal(),
        MessageUtils.getMessage(MessageCodec.AUTHORIZED),
        true);
  }
}
