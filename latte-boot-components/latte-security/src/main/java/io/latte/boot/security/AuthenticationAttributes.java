package io.latte.boot.security;

/**
 * AuthenticationAttributes
 *
 * @author : wugz
 * @since : 2021/10/14
 */
public interface AuthenticationAttributes {
  String AUTH_REQUIRED = "io.latte.boot.security.authentication.AUTH_REQUIRED";
  String AUTH_USE_TOKEN = "io.latte.boot.security.authentication.USE_TOKEN";
  String AUTH_SECURITY_CODE_CACHE = "io.latte.boot.security.authentication.CACHE_SECURITY_CODE";

  /* Redis keys */
  String AUTH_ACCESS_TOKEN_MAP = "io:latte:boot:security:authentication:CACHE_ACCESS_TOKEN";
  String AUTH_REFRESH_TOKEN_MAP = "io:latte:boot:security:authentication:CACHE_REFRESH_TOKEN";
  String AUTH_PERMS_MAP = "io:latte:boot:security:authentication:CACHE_PERMS";
}
