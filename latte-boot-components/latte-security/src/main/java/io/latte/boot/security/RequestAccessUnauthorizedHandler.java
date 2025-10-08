package io.latte.boot.security;

import io.latte.boot.support.web.JsonUtils;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 未认证入口Handler
 *
 * @author : wugz
 * @since : 2021/7/20
 */
public class RequestAccessUnauthorizedHandler implements AuthenticationEntryPoint {

  /**
   * Commences an authentication scheme.
   *
   * @param request
   * @param response
   * @param authException
   * @throws IOException
   * @throws ServletException
   */
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
    response.setHeader("Access-Control-Allow-Headers", "*");
    response.setHeader("Access-Control-Allow-Methods", "HEAD, OPTIONS, GET, POST, PUT, DELETE");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Max-Age", "3600");
    response.setHeader("Cache-Control", "no-cache");
    response.setContentType("application/json; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    PrintWriter printWriter = response.getWriter();
    String body = JsonUtils.fromObject(ApiResponse.failure(MessageCodec.UNAUTHORIZED));
    printWriter.write(body);
    printWriter.flush();
  }

}
