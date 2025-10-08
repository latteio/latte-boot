package io.latte.boot.security.token.provider;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.latte.boot.security.AuthenticationType;
import io.latte.boot.security.AuthenticationUser;
import io.latte.boot.security.CachedAuthenticationUser;
import io.latte.boot.security.SecurityAuthProperties;
import io.latte.boot.security.token.OpaqueToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * OpaqueTokenTemplateProviderUsingCaffeine
 *
 * @author : wugz
 * @since : 2024/9/28
 */
public class OpaqueTokenTemplateProviderUsingCaffeine extends OpaqueTokenTemplateAbstract {
  private final Logger logger = LoggerFactory.getLogger(OpaqueTokenTemplateProviderUsingCaffeine.class);

  /**
   * (AccessToken: RefreshToken)
   */
  private final Cache<String, String> accessTokenMap;
  /**
   * (RefreshToken: CachedAuthenticationUser)
   */
  private final Cache<String, CachedAuthenticationUser> refreshTokenMap;

  private final ReentrantLock localLock;

  /**
   * 构造函数
   *
   * @param securityAuthProperties Token Properties
   */
  public OpaqueTokenTemplateProviderUsingCaffeine(SecurityAuthProperties securityAuthProperties) {
    super(securityAuthProperties);
    this.accessTokenMap = Caffeine.newBuilder()
        .expireAfterWrite(securityAuthProperties.getAccessTokenDuration(), TimeUnit.SECONDS)
        .recordStats()
        .build();

    this.refreshTokenMap = Caffeine.newBuilder()
        .expireAfterWrite(securityAuthProperties.getRefreshTokenDuration(), TimeUnit.SECONDS)
        .recordStats()
        .build();

    this.localLock = new ReentrantLock();
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
      accessTokenMap.put(opaqueToken.getAccessToken(), opaqueToken.getRefreshToken());
      refreshTokenMap.put(opaqueToken.getRefreshToken(), new CachedAuthenticationUser(opaqueToken.getAccessToken(), authenticationUser));
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
        ? refreshTokenMap.asMap().containsKey(token)
        : accessTokenMap.asMap().containsKey(token);

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
    if (refreshTokenMap.asMap().containsKey(refreshToken)) {
      String accessToken = UUID.randomUUID().toString();
      OpaqueToken opaqueToken = new OpaqueToken(tokenType, accessToken, refreshToken, new Date(), true);
      opaqueToken.setUpdated(true);
      this.lockAndRunTask(() -> {
        accessTokenMap.put(opaqueToken.getAccessToken(), refreshToken);
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
      String refreshToken = accessTokenMap.asMap().get(opaqueToken.getAccessToken());
      return refreshTokenMap.asMap().get(refreshToken).getAuthenticationUser();
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
      for (Map.Entry<String, CachedAuthenticationUser> entry : refreshTokenMap.asMap().entrySet()) {
        if (entry.getValue().getAuthenticationUser().uuid().equals(uuid)) {
          accessToken = entry.getValue().getAccessToken();
          refreshToken = entry.getKey();
          break;
        }
      }
      if (StringUtils.hasText(accessToken)) {
        accessTokenMap.asMap().remove(accessToken);
      }
      if (StringUtils.hasText(refreshToken)) {
        refreshTokenMap.asMap().remove(refreshToken);
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
        refreshToken = accessTokenMap.asMap().get(accessToken);
        accessTokenMap.asMap().remove(accessToken);
      }
      if (StringUtils.hasText(refreshToken)) {
        refreshTokenMap.asMap().remove(refreshToken);
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
      if (localLock.tryLock()) {
        runnable.run();
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage());
    } finally {
      try {
        if (localLock.isLocked() && localLock.isHeldByCurrentThread()) {
          localLock.unlock();
        }
      } catch (Exception ex) {
        logger.error(ex.getMessage());
      }
    }
  }
}
