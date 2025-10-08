package io.latte.boot.support.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 基于线程池的线程执行器
 *
 * @author : wugz
 * @since : 2021/8/5
 */
public class PooledThreadExecutor {
  private static Logger logger = LoggerFactory.getLogger(PooledThreadExecutor.class);
  private final PooledThreadConfigurer configurer;
  private final ThreadPoolExecutor executor;

  /**
   * 构造函数
   */
  public PooledThreadExecutor() {
    this(new PooledThreadConfigurer());
  }

  /**
   * 构造函数
   *
   * @param configurer 配置器
   */
  public PooledThreadExecutor(PooledThreadConfigurer configurer) {
    Objects.requireNonNull(configurer, "configurer is null");

    /* 创建executor */
    this.configurer = configurer;
    this.executor = new ThreadPoolExecutor(
        configurer.getCorePoolSize(),
        configurer.getMaximumPoolSize(),
        configurer.getKeepAliveTime(),
        configurer.getKeepAliveTimeUnit(),
        configurer.getWorkQueue(),
        configurer.getThreadFactory(),
        configurer.getRejectedHandler()
    );
  }

  /**
   * 任务提交
   *
   * @param callableTask
   * @param <T>
   */
  public <T> void submit(final CallableTask<T> callableTask) throws RuntimeException {
    this.submit(callableTask, new CallableTaskListenerDefault<T>());
  }

  /**
   * 任务提交
   *
   * @param callableTask
   * @param callableTaskListener
   * @param <T>
   */
  public <T> void submit(final CallableTask<T> callableTask, final CallableTaskListener<T> callableTaskListener) throws RuntimeException {
    Objects.requireNonNull(callableTask, "callableTask is null");
    Objects.requireNonNull(callableTaskListener, "callableTaskListener is null");

    /*
     * 提交异步任务
     */
    final CompletableFuture<CallableTaskReceipt<T>> completableFuture = CompletableFuture.supplyAsync(() -> {
      try {
        T result = callableTask.call();
        return new CallableTaskReceipt<T>(true, result, null);
      } catch (Throwable throwable) {
        T result = callableTask.callException(throwable);
        return new CallableTaskReceipt<T>(false, result, throwable);
      }
    }, this.executor);

    /*
     *  完成回调
     */
    completableFuture.whenComplete((callableTaskReceipt, throwable) -> {
      try {
        if (callableTaskReceipt.isSuccess()) {
          callableTaskListener.onComplete(callableTaskReceipt.getResult());
        } else {
          callableTaskListener.onException(callableTaskReceipt.getResult(), callableTaskReceipt.getThrowable());
        }
      } catch (Throwable t) {
        logger.error(t.getMessage());
      }
    });

  }

  /**
   * 关闭线程池
   */
  public void shutdown() {
    if (logger.isDebugEnabled()) {
      logger.debug("Closing thread pool: pool name is " + this.configurer.getThreadNamePrefix() + " ...");
    }

    if (!this.executor.isShutdown()) {
      this.executor.shutdown();
    }
  }

}
