package io.latte.boot.security;

import io.latte.boot.security.customizer.LoginAuthenticationToken;
import io.latte.boot.security.exception.UnauthorizedException;
import io.latte.boot.security.token.OpaqueToken;
import io.latte.boot.security.token.OpaqueTokenTemplate;
import io.latte.boot.support.validate.Validate;
import io.latte.boot.web.context.AppContext;
import io.latte.boot.web.http.message.Constants;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.message.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RequestAuthenticationInterceptor
 *
 * @author : wugz
 * @since : 2023/2/9
 */
public class RequestAuthenticationInterceptor implements HandlerInterceptor {
  private final Logger logger = LoggerFactory.getLogger(RequestAuthenticationInterceptor.class);

  private final SecurityProperties securityProperties;
  private final OpaqueTokenTemplate opaqueTokenTemplate;
  private final List<PathPatternRequestMatcher> ANONYMOUS_REQUEST_MATCHERS;

  /**
   * 构造函数
   *
   * @param securityProperties
   * @param opaqueTokenTemplate
   */
  public RequestAuthenticationInterceptor(SecurityProperties securityProperties,
                                          OpaqueTokenTemplate opaqueTokenTemplate) {
    this.securityProperties = Validate.requireNonNull(securityProperties);
    this.opaqueTokenTemplate = Validate.requireNonNull(opaqueTokenTemplate);
    AnonymousRequests anonymousRequests = new AnonymousRequests()
        .addRequest("/")
        .addRequest("/api/auth/login")
        .addRequest("/api/auth/login/**")
        .addRequest(this.securityProperties.getHttp().getRequest().getApiPermits());
    List<PathPatternRequestMatcher> antPathRequestMatchers = new ArrayList<>(anonymousRequests.size() + 1);
    antPathRequestMatchers.add(PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.OPTIONS, "/**"));

    for (String anonymousRequest : anonymousRequests.get()) {
      antPathRequestMatchers.add(PathPatternRequestMatcher.withDefaults().matcher(anonymousRequest));
    }

    this.ANONYMOUS_REQUEST_MATCHERS = Collections.unmodifiableList(antPathRequestMatchers);
  }

  protected SecurityProperties getSecurityProperties() {
    return securityProperties;
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
    if (securityProperties.getAuth().isEnabled()
        && this.authRequiredRequest(request, securityProperties.getAuth().isUseToken())) {
      OpaqueToken opaqueToken = opaqueTokenTemplate.parseToken(request);

      authenticated = opaqueToken.isValid();
      this.setAuthenticated(opaqueToken.getAccessToken(), authenticated);
      if (authenticated) {
        /* 鉴权成功 */
        /* 鉴权成功: 如果token已经自动刷新, 则自动更新响应头 */
        response.setStatus(MessageCodec.CREATED.status());
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
      } else {
        /* 鉴权失败 */
        throw new UnauthorizedException(request.getRequestURI(), MessageUtils.getMessage(MessageCodec.UNAUTHORIZED));
      }
    } else {
      authenticated = true;
    }

    /* 设置上下文 */
    AppContext.setContext(request, response);
    return authenticated;
  }

  /**
   * Callback after completion of request processing, that is, after rendering
   * the view. Will be called on any outcome of handler execution, thus allows
   * for proper resource cleanup.
   */
  public void afterCompletion(HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler,
                              @Nullable Exception ex) throws Exception {
    /*
     * 完成: 清除上下文, 防止内存泄漏
     */
    AppContext.clearContext();
  }

  /**
   * 设置已认证
   *
   * @param token
   * @param authenticated
   */
  protected void setAuthenticated(String token, boolean authenticated) {
    AuthenticationUser principal;
    if (authenticated && null != (principal = opaqueTokenTemplate.getAuthenticationUser(token))) {
      Authentication authentication = new LoginAuthenticationToken(principal, Constants.PROTECTED, principal.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } else {
      opaqueTokenTemplate.removeToken(token);
      SecurityContextHolder.clearContext();
    }
  }

  /**
   * 检查当前请求是否需要检测认证情况, 并设置相应的标志
   *
   * @param request  请求
   * @param useToken 是否启用token验证
   * @return
   */
  protected boolean authRequiredRequest(HttpServletRequest request, boolean useToken) {
    for (PathPatternRequestMatcher matcher : this.ANONYMOUS_REQUEST_MATCHERS) {
      if (matcher.matches(request)) {
        request.setAttribute(AuthenticationAttributes.AUTH_REQUIRED, Boolean.FALSE);
        request.setAttribute(AuthenticationAttributes.AUTH_USE_TOKEN, useToken);
        return false;
      }
    }

    request.setAttribute(AuthenticationAttributes.AUTH_REQUIRED, Boolean.TRUE);
    request.setAttribute(AuthenticationAttributes.AUTH_USE_TOKEN, useToken);
    return true;
  }
}
