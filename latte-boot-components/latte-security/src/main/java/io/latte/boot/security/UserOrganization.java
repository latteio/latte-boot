package io.latte.boot.security;

import java.io.Serializable;

/**
 * 用户机构接口
 *
 * @author : wugz
 * @since : 2025/12/4
 */
public interface UserOrganization extends Serializable {
  String getId();

  String getCode();

  String getName();

  String getShortCode();

  String getShortName();

  Integer getIsPrimary();
}
