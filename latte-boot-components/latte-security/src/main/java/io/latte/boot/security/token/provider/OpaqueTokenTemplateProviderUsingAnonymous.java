package io.latte.boot.security.token.provider;

import io.latte.boot.security.AnonymousUser;
import io.latte.boot.security.AuthenticationUser;
import io.latte.boot.security.SecurityAuthProperties;
import io.latte.boot.security.token.OpaqueToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * OpaqueTokenTemplateProviderUsingAnonymous
 *
 * @author : wugz
 * @since : 2024/9/28
 */
public class OpaqueTokenTemplateProviderUsingAnonymous extends OpaqueTokenTemplateAbstract {
  private final Logger logger = LoggerFactory.getLogger(OpaqueTokenTemplateProviderUsingAnonymous.class);
  private final ReentrantLock localLock;

  /**
   * 构造函数
   *
   * @param securityAuthProperties Token Properties
   */
  public OpaqueTokenTemplateProviderUsingAnonymous(SecurityAuthProperties securityAuthProperties) {
    super(securityAuthProperties);
    this.localLock = new ReentrantLock();
  }

  /**
   * 创建token
   *
   * @param authenticationUser
   * @return
   */
  public OpaqueToken createToken(AuthenticationUser authenticationUser) {
    return OpaqueToken.ANON_TOKEN;
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
    return OpaqueToken.ANON_TOKEN;
  }

  /**
   * 刷新token
   *
   * @param tokenType
   * @param refreshToken
   * @return
   */
  public OpaqueToken refreshToken(String tokenType, String refreshToken) {
    return OpaqueToken.ANON_TOKEN;
  }

  /**
   * 按token获取用户信息
   *
   * @param accessToken
   * @return
   */
  public AuthenticationUser getAuthenticationUser(String accessToken) {
    return AnonymousUser.INSTANCE;
  }

  /**
   * 移除token
   *
   * @param uuid
   */
  public void removeTokenByUUID(String uuid) {
  }

  /**
   * 移除token
   *
   * @param accessToken
   */
  public void removeToken(String accessToken) {
  }
}
