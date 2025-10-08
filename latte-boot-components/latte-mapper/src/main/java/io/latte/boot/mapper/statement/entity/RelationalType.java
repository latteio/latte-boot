package io.latte.boot.mapper.statement.entity;

/**
 * RelationalType
 *
 * @author : wugz
 * @since : 2021/9/30
 */
public enum RelationalType {
  /**
   * 大于
   */
  GT(" > "),
  /**
   * 大于或等于
   */
  GE(" >= "),
  /**
   * 小于
   */
  LT(" < "),
  /**
   * 小于或等于
   */
  LE(" <= "),
  /**
   * 等于
   */
  EQ(" = "),
  /**
   * 不等于
   */
  NEQ(" != "),

  /**
   * null or not null
   */
  IS_NULL(" is null"),
  IS_NOT_NULL(" is not null");

  private String text;

  RelationalType(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
