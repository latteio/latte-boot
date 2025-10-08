package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.mapper.EntityGetter;
import io.latte.boot.mapper.EntityPropertyOrMethodMeta;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.statement.entity.LogicalType;
import io.latte.boot.mapper.statement.entity.RelationalType;
import io.latte.boot.mapper.statement.entity.WhereExists;
import io.latte.boot.mapper.statement.entity.WhereType;
import io.latte.boot.support.web.IdUtils;

import java.util.List;

/**
 * EntityQueryWhereExistsBuilder
 *
 * @author : wugz
 * @since : 2022/1/27
 */
public class EntityQueryWhereExistsBuilder<E, T> {
  private final EntityQuery<E> entityQuery;
  private final EntityQueryWhereBuilder<E> entityQueryWhereBuilder;
  private final WhereExists whereExists;
  private LogicalType logicalType;

  /**
   * 构造函数
   */
  public EntityQueryWhereExistsBuilder(EntityQuery<E> entityQuery,
                                       EntityQueryWhereBuilder<E> entityQueryWhereBuilder,
                                       WhereExists whereExists) {
    this.entityQuery = entityQuery;
    this.entityQuery.where().whereExpression(new WhereType(whereExists));
    this.entityQueryWhereBuilder = entityQueryWhereBuilder;
    this.logicalType = whereExists.getLogicalType();
    this.whereExists = whereExists;
  }

  /**
   * 逻辑and
   *
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> and() {
    this.logicalType = LogicalType.AND;
    return this;
  }

  /**
   * 逻辑or
   *
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> or() {
    this.logicalType = LogicalType.OR;
    return this;
  }

  /**
   * 结束Exists返回
   *
   * @return
   */
  public EntityQueryWhereBuilder<E> endExists() {
    return this.entityQueryWhereBuilder;
  }

