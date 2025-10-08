package io.latte.boot.mapper.provider.impl;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.entity.cmd.EntityUpdateCommand;
import io.latte.boot.mapper.EntityDefinition;
import io.latte.boot.mapper.EntityProperty;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.statement.entity.Update;
import io.latte.boot.support.web.MapUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.Objects;

/**
 * BaseUpdateMapperProvider
 *
 * @author : wugz
 * @since : 2018/4/22
 */
public class BaseUpdateMapperProvider<E extends BaseEntity> extends BaseMapperProvider {
  /**
   * 按主键更新
   *
   * @param context 上下文
   * @param entity  实体更新
   * @return
   */
  public String updateByPrimary(ProviderContext context, E entity) {
    Objects.requireNonNull(entity, "entity is null");
    Objects.requireNonNull(entity.getId(), "entity.getId() is null");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    if (null != entityDefinition) {
      EntityProperty propertyId = entityDefinition.getPropertyId();
      EntityProperty propertyVersion = entityDefinition.getPropertyVersion();
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();

      /* 构造sql */
      SQL sql = new SQL();
      sql.UPDATE(entityDefinition.getTableName());

      /* 设置set字段: 去除主键更新 */
      Map<String, Object> properties = MapUtils.from(entity);
      properties.remove(propertyId.getProperty());
      sql.SET(this.getSetEquations(entityDefinition.getPersistentProperties(), properties, null, false).toArray(new String[]{}));
      sql.SET(this.getEquation(propertyVersion.getField(), propertyVersion.getField() + " + 1"));

      /* WHERE条件: 删除条件, 逻辑删除条件 */
      sql.WHERE(this.getEquation(propertyId.getField(), propertyId.getProperty(), true));
      if (entityDefinition.isLogicalDelete()) {
        sql.AND().WHERE(this.getEquation(propertyIsDeleted.getField(), "0"));
      }
      return sql.toString();
    }
    return null;
  }

  /**
   * 按主键更新
   *
   * @param context 上下文
   * @param entity  实体更新
   * @return
   */
  public String updateSelectiveByPrimary(ProviderContext context, EntityUpdateCommand entity) {
    Objects.requireNonNull(entity, "entity is null");
    Objects.requireNonNull(entity.getId(), "entity.getId() is null");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    if (null != entityDefinition) {
      EntityProperty propertyId = entityDefinition.getPropertyId();
      EntityProperty propertyVersion = entityDefinition.getPropertyVersion();
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();

      /* 构造sql */
      SQL sql = new SQL();
      sql.UPDATE(entityDefinition.getTableName());

      /* 设置set字段: 去除主键更新 */
      Map<String, Object> properties = MapUtils.from(entity);
      properties.remove(propertyId.getProperty());
      sql.SET(this.getSetEquations(entityDefinition.getPersistentProperties(), properties, null, true).toArray(new String[]{}));
      sql.SET(this.getEquation(propertyVersion.getField(), propertyVersion.getField() + " + 1"));

      /* WHERE条件: 删除条件, 逻辑删除条件 */
      sql.WHERE(this.getEquation(propertyId.getField(), propertyId.getProperty(), true));
      if (entityDefinition.isLogicalDelete()) {
        sql.AND().WHERE(this.getEquation(propertyIsDeleted.getField(), "0"));
      }
      return sql.toString();
    }
    return null;
  }

  /**
   * 更新实体
   *
   * @param context      上下文
   * @param entityUpdate 实体更新
   * @return
   */
  public String updateBy(ProviderContext context, Update<E> entityUpdate) {
    Objects.requireNonNull(entityUpdate, "entityUpdate is null");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    if (null != entityDefinition) {
      EntityProperty propertyVersion = entityDefinition.getPropertyVersion();
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();

      /* 构造sql */
      SQL sql = new SQL();
      sql.UPDATE(entityDefinition.getTableName());
      sql.SET(entityUpdate.getSetExpressions().toArray(new String[]{}));
      sql.SET(this.getEquation(propertyVersion.getField(), propertyVersion.getField() + " + 1"));
      this.getWhereExpressions(sql, entityUpdate.getWhereExpressions());
      if (entityDefinition.isLogicalDelete()) {
        sql.AND().WHERE(this.getEquation(propertyIsDeleted.getField(), "0"));
      }
      return sql.toString();
    }
    return null;
  }

  /**
   * 更新实体
   *
   * @param context      上下文
   * @param entityUpdate 实体更新
   * @return
   */
  public String updateSelectiveBy(ProviderContext context, Update<E> entityUpdate) {
    Objects.requireNonNull(entityUpdate, "entityUpdate is null");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    if (null != entityDefinition) {
      EntityProperty propertyVersion = entityDefinition.getPropertyVersion();
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();

      /* 构造sql */
      SQL sql = new SQL();
      sql.UPDATE(entityDefinition.getTableName());
      sql.SET(entityUpdate.getSetExpressions().toArray(new String[]{}));
      sql.SET(this.getEquation(propertyVersion.getField(), propertyVersion.getField() + " + 1"));
      this.getWhereExpressions(sql, entityUpdate.getWhereExpressions());
      if (entityDefinition.isLogicalDelete()) {
        sql.AND().WHERE(this.getEquation(propertyIsDeleted.getField(), "0"));
      }
      return sql.toString();
    }
    return null;
  }

}
