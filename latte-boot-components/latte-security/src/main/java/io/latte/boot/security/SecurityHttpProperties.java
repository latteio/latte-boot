package io.latte.boot.security;

/**
 * SecurityHttpProperties
 *
 * @author : wugz
 * @since : 2023/5/7
 */
public class SecurityHttpProperties {
  private SecurityHttpRequestProperties request;
  private SecurityLoginProperties login;

  public SecurityHttpProperties() {
    this.request = new SecurityHttpRequestProperties();
    this.login = new SecurityLoginProperties();
  }

  public SecurityHttpRequestProperties getRequest() {
    return request;
  }

  public void setRequest(SecurityHttpRequestProperties request) {
    this.request = request;
  }

  public SecurityLoginProperties getLogin() {
    return login;
  }

  public void setLogin(SecurityLoginProperties login) {
    this.login = login;
  }
}
