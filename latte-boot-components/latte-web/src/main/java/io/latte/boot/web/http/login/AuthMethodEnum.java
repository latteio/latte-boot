package io.latte.boot.web.http.login;

import io.latte.boot.web.annotation.validation.IEnumType;

/**
 * 登录认证类型枚举类
 *
 * @author : wugz
 * @since : 2022/10/29
 */
public enum AuthMethodEnum implements IEnumType<String> {
  AUTH_USERNAME_PASSWORD("00", "账号+密码+无验证码登录"),
  AUTH_USERNAME_PASSWORD_CODE("10", "账号+密码+有验证码登录"),
  AUTH_MOBILE_CODE("20", "手机号+手机验证码登录"),
  AUTH_EMAIL_CODE("30", "邮箱+邮箱验证码登录"),
  AUTH_THIRD("40", "其他方式");

  private String code;
  private String desc;

  AuthMethodEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }
}
