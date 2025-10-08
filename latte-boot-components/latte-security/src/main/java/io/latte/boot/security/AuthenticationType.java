package io.latte.boot.security;

/**
 * AuthenticationType
 *
 * @author : wugz
 * @since : 2024/9/27
 */
public interface AuthenticationType {
  String AUTH_TOKEN_KEY = "Authorization";
  String AUTH_TOKEN_TYPE_BEARER = "Bearer";
  String AUTH_TOKEN_TYPE_BEARER_PREFIX = "Bearer ";
  String AUTH_TOKEN_TYPE_BASIC = "Basic";
  String AUTH_TOKEN_TYPE_BASIC_PREFIX = "Basic ";
  String AUTH_TOKEN_REFRESH_ACCESS_TOKEN = "Refresh-Access-Token";
  String ACCESS_TOKEN_TYPE = "AccessToken";
  String REFRESH_TOKEN_TYPE = "RefreshToken";
}

