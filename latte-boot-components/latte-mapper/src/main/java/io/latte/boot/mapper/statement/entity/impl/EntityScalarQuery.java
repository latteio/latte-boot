package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityDefinition;
import io.latte.boot.mapper.EntityGetter;
import io.latte.boot.mapper.EntityPropertyOrMethodMeta;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.statement.entity.QuerySelection;
import io.latte.boot.mapper.statement.entity.ScalarQuery;
import org.springframework.util.StringUtils;

/**
 * EntityScalarQuery
 *
 * @author : wugz
 * @since : 2024/10/20
 */
public class EntityScalarQuery<E, T extends BaseEntity> implements ScalarQuery<E, T> {
  private final EntityQuery<E> entityQuery;
  private final EntityQuerySelection<E> entityQuerySelection;
  private final Class<T> destEntityClass;
  private final String destEntityAlias;
  private final EntityGetter<T> destSelectGetter;
  private final String asName;

  /**
   * 构造函数
   *
   * @param entityQuery
   * @param entityQuerySelection
   * @param destEntityClass
   * @param destEntityAlias
   * @param destSelectGetter
   * @param asName
   */
  public EntityScalarQuery(EntityQuery<E> entityQuery,
                           EntityQuerySelection<E> entityQuerySelection,
                           Class<T> destEntityClass,
                           String destEntityAlias,
                           EntityGetter<T> destSelectGetter,
                           String asName) {
    this.entityQuery = entityQuery;
    this.entityQuerySelection = entityQuerySelection;
    this.destEntityClass = destEntityClass;
    this.destEntityAlias = destEntityAlias;
    this.destSelectGetter = destSelectGetter;
    this.asName = asName;
  }

  @Override
  public QuerySelection<E> on(EntityGetter<T> destGetter, EntityGetter<E> srcGetter) {
    if (null != destEntityClass && StringUtils.hasText(destEntityAlias) && null != destSelectGetter) {
      EntityPropertyOrMethodMeta propertyMeta = EntityUtils.getPropertyMetaFromGetter(destSelectGetter);
      if (destEntityClass.getName().equals(propertyMeta.getClassName()) && null != propertyMeta.getFieldName()) {

        /* 组装标量子查询 */
        EntityDefinition destEntityDefinition = EntityUtils.getEntityDefinition(destEntityClass);
        EntityPropertyOrMethodMeta destPropertyMeta = EntityUtils.getPropertyMetaFromGetter(destGetter);
        String destAlias = destEntityAlias;
        EntityPropertyOrMethodMeta srcPropertyMeta = EntityUtils.getPropertyMetaFromGetter(srcGetter);
        String srcAlias = entityQuery.getEntityAlias();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("(")
            .append("SELECT ")
            .append(destAlias).append(".").append(propertyMeta.getFieldName())
            .append(" FROM ")
            .append(destEntityDefinition.getTableName()).append(" ").append(destAlias)
            .append(" WHERE ")
            .append(destAlias).append(".").append(destPropertyMeta.getFieldName())
            .append(" = ")
            .append(srcAlias).append(".").append(srcPropertyMeta.getFieldName())
            .append(") AS ")
            .append(StringUtils.hasText(asName) ? asName : propertyMeta.getPropertyName());

        entityQuery.getSelectExpressions().add(sqlBuilder.toString());
      }
    }

    return entityQuerySelection;
  }
}
