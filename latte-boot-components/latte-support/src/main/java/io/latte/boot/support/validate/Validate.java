package io.latte.boot.support.validate;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Validate
 *
 * @author : wugz
 * @since : 2021/2/3
 */
public final class Validate {
  private static final String VALIDATION_IS_NULL = "Object is null";
  private static final String VALIDATION_IS_EMPTY = "Object is null or empty";

  /**
   * 构造函数
   */
  private Validate() {
  }

  /**
   * 非空校验
   *
   * @param object
   * @return
   */
  public static <T> T requireNonNull(final T object) {
    return requireNonNull(object, VALIDATION_IS_NULL);
  }

  /**
   * 非空校验
   *
   * @param object
   * @param message
   * @param values
   * @return
   */
  public static <T> T requireNonNull(final T object, final String message, final Object... values) {
    if (null == object) {
      throw new NullPointerException(String.format(message, values));
    }
    return object;
  }

  /**
   * 非空校验
   *
   * @param object
   * @return
   */
  public static <T> T requireNonEmpty(final T object) {
    return requireNonEmpty(object, VALIDATION_IS_EMPTY);
  }

  /**
   * 非空校验
   *
   * @param object
   * @param message
   * @param values
   * @return
   */
  public static <T> T requireNonEmpty(final T object, final String message, final Object... values) {
    if (isNullOrBlank(object)) {
      throw new NullPointerException(String.format(message, values));
    }
    return object;
  }

  /**
   * 满足条件测试: 条件不满足抛出异常
   *
   * @param condition
   * @param message
   */
  public static boolean requireTrue(boolean condition, final String message) {
    if (!condition) {
      throw new RuntimeException(message);
    }
    return true;
  }

  /**
   * 满足条件测试: 条件不满足抛出异常
   *
   * @param condition
   * @param message
   */
  public static boolean requireFalse(boolean condition, final String message) {
    if (condition) {
      throw new RuntimeException(message);
    }
    return true;
  }

  /**
   * Spring验空
   *
   * @param obj
   * @return
   */
  public static boolean isNullOrBlank(Object obj) {
    if (null == obj) {
      return true;
    }

    if (obj instanceof Optional) {
      return ((Optional<?>) obj).isEmpty();
    }
    if (obj instanceof CharSequence) {
      return ((CharSequence) obj).isEmpty();
    }
    if (obj.getClass().isArray()) {
      return Array.getLength(obj) == 0;
    }
    if (obj instanceof Collection) {
      return ((Collection<?>) obj).isEmpty();
    }
    if (obj instanceof Map) {
      return ((Map<?, ?>) obj).isEmpty();
    }

    // else
    return false;
  }

}
