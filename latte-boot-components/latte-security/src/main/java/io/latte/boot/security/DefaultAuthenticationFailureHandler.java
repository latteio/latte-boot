package io.latte.boot.security;

import io.latte.boot.security.customizer.LoginAuthenticationParameters;
import io.latte.boot.security.exception.ValidateCodeException;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.message.MessageUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import javax.security.auth.login.AccountExpiredException;
import java.io.IOException;

/**
 * DefaultAuthenticationFailureHandler
 *
 * @author : wugz
 * @since : 2022/11/25
 */
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {
  private final AuthenticationService authenticationService;
  private int status;

  /**
   * 构造函数
   *
   * @param authenticationService
   */
  public DefaultAuthenticationFailureHandler(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    /* 处理错误码 */
    int code = MessageCodec.DEFAULT_FAILURE.status();
    String desc = null;
    Throwable exceptionCause = exception instanceof InternalAuthenticationServiceException ? exception.getCause() : exception;
    if (null != exceptionCause) {
      if (exceptionCause instanceof UsernameNotFoundException) {
        code = MessageCodec.USERNAME_NOTFOUND.status();
        desc = MessageUtils.getMessage(MessageCodec.USERNAME_NOTFOUND);
      } else if (exceptionCause instanceof LockedException) {
        code = MessageCodec.ACCOUNT_LOCKED.status();
        desc = MessageUtils.getMessage(MessageCodec.ACCOUNT_LOCKED);
      } else if (exceptionCause instanceof AccountExpiredException) {
        code = MessageCodec.ACCOUNT_EXPIRED.status();
        desc = MessageUtils.getMessage(MessageCodec.ACCOUNT_EXPIRED);
      } else if (exceptionCause instanceof DisabledException) {
        code = MessageCodec.ACCOUNT_DISABLED.status();
        desc = MessageUtils.getMessage(MessageCodec.ACCOUNT_DISABLED);
      } else if (exceptionCause instanceof BadCredentialsException) {
        code = MessageCodec.BAD_CREDENTIALS.status();
        desc = MessageUtils.getMessage(MessageCodec.BAD_CREDENTIALS);
      } else if (exceptionCause instanceof CredentialsExpiredException) {
        code = MessageCodec.CREDENTIALS_EXPIRED.status();
        desc = MessageUtils.getMessage(MessageCodec.CREDENTIALS_EXPIRED);
      } else if (exceptionCause instanceof ValidateCodeException) {
        code = ((ValidateCodeException) exceptionCause).getStatus();
        desc = MessageUtils.getMessage(MessageCodec.CODE_VALIDATE_FAILURE);
      } else if (exceptionCause instanceof SessionAuthenticationException) {
        code = MessageCodec.SESSION_AUTH_FAILURE.status();
        desc = MessageUtils.getMessage(MessageCodec.SESSION_AUTH_FAILURE);
      }
    }
    this.setStatus(code);

    LoginAuthenticationParameters loginAuthenticationParameters = new LoginAuthenticationParameters(request);
    this.authenticationService.loginAuthenticationCompleted(
        request,
        loginAuthenticationParameters.obtainLoginCommand(),
        null,
        desc,
        false);
  }
}
