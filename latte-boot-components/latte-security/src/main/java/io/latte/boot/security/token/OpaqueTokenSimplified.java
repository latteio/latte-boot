package io.latte.boot.security.token;

import java.io.Serializable;

/**
 * OpaqueTokenSimplified
 *
 * @author : wugz
 * @since : 2024/9/28
 */
public class OpaqueTokenSimplified implements Serializable {
  private String type;
  private String accessToken;
  private String refreshToken;

  public OpaqueTokenSimplified() {
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
