package io.latte.boot.web.context;

import io.latte.boot.security.AuthenticationUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * WebContextBean
 *
 * @author : wugz
 * @since : 2022/3/22
 */
class WebContextBean extends UserContextBean implements WebContext {
  private HttpServletRequest request;
  private HttpServletResponse response;

  public static final WebContextBean ANONYMOUS_WEB_CONTEXT = new WebContextBean(
      (AuthenticationUser) null,
      false,
      true)
      .build();

  /**
   * 构造函数
   *
   * @param request
   * @param response
   */
  WebContextBean(HttpServletRequest request, HttpServletResponse response) {
    super(request);
    this.request = request;
    this.response = response;
  }

  /**
   * 构造函数
   *
   * @param requestMap
   * @param requireAuthorized
   * @param useToken
   */
  WebContextBean(Map<String, Object> requestMap, boolean requireAuthorized, boolean useToken) {
    super(requestMap, requireAuthorized, useToken);
    this.request = null;
    this.response = null;
  }

  /**
   * 构造函数
   *
   * @param authenticationUser 用户信息
   * @param requireAuthorized  是否需要认证
   * @param useToken           是否使用token验证
   */
  WebContextBean(AuthenticationUser authenticationUser, boolean requireAuthorized, boolean useToken) {
    super(authenticationUser, requireAuthorized, useToken);
  }

  /**
   * 分析构建上下文
   *
   * @return
   */
  public WebContextBean build() {
    super.build();
    return this;
  }

  public HttpServletRequest getRequest() {
    return request;
  }

  public HttpServletResponse getResponse() {
    return response;
  }

}
