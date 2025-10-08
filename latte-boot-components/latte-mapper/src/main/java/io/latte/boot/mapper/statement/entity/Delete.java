package io.latte.boot.mapper.statement.entity;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.statement.entity.impl.EntityWhereBuilder;

/**
 * Delete
 *
 * @author : wugz
 * @since : 2018/5/14
 */
public interface Delete<E extends BaseEntity> extends Where<E>, IEntityMap {
  /**
   * 构造where
   *
   * @return
   */
  EntityWhereBuilder<E> where();
}
