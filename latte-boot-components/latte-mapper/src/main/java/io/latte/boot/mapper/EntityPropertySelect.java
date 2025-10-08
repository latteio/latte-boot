package io.latte.boot.mapper;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EntityPropertySelect
 *
 * @author : wugz
 * @since : 2018/4/26
 */
public class EntityPropertySelect extends EntityProperty {
  private final boolean isTransient;
  private String text;

  /**
   * 构造函数
   *
   * @param property 属性
   * @param field    字段
   */
  public EntityPropertySelect(String property, String field) {
    super(property, field);
    this.text = new StringBuilder().append("{0}").append(".").append(field).append(" AS ").append(property).toString();
    this.isTransient = false;
  }

  /**
   * 构造函数
   *
   * @param property    属性
   * @param field       字段
   * @param isTransient 是否临时属性
   */
  public EntityPropertySelect(String property, String field, boolean isTransient) {
    super(property, field);
    this.text = new StringBuilder().append("{0}").append(".").append(field).append(" AS ").append(property).toString();
    this.isTransient = isTransient;
  }

  /**
   * 构造函数
   *
   * @param property    属性
   * @param sql         sql语句
   * @param isTransient 是否临时属性
   * @param flag        sql标志
   */
  public EntityPropertySelect(String property, String sql, boolean isTransient, int flag) {
    super(property, null);
    this.text = MessageFormat.format("{0} AS {1}", handleReferField(sql), property);
    this.isTransient = isTransient;
  }

  /**
   * 解析sql子查询中的引用字段
   *
   * @param sql
   * @return
   */
  private static String handleReferField(String sql) {
    Pattern pat = Pattern.compile("#\\{\\w+\\}");
    Matcher matcher = pat.matcher(sql);
    while (matcher.find()) {
      String matchedString = matcher.group();
      String referField = matchedString.substring(2, matchedString.length() - 1);
      sql = sql.replace(matchedString, new StringBuilder().append("{0}").append(".").append(referField));
    }
    return (sql.startsWith("(") && sql.endsWith(")")) ? sql : ("(" + sql + ")");
  }

  public String getText() {
    return text;
  }

  public boolean isTransient() {
    return isTransient;
  }
}
