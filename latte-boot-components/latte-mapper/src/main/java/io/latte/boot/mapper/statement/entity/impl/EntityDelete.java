package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.statement.entity.Delete;

/**
 * EntityDelete
 *
 * @author : wugz
 * @since : 2018/5/13
 */
public class EntityDelete<E extends BaseEntity> extends EntityWhere<E> implements Delete<E> {
  /**
   * 构造函数
   *
   * @param entityClass
   */
  public EntityDelete(Class<E> entityClass) {
    super(entityClass);
  }
}