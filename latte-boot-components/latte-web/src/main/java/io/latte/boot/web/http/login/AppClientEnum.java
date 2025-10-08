package io.latte.boot.web.http.login;

import io.latte.boot.web.annotation.validation.IEnumType;

/**
 * 客户端枚举类
 *
 * @author : wugz
 * @since : 2022/10/29
 */
public enum AppClientEnum implements IEnumType<String> {
  ADMIN("admin"),
  APP("app"),
  WEB("web");

  private String code;

  AppClientEnum(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
