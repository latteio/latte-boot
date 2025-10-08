package io.latte.boot.support.thread;

import java.io.Serializable;

/**
 * 异步任务反馈实体类
 *
 * @author : wugz
 * @since : 2021/8/13
 */
public class CallableTaskReceipt<T> implements Serializable {
  private final boolean success;
  private final T result;
  private final Throwable throwable;

  public CallableTaskReceipt(boolean success, T result, Throwable throwable) {
    this.success = success;
    this.result = result;
    this.throwable = throwable;
  }

  public boolean isSuccess() {
    return success;
  }

  public T getResult() {
    return result;
  }

  public Throwable getThrowable() {
    return throwable;
  }

}
