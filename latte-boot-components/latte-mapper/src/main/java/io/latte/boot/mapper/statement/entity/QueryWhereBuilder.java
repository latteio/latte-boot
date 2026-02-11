package io.latte.boot.mapper.statement.entity;

import io.latte.boot.mapper.EntityGetter;
import io.latte.boot.mapper.statement.entity.impl.EntityQueryWhereExists;
import io.latte.boot.mapper.statement.entity.impl.EntityQueryWhereGroup;

import java.io.Serializable;
import java.util.List;

/**
 * QueryWhereBuilder
 *
 * @author : wugz
 * @since : 2021/9/3
 */
public interface QueryWhereBuilder<E> extends Serializable {
  /**
   * 逻辑and
   *
   * @return
   */
  QueryWhereBuilder<E> and();

  /**
   * 逻辑or
   *
   * @return
   */
  QueryWhereBuilder<E> or();

  /**
   * 创建大于匹配语句
   *
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> gt(EntityGetter<T> getter, Object value);

  /**
   * 当条件为真, 则创建大于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> gt(boolean condition, EntityGetter<T> getter, Object value);

  /**
   * 创建大于或等于匹配语句
   *
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> ge(EntityGetter<T> getter, Object value);

  /**
   * 当条件为真, 则创建大于或等于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> ge(boolean condition, EntityGetter<T> getter, Object value);

  /**
   * 创建小于匹配语句
   *
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> lt(EntityGetter<T> getter, Object value);

  /**
   * 当条件为真, 则创建小于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> lt(boolean condition, EntityGetter<T> getter, Object value);

  /**
   * 创建小于或等于匹配语句
   *
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> le(EntityGetter<T> getter, Object value);

  /**
   * 当条件为真, 则创建小于或等于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> le(boolean condition, EntityGetter<T> getter, Object value);

  /**
   * 创建相等匹配语句
   *
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> eq(EntityGetter<T> getter, Object value);

  /**
   * 当条件为真, 则创建相等匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> eq(boolean condition, EntityGetter<T> getter, Object value);

  /**
   * 创建不相等匹配语句
   *
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> neq(EntityGetter<T> getter, Object value);

  /**
   * 当条件为真, 则创建不相等匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> neq(boolean condition, EntityGetter<T> getter, Object value);

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> like(EntityGetter<T> getter, Object value);

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> like(boolean condition, EntityGetter<T> getter, Object value);

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> leftLike(EntityGetter<T> getter, Object value);

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> leftLike(boolean condition, EntityGetter<T> getter, Object value);

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> rightLike(EntityGetter<T> getter, Object value);

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  <T> QueryWhereBuilder<E> rightLike(boolean condition, EntityGetter<T> getter, Object value);

  /**
   * 创建between语句
   *
   * @param getter
   * @param start
   * @param end
   * @param <T>
   * @return
   */
  <T> QueryWhereBuilder<E> between(EntityGetter<T> getter, Object start, Object end);

  /**
   * 如果条件为真, 则创建between语句
   *
   * @param condition
   * @param getter
   * @param start
   * @param end
   * @param <T>
   * @return
   */
  <T> QueryWhereBuilder<E> between(boolean condition, EntityGetter<T> getter, Object start, Object end);

  /**
   * 创建between语句
   *
   * @param getter
   * @param values
   * @param <T>
   * @return
   */
  <T> QueryWhereBuilder<E> between(EntityGetter<T> getter, List<?> values);

  /**
   * 创建in语句
   *
   * @param getter
   * @param values
   * @param <T>
   * @return
   */
  <T> QueryWhereBuilder<E> in(EntityGetter<T> getter, List<?> values);

  /**
   * 属性值为空
   *
   * @param getter
   * @param <T>
   * @return
   */
  <T> QueryWhereBuilder<E> isNull(EntityGetter<T> getter);

  /**
   * 属性值为空
   *
   * @param condition
   * @param getter
   * @param <T>
   * @return
   */
  <T> QueryWhereBuilder<E> isNull(boolean condition, EntityGetter<T> getter);

  /**
   * 属性值不为空
   *
   * @param getter
   * @param <T>
   * @return
   */
  <T> QueryWhereBuilder<E> isNotNull(EntityGetter<T> getter);

  /**
   * 属性值不为空
   *
   * @param condition
   * @param getter
   * @param <T>
   * @return
   */
  <T> QueryWhereBuilder<E> isNotNull(boolean condition, EntityGetter<T> getter);

  /**
   * 存在性判定
   *
   * @param entityClass
   * @param entityAlias
   * @param <T>
   * @return
   */
  <T> EntityQueryWhereExists<E, T> exists(Class<T> entityClass, String entityAlias);

  /**
   * 存在性判定
   *
   * @param condition
   * @param entityClass
   * @param entityAlias
   * @param <T>
   * @return
   */
  <T> EntityQueryWhereExists<E, T> exists(boolean condition, Class<T> entityClass, String entityAlias);

  /**
   * 非存在性判定
   *
   * @param entityClass
   * @param entityAlias
   * @param <T>
   * @return
   */
  <T> EntityQueryWhereExists<E, T> notExists(Class<T> entityClass, String entityAlias);

  /**
   * 非存在性判定
   *
   * @param condition
   * @param entityClass
   * @param entityAlias
   * @param <T>
   * @return
   */
  <T> EntityQueryWhereExists<E, T> notExists(boolean condition, Class<T> entityClass, String entityAlias);

  /**
   * 表达式组
   *
   * @return
   */
  EntityQueryWhereGroup<E> group();

  /**
   * endWhere
   *
   * @return
   */
  QuerySelection<E> endWhere();
}
