package io.latte.boot.mapper.statement.entity;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityGetter;

import java.io.Serializable;

/**
 * ScalarQuery
 *
 * @author : wugz
 * @desc: 标量子查询
 * @since : 2023/7/22
 */
public interface ScalarQuery<E, T extends BaseEntity> extends Serializable {
  /**
   * 标量子查询匹配条件
   *
   * @param destGetter 目标表比较字段
   * @param srcGetter  原表比较字段
   * @return
   */
  QuerySelection<E> on(EntityGetter<T> destGetter, EntityGetter<E> srcGetter);
}
