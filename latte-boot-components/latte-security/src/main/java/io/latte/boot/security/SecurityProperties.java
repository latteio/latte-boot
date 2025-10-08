package io.latte.boot.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SecurityProperties
 *
 * @author : wugz
 * @since : 2021/11/5
 */
@ConfigurationProperties(prefix = "application.security")
public class SecurityProperties {
  private SecurityAuthProperties auth;
  private SecurityHttpProperties http;

  public SecurityProperties() {
    this.auth = new SecurityAuthProperties();
    this.http = new SecurityHttpProperties();
  }

  public SecurityAuthProperties getAuth() {
    return auth;
  }

  public void setAuth(SecurityAuthProperties auth) {
    this.auth = auth;
  }

  public SecurityHttpProperties getHttp() {
    return http;
  }

  public void setHttp(SecurityHttpProperties http) {
    this.http = http;
  }

}
