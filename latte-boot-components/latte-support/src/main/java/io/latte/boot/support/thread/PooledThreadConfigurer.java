package io.latte.boot.support.thread;

import java.util.concurrent.*;

/**
 * 线程执行配置器
 *
 * @author : wugz
 * @since : 2021/8/5
 */
public class PooledThreadConfigurer {
  /**
   * base
   */
  private int corePoolSize;
  private int maximumPoolSize;
  private long keepAliveTime;
  private TimeUnit keepAliveTimeUnit;
  private BlockingQueue<Runnable> workQueue;
  private ThreadFactory threadFactory;
  private RejectedExecutionHandler rejectedHandler;

  /**
   * extend
   */
  private String threadNamePrefix;
  private long executeTimeout;
  private TimeUnit executeTimeoutUnit;

  public PooledThreadConfigurer() {
    // base init
    this.corePoolSize = Math.max(Runtime.getRuntime().availableProcessors() + 1, 4);
    this.maximumPoolSize = Math.max((Runtime.getRuntime().availableProcessors() + 1) * 4, 16);
    this.keepAliveTime = 30L;
    this.keepAliveTimeUnit = TimeUnit.SECONDS;
    this.workQueue = new LinkedBlockingQueue<>(1024);
    this.threadFactory = Executors.defaultThreadFactory();
    this.rejectedHandler = new ThreadPoolExecutor.AbortPolicy();

    // extend init
    this.threadNamePrefix = null;
    this.executeTimeout = 1800;
    this.executeTimeoutUnit = TimeUnit.SECONDS;
  }

  /**
   * 连接池参数配置: corePoolSize, maximumPoolSize
   *
   * @param corePoolSize
   * @param maximumPoolSize
   * @return
   */
  public PooledThreadConfigurer poolSize(int corePoolSize, int maximumPoolSize) {
    if (corePoolSize > 0 && maximumPoolSize > 0) {
      this.corePoolSize = Math.min(corePoolSize, maximumPoolSize);
      this.maximumPoolSize = Math.max(corePoolSize, maximumPoolSize);
    }
    return this;
  }

  /**
   * 连接池参数配置: keepAliveTime, keepAliveTimeUnit
   *
   * @param keepAliveTime
   * @param keepAliveTimeUnit
   * @return
   */
  public PooledThreadConfigurer keepAliveTime(long keepAliveTime, TimeUnit keepAliveTimeUnit) {
    if (keepAliveTime > 0 && null != keepAliveTimeUnit) {
      this.keepAliveTime = keepAliveTime;
      this.keepAliveTimeUnit = keepAliveTimeUnit;
    }
    return this;
  }

  /**
   * 连接池参数配置: workQueue
   *
   * @param workQueue
   * @return
   */
  public PooledThreadConfigurer workQueue(BlockingQueue<Runnable> workQueue) {
    if (null != workQueue) {
      this.workQueue = workQueue;
    }
    return this;
  }

  /**
   * 连接池参数配置: threadFactory
   *
   * @param threadFactory
   * @return
   */
  public PooledThreadConfigurer threadFactory(ThreadFactory threadFactory) {
    if (null != threadFactory) {
      this.threadFactory = threadFactory;
    }
    return this;
  }

  /**
   * 连接池参数配置: 线程基础前缀
   *
   * @param threadNamePrefix
   * @return
   */
  public PooledThreadConfigurer threadNamePrefix(String threadNamePrefix) {
    if (null != threadNamePrefix && !threadNamePrefix.isEmpty()) {
      this.threadNamePrefix = threadNamePrefix;
      this.threadFactory = new CallableTaskThreadFactory(threadNamePrefix);
    }
    return this;
  }

  /**
   * 连接池参数配置: 拒绝服务策略
   *
   * @param rejectedHandler
   * @return
   */
  public PooledThreadConfigurer rejectedHandler(RejectedExecutionHandler rejectedHandler) {
    if (null != rejectedHandler) {
      this.rejectedHandler = rejectedHandler;
    }
    return this;
  }

  /**
   * 连接池参数配置: 同步执行时的超时时间
   *
   * @param executeTimeout
   * @param executeTimeoutUnit
   * @return
   */
  public PooledThreadConfigurer executeTimeout(long executeTimeout, TimeUnit executeTimeoutUnit) {
    if (executeTimeout > 0 && null != executeTimeoutUnit) {
      this.executeTimeout = executeTimeout;
      this.executeTimeoutUnit = executeTimeoutUnit;
    }
    return this;
  }

  public int getCorePoolSize() {
    return corePoolSize;
  }

  public int getMaximumPoolSize() {
    return maximumPoolSize;
  }

  public long getKeepAliveTime() {
    return keepAliveTime;
  }

  public TimeUnit getKeepAliveTimeUnit() {
    return keepAliveTimeUnit;
  }

  public BlockingQueue<Runnable> getWorkQueue() {
    return workQueue;
  }

  public ThreadFactory getThreadFactory() {
    return threadFactory;
  }

  public RejectedExecutionHandler getRejectedHandler() {
    return rejectedHandler;
  }

  public String getThreadNamePrefix() {
    return threadNamePrefix;
  }

  public long getExecuteTimeout() {
    return executeTimeout;
  }

  public TimeUnit getExecuteTimeoutUnit() {
    return executeTimeoutUnit;
  }
}
