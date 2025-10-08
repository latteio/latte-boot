package io.latte.boot.webmvc.core;

import io.latte.boot.web.context.AppContext;
import io.latte.boot.web.exception.ThrowableFailure;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * IService
 *
 * @author : wugz
 * @since : 2021/7/14
 */
public interface IService extends IContextService, ThrowableFailure {
  /**
   * 返回请求
   *
   * @return
   */
  default HttpServletRequest getRequest() {
    return AppContext.getRequest();
  }

  /**
   * 返回响应
   *
   * @return
   */
  default HttpServletResponse getResponse() {
    return AppContext.getResponse();
  }
}
