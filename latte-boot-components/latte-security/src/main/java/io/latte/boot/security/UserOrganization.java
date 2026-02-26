package io.latte.boot.security;

/**
 * UserOrganization
 *
 * @author : wugz
 * @since :  2025/12/4
 */
public class UserOrganization implements GrantedOrganization {
  private final String id;
  private final String code;
  private final String name;
  private final String shortCode;
  private final String shortName;
  private final Integer isPrimary;

  public UserOrganization(String id,
                          String code,
                          String name,
                          String shortCode,
                          String shortName,
                          Integer isPrimary) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.shortCode = shortCode;
    this.shortName = shortName;
    this.isPrimary = isPrimary;
  }

  public UserOrganization(GrantedOrganization organization) {
    this(organization.getId(),
        organization.getCode(),
        organization.getName(),
        organization.getShortCode(),
        organization.getShortName(),
        organization.getIsPrimary());
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
