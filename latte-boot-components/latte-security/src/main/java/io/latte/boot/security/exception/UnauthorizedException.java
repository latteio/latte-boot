package io.latte.boot.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * UnauthorizedException
 *
 * @author : wugz
 * @since : 2021/9/1
 */
public class UnauthorizedException extends AuthenticationException {
  private final String url;

  /**
   * 构造函数
   *
   * @param url
   * @param message
   */
  public UnauthorizedException(String url, String message) {
    super(message);
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
