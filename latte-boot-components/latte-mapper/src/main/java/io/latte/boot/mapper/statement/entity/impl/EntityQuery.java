package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.statement.entity.Query;
import io.latte.boot.mapper.statement.entity.QueryJoin;
import io.latte.boot.mapper.statement.entity.QueryWhere;
import io.latte.boot.support.validate.Validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于实体的查询
 *
 * @author : wugz
 * @since : 2023/7/26
 */
public class EntityQuery<E> extends EntityMap implements Query<E> {
  private final Class<E> entityClass;
  private final String entityAlias;
  private final Map<String, String> entityAliasMap;
  private final boolean selectDefaults;
  private final List<String> selectExpressions = new ArrayList<>();
  private final List<QueryJoin<?, ? extends BaseEntity>> innerJoinCollections = new ArrayList<>();
  private final List<QueryJoin<?, ? extends BaseEntity>> leftJoinCollections = new ArrayList<>();
  private final List<QueryJoin<?, ? extends BaseEntity>> rightJoinCollections = new ArrayList<>();
  private final EntityQueryWhere<E> where = new EntityQueryWhere<>(this);
  private final List<String> groupByExpressions = new ArrayList<>();
  private final List<String> orderByExpressions = new ArrayList<>();

  /**
   * 构造函数
   *
   * @param entityClass 实体类
   * @param entityAlias 实体别名(表别名)
   */
  public EntityQuery(Class<E> entityClass, String entityAlias) {
    this(entityClass, entityAlias, true);
  }

  /**
   * 构造函数
   *
   * @param entityClass    实体类
   * @param entityAlias    实体别名(表别名)
   * @param selectDefaults 是否自动添加实体所有默认字段到查询列表
   */
  public EntityQuery(Class<E> entityClass, String entityAlias, boolean selectDefaults) {
    this.entityClass = Validate.requireNonNull(entityClass);
    this.entityAlias = Validate.requireNonEmpty(entityAlias);
    this.entityAliasMap = new HashMap<>();
    this.entityAliasMap.put(entityClass.getName(), entityAlias);
    this.selectDefaults = selectDefaults;
  }

  /**
   * 内连接
   *
   * @param destEntityClass 目标表实体类
   * @param destEntityAlias 目标表别名
   * @return
   */
  public <T extends BaseEntity> QueryJoin<E, T> innerJoin(Class<T> destEntityClass, String destEntityAlias) {
    QueryJoin<E, T> queryJoin = new EntityQueryJoin<>(this, destEntityClass, destEntityAlias, entityAliasMap);
    this.innerJoinCollections.add(queryJoin);
    return queryJoin;
  }

  /**
   * 左外连接
   *
   * @param destEntityClass 目标表实体类
   * @param destEntityAlias 目标表别名
   * @return
   */
  public <T extends BaseEntity> QueryJoin<E, T> leftJoin(Class<T> destEntityClass, String destEntityAlias) {
    QueryJoin<E, T> queryJoin = new EntityQueryJoin<>(this, destEntityClass, destEntityAlias, entityAliasMap);
    this.leftJoinCollections.add(queryJoin);
    return queryJoin;
  }

  /**
   * 右外连接
   *
   * @param destEntityClass 目标表实体类
   * @param destEntityAlias 目标表别名
   * @return
   */
  public <T extends BaseEntity> QueryJoin<E, T> rightJoin(Class<T> destEntityClass, String destEntityAlias) {
    QueryJoin<E, T> queryJoin = new EntityQueryJoin<>(this, destEntityClass, destEntityAlias, entityAliasMap);
    this.rightJoinCollections.add(queryJoin);
    return queryJoin;
  }

  /**
   * where
   *
   * @return QueryWhere
   */
  public QueryWhere<E> where() {
    return where;
  }

  /**
   * 返回实体类
   *
   * @return
   */
  public Class<E> getEntityClass() {
    return entityClass;
  }

  /**
   * 返回实体查询时使用的别名
   *
   * @return
   */
  public String getEntityAlias() {
    return entityAlias;
  }

  /**
   * 返回实体查询时使用的别名集
   *
   * @return
   */
  public Map<String, String> getEntityAliasMap() {
    return entityAliasMap;
  }

  public boolean isSelectDefaults() {
    return selectDefaults;
  }

  public List<String> getSelectExpressions() {
    return selectExpressions;
  }

  public List<QueryJoin<?, ? extends BaseEntity>> getInnerJoinCollections() {
    return innerJoinCollections;
  }

  public List<QueryJoin<?, ? extends BaseEntity>> getLeftJoinCollections() {
    return leftJoinCollections;
  }

  public List<QueryJoin<?, ? extends BaseEntity>> getRightJoinCollections() {
    return rightJoinCollections;
  }

  public List<String> getGroupByExpressions() {
    return groupByExpressions;
  }

  public List<String> getOrderByExpressions() {
    return orderByExpressions;
  }

}
