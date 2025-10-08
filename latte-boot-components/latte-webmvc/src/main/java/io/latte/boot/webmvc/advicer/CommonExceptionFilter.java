package io.latte.boot.webmvc.advicer;

import io.latte.boot.web.util.BeanUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * CommonExceptionFilter
 *
 * @author : wugz
 * @since : 2022/8/28
 */
public class CommonExceptionFilter implements Filter {

  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } catch (Exception exception) {
      HandlerExceptionResolver handlerExceptionResolver = BeanUtils.getBean("handlerExceptionResolver");
      handlerExceptionResolver.resolveException((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, null, exception);
    }
  }

  public void destroy() {
    Filter.super.destroy();
  }
}
