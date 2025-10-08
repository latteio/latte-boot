package io.latte.boot.web.exception;

/**
 * RequestLimitException
 *
 * @author : wugz
 * @since : 2022/2/22
 */
public class RequestLimitException extends RuntimeException {
  private final String url;

  /**
   * 构造函数
   *
   * @param url
   * @param message
   */
  public RequestLimitException(String url, String message) {
    super(message);
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
