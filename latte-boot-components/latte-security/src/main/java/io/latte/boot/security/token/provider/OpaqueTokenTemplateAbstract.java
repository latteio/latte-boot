package io.latte.boot.security.token.provider;

import io.latte.boot.security.AuthenticationType;
import io.latte.boot.security.SecurityAuthProperties;
import io.latte.boot.security.token.OpaqueToken;
import io.latte.boot.security.token.OpaqueTokenTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * OpaqueTokenTemplateAbstract
 *
 * @author : wugz
 * @since : 2024/9/27
 */
public abstract class OpaqueTokenTemplateAbstract implements OpaqueTokenTemplate {
  private final SecurityAuthProperties securityAuthProperties;

  /**
   * 构造函数
   *
   * @param securityAuthProperties Token Properties
   */
  public OpaqueTokenTemplateAbstract(SecurityAuthProperties securityAuthProperties) {
    this.securityAuthProperties = securityAuthProperties;
  }

  protected SecurityAuthProperties getSecurityAuthProperties() {
    return securityAuthProperties;
  }

  /**
   * 解析token
   *
   * @param request 请求
   * @return
   */
  public OpaqueToken parseToken(HttpServletRequest request) {
    String authToken = request.getHeader(AuthenticationType.AUTH_TOKEN_KEY);
    String token = null;
    String tokenType = null;
    if (!StringUtils.hasText(authToken)) {
      authToken = (String) request.getAttribute(AuthenticationType.AUTH_TOKEN_KEY);
    }

    if (StringUtils.hasText(authToken) && authToken.startsWith(AuthenticationType.AUTH_TOKEN_TYPE_BEARER_PREFIX)) {
      token = authToken.substring(AuthenticationType.AUTH_TOKEN_TYPE_BEARER_PREFIX.length());
      tokenType = AuthenticationType.AUTH_TOKEN_TYPE_BEARER;
    }

    if (StringUtils.hasText(authToken) && authToken.startsWith(AuthenticationType.AUTH_TOKEN_TYPE_BASIC_PREFIX)) {
      token = authToken.substring(AuthenticationType.AUTH_TOKEN_TYPE_BASIC_PREFIX.length());
      tokenType = AuthenticationType.AUTH_TOKEN_TYPE_BASIC;
    }

    String refreshAccessToken = request.getHeader(AuthenticationType.AUTH_TOKEN_REFRESH_ACCESS_TOKEN);
    if (!StringUtils.hasText(refreshAccessToken)) {
      refreshAccessToken = (String) request.getAttribute(AuthenticationType.AUTH_TOKEN_REFRESH_ACCESS_TOKEN);
    }
    return validateToken(tokenType, token, Boolean.parseBoolean(refreshAccessToken));
  }

  /**
   * 解析token
   *
   * @param requestMap 请求Map
   * @return
   */
  public OpaqueToken parseToken(Map<String, Object> requestMap) {
    String authToken = null;
    String token = null;
    String tokenType = null;
    if (!ObjectUtils.isEmpty(requestMap.get(AuthenticationType.AUTH_TOKEN_KEY))) {
      authToken = requestMap.get(AuthenticationType.AUTH_TOKEN_KEY).toString();
    }

    if (StringUtils.hasText(authToken) && authToken.startsWith(AuthenticationType.AUTH_TOKEN_TYPE_BEARER_PREFIX)) {
      token = authToken.substring(AuthenticationType.AUTH_TOKEN_TYPE_BEARER_PREFIX.length());
      tokenType = AuthenticationType.AUTH_TOKEN_TYPE_BEARER;
    }

    if (StringUtils.hasText(authToken) && authToken.startsWith(AuthenticationType.AUTH_TOKEN_TYPE_BASIC_PREFIX)) {
      token = authToken.substring(AuthenticationType.AUTH_TOKEN_TYPE_BASIC_PREFIX.length());
      tokenType = AuthenticationType.AUTH_TOKEN_TYPE_BASIC;
    }

    String refreshAccessToken = (String) requestMap.get(AuthenticationType.AUTH_TOKEN_REFRESH_ACCESS_TOKEN);
    return validateToken(tokenType, token, Boolean.parseBoolean(refreshAccessToken));
  }

  /**
   * 生成token
   *
   * @return
   */
  protected OpaqueToken createTokenInternal() {
    return new OpaqueToken(AuthenticationType.AUTH_TOKEN_TYPE_BEARER,
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        new Date(),
        true);
  }
}
