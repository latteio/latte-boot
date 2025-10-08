package io.latte.boot.entity.cmd;

/**
 * EntityInsertCommand
 *
 * @author : wugz
 * @since : 2021/12/16
 */
public interface EntityInsertCommand extends ICommand {
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
}
