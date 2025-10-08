package io.latte.boot.web.exception;

import io.latte.boot.web.http.response.ApiResponse;

import java.io.Serializable;

/**
 * ThrowableFailure
 *
 * @author : wugz
 * @since : 2021/4/22
 */
public interface ThrowableFailure extends Serializable {
  default <T> ApiResponse<T> throwFailure(String message) {
    throw new RuntimeException(message);
  }

  default <T> ApiResponse<T> throwFailure(String message, Throwable cause) {
    throw new RuntimeException(message, cause);
  }

  default <T> ApiResponse<T> throwFailure(Throwable cause) {
    throw new RuntimeException(cause);
  }

}
