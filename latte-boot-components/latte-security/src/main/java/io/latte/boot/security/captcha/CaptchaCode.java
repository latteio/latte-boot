package io.latte.boot.security.captcha;

import java.io.Serializable;

/**
 * CaptchaCode
 *
 * @author : wugz
 * @since : 2022/10/21
 */
public class CaptchaCode implements Serializable {
  private String authId;
  private String code;

  public CaptchaCode(String authId, String code) {
    this.authId = authId;
    this.code = code;
  }

  public String getAuthId() {
    return authId;
  }

  public void setAuthId(String authId) {
    this.authId = authId;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
