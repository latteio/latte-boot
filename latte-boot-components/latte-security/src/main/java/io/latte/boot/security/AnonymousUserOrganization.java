package io.latte.boot.security;

import io.latte.boot.web.http.message.Constants;

/**
 * AnonymousUserOrganization
 *
 * @author : wugz
 * @since :  2025/12/4
 */
public class AnonymousUserOrganization implements UserOrganization {
  private final String id;
  private final String code;
  private final String name;
  private final String shortCode;
  private final String shortName;
  private final Integer isPrimary;

  public AnonymousUserOrganization() {
    this.id = "0";
    this.code = Constants.ANONYMOUS;
    this.name = Constants.ANONYMOUS;
    this.shortCode = Constants.ANONYMOUS;
    this.shortName = Constants.ANONYMOUS;
    this.isPrimary = 1;
  }

  public String getId() {
    return this.id;
  }

  public String getCode() {
    return this.code;
  }

  public String getName() {
    return this.name;
  }

  public String getShortCode() {
    return this.shortCode;
  }

  public String getShortName() {
    return this.shortName;
  }

  public Integer getIsPrimary() {
    return this.isPrimary;
  }
}
