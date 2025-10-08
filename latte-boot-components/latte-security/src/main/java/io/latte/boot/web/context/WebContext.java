package io.latte.boot.web.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * WebContext
 *
 * @author : wugz
 * @since : 2022/3/22
 */
public interface WebContext extends UserContext {
  /**
   * 返回请求
   *
   * @return
   */
  HttpServletRequest getRequest();

  /**
   * 返回响应
   *
   * @return
   */
  HttpServletResponse getResponse();
}
