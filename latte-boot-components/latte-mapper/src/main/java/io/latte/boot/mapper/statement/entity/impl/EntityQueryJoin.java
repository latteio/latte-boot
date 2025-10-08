package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityGetter;
import io.latte.boot.mapper.EntityPropertyOrMethodMeta;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.annotation.Entity;
import io.latte.boot.mapper.statement.entity.Query;
import io.latte.boot.mapper.statement.entity.QueryJoin;

import java.util.Map;

/**
 * EntityQueryJoin
 *
 * @author : wugz
 * @since : 2023/7/26
 */
public class EntityQueryJoin<E, T extends BaseEntity> implements QueryJoin<E, T> {
  private final EntityQuery<E> entityQuery;
  private final String destEntityAlias;
  private final StringBuilder joinExpressions = new StringBuilder();

  /**
   * 构造函数
   *
   * @param entityQuery
   * @param destEntityClass
   * @param destEntityAlias
   * @param aliasMap
   */
  public EntityQueryJoin(EntityQuery<E> entityQuery, Class<T> destEntityClass, String destEntityAlias, Map<String, String> aliasMap) {
    this.entityQuery = entityQuery;
    this.destEntityAlias = destEntityAlias;
    String tableName = destEntityClass.getAnnotation(Entity.class).table();
    joinExpressions.append(tableName).append(" ").append(destEntityAlias);
    aliasMap.put(destEntityClass.getName(), destEntityAlias);
  }

  /**
   * 连接条件
   *
   * @param destGetter 关联目标属性Getter
   * @param srcGetter  关联源属性Getter
   * @return
   */
  public Query<E> on(EntityGetter<T> destGetter, EntityGetter<E> srcGetter) {
    EntityPropertyOrMethodMeta destPropertyMeta = EntityUtils.getPropertyMetaFromGetter(destGetter);
    String destField = destPropertyMeta.getFieldName();
    EntityPropertyOrMethodMeta srcPropertyMeta = EntityUtils.getPropertyMetaFromGetter(srcGetter);
    String srcField = srcPropertyMeta.getFieldName();
    if (null != destField && null != srcField) {
      joinExpressions.append(" ON (")
          .append(destEntityAlias)
          .append(".")
          .append(destField)
          .append(" = ")
          .append(entityQuery.getEntityAlias())
          .append(".")
          .append(srcField)
          .append(")");
    }

    return entityQuery;
  }

  /**
   * 构造连接条件
   *
   * @return
   */
  public String build() {
    return joinExpressions.toString();
  }
}
