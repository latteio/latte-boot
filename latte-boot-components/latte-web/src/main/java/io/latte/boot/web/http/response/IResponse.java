package io.latte.boot.web.http.response;

import java.io.Serializable;

/**
 * IResponse
 *
 * @author : wugz
 * @since : 2018/6/10
 */
public interface IResponse<T> extends Serializable {
  int getCode();

  int getStatus();

  String getMsg();

  boolean isSuccess();

  T getData();

  String getRequestId();

  String getEventId();

  void throwFailure(String msg);
}
