package io.latte.boot.autoconfigure.aspect;

import io.latte.boot.web.context.AppContext;
import io.latte.boot.web.context.WebContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 基础日志切面自动配置器
 *
 * @author : wugz
 * @since : 2020/11/20
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(value = LoggingProvider.class)
@Aspect
public class LoggingAspectAutoConfiguration<E> {
  private final Logger logger = LoggerFactory.getLogger(LoggingAspectAutoConfiguration.class);
  private final LoggingProvider<E> loggingProvider;

  public LoggingAspectAutoConfiguration(LoggingProvider<E> loggingProvider) {
    this.loggingProvider = loggingProvider;
  }

  /**
   * 指定包含指定注解作为切面
   */
  @Pointcut("@annotation(io.latte.boot.web.annotation.logging.Logging)")
  public void controllerMethodsWith() {
  }

  /**
   * 拦截控制层的正常执行
   *
   * @param joinPoint
   * @throws Throwable
   */
  @AfterReturning(value = "controllerMethodsWith()")
  public void recordLogging(JoinPoint joinPoint) throws Throwable {
    this.recordLogging(joinPoint, null);
  }

  /**
   * 拦截控制层的执行异常
   *
   * @param joinPoint
   * @param exception
   * @throws Throwable
   */
  @AfterThrowing(value = "controllerMethodsWith()", throwing = "exception")
  public void recordException(JoinPoint joinPoint, Exception exception) throws Throwable {
    this.recordLogging(joinPoint, exception);
  }

  /**
   * 记录日志
   *
   * @param point
   * @param exception
   * @throws Throwable
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
  public void recordLogging(JoinPoint point, Exception exception) throws Throwable {
    /* 获取方法相关信息 */
    Signature signature = point.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();

    /* 保存日志 */
    /* 2.1 获取当前上下文 */
    WebContext context = AppContext.getWebContext();
    if (null != context
        && null != context.getRequest()
        && null != context.getUser()) {
      try {
        /* 2.2 准备数据, 并保存 */
        E entity = loggingProvider.createEntity(context, method, point.getArgs(), exception);
        if (null != entity) {
          loggingProvider.saveEntity(context, entity);
        }
      } catch (Exception e) {
        logger.error(e.getMessage());
      }
    }
  }

}
