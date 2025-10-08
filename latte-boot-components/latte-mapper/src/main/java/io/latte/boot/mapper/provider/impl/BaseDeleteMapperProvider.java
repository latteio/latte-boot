package io.latte.boot.mapper.provider.impl;

import com.google.common.base.Strings;
import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityDefinition;
import io.latte.boot.mapper.EntityFields;
import io.latte.boot.mapper.EntityProperty;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.statement.entity.Delete;
import io.latte.boot.support.web.MapUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * BaseDeleteMapperProvider
 *
 * @author : wugz
 * @since : 2018/4/23
 */
public class BaseDeleteMapperProvider<E extends BaseEntity> extends BaseMapperProvider {
  /**
   * 删除实体
   *
   * @param context     上下文
   * @param whereEntity where实参
   * @return
   */
  public String deleteByPrimary(ProviderContext context, E whereEntity) {
    Objects.requireNonNull(whereEntity, "whereEntity is null");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    if (null != entityDefinition) {
      EntityProperty propertyId = entityDefinition.getPropertyId();
      EntityProperty propertyUpdateUser = entityDefinition.getPropertyUpdateUser();
      EntityProperty propertyUpdateTime = entityDefinition.getPropertyUpdateTime();
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();
      SQL sql = new SQL();
      if (entityDefinition.isLogicalDelete()) {
        sql.UPDATE(entityDefinition.getTableName());
        sql.SET(this.getEquation(propertyIsDeleted.getField(), "1"));
        if (!Strings.isNullOrEmpty(whereEntity.getUpdateUser())) {
          sql.SET(this.getEquation(propertyUpdateUser.getField(), propertyUpdateUser.getProperty(), true));
        }
        if (null != whereEntity.getUpdateTime()) {
          sql.SET(this.getEquation(propertyUpdateTime.getField(), propertyUpdateTime.getProperty(), true));
        }
        sql.WHERE(this.getEquation(propertyId.getField(), propertyId.getProperty(), true));
        sql.WHERE(this.getEquation(propertyIsDeleted.getField(), "0"));
        return sql.toString();
      } else {
        sql.DELETE_FROM(entityDefinition.getTableName());
        sql.WHERE(this.getEquation(propertyId.getField(), propertyId.getProperty(), true));
        return sql.toString();
      }
    }
    return null;
  }

  /**
   * 删除实体
   *
   * @param context     上下文
   * @param whereEntity where实参
   * @return
   */
  public String delete(ProviderContext context, E whereEntity) {
    Objects.requireNonNull(whereEntity, "whereEntity is null");

    Map<String, Object> propertiesMap = MapUtils.from(whereEntity);
    StringJoiner excludeWhereProperties = new StringJoiner(",");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    if (null != entityDefinition) {
      EntityProperty propertyUpdateUser = entityDefinition.getPropertyUpdateUser();
      EntityProperty propertyUpdateTime = entityDefinition.getPropertyUpdateTime();
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();

      if (!Strings.isNullOrEmpty(whereEntity.getUpdateUser())) {
        excludeWhereProperties.add(propertyUpdateUser.getProperty());
      }

      if (null != whereEntity.getUpdateTime()) {
        excludeWhereProperties.add(propertyUpdateTime.getProperty());
      }

      if (excludeWhereProperties.length() > 0) {
        propertiesMap.put(EntityFields.WHERE_PROPERTY_EXCLUDES, excludeWhereProperties.toString());
      }

      SQL sql = new SQL();
      if (entityDefinition.isLogicalDelete()) {
        sql.UPDATE(entityDefinition.getTableName());
        sql.SET(this.getEquation(propertyIsDeleted.getField(), "1"));
        if (!Strings.isNullOrEmpty(whereEntity.getUpdateUser())) {
          sql.SET(this.getEquation(propertyUpdateUser.getField(), propertyUpdateUser.getProperty(), true));
        }
        if (null != whereEntity.getUpdateTime()) {
          sql.SET(this.getEquation(propertyUpdateTime.getField(), propertyUpdateTime.getProperty(), true));
        }
        sql.WHERE(this.getWhereEquations(entityDefinition.getPersistentProperties(), propertiesMap, null).toArray(new String[]{}));
        sql.WHERE(this.getEquation(propertyIsDeleted.getField(), "0"));
        return sql.toString();
      } else {
        sql.DELETE_FROM(entityDefinition.getTableName());
        sql.WHERE(this.getWhereEquations(entityDefinition.getPersistentProperties(), propertiesMap, null).toArray(new String[]{}));
        return sql.toString();
      }
    }
    return null;
  }

  /**
   * 删除实体
   *
   * @param context      上下文
   * @param entityDelete 实体删除实参
   * @return
   */
  public String deleteBy(ProviderContext context, Delete<E> entityDelete) {
    Objects.requireNonNull(entityDelete, "entityDelete is null");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    if (null != entityDefinition) {
      EntityProperty propertyUpdateUser = entityDefinition.getPropertyUpdateUser();
      EntityProperty propertyUpdateTime = entityDefinition.getPropertyUpdateTime();
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();

      /* 构造sql */
      SQL sql = new SQL();
      if (entityDefinition.isLogicalDelete()) {
        sql.UPDATE(entityDefinition.getTableName());
        sql.SET(this.getEquation(propertyIsDeleted.getField(), "1"));
        Map<String, Object> map = entityDelete.getParameters();
        if (Objects.nonNull(map.get(propertyUpdateUser.getProperty()))) {
          sql.SET(this.getEquation(propertyUpdateUser.getField(), propertyUpdateUser.getProperty(), true));
        }
        if (null != map.get(propertyUpdateTime.getProperty())) {
          sql.SET(this.getEquation(propertyUpdateTime.getField(), propertyUpdateTime.getProperty(), true));
        }
        this.getWhereExpressions(sql, entityDelete.getWhereExpressions());
        sql.WHERE(this.getEquation(propertyIsDeleted.getField(), "0"));
        return sql.toString();
      } else {
        sql.DELETE_FROM(entityDefinition.getTableName());
        this.getWhereExpressions(sql, entityDelete.getWhereExpressions());
        return sql.toString();
      }
    }
    return null;
  }
}
