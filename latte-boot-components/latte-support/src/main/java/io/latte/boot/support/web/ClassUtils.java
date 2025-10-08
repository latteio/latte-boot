package io.latte.boot.support.web;

import io.latte.boot.support.validate.Validate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassUtils
 *
 * @author : wugz
 * @since : 2024/11/12
 */
public final class ClassUtils {
  private ClassUtils() {
  }

  /**
   * 获取类的所有字段
   *
   * @param clazz              类对象
   * @param traverseSuperclass 是否递归遍历父类
   * @return 字段集
   */
  public static Map<String, Field> getAllFields(Class<?> clazz, boolean traverseSuperclass) {
    Validate.requireNonNull(clazz);

    Map<String, Field> fields = new ConcurrentHashMap<>();
    for (Field field : clazz.getDeclaredFields()) {
      fields.put(field.getName(), field);
    }

    if (traverseSuperclass) {
      Class<?> superClazz = clazz.getSuperclass();
      while (null != superClazz && !superClazz.getName().equalsIgnoreCase("java.lang.Object")) {
        for (Field field : superClazz.getDeclaredFields()) {
          fields.putIfAbsent(field.getName(), field);
        }

        superClazz = superClazz.getSuperclass();
      }
    }

    return fields;
  }

  /**
   * 获取方法的参数类型集
   *
   * @param method 方法对象
   * @return 参数类型集
   */
  public static Class<?>[] getActualParameterTypes(Method method) {
    Validate.requireNonNull(method);

    Type[] genericParameterTypes = method.getGenericParameterTypes();
    List<Class<?>> actualParameterTypes = new ArrayList<>();
    for (Type genericParameterType : genericParameterTypes) {
      if (genericParameterType instanceof ParameterizedType parameterizedType) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length > 0) {
          actualParameterTypes.add((Class<?>) actualTypeArguments[0]);
        }
      } else {
        actualParameterTypes.add((Class<?>) genericParameterType);
      }
    }

    return actualParameterTypes.toArray(new Class<?>[]{});
  }

}
