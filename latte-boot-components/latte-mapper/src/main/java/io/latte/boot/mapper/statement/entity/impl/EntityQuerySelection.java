package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityGetter;
import io.latte.boot.mapper.EntityPropertyOrMethodMeta;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.statement.entity.OrderType;
import io.latte.boot.mapper.statement.entity.Query;
import io.latte.boot.mapper.statement.entity.QuerySelection;
import io.latte.boot.mapper.statement.entity.ScalarQuery;
import org.springframework.util.StringUtils;

/**
 * EntityQuerySelection
 *
 * @author : wugz
 * @since : 2024/10/20
 */
public class EntityQuerySelection<E> implements QuerySelection<E> {
  private final EntityQuery<E> entityQuery;

  /**
   * 构造函数
   *
   * @param entityQuery
   */
  public EntityQuerySelection(EntityQuery<E> entityQuery) {
    this.entityQuery = entityQuery;
  }

  /**
   * 查询
   *
   * @param expr 查询表达式
   */
  public <T> QuerySelection<E> select(String expr) {
    if (StringUtils.hasText(expr)) {
      entityQuery.getSelectExpressions().add(expr);
    }
    return this;
  }

  /**
   * 查询字段getter表达式集
   *
   * @param getter
   */
  public <T> QuerySelection<E> select(EntityGetter<T> getter) {
    if (null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String alias = entityQuery.getEntityAliasMap().get(propertyMeta.getClassName());
        entityQuery.getSelectExpressions()
            .add(alias + "." + propertyMeta.getFieldName() + " AS " + propertyMeta.getPropertyName());
      }
    }
    return this;
  }

  /**
   * 查询字段getter表达式集
   *
   * @param getter
   * @param asName 查询别名
   * @param <T>
   * @return
   */
  public <T> QuerySelection<E> select(EntityGetter<T> getter, String asName) {
    if (null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String alias = entityQuery.getEntityAliasMap().get(propertyMeta.getClassName());
        entityQuery.getSelectExpressions()
            .add(alias + "." + propertyMeta.getFieldName() + " AS " + asName);
      }
    }
    return this;
  }

  /**
   * 查询字段getter表达式集
   *
   * @param getter
   * @param asName
   * @param <T>
   * @return
   */
  public <T> QuerySelection<E> selectCount(EntityGetter<T> getter, String asName) {
    if (null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String alias = entityQuery.getEntityAliasMap().get(propertyMeta.getClassName());
        entityQuery.getSelectExpressions()
            .add("count(" + alias + "." + propertyMeta.getFieldName() + ") AS " + asName);
      }
    }
    return this;
  }

  /**
   * 标量子查询
   *
   * @param destEntityClass
   * @param destEntityAlias
   * @param destSelectGetter
   * @param <T>
   * @return
   */
  public <T extends BaseEntity> ScalarQuery<E, T> selectScalar(Class<T> destEntityClass, String destEntityAlias, EntityGetter<T> destSelectGetter) {
    return new EntityScalarQuery<>(entityQuery, this, destEntityClass, destEntityAlias, destSelectGetter, null);
  }

  /**
   * 标量子查询
   *
   * @param destEntityClass
   * @param destEntityAlias
   * @param destSelectGetter
   * @param asName
   * @param <T>
   * @return
   */
  public <T extends BaseEntity> ScalarQuery<E, T> selectScalar(Class<T> destEntityClass, String destEntityAlias, EntityGetter<T> destSelectGetter, String asName) {
    return new EntityScalarQuery<>(entityQuery, this, destEntityClass, destEntityAlias, destSelectGetter, asName);
  }

  /**
   * 分组
   *
   * @param expr 分组表达式
   */
  public <T> QuerySelection<E> groupBy(String expr) {
    if (StringUtils.hasText(expr)) {
      entityQuery.getGroupByExpressions().add(expr);
    }
    return this;
  }

  /**
   * 分组
   *
   * @param getter 字段属性Getter
   */
  public <T> QuerySelection<E> groupBy(EntityGetter<T> getter) {
    if (null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        entityQuery.getGroupByExpressions().add(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName());
      }
    }
    return this;
  }

  /**
   * 排序
   *
   * @param expr 排序表达式
   */
  public <T> QuerySelection<E> orderBy(String expr) {
    if (StringUtils.hasText(expr)) {
      entityQuery.getOrderByExpressions().add(expr);
    }
    return this;
  }

  /**
   * 升序排序
   *
   * @param getter 字段属性Getter
   */
  public <T> QuerySelection<E> orderAsc(EntityGetter<T> getter) {
    if (null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        orderAsc(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName());
      }
    }
    return this;
  }

  /**
   * 降序排序
   *
   * @param getter 字段属性Getter
   */
  public <T> QuerySelection<E> orderDesc(EntityGetter<T> getter) {
    if (null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        orderDesc(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName());
      }
    }
    return this;
  }

  /**
   * 升序排序
   */
  private void orderAsc(String column) {
    entityQuery.getOrderByExpressions().add(column + " " + OrderType.ASC.getText());
  }

  /**
   * 降序排序
   */
  private void orderDesc(String column) {
    entityQuery.getOrderByExpressions().add(column + " " + OrderType.DESC.getText());
  }

  /**
   * 返回到 Query
   *
   * @return
   */
  public Query<E> endQuery() {
    return entityQuery;
  }

}
