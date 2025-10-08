package io.latte.boot.support.thread;

/**
 * 默认任务监听器
 *
 * @author : wugz
 * @since : 2021/8/7
 */
public class CallableTaskListenerDefault<T> implements CallableTaskListener<T> {
  /**
   * 任务正常结束
   *
   * @param result
   * @throws RuntimeException
   */
  public void onComplete(T result) throws RuntimeException {
  }

  /**
   * 任务异常结束
   *
   * @param result
   * @param throwable
   * @throws RuntimeException
   */
  public void onException(T result, Throwable throwable) throws RuntimeException {
  }

}
