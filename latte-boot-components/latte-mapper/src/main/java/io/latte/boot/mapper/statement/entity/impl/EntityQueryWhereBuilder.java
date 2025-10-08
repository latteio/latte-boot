package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.mapper.EntityGetter;
import io.latte.boot.mapper.EntityPropertyOrMethodMeta;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.statement.entity.*;
import io.latte.boot.support.web.IdUtils;

import java.util.List;

/**
 * EntityQueryWhereBuilder
 *
 * @author : wugz
 * @since : 2021/9/3
 */
public class EntityQueryWhereBuilder<E> implements QueryWhereBuilder<E> {
  private final EntityQuery<E> entityQuery;
  private final EntityQueryWhere<E> entityQueryWhere;
  private LogicalType logicalType;

  public EntityQueryWhereBuilder(EntityQuery<E> entityQuery, EntityQueryWhere<E> entityQueryWhere, LogicalType logicalType) {
    this.entityQuery = entityQuery;
    this.entityQueryWhere = entityQueryWhere;
    this.logicalType = logicalType;
  }

  /**
   * 逻辑and
   *
   * @return
   */
  public QueryWhereBuilder<E> and() {
    this.logicalType = LogicalType.AND;
    return this;
  }

  /**
   * 逻辑or
   *
   * @return
   */
  public QueryWhereBuilder<E> or() {
    this.logicalType = LogicalType.OR;
    return this;
  }

