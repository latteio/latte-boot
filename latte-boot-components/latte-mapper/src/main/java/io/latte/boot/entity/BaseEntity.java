package io.latte.boot.entity;

import java.io.Serializable;

/**
 * BaseEntity
 *
 * @author : wugz
 * @since : 2018/4/15
 */
public interface BaseEntity extends Serializable {
  String getId();

  void setId(String id);

  String getCreateUser();

  void setCreateUser(String createUser);

  Object getCreateTime();

  void setCreateTime(Object createTime);

  String getCreateIp();

  void setCreateIp(String createIp);

  String getUpdateUser();

  void setUpdateUser(String updateUser);

  Object getUpdateTime();

  void setUpdateTime(Object updateTime);

  String getUpdateIp();

  void setUpdateIp(String updateIp);

  Integer getVersion();

  void setVersion(Integer version);

  Integer getIsDeleted();

  void setIsDeleted(Integer isDeleted);
}
