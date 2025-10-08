package io.latte.boot.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * ValidateCodeException
 *
 * @author : wugz
 * @since : 2021/9/1
 */
public class ValidateCodeException extends AuthenticationException {
  private final int status;

  public ValidateCodeException(int status, String message) {
    super(message);
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
}
