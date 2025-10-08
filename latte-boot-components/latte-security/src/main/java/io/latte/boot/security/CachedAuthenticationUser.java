package io.latte.boot.security;

import java.io.Serializable;

/**
 * CachedAuthenticationUser
 *
 * @author : wugz
 * @since : 2024/9/28
 */
public class CachedAuthenticationUser implements Serializable {
  private String accessToken;
  private AuthenticationUser authenticationUser;

  public CachedAuthenticationUser() {
  }

  public CachedAuthenticationUser(String accessToken, AuthenticationUser authenticationUser) {
    this.accessToken = accessToken;
    this.authenticationUser = authenticationUser;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public AuthenticationUser getAuthenticationUser() {
    return authenticationUser;
  }

  public void setAuthenticationUser(AuthenticationUser authenticationUser) {
    this.authenticationUser = authenticationUser;
  }
}
