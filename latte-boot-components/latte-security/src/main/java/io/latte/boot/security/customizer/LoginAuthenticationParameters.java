package io.latte.boot.security.customizer;

import io.latte.boot.web.http.login.LoginCommand;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;

/**
 * LoginAuthenticationParameters
 *
 * @author : wugz
 * @since : 2022/11/24
 */
public class LoginAuthenticationParameters implements Serializable {
  public static final String SPRING_SECURITY_FORM_APP_ID_KEY = "appId";
  public static final String SPRING_SECURITY_FORM_APP_CLIENT_KEY = "appClient";
  public static final String SPRING_SECURITY_FORM_AUTH_ID_KEY = "authId";
  public static final String SPRING_SECURITY_FORM_TYPE_KEY = "type";
  public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
  public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
  public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";
  private final HttpServletRequest request;

  /**
   * 构造函数
   *
   * @param request
   */
  public LoginAuthenticationParameters(HttpServletRequest request) {
    this.request = request;
  }

  /**
   * 获取登录参数
   *
   * @return
   */
  public LoginCommand obtainLoginCommand() {
    String username = obtainUsername();
    username = (username != null) ? username.trim() : "";
    String password = obtainPassword();
    password = (password != null) ? password : "";

    LoginCommand loginCommand = new LoginCommand();
    loginCommand.setAppId(obtainAppId());
    loginCommand.setAppClient(obtainAppClient());
    loginCommand.setAuthId(obtainAuthId());
    loginCommand.setType(obtainType());
    loginCommand.setUsername(username);
    loginCommand.setPassword(password);
    loginCommand.setCode(obtainCode());
    return loginCommand;
  }

  private String obtainAppId() {
    return request.getParameter(SPRING_SECURITY_FORM_APP_ID_KEY);
  }

  private String obtainAppClient() {
    return request.getParameter(SPRING_SECURITY_FORM_APP_CLIENT_KEY);
  }

  private String obtainAuthId() {
    return request.getParameter(SPRING_SECURITY_FORM_AUTH_ID_KEY);
  }

  private String obtainType() {
    return request.getParameter(SPRING_SECURITY_FORM_TYPE_KEY);
  }

  private String obtainUsername() {
    return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
  }

  private String obtainPassword() {
    return request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
  }

  private String obtainCode() {
    return request.getParameter(SPRING_SECURITY_FORM_CODE_KEY);
  }
}