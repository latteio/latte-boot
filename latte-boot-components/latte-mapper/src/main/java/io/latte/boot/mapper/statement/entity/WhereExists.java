package io.latte.boot.mapper.statement.entity;

import io.latte.boot.mapper.EntityUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Objects;

/**
 * Where Exists逻辑关系表达式子句
 *
 * @author : wugz
 * @since : 2022/2/5
 */
public class WhereExists extends SQL implements LogicalGroup {
  private final LogicalType logicalType;
  private final boolean notExists;
  private final Class<?> entityClass;
  private final String entityAlias;

  /**
   * 构造函数
   */
  public WhereExists(LogicalType logicalType,
                     boolean notExists,
                     Class<?> entityClass,
                     String entityAlias) {
    Objects.requireNonNull(logicalType);
    Objects.requireNonNull(entityClass);
    Objects.requireNonNull(entityAlias);

    this.logicalType = logicalType;
    this.notExists = notExists;
    this.entityClass = entityClass;
    this.entityAlias = entityAlias;
    String tableName = EntityUtils.getEntityDefinition(entityClass).getTableName();

    /* 构造Where Exists子句 */
    this.SELECT("true").FROM(tableName + " " + entityAlias);
  }

  public LogicalType getLogicalType() {
    return logicalType;
  }

  public Class<?> getEntityClass() {
    return entityClass;
  }

  public String getEntityAlias() {
    return entityAlias;
  }

  public String getGroup() {
    String sql = super.toString();
    StringBuilder sqlBuilder = new StringBuilder();

    if (this.notExists) {
      sqlBuilder.append("NOT ");
    }

    sqlBuilder.append("EXISTS")
        .append("(")
        .append(sql)
        .append(")");
    return sqlBuilder.toString();
  }

}
