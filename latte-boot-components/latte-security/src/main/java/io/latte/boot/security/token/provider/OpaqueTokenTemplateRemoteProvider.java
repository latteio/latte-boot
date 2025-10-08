package io.latte.boot.security.token.provider;

import io.latte.boot.security.AuthenticationUser;
import io.latte.boot.security.token.OpaqueToken;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * OpaqueTokenTemplateRemoteProvider
 *
 * @author : wugz
 * @since : 2024/9/28
 */
public interface OpaqueTokenTemplateRemoteProvider {
  /**
   * 创建token
   *
   * @param authenticationUser
   * @return
   */
  OpaqueToken createToken(AuthenticationUser authenticationUser);

  /**
   * 解析token
   *
   * @param request
   * @return
   */
  OpaqueToken parseToken(HttpServletRequest request);

  /**
   * 解析token
   *
   * @param requestMap
   * @return
   */
  OpaqueToken parseToken(Map<String, Object> requestMap);

  /**
   * 校验Token
   *
   * @param tokenType
   * @param token
   * @param isRefreshToken
   * @return
   */
  OpaqueToken validateToken(String tokenType, String token, boolean isRefreshToken);

  /**
   * 刷新token
   *
   * @param tokenType
   * @param refreshToken
   * @return
   */
  OpaqueToken refreshToken(String tokenType, String refreshToken);

  /**
   * 按token获取用户信息
   *
   * @param accessToken
   * @return
   */
  AuthenticationUser getAuthenticationUser(String accessToken);

  /**
   * 移除token
   *
   * @param id
   */
  void removeTokenById(String id);

  /**
   * 移除token
   *
   * @param accessToken
   */
  void removeToken(String accessToken);
}
