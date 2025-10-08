package io.latte.boot.security.token.provider;

import io.latte.boot.security.*;
import io.latte.boot.security.token.OpaqueToken;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * OpaqueTokenTemplateProviderUsingRedission
 *
 * @author : wugz
 * @since : 2024/9/27
 */
public class OpaqueTokenTemplateProviderUsingRedission extends OpaqueTokenTemplateAbstract {
  private static final Logger LOGGER = LoggerFactory.getLogger(OpaqueTokenTemplateProviderUsingRedission.class);
  private static final String REDISSION_LOCKING_KEY = "redission_locking_key@opaque_token_";

  /**
   * (AccessToken: RefreshToken)
   */
  private final RMapCache<String, String> accessTokenMap;
  /**
   * (RefreshToken: CachedAuthenticationUser)
   */
  private final RMapCache<String, CachedAuthenticationUser> refreshTokenMap;

  private final RLock redissonLock;

  /**
   * 构造函数
   *
   * @param securityAuthProperties Token Properties
   * @param redissonClient         Redisson Client
   */
  public OpaqueTokenTemplateProviderUsingRedission(SecurityAuthProperties securityAuthProperties, RedissonClient redissonClient) {
    super(securityAuthProperties);
    this.accessTokenMap = redissonClient.getMapCache(AuthenticationAttributes.AUTH_ACCESS_TOKEN_MAP);
    this.refreshTokenMap = redissonClient.getMapCache(AuthenticationAttributes.AUTH_REFRESH_TOKEN_MAP);
    this.redissonLock = redissonClient.getLock(REDISSION_LOCKING_KEY);
  }

  /**
   * 创建token
   *
   * @param authenticationUser
   * @return
   */
  public OpaqueToken createToken(AuthenticationUser authenticationUser) {
    /* 以不同token记录同一用户的登录信息, 能满足同一用户不同客户端登录的需求 */
    /* 若要保证所有客户端使用同一token, 则设置maxSessions=1即可  */
    Integer maxSessions = getSecurityAuthProperties().getMaxSessions();
    if (maxSessions == 1) {
      this.removeTokenByUUID(authenticationUser.uuid());
    }

    /* 创建token */
    final OpaqueToken opaqueToken = createTokenInternal();
    this.lockAndRunTask(() -> {
      accessTokenMap.fastPut(opaqueToken.getAccessToken(), opaqueToken.getRefreshToken(), getSecurityAuthProperties().getAccessTokenDuration(), TimeUnit.SECONDS);
      refreshTokenMap.fastPut(opaqueToken.getRefreshToken(), new CachedAuthenticationUser(opaqueToken.getAccessToken(), authenticationUser), getSecurityAuthProperties().getRefreshTokenDuration(), TimeUnit.SECONDS);
    });

    return opaqueToken;
  }

  /**
   * 校验Token
   *
   * @param tokenType
   * @param token
   * @param isRefreshToken
   * @return
   */
  public OpaqueToken validateToken(String tokenType, String token, boolean isRefreshToken) {
    if (!StringUtils.hasText(token)) {
      return OpaqueToken.BAD_TOKEN;
    }

    /* 1.Token有效性验证 */
    boolean exists = isRefreshToken
        ? refreshTokenMap.containsKey(token)
        : accessTokenMap.containsKey(token);

    /* 2.返回Token */
    return (exists && isRefreshToken)
        ? refreshToken(tokenType, token)
        : new OpaqueToken(tokenType, token, null, null, exists);
  }

  /**
   * 刷新token
   *
   * @param tokenType
   * @param refreshToken
   * @return
   */
  public OpaqueToken refreshToken(String tokenType, String refreshToken) {
    if (refreshTokenMap.containsKey(refreshToken)) {
      String accessToken = UUID.randomUUID().toString();
      OpaqueToken opaqueToken = new OpaqueToken(tokenType, accessToken, refreshToken, new Date(), true);
      opaqueToken.setUpdated(true);
      this.lockAndRunTask(() -> {
        accessTokenMap.fastPut(opaqueToken.getAccessToken(), refreshToken, getSecurityAuthProperties().getAccessTokenDuration(), TimeUnit.SECONDS);
      });
      return opaqueToken;
    }

    return OpaqueToken.BAD_TOKEN;
  }

  /**
   * 按token获取用户信息
   *
   * @param accessToken
   * @return
   */
  public AuthenticationUser getAuthenticationUser(String accessToken) {
    OpaqueToken opaqueToken = validateToken(AuthenticationType.AUTH_TOKEN_TYPE_BEARER, accessToken, false);
    if (opaqueToken.isValid()) {
      String refreshToken = accessTokenMap.get(opaqueToken.getAccessToken());
      return refreshTokenMap.get(refreshToken).getAuthenticationUser();
    }

    return null;
  }

  /**
   * 移除token
   *
   * @param uuid
   */
  public void removeTokenByUUID(String uuid) {
    this.lockAndRunTask(() -> {
      String accessToken = null;
      String refreshToken = null;
      for (Map.Entry<String, CachedAuthenticationUser> entry : refreshTokenMap.entrySet()) {
        if (entry.getValue().getAuthenticationUser().uuid().equals(uuid)) {
          accessToken = entry.getValue().getAccessToken();
          refreshToken = entry.getKey();
          break;
        }
      }
      if (StringUtils.hasText(accessToken)) {
        accessTokenMap.remove(accessToken);
      }
      if (StringUtils.hasText(refreshToken)) {
        refreshTokenMap.remove(refreshToken);
      }
    });
  }

  /**
   * 移除token
   *
   * @param accessToken
   */
  public void removeToken(String accessToken) {
    this.lockAndRunTask(() -> {
      String refreshToken = null;
      if (StringUtils.hasText(accessToken)) {
        refreshToken = accessTokenMap.get(accessToken);
        accessTokenMap.remove(accessToken);
      }
      if (StringUtils.hasText(refreshToken)) {
        refreshTokenMap.remove(refreshToken);
      }
    });
  }

  /**
   * 加锁并执行任务
   *
   * @param runnable
   */
  protected void lockAndRunTask(Runnable runnable) {
    try {
      if (redissonLock.tryLock()) {
        runnable.run();
      }
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage());
    } finally {
      try {
        if (redissonLock.isLocked() && redissonLock.isHeldByCurrentThread()) {
          redissonLock.unlock();
        }
      } catch (Exception ex) {
        LOGGER.error(ex.getMessage());
      }
    }
  }
}
