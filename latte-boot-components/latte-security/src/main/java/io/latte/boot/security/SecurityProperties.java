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
  private SecurityLoginProperties login;
  private SecurityHttpRequestProperties httpRequest;

  public SecurityProperties() {
    this.auth = new SecurityAuthProperties();
    this.login = new SecurityLoginProperties();
    this.httpRequest = new SecurityHttpRequestProperties();
  }

  public SecurityAuthProperties getAuth() {
    return auth;
  }

  public void setAuth(SecurityAuthProperties auth) {
    this.auth = auth;
  }

  public SecurityLoginProperties getLogin() {
    return login;
  }

  public void setLogin(SecurityLoginProperties login) {
    this.login = login;
  }

  public SecurityHttpRequestProperties getHttpRequest() {
    return httpRequest;
  }

  public void setHttpRequest(SecurityHttpRequestProperties httpRequest) {
    this.httpRequest = httpRequest;
  }
}