  /**
   * 创建大于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> gt(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.GT, value);
  }

  /**
   * 当条件为真, 则创建大于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> gt(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.GT, value);
  }

  /**
   * 创建大于或等于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> ge(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.GE, value);
  }

  /**
   * 当条件为真, 则创建大于或等于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> ge(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.GE, value);
  }

  /**
   * 创建小于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> lt(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.LT, value);
  }

  /**
   * 当条件为真, 则创建小于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> lt(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.LT, value);
  }

  /**
   * 创建小于或等于匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> le(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.LE, value);
  }

  /**
   * 当条件为真, 则创建小于或等于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> le(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.LE, value);
  }

  /**
   * 创建相等匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> eq(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.EQ, value);
  }

  /**
   * 当条件为真, 则创建相等匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> eq(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.EQ, value);
  }

  /**
   * 创建不相等匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> neq(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.NEQ, value);
  }

  /**
   * 当条件为真, 创建不相等匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> neq(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.NEQ, value);
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> like(EntityGetter<T> getter, Object value) {
    return like(true, getter, "%", value, "%");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> like(boolean condition, EntityGetter<T> getter, Object value) {
    return like(condition, getter, "%", value, "%");
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> leftLike(EntityGetter<T> getter, Object value) {
    return like(true, getter, "", value, "%");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> leftLike(boolean condition, EntityGetter<T> getter, Object value) {
    return like(condition, getter, "", value, "%");
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> rightLike(EntityGetter<T> getter, Object value) {
    return like(true, getter, "%", value, "");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public <T> QueryWhereBuilder<E> rightLike(boolean condition, EntityGetter<T> getter, Object value) {
    return like(condition, getter, "%", value, "");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param left
   * @param value
   * @param right
   * @param <T>
   * @return
   */
  private <T> QueryWhereBuilder<E> like(boolean condition, EntityGetter<T> getter, Object left, Object value, Object right) {
    if (condition && null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propName = entityQuery.checkParameterName(propertyMeta.getPropertyName());
        entityQuery.where().whereExpression(
            new WhereType(logicalType,
                entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()),
                ".",
                propertyMeta.getFieldName(),
                " like #{",
                propName,
                "}"));
        entityQuery.put(propName, left.toString() + value.toString() + right.toString());
      }
    }

    return this;
  }

  /**
   * 创建between语句
   *
   * @param getter
   * @param start
   * @param end
   * @param <T>
   * @return
   */
  public <T> QueryWhereBuilder<E> between(EntityGetter<T> getter, Object start, Object end) {
    return between(true, getter, start, end);
  }

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
  public <T> QueryWhereBuilder<E> between(boolean condition, EntityGetter<T> getter, Object start, Object end) {
    if (condition && null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propertyStart = propertyMeta.getPropertyName() + IdUtils.getId();
        String propertyEnd = propertyMeta.getPropertyName() + IdUtils.getId();
        entityQuery.where().whereExpression(
            new WhereType(logicalType,
                entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()),
                ".",
                propertyMeta.getFieldName(),
                " between #{",
                propertyStart,
                "} and #{",
                propertyEnd,
                "}"));
        entityQuery.put(propertyStart, start);
        entityQuery.put(propertyEnd, end);
      }
    }
    return this;
  }

  /**
   * 创建between语句
   *
   * @param getter
   * @param values
   * @param <T>
   * @return
   */
  public <T> QueryWhereBuilder<E> between(EntityGetter<T> getter, List<?> values) {
    if (null != values && values.size() == 2) {
      return this.between(getter, values.get(0), values.get(1));
    }

    return this;
  }

  /**
   * 创建in语句
   *
   * @param getter
   * @param values
   * @param <T>
   * @return
   */
  public <T> QueryWhereBuilder<E> in(EntityGetter<T> getter, List<?> values) {
    if (null != getter && null != values && values.size() > 0) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        /* 遍历元素: 每个元素单独形成一个#{propNameX}, X为元素索引值, 同时将对应值压入参数值map */
        String propName;
        StringBuilder vals = new StringBuilder();
        for (int e = 0; e < values.size(); ++e) {
          propName = propertyMeta.getPropertyName() + IdUtils.getId();
          entityQuery.put(propName, values.get(e));
          if (e == 0) {
            vals.append("#{").append(propName).append("}");
          } else {
            vals.append(", ").append("#{").append(propName).append("}");
          }
        }

        /* 生成in表达式 */
        entityQuery.where().whereExpression(
            new WhereType(logicalType,
                entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()),
                ".",
                propertyMeta.getFieldName(),
                " in (",
                vals.toString(),
                ")"));
      }
    }

    return this;
  }

  /**
   * 属性值为空
   *
   * @param getter
   * @param <T>
   * @return
   */
  public <T> QueryWhereBuilder<E> isNull(EntityGetter<T> getter) {
    return isNullOrNotNull(true, getter, RelationalType.IS_NULL);
  }

  /**
   * 属性值为空
   *
   * @param condition
   * @param getter
   * @param <T>
   * @return
   */
  public <T> QueryWhereBuilder<E> isNull(boolean condition, EntityGetter<T> getter) {
    return isNullOrNotNull(condition, getter, RelationalType.IS_NULL);
  }

  /**
   * 属性值不为空
   *
   * @param getter
   * @param <T>
   * @return
   */
  public <T> QueryWhereBuilder<E> isNotNull(EntityGetter<T> getter) {
    return isNullOrNotNull(true, getter, RelationalType.IS_NOT_NULL);
  }

  /**
   * 属性值不为空
   *
   * @param condition
   * @param getter
   * @param <T>
   * @return
   */
  public <T> QueryWhereBuilder<E> isNotNull(boolean condition, EntityGetter<T> getter) {
    return isNullOrNotNull(condition, getter, RelationalType.IS_NOT_NULL);
  }

  /**
   * 存在性判定
   *
   * @param entityClass
   * @param entityAlias
   * @param <T>
   * @return
   */
  public <T> EntityQueryWhereExists<E, T> exists(Class<T> entityClass, String entityAlias) {
    WhereExists whereExists = new WhereExists(logicalType, false, entityClass, entityAlias);
    return new EntityQueryWhereExists<>(entityQuery, this, whereExists);
  }

  /**
   * 非存在性判定
   *
   * @param entityClass
   * @param entityAlias
   * @param <T>
   * @return
   */
  public <T> EntityQueryWhereExists<E, T> notExists(Class<T> entityClass, String entityAlias) {
    WhereExists whereExists = new WhereExists(logicalType, true, entityClass, entityAlias);
    return new EntityQueryWhereExists<>(entityQuery, this, whereExists);
  }

  /**
   * 表达式组
   *
   * @return
   */
  public EntityQueryWhereGroup<E> group() {
    return new EntityQueryWhereGroup<>(this.entityQuery, this, new WhereGroup(this.logicalType));
  }

  /**
   * endWhere
   *
   * @return
   */
  public QuerySelection<E> endWhere() {
    return entityQueryWhere.endWhere();
  }

  /**
   * 属性值为空或不为空
   *
   * @param condition
   * @param getter
   * @param relationalType
   * @param <T>
   * @return
   */
  private <T> QueryWhereBuilder<E> isNullOrNotNull(boolean condition, EntityGetter<T> getter, RelationalType relationalType) {
    if (condition && null != getter && null != relationalType) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        entityQuery.where().whereExpression(
            new WhereType(logicalType,
                entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()),
                ".",
                propertyMeta.getFieldName(),
                relationalType.getText()));
      }
    }
    return this;
  }

  /**
   * 关系表达式
   *
   * @param condition
   * @param getter
   * @param value
   */
  private <T> QueryWhereBuilder<E> relationalExpression(boolean condition, EntityGetter<T> getter, RelationalType relationalType, Object value) {
    if (condition && null != getter && null != relationalType) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propName = entityQuery.checkParameterName(propertyMeta.getPropertyName());
        entityQuery.where().whereExpression(
            new WhereType(logicalType,
                entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()),
                ".",
                propertyMeta.getFieldName(),
                relationalType.getText(),
                "#{",
                propName,
                "}"));
        entityQuery.put(propName, value);
      }
    }
    return this;
  }

}
