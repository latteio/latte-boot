package io.latte.boot.mapper.statement.entity;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityGetter;
import io.latte.boot.mapper.statement.entity.impl.EntityWhereBuilder;

import java.util.List;

/**
 * Update
 *
 * @author : wugz
 * @since : 2018/5/13
 */
public interface Update<E extends BaseEntity> extends Where<E>, IEntityMap {
  /**
   * 返回set表达式集
   *
   * @return
   */
  List<String> getSetExpressions();

  /**
   * UPDATE set
   *
   * @param getter set字段的getter方法
   * @param value  参数值
   */
  Update<E> set(EntityGetter<E> getter, Object value);

  /**
   * 构造where
   *
   * @return
   */
  EntityWhereBuilder<E> where();
}
