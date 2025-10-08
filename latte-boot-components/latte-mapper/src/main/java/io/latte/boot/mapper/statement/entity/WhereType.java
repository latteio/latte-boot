package io.latte.boot.mapper.statement.entity;


/**
 * Where表达式类型
 *
 * @author : wugz
 * @since : 2018/4/23
 */
public class WhereType {
  private final LogicalType logicalType;
  private final boolean isGroup;
  private final LogicalGroup logicalGroup;
  private final String expression;

  public WhereType(LogicalType logicalType, String expression) {
    this.isGroup = false;
    this.logicalType = logicalType;
    this.logicalGroup = null;
    this.expression = expression;
  }

  public WhereType(LogicalType logicalType, String... expressions) {
    this.isGroup = false;
    this.logicalType = logicalType;
    this.logicalGroup = null;
    StringBuilder exprBuilder = new StringBuilder();
    for (String expr : expressions) {
      exprBuilder.append(expr);
    }
    this.expression = exprBuilder.toString();
  }

  public WhereType(LogicalGroup logicalGroup) {
    this.isGroup = true;
    this.logicalType = logicalGroup.getLogicalType();
    this.logicalGroup = logicalGroup;
    this.expression = null;
  }

  public LogicalType getLogicalType() {
    return logicalType;
  }

  public String getExpression() {
    return isGroup && null != logicalGroup ? logicalGroup.getGroup() : expression;
  }

}
