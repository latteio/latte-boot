package io.latte.boot.entity.cmd;

import io.latte.boot.entity.TreeEntity;

/**
 * EntityTreeInsertCommand
 *
 * @author : wugz
 * @since : 2021/12/16
 */
public interface EntityTreeInsertCommand extends EntityInsertCommand, TreeEntity {
  String getParentId();

  void setParentId(String parentId);

  String getParentPath();

  void setParentPath(String parentPath);

  Integer getSortNo();

  void setSortNo(Integer sortNo);
}
