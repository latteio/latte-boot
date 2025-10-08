package io.latte.boot.mapper;

import java.text.MessageFormat;

/**
 * EntityPropertyDefault
 *
 * @author : wugz
 * @since : 2024/8/19
 */
public class EntityProperty {
  private final String property;
  private final String field;
  private final String text;

  /**
   * 构造函数
   *
   * @param property 属性名
   * @param field    字段名
   */
  public EntityProperty(String property, String field) {
    this.property = property;
    this.field = field;
    this.text = MessageFormat.format("{0}{1}{2}", "#{", this.property, "}");
  }

  public String getProperty() {
    return property;
  }

  public String getField() {
    return field;
  }

  public String getText() {
    return text;
  }
}
