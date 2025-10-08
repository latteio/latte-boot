package io.latte.boot.security;

import io.latte.boot.security.token.TokenStoreType;

/**
 * SecurityAuthProperties
 *
 * @author : wugz
 * @since : 2024/9/27
 */
public class SecurityAuthProperties {
  /**
   * 是否启用
   */
  private boolean enabled = false;

  /**
   * 使用token进行认证
   */
  private boolean useToken = false;

  /**
   * token单用户最大会话数
   */
  private Integer maxSessions = 1;

  /*
   * token有效时长(毫秒)
   */
  private Long accessTokenDuration = 14_400L;
  private Long refreshTokenDuration = 8_640_000L;

  /*
   * 存储缓存类型
   */
  private TokenStoreType tokenStore = TokenStoreType.MAP;

  /**
   * 构造函数
   */
  public SecurityAuthProperties() {
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isUseToken() {
    return useToken;
  }

  public void setUseToken(boolean useToken) {
    this.useToken = useToken;
  }

  public Integer getMaxSessions() {
    return maxSessions;
  }

  public void setMaxSessions(Integer maxSessions) {
    this.maxSessions = maxSessions;
  }

  public Long getAccessTokenDuration() {
    return accessTokenDuration;
  }

  public void setAccessTokenDuration(Long accessTokenDuration) {
    this.accessTokenDuration = accessTokenDuration;
  }

  public Long getRefreshTokenDuration() {
    return refreshTokenDuration;
  }

  public void setRefreshTokenDuration(Long refreshTokenDuration) {
    this.refreshTokenDuration = refreshTokenDuration;
  }

  public TokenStoreType getTokenStore() {
    return tokenStore;
  }

  public void setTokenStore(TokenStoreType tokenStore) {
    this.tokenStore = tokenStore;
  }

}
