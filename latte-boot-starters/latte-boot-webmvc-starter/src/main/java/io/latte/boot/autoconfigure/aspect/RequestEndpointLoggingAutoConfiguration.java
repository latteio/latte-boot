package io.latte.boot.autoconfigure.aspect;

import io.latte.boot.webmvc.registrar.DomainModelEndpointAdvice;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 领域方法 AOP 日志
 *
 * @author : wugz
 * @since : 2025/12/02
 */
@Configuration(proxyBeanMethods = false)
@Import(value = DomainModelEndpointAdvice.class)
@Aspect
public class RequestEndpointLoggingAutoConfiguration {
  private final DomainModelEndpointAdvice domainModelEndpointAdvice;

  /**
   * 构造函数
   *
   * @param domainModelEndpointAdvice domainModelEndpointAdvice
   */
  public RequestEndpointLoggingAutoConfiguration(DomainModelEndpointAdvice domainModelEndpointAdvice) {
    this.domainModelEndpointAdvice = domainModelEndpointAdvice;
  }

  /**
   * 指定包含目标注解作为切面
   */
  @Pointcut("@annotation(io.latte.boot.web.annotation.component.DomainFunction)")
  public void domainFunctionsWith() {
  }

  /**
   * 拦截控制层的正常执行
   *
   * @param joinPoint joinPoint
   */
  @Before(value = "domainFunctionsWith()")
  public void domainFunctionParametersLogging(JoinPoint joinPoint) {
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    domainModelEndpointAdvice.beforeInvoke(joinPoint.getTarget(), methodSignature.getMethod(), joinPoint.getArgs());
  }

}
