package io.latte.boot.support.thread;

import java.util.concurrent.Callable;

/**
 * 任务
 *
 * @author : wugz
 * @since : 2021/8/7
 */
public interface CallableTask<T> extends Callable<T> {
  /**
   * 任务执行完成
   *
   * @return
   */
  T call();

  /**
   * 任务执行异常
   *
   * @param throwable
   * @return
   */
  T callException(Throwable throwable);

}
