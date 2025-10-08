package io.latte.boot.security;

import io.latte.boot.web.http.message.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * 用户权限
 *
 * @author : wugz
 * @since : 2023/8/23
 */
public final class UserAuthority implements GrantedAuthority {
  private String authority;

  /**
   * 构造函数
   */
  public UserAuthority() {
    this(Constants.UNDEFINED);
  }

  /**
   * 构造函数
   *
   * @param authority
   */
  public UserAuthority(String authority) {
    Assert.hasText(authority, "A granted authority textual representation is required");
    this.authority = authority;
  }

  public String getAuthority() {
    return this.authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else {
      return obj instanceof UserAuthority && this.authority.equals(((UserAuthority) obj).authority);
    }
  }

  public int hashCode() {
    return this.authority.hashCode();
  }

  public String toString() {
    return this.authority;
  }

}
