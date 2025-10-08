package io.latte.boot.autoconfigure.aspect;

import io.latte.boot.web.context.WebContext;

import java.lang.reflect.Method;

/**
 * LoggingProvider
 *
 * @author : wugz
 * @since : 2021/9/2
 */
public interface LoggingProvider<E> {

  /**
   * 创建日志
   *
   * @param context
   * @param method
   * @param args
   * @param exception
   * @return
   */
  E createEntity(WebContext context, Method method, Object[] args, Exception exception);

  /**
   * 保存日志
   *
   * @param context
   * @param entity
   * @return
   */
  Boolean saveEntity(WebContext context, E entity);
}
