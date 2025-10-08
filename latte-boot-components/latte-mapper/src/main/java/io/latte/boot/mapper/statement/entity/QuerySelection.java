package io.latte.boot.mapper.statement.entity;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityGetter;

/**
 * QuerySelection
 *
 * @author : wugz
 * @since : 2024/10/18
 */
public interface QuerySelection<E> {
  /**
   * 查询
   *
   * @param expr 查询表达式
   */
  <T> QuerySelection<E> select(String expr);

  /**
   * 查询字段getter表达式集
   *
   * @param getter 字段属性Getter
   */
  <T> QuerySelection<E> select(EntityGetter<T> getter);

  /**
   * 查询字段getter表达式集
   *
   * @param getter 字段属性Getter
   * @param asName 查询别名
   * @return
   */
  <T> QuerySelection<E> select(EntityGetter<T> getter, String asName);

  /**
   * 查询字段getter表达式集
   *
   * @param getter 字段属性Getter
   * @param asName 查询别名
   * @return
   */
  <T> QuerySelection<E> selectCount(EntityGetter<T> getter, String asName);

  /**
   * 标量子查询
   *
   * @param destEntityClass  目标表实体类
   * @param destEntityAlias  目标表别名
   * @param destSelectGetter 目标表查询标量字段
   * @return
   */
  <T extends BaseEntity> ScalarQuery<E, T> selectScalar(Class<T> destEntityClass, String destEntityAlias, EntityGetter<T> destSelectGetter);

  /**
   * 标量子查询
   *
   * @param destEntityClass  目标表实体类
   * @param destEntityAlias  目标表别名
   * @param destSelectGetter 目标表查询标量字段
   * @param asName           目标表查询标量字段的别名(xx AS xxx)
   * @return
   */
  <T extends BaseEntity> ScalarQuery<E, T> selectScalar(Class<T> destEntityClass, String destEntityAlias, EntityGetter<T> destSelectGetter, String asName);

  /**
   * 分组
   *
   * @param expr 分组表达式
   */
  <T> QuerySelection<E> groupBy(String expr);

  /**
   * 分组
   *
   * @param getter 字段属性Getter
   */
  <T> QuerySelection<E> groupBy(EntityGetter<T> getter);

  /**
   * 排序
   *
   * @param expr 排序表达式
   */
  <T> QuerySelection<E> orderBy(String expr);

  /**
   * 升序排序
   *
   * @param getter 字段属性Getter
   */
  <T> QuerySelection<E> orderAsc(EntityGetter<T> getter);

  /**
   * 降序排序
   *
   * @param getter 字段属性Getter
   */
  <T> QuerySelection<E> orderDesc(EntityGetter<T> getter);

  /**
   * 返回到 Query
   *
   * @return
   */
  Query<E> endQuery();
}
