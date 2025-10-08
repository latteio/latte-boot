package io.latte.boot.mapper.provider.impl;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityDefinition;
import io.latte.boot.mapper.EntityProperty;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.statement.entity.Query;
import io.latte.boot.mapper.statement.entity.QueryJoin;
import io.latte.boot.mapper.statement.entity.impl.EntityQuery;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.core.ResolvableType;

import java.io.Serializable;
import java.util.Objects;

/**
 * BaseSelectMapperProvider
 *
 * @author : wugz
 * @since : 2018/4/22
 */
public class BaseSelectMapperProvider<E> extends BaseMapperProvider {
  /**
   * 查询数量
   *
   * @param context 上下文
   * @param entity  实体查询
   * @return
   */
  public String selectCount(ProviderContext context, E entity) {
    Objects.requireNonNull(entity, "entity is null");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinition(entity.getClass());
    if (null != entityDefinition) {
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();
      SQL sql = new SQL();
      sql.SELECT("count(0)");
      sql.FROM(entityDefinition.getTableName() + " " + entityDefinition.getDefaultTableAlias());
      sql.WHERE(this.getSelectWhereConditions(entityDefinition, entity, entityDefinition.getDefaultTableAlias()).toArray(new String[]{}));
      if (entityDefinition.isLogicalDelete()) {
        sql.AND().WHERE(this.getEquation(entityDefinition.getTableField(entityDefinition.getDefaultTableAlias(), propertyIsDeleted.getField()), "0"));
      }
      return sql.toString();
    }
    return null;
  }

  /**
   * 查询数量
   *
   * @param context     上下文
   * @param entityQuery 实体查询
   * @return
   */
  public String selectCountBy(ProviderContext context, Query<E> entityQuery) {
    Objects.requireNonNull(entityQuery, "entityQuery is null");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinition(entityQuery.getEntityClass());
    if (null != entityDefinition) {
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();
      SQL sql = new SQL();
      sql.SELECT("count(0)");
      sql.FROM(entityDefinition.getTableName() + " " + entityQuery.getEntityAlias());

      /* 追加内连接 */
      if (entityQuery.getInnerJoinCollections().size() > 0) {
        for (QueryJoin<?, ? extends BaseEntity> entityJoin : entityQuery.getInnerJoinCollections()) {
          sql.INNER_JOIN(entityJoin.build());
        }
      }

      /* 追加外连接: */
      /* 追加外连接: 左连接 */
      if (entityQuery.getLeftJoinCollections().size() > 0) {
        for (QueryJoin<?, ? extends BaseEntity> entityJoin : entityQuery.getLeftJoinCollections()) {
          sql.LEFT_OUTER_JOIN(entityJoin.build());
        }
      }

      /* 追加外连接: 右连接 */
      if (entityQuery.getRightJoinCollections().size() > 0) {
        for (QueryJoin<?, ? extends BaseEntity> entityJoin : entityQuery.getRightJoinCollections()) {
          sql.RIGHT_OUTER_JOIN(entityJoin.build());
        }
      }

      /* WHERE */
      this.getWhereExpressions(sql, entityQuery.where().getWhereExpressions());
      if (entityDefinition.isLogicalDelete()) {
        sql.AND().WHERE(this.getEquation(entityDefinition.getTableField(entityQuery.getEntityAlias(), propertyIsDeleted.getField()), "0"));
      }

      return sql.toString();
    }
    return null;
  }

  /**
   * 查询数量
   *
   * @param context 上下文
   * @param id      主键查询
   * @return
   */
  public String selectCountByPrimary(ProviderContext context, Serializable id) {
    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    Objects.requireNonNull(entityDefinition, "entityDefinition is null");

    ResolvableType[] types = ResolvableType.forClass(context.getMapperType()).getInterfaces();
    Class<E> entityClass = (Class<E>) types[0].getGeneric(0).resolve();
    Query<E> query = new EntityQuery<>(entityClass, "et");
    query.where().and(entityDefinition.getPropertyId().getField() + " = ?", id);
    return selectCountBy(context, query);
  }

  /**
   * 查询实体
   *
   * @param context 上下文
   * @param entity  实体查询
   * @return
   */
  public String selectOne(ProviderContext context, E entity) {
    Objects.requireNonNull(entity, "entity is null");

    return this.byEntity(context, entity);
  }

  /**
   * 查询实体
   *
   * @param context     上下文
   * @param entityQuery 实体查询
   * @return
   */
  public String selectOneBy(ProviderContext context, Query<E> entityQuery) {
    Objects.requireNonNull(entityQuery, "entityQuery is null");

    return this.byEntityQuery(context, entityQuery);
  }

  /**
   * 查询实体
   *
   * @param context 上下文
   * @param id      主键查询
   * @return
   */
  public String selectOneByPrimary(ProviderContext context, Serializable id) {
    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    return byPrimary(context, id, entityDefinition);
  }

  /**
   * 查询实体列表
   *
   * @param context 上下文
   * @param entity  实体查询
   * @return
   */
  public String selectList(ProviderContext context, E entity) {
    Objects.requireNonNull(entity, "entity is null");

    return this.byEntity(context, entity);
  }

