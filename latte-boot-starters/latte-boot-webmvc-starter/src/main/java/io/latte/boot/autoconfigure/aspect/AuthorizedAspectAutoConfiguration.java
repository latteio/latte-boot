package io.latte.boot.autoconfigure.aspect;

import io.latte.boot.security.exception.UnauthorizedException;
import io.latte.boot.security.token.OpaqueToken;
import io.latte.boot.security.token.OpaqueTokenTemplate;
import io.latte.boot.web.annotation.http.Authorized;
import io.latte.boot.web.context.AppContext;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.message.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * 通过@Authorized注解的形式提供认证授权控制, 默认不认证.
 * 特别地, 对基于诸如网站的接口允许不登录进行接口访问.
 *
 * @author : wugz
 * @since : 2020/11/20
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(value = OpaqueTokenTemplate.class)
@Aspect
public class AuthorizedAspectAutoConfiguration {
  private static final Logger logger = LoggerFactory.getLogger(AuthorizedAspectAutoConfiguration.class);
  private final OpaqueTokenTemplate opaqueTokenTemplate;

  /**
   * 构造函数
   *
   * @param opaqueTokenTemplate opaqueTokenTemplate
   */
  public AuthorizedAspectAutoConfiguration(OpaqueTokenTemplate opaqueTokenTemplate) {
    this.opaqueTokenTemplate = opaqueTokenTemplate;
  }

  /**
   * 指定包含目标注解作为切面
   */
  @Pointcut("@annotation(io.latte.boot.web.annotation.http.Authorized)")
  public void controllerMethodsWith() {
  }

  /**
   * 拦截控制层的正常执行
   *
   * @param joinPoint joinPoint
   * @throws Throwable Throwable
   */
  @Before(value = "controllerMethodsWith()")
  public void checkAuthorized(JoinPoint joinPoint) throws Throwable {
    this.checkAuthorized(joinPoint, null);
  }

  /**
   * 判定认证情况
   *
   * @param point     point
   * @param exception exception
   * @throws Throwable Throwable
   */
  public void checkAuthorized(JoinPoint point, Exception exception) throws Throwable {
    /* 获取方法相关信息 */
    MethodSignature methodSignature = (MethodSignature) point.getSignature();
    Method method = methodSignature.getMethod();

    /* 忽略不需要验证的方法 */
    Authorized authorized = method.getAnnotation(Authorized.class);
    if (!authorized.required()) {
      return;
    }

    /* 验证token的有效性 */
    HttpServletRequest request = AppContext.getRequest();
    if (null != request) {
      try {
        OpaqueToken opaqueToken = this.opaqueTokenTemplate.parseToken(request);
        if (!opaqueToken.isValid()) {
          throw new UnauthorizedException(request.getRequestURI(), MessageUtils.getMessage(MessageCodec.UNAUTHORIZED));
        }
      } catch (Exception e) {
        logger.error(e.getMessage());
      }
    }
  }

}
