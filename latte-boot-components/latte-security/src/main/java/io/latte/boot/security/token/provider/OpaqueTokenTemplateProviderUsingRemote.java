package io.latte.boot.security.token.provider;

import io.latte.boot.security.AuthenticationUser;
import io.latte.boot.security.SecurityAuthProperties;
import io.latte.boot.security.token.OpaqueToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * OpaqueTokenTemplateProviderUsingRemote
 *
 * @author : wugz
 * @since : 2024/9/28
 */
public class OpaqueTokenTemplateProviderUsingRemote extends OpaqueTokenTemplateAbstract {
  private final Logger logger = LoggerFactory.getLogger(OpaqueTokenTemplateProviderUsingRemote.class);
  private final OpaqueTokenTemplateRemoteProvider opaqueTokenTemplateRemoteProvider;
  private final ReentrantLock localLock;

  /**
   * 构造函数
   *
   * @param securityAuthProperties            Token Properties
   * @param opaqueTokenTemplateRemoteProvider OpaqueTokenTemplateRemoteProvider
   */
  public OpaqueTokenTemplateProviderUsingRemote(SecurityAuthProperties securityAuthProperties, OpaqueTokenTemplateRemoteProvider opaqueTokenTemplateRemoteProvider) {
    super(securityAuthProperties);
    this.opaqueTokenTemplateRemoteProvider = opaqueTokenTemplateRemoteProvider;
    this.localLock = new ReentrantLock();
  }

  /**
   * 创建token
   *
   * @param authenticationUser
   * @return
   */
  public OpaqueToken createToken(AuthenticationUser authenticationUser) {
    return opaqueTokenTemplateRemoteProvider.createToken(authenticationUser);
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
    return opaqueTokenTemplateRemoteProvider.validateToken(tokenType, token, isRefreshToken);
  }

  /**
   * 刷新token
   *
   * @param tokenType
   * @param refreshToken
   * @return
   */
  public OpaqueToken refreshToken(String tokenType, String refreshToken) {
    return opaqueTokenTemplateRemoteProvider.refreshToken(tokenType, refreshToken);
  }

  /**
   * 按token获取用户信息
   *
   * @param accessToken
   * @return
   */
  public AuthenticationUser getAuthenticationUser(String accessToken) {
    return opaqueTokenTemplateRemoteProvider.getAuthenticationUser(accessToken);
  }

  /**
   * 移除token
   *
   * @param uuid
   */
  public void removeTokenByUUID(String uuid) {
    opaqueTokenTemplateRemoteProvider.removeTokenById(uuid);
  }

  /**
   * 移除token
   *
   * @param accessToken
   */
  public void removeToken(String accessToken) {
    opaqueTokenTemplateRemoteProvider.removeToken(accessToken);
  }
}
