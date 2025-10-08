package io.latte.boot.entity.cmd;

/**
 * EntityUpdateCommand
 *
 * @author : wugz
 * @since : 2021/12/16
 */
public interface EntityUpdateCommand extends ICommand {
  String getId();

  void setId(String id);

  Integer getVersion();

  void setVersion(Integer version);

  String getUpdateUser();

  void setUpdateUser(String updateUser);

  Object getUpdateTime();

  void setUpdateTime(Object updateTime);

  String getUpdateIp();

  void setUpdateIp(String updateIp);
}
