package io.latte.boot.entity.cmd;

import io.latte.boot.entity.TreeEntity;

/**
 * EntityTreeCommand
 *
 * @author : wugz
 * @since : 2022/2/13
 */
public interface EntityTreeCommand extends EntityCommand, TreeEntity {
  String getParentId();

  void setParentId(String parentId);

  String getParentPath();

  void setParentPath(String parentPath);

  Integer getSortNo();

  void setSortNo(Integer sortNo);
}
