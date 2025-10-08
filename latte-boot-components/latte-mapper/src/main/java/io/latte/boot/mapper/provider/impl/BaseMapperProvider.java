package io.latte.boot.mapper.provider.impl;

import io.latte.boot.mapper.EntityDefinition;
import io.latte.boot.mapper.EntityFields;
import io.latte.boot.mapper.EntityProperty;
import io.latte.boot.mapper.EntityPropertySelect;
import io.latte.boot.mapper.statement.entity.LogicalType;
import io.latte.boot.mapper.statement.entity.WhereType;
import io.latte.boot.support.web.MapUtils;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseMapperProvider
 *
 * @author : wugz
 * @since : 2018/4/23
 */
public abstract class BaseMapperProvider {
  /**
   * 返回表达式等于
   *
   * @param srcProperty  源字段或属性
   * @param destProperty 目标对象字段或属性
   * @return
   */
  protected String getEquation(String srcProperty, String destProperty) {
    return this.getEquation(srcProperty, destProperty, null, false);
  }

  /**
   * 返回表达式等于
   *
   * @param srcProperty  源字段或属性
   * @param destProperty 目标对象字段或属性
   * @param dynamic      是否动态等于
   * @return
   */
  protected String getEquation(String srcProperty, String destProperty, boolean dynamic) {
    return getEquation(srcProperty, destProperty, null, dynamic);
  }

  /**
   * 返回表达式等于
   *
   * @param srcProperty  源字段或属性
   * @param destProperty 目标对象字段或属性
   * @param dest         目标对象别名
   * @param dynamic      是否动态等于
   * @return
   */
  protected String getEquation(String srcProperty, String destProperty, String dest, boolean dynamic) {
    if (!dynamic) {
      return MessageFormat.format("{0} = {1}", srcProperty, destProperty);
    }
    return MessageFormat.format("{0} = {1}{2}{3}{4}", srcProperty, "#{", null != dest ? (dest + ".") : "", destProperty, "}");
  }

  /**
   * 返回查询的属性集
   *
   * @param entityDefinition 实体定义
   * @param tableAlias       表别名
   * @return
   */
  protected List<String> getSelectColumns(EntityDefinition entityDefinition, String tableAlias) {
    List<EntityPropertySelect> selectProperties = entityDefinition.getSelectProperties();
    List<String> properties = new ArrayList<>(selectProperties.size());
    for (EntityPropertySelect query : selectProperties) {
      properties.add(MessageFormat.format(query.getText(), tableAlias));
    }
    return properties;
  }

  /**
   * 返回动态等式集
   *
   * @param properties    属性集
   * @param whereMap      where
   * @param referenceName 引用名
   * @param selective     是否可被选择
   * @return
   */
  protected List<String> getSetEquations(List<EntityProperty> properties, Map<String, Object> whereMap, String referenceName, boolean selective) {
    List<String> results = new ArrayList<>();
    for (EntityProperty property : properties) {
      if (selective) {
        if (whereMap.containsKey(property.getProperty())) {
          results.add(this.getEquation(property.getField(), property.getProperty(), referenceName, true));
        }
      } else {
        results.add(this.getEquation(property.getField(), property.getProperty(), referenceName, true));
      }
    }
    return results;
  }

  /**
   * 返回动态等式集
   *
   * @param entityDefinition 实体定义
   * @param entity           实体
   * @param alias            别名
   * @return
   */
  protected List<String> getSelectWhereConditions(EntityDefinition entityDefinition, Object entity, String alias) {
    List<String> results = new ArrayList<>();
    Map<String, Object> propertiesMap = MapUtils.from(entity);
    for (EntityPropertySelect property : entityDefinition.getSelectProperties()) {
      if (null != propertiesMap.get(property.getProperty())) {
        results.add(this.getEquation(alias + "." + property.getField(), property.getProperty(), true));
      }
    }
    return results;
  }

  /**
   * 返回动态等式集
   *
   * @param properties    属性集
   * @param whereMap      where
   * @param referenceName 引用名
   * @return
   */
  protected List<String> getWhereEquations(List<EntityProperty> properties, Map<String, Object> whereMap, String referenceName) {
    List<String> results = new ArrayList<>();
    Map<String, Object> excludes = new HashMap<>();
    if (whereMap.containsKey(EntityFields.WHERE_PROPERTY_EXCLUDES)) {
      for (String key : whereMap.get(EntityFields.WHERE_PROPERTY_EXCLUDES).toString().split(",")) {
        excludes.put(key, key);
      }
    }
    for (EntityProperty property : properties) {
      if (!excludes.containsKey(property.getProperty()) && null != whereMap.get(property.getProperty())) {
        results.add(this.getEquation(property.getField(), property.getProperty(), referenceName, true));
      }
    }
    return results;
  }

  /**
   * 构造where条件
   *
   * @param sql              sql
   * @param whereExpressions where表达式集
   */
  protected void getWhereExpressions(SQL sql, List<WhereType> whereExpressions) {
    if (null != whereExpressions && !whereExpressions.isEmpty()) {
      for (WhereType whereType : whereExpressions) {
        if (StringUtils.hasText(whereType.getExpression())) {
          if (LogicalType.AND.equals(whereType.getLogicalType())) {
            sql.AND().WHERE(whereType.getExpression());
          } else if (LogicalType.OR.equals(whereType.getLogicalType())) {
            sql.OR().WHERE(whereType.getExpression());
          }
        }
      }
    }
  }

}
