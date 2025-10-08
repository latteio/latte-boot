package io.latte.boot.web.http.login;

import io.latte.boot.web.http.message.Constants;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * LoginCommand
 *
 * @author : wugz
 * @since : 2021/6/1
 */
public class LoginCommand implements Serializable {
  /**
   * 应用标识: 认证中心模式
   */
  private String appId;

  /**
   * 应用端
   *
   * @see AppClientEnum
   */
  private String appClient;

  /**
   * 认证标识(服务器端颁发)
   */
  private String authId;

  /**
   * 认证类型
   *
   * @see AuthMethodEnum
   */
  @NotBlank
  private String type;

  /**
   * 用户名
   */
  @NotBlank
  private String username;

  /**
   * 密码: type=AUTH_MOBILE_CODE, AUTH_EMAIL_CODE 不需要密码
   */
  private String password;

  /**
   * 验证码
   */
  private String code;

  /**
   * 构造函数
   */
  public LoginCommand() {
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getAppClient() {
    return appClient;
  }

  public void setAppClient(String appClient) {
    this.appClient = appClient;
  }

  public String getAuthId() {
    return authId;
  }

  public void setAuthId(String authId) {
    this.authId = authId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.getClass().getSimpleName());
    builder.append("(");
    builder.append("appId=").append(appId).append(", ");
    builder.append("appClient=").append(appClient).append(", ");
    builder.append("authId=").append(authId).append(", ");
    builder.append("type=").append(type).append(", ");
    builder.append("username=").append(username).append(", ");
    builder.append("password=").append(Constants.PROTECTED).append(", ");
    builder.append("code=").append(code);
    builder.append(")");
    return builder.toString();
  }
}
