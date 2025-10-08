package io.latte.boot.web.http.login;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * LoginCodeCommand
 *
 * @author : wugz
 * @since : 2022/10/21
 */
public class LoginCodeCommand implements Serializable {
  /**
   * 登录类型
   *
   * @see AuthMethodEnum
   */
  @NotBlank
  private String type;

  /**
   * 主体: 账号/手机号/邮箱
   */
  private String principal;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPrincipal() {
    return principal;
  }

  public void setPrincipal(String principal) {
    this.principal = principal;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.getClass().getSimpleName());
    builder.append("(");
    builder.append("type=").append(type).append(", ");
    builder.append("principal=").append(principal);
    builder.append(")");
    return builder.toString();
  }
}


