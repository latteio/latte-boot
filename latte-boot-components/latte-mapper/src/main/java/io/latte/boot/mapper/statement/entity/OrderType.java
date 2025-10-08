package io.latte.boot.mapper.statement.entity;

/**
 * OrderType
 *
 * @author : wugz
 * @since : 2018/4/23
 */
public enum OrderType {
  /**
   * unknown
   */
  UNKNOWN("unknown"),
  /**
   * 升序排序
   */
  ASC("asc"),
  /**
   * 降序排序
   */
  DESC("desc");

  private String text;

  /**
   * 构造函数
   *
   * @param text
   */
  OrderType(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
