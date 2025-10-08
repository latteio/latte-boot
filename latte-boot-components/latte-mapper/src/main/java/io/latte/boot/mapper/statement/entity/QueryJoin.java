package io.latte.boot.mapper.statement.entity;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityGetter;

/**
 * QueryJoin
 *
 * @author : wugz
 * @since : 2021/7/6
 */
public interface QueryJoin<E, T extends BaseEntity> {
  /**
   * 连接条件
   *
   * @param destGetter 关联目标属性Getter
   * @param srcGetter  关联源属性Getter
   * @return
   */
  Query<E> on(EntityGetter<T> destGetter, EntityGetter<E> srcGetter);

  /**
   * 构造连接条件
   *
   * @return
   */
  String build();
}
