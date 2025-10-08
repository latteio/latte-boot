package io.latte.boot.support.thread;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程任务工厂类
 *
 * @author : wugz
 * @since : 2021/8/5
 */
public class CallableTaskThreadFactory implements ThreadFactory {
  private final ThreadGroup group;
  private final AtomicInteger threadNumber = new AtomicInteger(1);
  private final String namePrefix;

  public CallableTaskThreadFactory(String namePrefix) {
    this.group = Thread.currentThread().getThreadGroup();
    this.namePrefix = "pooled-thread-" + namePrefix;
  }

  public Thread newThread(Runnable target) {
    Thread t = new Thread(group, Objects.requireNonNull(target,
        CallableTaskThreadFactory.class.getSimpleName() + ".newThread(): target is null"),
        namePrefix + "-" + threadNumber.getAndIncrement(),
        0);
    if (t.isDaemon())
      t.setDaemon(false);
    if (t.getPriority() != Thread.NORM_PRIORITY)
      t.setPriority(Thread.NORM_PRIORITY);
    return t;
  }

}
