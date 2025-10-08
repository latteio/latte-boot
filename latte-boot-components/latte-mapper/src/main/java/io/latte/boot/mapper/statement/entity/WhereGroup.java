package io.latte.boot.mapper.statement.entity;

import org.apache.ibatis.jdbc.SQL;

import java.util.Objects;

/**
 * Where逻辑关系表达式组
 * 因为SQL需要一个确切的语句类型(StatementType), 不然构造不成功.
 * 所以通过构造一个虚拟查询语句, WHERE部分后续填充真实条件, 最后取出这些条件实现.
 *
 * @author : wugz
 * @since : 2022/2/5
 */
public class WhereGroup extends SQL implements LogicalGroup {
  private final LogicalType logicalType;

  /**
   * 构造函数
   */
  public WhereGroup(LogicalType logicalType) {
    Objects.requireNonNull(logicalType);

    this.logicalType = logicalType;

    /* 构造虚拟查询语句 */
    this.SELECT("SELECT__COLUMN__").FROM("SELECT__TABLE__");
  }

  public LogicalType getLogicalType() {
    return logicalType;
  }

  public String getGroup() {
    String sql = super.toString();
    int whereIndex = sql.indexOf("WHERE");
    return whereIndex > 0
        ? sql.substring(whereIndex + 5).trim()
        : "";
  }

}
