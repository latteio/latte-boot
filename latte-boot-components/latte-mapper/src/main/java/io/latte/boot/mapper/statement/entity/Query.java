package io.latte.boot.mapper.statement.entity;

import io.latte.boot.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Query
 *
 * @author : wugz
 * @since : 2024/10/12
 */
public interface Query<E> extends IEntityMap, Serializable {
  /**
   * 内连接
   *
   * @param destEntityClass 目标表实体类
   * @param destEntityAlias 目标表别名
   * @return
   */
  <T extends BaseEntity> QueryJoin<E, T> innerJoin(Class<T> destEntityClass, String destEntityAlias);

  /**
   * 左外连接
   *
   * @param destEntityClass 目标表实体类
   * @param destEntityAlias 目标表别名
   * @return
   */
  <T extends BaseEntity> QueryJoin<E, T> leftJoin(Class<T> destEntityClass, String destEntityAlias);

  /**
   * 右外连接
   *
   * @param destEntityClass 目标表实体类
   * @param destEntityAlias 目标表别名
   * @return
   */
  <T extends BaseEntity> QueryJoin<E, T> rightJoin(Class<T> destEntityClass, String destEntityAlias);

  /**
   * where
   *
   * @return
   */
  QueryWhere<E> where();

  /**
   * 返回实体类
   *
   * @return
   */
  Class<E> getEntityClass();

  /**
   * 返回实体查询时使用的别名
   *
   * @return
   */
  String getEntityAlias();

  /**
   * 返回实体查询时使用的别名集
   *
   * @return
   */
  Map<String, String> getEntityAliasMap();

  boolean isSelectDefaults();

  List<String> getSelectExpressions();

  List<QueryJoin<?, ? extends BaseEntity>> getInnerJoinCollections();

  List<QueryJoin<?, ? extends BaseEntity>> getLeftJoinCollections();

  List<QueryJoin<?, ? extends BaseEntity>> getRightJoinCollections();

  List<String> getGroupByExpressions();

  List<String> getOrderByExpressions();

}