  /**
   * 查询实体列表
   *
   * @param context     上下文
   * @param entityQuery 实体查询
   * @return
   */
  public String selectListBy(ProviderContext context, Query<E> entityQuery) {
    Objects.requireNonNull(entityQuery, "entityQuery is null");

    return this.byEntityQuery(context, entityQuery);
  }

  /**
   * 按主键查询
   *
   * @param context          上下文
   * @param id               主键查询
   * @param entityDefinition 实体定义
   * @return
   */
  private String byPrimary(ProviderContext context, Serializable id, EntityDefinition entityDefinition) {
    Objects.requireNonNull(id, "id is null");
    Objects.requireNonNull(entityDefinition, "entityDefinition is null");

    EntityProperty propertyId = entityDefinition.getPropertyId();
    SQL sql = new SQL();
    sql.SELECT(this.getSelectColumns(entityDefinition, entityDefinition.getDefaultTableAlias()).toArray(new String[]{}));
    sql.FROM(entityDefinition.getTableName() + " " + entityDefinition.getDefaultTableAlias());

    sql.WHERE(this.getEquation(entityDefinition.getTableField(entityDefinition.getDefaultTableAlias(), propertyId.getField()), propertyId.getProperty(), true));
    if (entityDefinition.isLogicalDelete()) {
      sql.AND().WHERE(this.getEquation(entityDefinition.getTableField(entityDefinition.getDefaultTableAlias(), entityDefinition.getPropertyIsDeleted().getField()), "0"));
    }

    return sql.toString();
  }

  /**
   * 按实体查询
   *
   * @param context 上下文
   * @param entity  实体查询
   * @return
   */
  private String byEntity(ProviderContext context, E entity) {
    EntityDefinition entityDefinition = EntityUtils.getEntityDefinition(entity.getClass());
    if (null != entityDefinition) {
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();
      /* sql */
      SQL sql = new SQL();
      sql.SELECT(this.getSelectColumns(entityDefinition, entityDefinition.getDefaultTableAlias()).toArray(new String[]{}));
      sql.FROM(entityDefinition.getTableName() + " " + entityDefinition.getDefaultTableAlias());

      sql.WHERE(this.getSelectWhereConditions(entityDefinition, entity, entityDefinition.getDefaultTableAlias()).toArray(new String[]{}));
      if (entityDefinition.isLogicalDelete()) {
        sql.AND().WHERE(this.getEquation(entityDefinition.getTableField(entityDefinition.getDefaultTableAlias(), propertyIsDeleted.getField()), "0"));
      }

      return sql.toString();
    }
    return null;
  }

  /**
   * 按实体包装器查询
   *
   * @param context     上下文
   * @param entityQuery 实体查询
   * @return
   */
  private String byEntityQuery(ProviderContext context, Query<E> entityQuery) {
    EntityDefinition entityDefinition = EntityUtils.getEntityDefinition(entityQuery.getEntityClass());
    if (null != entityDefinition) {
      EntityProperty propertyIsDeleted = entityDefinition.getPropertyIsDeleted();
      /* sql */
      SQL sql = new SQL();
      if (entityQuery.isSelectDefaults()) {
        sql.SELECT(this.getSelectColumns(entityDefinition, entityQuery.getEntityAlias()).toArray(new String[]{}));
      }

      /* 追加SELECT */
      if (entityQuery.getSelectExpressions().size() > 0) {
        sql.SELECT(entityQuery.getSelectExpressions().toArray(new String[]{}));
      }

      /* FROM */
      sql.FROM(entityDefinition.getTableName() + " " + entityQuery.getEntityAlias());

      /* 追加内连接 */
      if (entityQuery.getInnerJoinCollections().size() > 0) {
        for (QueryJoin<?, ? extends BaseEntity> entityJoin : entityQuery.getInnerJoinCollections()) {
          sql.INNER_JOIN(entityJoin.build());
        }
      }

      /* 追加外连接: */
      /* 追加外连接: 左外连接 */
      if (entityQuery.getLeftJoinCollections().size() > 0) {
        for (QueryJoin<?, ? extends BaseEntity> entityJoin : entityQuery.getLeftJoinCollections()) {
          sql.LEFT_OUTER_JOIN(entityJoin.build());
        }
      }

      /* 追加外连接: 右外连接 */
      if (entityQuery.getRightJoinCollections().size() > 0) {
        for (QueryJoin<?, ? extends BaseEntity> entityJoin : entityQuery.getRightJoinCollections()) {
          sql.RIGHT_OUTER_JOIN(entityJoin.build());
        }
      }

      /* WHERE:逻辑删除 */
      /* 追加WHERE */
      if (entityQuery.where().getWhereExpressions().size() > 0) {
        this.getWhereExpressions(sql, entityQuery.where().getWhereExpressions());
      }

      if (entityDefinition.isLogicalDelete()) {
        sql.AND().WHERE(this.getEquation(entityDefinition.getTableField(entityQuery.getEntityAlias(), propertyIsDeleted.getField()), "0"));
      }

      /* 分组 */
      if (entityQuery.getGroupByExpressions().size() > 0) {
        sql.GROUP_BY(entityQuery.getGroupByExpressions().toArray(new String[]{}));
      }

      /* 排序 */
      if (entityQuery.getOrderByExpressions().size() > 0) {
        sql.ORDER_BY(entityQuery.getOrderByExpressions().toArray(new String[]{}));
      }

      return sql.toString();
    }
    return null;
  }

}
