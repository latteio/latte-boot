package io.latte.boot.entity;

import java.io.Serializable;

/**
 * TreeEntity
 *
 * @author : wugz
 * @since : 2018/7/15
 */
public interface TreeEntity extends Serializable {
  String getId();

  void setId(String id);

  String getParentId();

  void setParentId(String parentId);

  String getParentPath();

  void setParentPath(String parentPath);

  Integer getSortNo();

  void setSortNo(Integer sortNo);
}
