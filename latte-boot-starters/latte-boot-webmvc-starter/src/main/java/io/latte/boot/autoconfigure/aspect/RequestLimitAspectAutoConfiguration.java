package io.latte.boot.autoconfigure.aspect;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.latte.boot.web.annotation.http.RequestLimit;
import io.latte.boot.web.context.AppContext;
import io.latte.boot.web.exception.RequestLimitException;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.message.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基础访问限制切面自动配置器
 *
 * @author : wugz
 * @since : 2022/2/22
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(value = Caffeine.class)
@Aspect
public class RequestLimitAspectAutoConfiguration {
  private final Cache<String, LocalDateTime> requestLimitCache;

  /**
   * 构造函数
   */
  public RequestLimitAspectAutoConfiguration() {
    /* 一小时强制过期 */
    this.requestLimitCache = Caffeine.newBuilder()
        .expireAfterWrite(3600_000, TimeUnit.MILLISECONDS)
        .expireAfterAccess(3600_000, TimeUnit.MILLISECONDS)
        .recordStats()
        .build();
  }

  /**
   * 指定包含目标注解作为切面
   */
  @Pointcut("@annotation(io.latte.boot.web.annotation.http.RequestLimit)")
  public void controllerMethodsWith() {
  }

  /**
   * 拦截控制层的正常执行
   *
   * @param point
   * @throws Throwable
   */
  @Before(value = "controllerMethodsWith()")
  public void requestLimit(JoinPoint point) throws Throwable {
    /* 1.获取方法相关信息 */
    MethodSignature methodSignature = (MethodSignature) point.getSignature();
    Method method = methodSignature.getMethod();

    /* 2.获取请求信息 */
    RequestLimit requestLimit = method.getAnnotation(RequestLimit.class);
    HttpServletRequest request = AppContext.getRequest();
    if (null != request) {
      String ip = requestLimit.ip();
      if ("all".equalsIgnoreCase(ip)) {
        checkRequestLimit(request, requestLimit);
      } else if (!"all".equalsIgnoreCase(ip) && request.getRemoteAddr().equals(ip)) {
        checkRequestLimit(request, requestLimit);
      } else {
        throw new RequestLimitException(request.getRequestURI(),
            MessageUtils.getMessage(MessageCodec.NOT_ALLOWED_IP));
      }
    }
  }

  /**
   * 校验请求限制: 缓存记录url以及解除限制时间. 如果解除则不允许访问, 否则可以访问
   *
   * @param request
   * @param requestLimit
   */
  private void checkRequestLimit(HttpServletRequest request, RequestLimit requestLimit) throws RequestLimitException {
    /* 1.获取缓存, 并计算 */
    LocalDateTime nextVisitTime = requestLimitCache.getIfPresent(request.getRequestURI());
    if (Objects.nonNull(nextVisitTime) && LocalDateTime.now().isBefore(nextVisitTime)) {
      throw new RequestLimitException(request.getRequestURI(), MessageUtils.getMessage(MessageCodec.REQUEST_BUSY));
    }

    /* 2.已失效, 允许访问 */
    /* 2.1换算出下次访问时间, 并写入缓存 */
    long milliseconds = requestLimit.timeUnit().toMillis(1) / requestLimit.frequency();
    requestLimitCache.put(request.getRequestURI(), LocalDateTime.now().plus(milliseconds, ChronoUnit.MILLIS));
  }

}
