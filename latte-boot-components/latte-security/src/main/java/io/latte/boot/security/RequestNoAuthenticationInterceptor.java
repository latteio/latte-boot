package io.latte.boot.security;

import io.latte.boot.security.token.OpaqueTokenTemplate;
import io.latte.boot.web.context.AppContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 匿名会话拦截器
 *
 * @author : wugz
 * @since : 2023/2/9
 */
public class RequestNoAuthenticationInterceptor extends RequestAuthenticationInterceptor {
  /**
   * 构造函数
   *
   * @param securityProperties
   * @param opaqueTokenTemplate
   */
  public RequestNoAuthenticationInterceptor(SecurityProperties securityProperties, OpaqueTokenTemplate opaqueTokenTemplate) {
    super(securityProperties, opaqueTokenTemplate);
  }

  /**
   * Interception point before the execution of a handler. Called after
   * HandlerMapping determined an appropriate handler object, but before
   * HandlerAdapter invokes the handler.
   */
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {
    /* 设置上下文 */
    request.setAttribute(AuthenticationAttributes.AUTH_REQUIRED, false);
    request.setAttribute(AuthenticationAttributes.AUTH_USE_TOKEN, false);
    AppContext.setContext(request, response);
    return true;
  }

}
