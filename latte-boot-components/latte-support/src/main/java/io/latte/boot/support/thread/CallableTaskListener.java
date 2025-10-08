package io.latte.boot.support.thread;

/**
 * 任务监听器
 *
 * @author : wugz
 * @since : 2021/8/7
 */
public interface CallableTaskListener<T> {
  /**
   * 任务正常结束
   *
   * @param result
   * @throws RuntimeException
   */
  void onComplete(T result) throws RuntimeException;

  /**
   * 任务异常结束
   *
   * @param result
   * @param throwable
   * @throws RuntimeException
   */
  void onException(T result, Throwable throwable) throws RuntimeException;
}
