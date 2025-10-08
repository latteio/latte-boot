package com.github.easycaptcha;

import com.github.easycaptcha.utils.CaptchaUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


/**
 * 验证码Servlet
 */
public class CaptchaServlet extends HttpServlet {
  private static final long serialVersionUID = -90304944339413093L;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    CaptchaUtil.out(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

}
