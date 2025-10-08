package io.latte.boot.web.context;

import io.latte.boot.security.AuthenticationUser;
import io.latte.boot.web.http.message.AppMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * AppContext
 *
 * @author : wugz
 * @since : 2021/9/9
 */
public class AppContext {
  private static final ThreadLocal<WebContext> APP_CONTEXT_CONTAINER = new ThreadLocal<>();
  private static AppMessageContext APP_MESSAGE_CONTEXT;

  /**
   * 构造函数
   *
   * @param appMessageContext 消息上下文
   */
  public AppContext(AppMessageContext appMessageContext) {
    AppContext.APP_MESSAGE_CONTEXT = appMessageContext;
  }

  /**
   * 设置上下文
   *
   * @param request
   * @param response
   */
  public static void setContext(HttpServletRequest request, HttpServletResponse response) {
    APP_CONTEXT_CONTAINER.set(
        new WebContextBean(
            request,
            response).build());
  }

  /**
   * 设置上下文
   *
   * @param requireAuthorized
   * @param useToken
   * @param requestMap
   */
  public static void setContext(boolean requireAuthorized, boolean useToken, Map<String, Object> requestMap) {
    APP_CONTEXT_CONTAINER.set(new WebContextBean(
        requestMap,
        requireAuthorized,
        useToken).build());
  }

  /**
   * 设置上下文
   *
   * @param authenticationUser 用户信息
   */
  public static void setContext(AuthenticationUser authenticationUser) {
    APP_CONTEXT_CONTAINER.set(new WebContextBean(
        authenticationUser,
        true,
        true).build());
  }

  /**
   * 清除上下文
   */
  public static void clearContext() {
    APP_CONTEXT_CONTAINER.remove();
  }

  /**
   * 返回请求
   *
   * @return HttpServletRequest
   */
  public static HttpServletRequest getRequest() {
    return APP_CONTEXT_CONTAINER.get().getRequest();
  }

  /**
   * 返回响应
   *
   * @return HttpServletResponse
   */
  public static HttpServletResponse getResponse() {
    return APP_CONTEXT_CONTAINER.get().getResponse();
  }

  /**
   * 返回上下文
   *
   * @return WebContext
   */
  public static WebContext getWebContext() {
    WebContext webContext = APP_CONTEXT_CONTAINER.get();
    return null != webContext ? webContext : WebContextBean.ANONYMOUS_WEB_CONTEXT;
  }

  /**
   * 返回用户上下文
   *
   * @return UserContext
   */
  public static UserContext getUserContext() {
    WebContext webContext = APP_CONTEXT_CONTAINER.get();
    return null != webContext ? webContext : WebContextBean.ANONYMOUS_WEB_CONTEXT;
  }

  private static AppMessageContext getAppMessageContext() {
    return APP_MESSAGE_CONTEXT;
  }
}
