package io.latte.boot.entity;

import java.io.Serializable;

/**
 * BaseSecurityEntity
 *
 * @author : wugz
 * @since : 2018/4/15
 */
public interface BaseSecurityEntity extends BaseEntity, Serializable {
  Integer getSecretLevel();

  void setSecretLevel(Integer secretLevel);
}