  /**
   * 相等匹配
   *
   * @param destGetter
   * @param srcGetter
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> eq(EntityGetter<T> destGetter, EntityGetter<E> srcGetter) {
    EntityPropertyOrMethodMeta destPropertyMeta = EntityUtils.getPropertyMetaFromGetter(destGetter);
    String destField = destPropertyMeta.getFieldName();

    EntityPropertyOrMethodMeta srcPropertyMeta = EntityUtils.getPropertyMetaFromGetter(srcGetter);
    String srcField = srcPropertyMeta.getFieldName();

    if (null != destField && null != srcField) {
      StringBuilder eqBuilder = new StringBuilder();
      eqBuilder.append(entityQuery.getEntityAliasMap().get(destPropertyMeta.getClassName())).append(".").append(destPropertyMeta.getFieldName());
      eqBuilder.append(" = ");
      eqBuilder.append(entityQuery.getEntityAliasMap().get(srcPropertyMeta.getClassName())).append(".").append(srcPropertyMeta.getFieldName());

      if (LogicalType.AND.equals(logicalType)) {
        whereExists.AND().WHERE(eqBuilder.toString());
      } else if (LogicalType.OR.equals(logicalType)) {
        whereExists.OR().WHERE(eqBuilder.toString());
      }
    }

    return this;
  }

  /**
   * 创建大于匹配语句
   *
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> gt(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.GT, value);
  }

  /**
   * 当条件为真, 则创建大于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> gt(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.GT, value);
  }

  /**
   * 创建大于或等于匹配语句
   *
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> ge(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.GE, value);
  }

  /**
   * 当条件为真, 则创建大于或等于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> ge(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.GE, value);
  }

  /**
   * 创建小于匹配语句
   *
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> lt(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.LT, value);
  }

  /**
   * 当条件为真, 则创建小于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> lt(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.LT, value);
  }

  /**
   * 创建小于或等于匹配语句
   *
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> le(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.LE, value);
  }

  /**
   * 当条件为真, 则创建小于或等于匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> le(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.LE, value);
  }

  /**
   * 创建相等匹配语句
   *
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> eq(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.EQ, value);
  }

  /**
   * 当条件为真, 则创建相等匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> eq(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.EQ, value);
  }

  /**
   * 创建不相等匹配语句
   *
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> neq(EntityGetter<T> getter, Object value) {
    return relationalExpression(true, getter, RelationalType.NEQ, value);
  }

  /**
   * 当条件为真, 创建不相等匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> neq(boolean condition, EntityGetter<T> getter, Object value) {
    return relationalExpression(condition, getter, RelationalType.NEQ, value);
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> like(EntityGetter<T> getter, Object value) {
    return like(true, getter, "%", value, "%");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> like(boolean condition, EntityGetter<T> getter, Object value) {
    return like(condition, getter, "%", value, "%");
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> leftLike(EntityGetter<T> getter, Object value) {
    return like(true, getter, "", value, "%");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> leftLike(boolean condition, EntityGetter<T> getter, Object value) {
    return like(condition, getter, "", value, "%");
  }

  /**
   * 创建模糊匹配语句
   *
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> rightLike(EntityGetter<T> getter, Object value) {
    return like(true, getter, "%", value, "");
  }

  /**
   * 当条件为真, 则创建模糊匹配语句
   *
   * @param condition
   * @param getter
   * @param value
   */
  public EntityQueryWhereExistsBuilder<E, T> rightLike(boolean condition, EntityGetter<T> getter, Object value) {
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
   * @return
   */
  private EntityQueryWhereExistsBuilder<E, T> like(boolean condition, EntityGetter<T> getter, Object left, Object value, Object right) {
    if (condition && null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propName = entityQuery.checkParameterName(propertyMeta.getPropertyName());
        if (LogicalType.AND.equals(logicalType)) {
          whereExists.AND().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + " like #{" + propName + "}");
        } else if (LogicalType.OR.equals(logicalType)) {
          whereExists.OR().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + " like #{" + propName + "}");
        }
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
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> between(EntityGetter<T> getter, Object start, Object end) {
    return between(true, getter, start, end);
  }

  /**
   * 如果条件为真, 则创建between语句
   *
   * @param condition
   * @param getter
   * @param start
   * @param end
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> between(boolean condition, EntityGetter<T> getter, Object start, Object end) {
    if (condition && null != getter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propertyStart = propertyMeta.getPropertyName() + IdUtils.getId();
        String propertyEnd = propertyMeta.getPropertyName() + IdUtils.getId();
        if (LogicalType.AND.equals(logicalType)) {
          whereExists.AND().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + " between #{" + propertyStart + "} and #{" + propertyEnd + "}");
        } else if (LogicalType.OR.equals(logicalType)) {
          whereExists.OR().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + " between #{" + propertyStart + "} and #{" + propertyEnd + "}");
        }
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
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> between(EntityGetter<T> getter, List<?> values) {
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
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> in(EntityGetter<T> getter, List<?> values) {
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
        String expr = entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + " in (" + vals + ")";
        if (LogicalType.AND.equals(logicalType)) {
          whereExists.AND().WHERE(expr);
        } else if (LogicalType.OR.equals(logicalType)) {
          whereExists.OR().WHERE(expr);
        }
      }
    }

    return this;
  }

  /**
   * 属性值为空
   *
   * @param getter
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> isNull(EntityGetter<T> getter) {
    return isNullOrNotNull(true, getter, RelationalType.IS_NULL);
  }

  /**
   * 属性值为空
   *
   * @param condition
   * @param getter
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> isNull(boolean condition, EntityGetter<T> getter) {
    return isNullOrNotNull(condition, getter, RelationalType.IS_NULL);
  }

  /**
   * 属性值不为空
   *
   * @param getter
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> isNotNull(EntityGetter<T> getter) {
    return isNullOrNotNull(true, getter, RelationalType.IS_NOT_NULL);
  }

  /**
   * 属性值不为空
   *
   * @param condition
   * @param getter
   * @return
   */
  public EntityQueryWhereExistsBuilder<E, T> isNotNull(boolean condition, EntityGetter<T> getter) {
    return isNullOrNotNull(condition, getter, RelationalType.IS_NOT_NULL);
  }

  /**
   * 属性值为空或不为空
   *
   * @param condition
   * @param getter
   * @param relationalType
   * @return
   */
  private EntityQueryWhereExistsBuilder<E, T> isNullOrNotNull(boolean condition, EntityGetter<T> getter, RelationalType relationalType) {
    if (condition && null != getter && null != relationalType) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        if (LogicalType.AND.equals(logicalType)) {
          whereExists.AND().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + relationalType.getText());
        } else if (LogicalType.OR.equals(logicalType)) {
          whereExists.OR().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + relationalType.getText());
        }
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
  private EntityQueryWhereExistsBuilder<E, T> relationalExpression(boolean condition, EntityGetter<T> getter, RelationalType relationalType, Object value) {
    if (condition && null != getter && null != relationalType) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(getter);
      if (null != propertyMeta.getFieldName()) {
        String propName = entityQuery.checkParameterName(propertyMeta.getPropertyName());
        if (LogicalType.AND.equals(logicalType)) {
          whereExists.AND().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + relationalType.getText() + "#{" + propName + "}");
        } else if (LogicalType.OR.equals(logicalType)) {
          whereExists.OR().WHERE(entityQuery.getEntityAliasMap().get(propertyMeta.getClassName()) + "." + propertyMeta.getFieldName() + relationalType.getText() + "#{" + propName + "}");
        }
        entityQuery.put(propName, value);
      }
    }
    return this;
  }

}
