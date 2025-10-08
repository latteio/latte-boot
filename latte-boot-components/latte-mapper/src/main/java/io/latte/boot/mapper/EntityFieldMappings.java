package io.latte.boot.mapper;

/**
 * EntityFieldMappings
 *
 * @author : wugz
 * @since : 2024/10/20
 */
public interface EntityFieldMappings {
  String getId();

  void setId(String id);

  String getParentId();

  void setParentId(String parentId);

  String getParentPath();

  void setParentPath(String parentPath);

  String getSortNo();

  void setSortNo(String sortNo);

  String getRemark();

  void setRemark(String remark);

  String getVersion();

  void setVersion(String version);

  String getCreateUser();

  void setCreateUser(String createUser);

  String getCreateTime();

  void setCreateTime(String createTime);

  String getCreateIp();

  void setCreateIp(String createIp);

  String getUpdateUser();

  void setUpdateUser(String updateUser);

  String getUpdateTime();

  void setUpdateTime(String updateTime);

  String getUpdateIp();

  void setUpdateIp(String updateIp);

  String getSecretLevel();

  void setSecretLevel(String secretLevel);

  String getIsDeleted();

  void setIsDeleted(String isDeleted);
}
