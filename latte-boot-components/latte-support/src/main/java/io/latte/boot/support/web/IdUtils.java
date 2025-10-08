package io.latte.boot.support.web;

import io.latte.boot.support.lang.Snowflake;

/**
 * IdUtils
 *
 * @author : wugz
 * @since : 2018/6/29
 */
public final class IdUtils {
  private volatile static Snowflake INSTANCE = getInstance();

  /**
   * 构造函数
   */
  private IdUtils() {
  }

  private static Snowflake getInstance() {
    if (null == INSTANCE) {
      synchronized (Snowflake.class) {
        if (null == INSTANCE) {
          INSTANCE = new Snowflake(1, 1);
        }
      }
    }
    return INSTANCE;
  }

  public static String getId() {
    return String.valueOf(INSTANCE.nextId());
  }

  /**
   * 20210425: 64位数值转hex, 避免js端接收数值(最大53位)字符串转数字出现位数不匹配问题.
   *
   * @return
   */
  public static String getHexId() {
    return Long.toHexString(INSTANCE.nextId());
  }

}
