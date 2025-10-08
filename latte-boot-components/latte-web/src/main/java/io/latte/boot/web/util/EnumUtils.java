package io.latte.boot.web.util;

import io.latte.boot.web.annotation.validation.IEnumType;
import io.latte.boot.web.http.login.AppClientEnum;

import java.util.Objects;

/**
 * EnumUtils
 *
 * @author : wugz
 * @since : 2022/3/11
 */
public final class EnumUtils {
  private EnumUtils() {
  }

  /**
   * 按code码返回枚举实例
   *
   * @param enumTypeClass 枚举类
   * @param code          枚举编码值
   * @return
   */
  public static <E extends Enum<E> & IEnumType<T>, T> E codeOf(Class<E> enumTypeClass, T code) {
    final Class<E> targetClass = Objects.requireNonNull(
        enumTypeClass,
        "Argument 'enumTypeClass' cannot be null");

    E[] enumEntries = targetClass.getEnumConstants();
    if (null != enumEntries) {
      for (E enumEntry : enumEntries) {
        if (enumEntry.getCode().equals(code)) {
          return enumEntry;
        }
      }
    }
    return null;
  }

  /**
   * 按枚举名称name返回枚举实例
   *
   * @param enumTypeClass 枚举类
   * @param name          枚举名称
   * @return
   * @see AppClientEnum (name is ADMIN, APP, WEB, ...)
   */
  public static <E extends Enum<E>> E nameOf(Class<E> enumTypeClass, String name) {
    Objects.requireNonNull(enumTypeClass,
        "Argument 'enumTypeClass' cannot be null");

    return Enum.valueOf(enumTypeClass, name);
  }

  /**
   * 校验目标code是否在指定枚举类型中定义
   *
   * @param enumTypeClass 枚举类
   * @param code          枚举编码值
   * @return
   */
  public static <E extends Enum<E> & IEnumType<T>, T> boolean validate(Class<E> enumTypeClass, T code) {
    return null != codeOf(enumTypeClass, code);
  }
}
