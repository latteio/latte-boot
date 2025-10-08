package io.latte.boot.security.session;

import io.latte.boot.security.RequestAuthenticationInterceptor;
import io.latte.boot.security.SecurityProperties;
import io.latte.boot.security.token.OpaqueTokenTemplate;
import io.latte.boot.web.context.AppContext;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.message.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * RequestSessionAuthenticationInterceptor
 *
 * @author : wugz
 * @since : 2023/2/9
 */
public class RequestSessionAuthenticationInterceptor extends RequestAuthenticationInterceptor {
  private final Logger logger = LoggerFactory.getLogger(RequestSessionAuthenticationInterceptor.class);

  /**
   * 构造函数
   *
   * @param securityProperties
   * @param opaqueTokenTemplate
   */
  public RequestSessionAuthenticationInterceptor(SecurityProperties securityProperties, OpaqueTokenTemplate opaqueTokenTemplate) {
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
    boolean authenticated;

    /* 设置认证等标志 */
    if (getSecurityProperties().getAuth().isEnabled()
        && this.authRequiredRequest(request, getSecurityProperties().getAuth().isUseToken())) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (null != authentication
          && !(authentication instanceof AnonymousAuthenticationToken)
          && authentication.isAuthenticated()) {
        authenticated = true;
      } else {
        logger.error(MessageUtils.getMessage(MessageCodec.UNAUTHORIZED) + ": status = {}, uri = {}", MessageCodec.UNAUTHORIZED.status(), request.getRequestURI());
        response.sendRedirect(request.getContextPath() + "/login?status=" + MessageCodec.UNAUTHORIZED.status());
        authenticated = false;
      }
    } else {
      authenticated = true;
    }

    /* 设置上下文 */
    AppContext.setContext(request, response);

    return authenticated;
  }
}
