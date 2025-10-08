package io.latte.boot.entity.cmd;

/**
 * EntityCommand
 *
 * @author : wugz
 * @since : 2021/12/16
 */
public interface EntityCommand extends EntityInsertCommand, EntityUpdateCommand {
  String getId();

  void setId(String id);

  Integer getVersion();

  void setVersion(Integer version);

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
}
