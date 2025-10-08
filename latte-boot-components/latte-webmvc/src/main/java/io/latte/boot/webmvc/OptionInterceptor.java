package io.latte.boot.webmvc;

import io.latte.boot.web.http.message.MessageCodec;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Option拦截器
 *
 * @author : wugz
 * @since : 2022/1/19
 */
public class OptionInterceptor implements HandlerInterceptor {
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
      response.setStatus(MessageCodec.OK.status());
      return false;
    }
    return true;
  }
}
