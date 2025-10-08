package io.latte.boot.security.token;

import io.latte.boot.security.AuthenticationType;

import java.io.Serializable;
import java.util.Date;

/**
 * 不透明令牌
 *
 * @author : wugz
 * @since : 2024/9/27
 */
public class OpaqueToken implements Serializable {
  public static final OpaqueToken BAD_TOKEN = new OpaqueToken(
      null,
      null,
      null,
      null,
      false);

  public static final OpaqueToken ANON_TOKEN = new OpaqueToken(
      AuthenticationType.AUTH_TOKEN_TYPE_BEARER,
      "",
      "",
      new Date(),
      true);

  /* Bearer / Basic */
  private final String type;
  private final String accessToken;
  private final String refreshToken;
  private final Date createTime;
  private final boolean valid;
  private boolean updated;

  public OpaqueToken(String type,
                     String accessToken,
                     String refreshToken,
                     Date createTime,
                     boolean valid) {
    this.type = type;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.createTime = createTime;
    this.valid = valid;
    this.updated = false;
  }

  public String getType() {
    return type;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public boolean isValid() {
    return valid;
  }

  public boolean isUpdated() {
    return updated;
  }

  public void setUpdated(boolean updated) {
    this.updated = updated;
  }
}
