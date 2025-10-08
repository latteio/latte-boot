package io.latte.boot.mapper.statement.entity;

/**
 * LogicalType
 *
 * @author : wugz
 * @since : 2021/9/28
 */
public enum LogicalType {
  NA("NA"),
  AND("AND"),
  OR("OR");

  private String text;

  LogicalType(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public String getPrettyText() {
    return " " + text + " ";
  }
}
