package io.latte.boot.mapper.provider.impl;

import com.google.common.base.Strings;
import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.EntityDefinition;
import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.support.web.IdUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.util.Objects;

/**
 * BaseInsertMapperProvider
 *
 * @author : wugz
 * @since : 2018/4/23
 */
public class BaseInsertMapperProvider<E extends BaseEntity> extends BaseMapperProvider {
  /**
   * 插入实体(全字段)
   *
   * @param context 上下文
   * @param entity  实参
   * @return
   */
  public String insert(ProviderContext context, E entity) {
    Objects.requireNonNull(entity, "entity is null");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    if (null != entityDefinition) {
      /* 设置主键值 */
      if (Strings.isNullOrEmpty(entity.getId())) {
        entity.setId(IdUtils.getId());
      }

      /* 设置逻辑删除标志 */
      if (entityDefinition.isLogicalDelete()) {
        entity.setIsDeleted(0);
      }

      SQL sql = new SQL();
      sql.INSERT_INTO(entityDefinition.getTableName());
      sql.INTO_COLUMNS(entityDefinition.getInsertColumns(entity, false).toArray(new String[]{}));
      sql.INTO_VALUES(entityDefinition.getInsertValues(entity, false).toArray(new String[]{}));
      return sql.toString();
    }
    return null;
  }

  /**
   * 插入实体(选择字段)
   *
   * @param context 上下文
   * @param entity  实参
   * @return
   */
  public String insertSelective(ProviderContext context, E entity) {
    Objects.requireNonNull(entity, "entity is null");

    EntityDefinition entityDefinition = EntityUtils.getEntityDefinitionBy(context.getMapperType());
    if (null != entityDefinition) {
      /* 设置主键值 */
      if (Strings.isNullOrEmpty(entity.getId())) {
        entity.setId(IdUtils.getId());
      }

      /* 设置逻辑删除标志 */
      if (entityDefinition.isLogicalDelete()) {
        entity.setIsDeleted(0);
      }

      SQL sql = new SQL();
      sql.INSERT_INTO(entityDefinition.getTableName());
      sql.INTO_COLUMNS(entityDefinition.getInsertColumns(entity, true).toArray(new String[]{}));
      sql.INTO_VALUES(entityDefinition.getInsertValues(entity, true).toArray(new String[]{}));
      return sql.toString();
    }
    return null;
  }
}
