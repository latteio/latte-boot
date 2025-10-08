package io.latte.boot.mapper;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EntityUtils
 *
 * @author : wugz
 * @since : 2018/4/24
 */
public final class EntityUtils {
  private static final Logger logger = LoggerFactory.getLogger(EntityUtils.class);
  /**
   * 实体信息缓存表
   */
  private static final Map<String, EntityDefinition> ENTITY_DEFINITION_MAP = new ConcurrentHashMap<>();
  private static final Map<Class<?>, SerializedLambda> ENTITY_CLASS_LAMBDA_CACHE = new ConcurrentHashMap<>();

  /**
   * 构造函数
   */
  private EntityUtils() {
  }

  /**
   * 获得基于指定基类的目标类的泛型实体
   *
   * @param targetParamOrMapperClass
   * @return
   */
  public static EntityDefinition getEntityDefinitionBy(Class<?> targetParamOrMapperClass) {
    Objects.requireNonNull(targetParamOrMapperClass, "targetParamOrMapperClass is null");

    Type[] baseMappers = targetParamOrMapperClass.getGenericInterfaces();
    for (Type mapper : baseMappers) {
      if (mapper instanceof ParameterizedType) {
        Type entityType = ((ParameterizedType) mapper).getActualTypeArguments()[0];
        if (null != entityType) {
          return getEntityDefinition(entityType.getTypeName());
        }
      }
    }

    return null;
  }

  /**
   * 返回实体定义信息
   *
   * @param entityClass
   * @return
   */
  public static EntityDefinition getEntityDefinition(Class<?> entityClass) {
    if (null != entityClass) {
      String key = entityClass.getName();
      return getEntityDefinition(key);
    }
    return null;
  }

  /**
   * 返回实体定义信息
   *
   * @param entityClassName
   * @return
   */
  private static EntityDefinition getEntityDefinition(String entityClassName) {
    if (!Strings.isNullOrEmpty(entityClassName) && ENTITY_DEFINITION_MAP.containsKey(entityClassName)) {
      return ENTITY_DEFINITION_MAP.get(entityClassName);
    }
    return null;
  }

  /**
   * 注册实体并返回实体定义信息
   *
   * @param entityClassName
   * @return
   */
  public static EntityDefinition registerAndGetEntityDefinition(String entityClassName, EntityFieldMappings entityFieldMappings) {
    EntityDefinition entityDefinition = null;

    /* 定义存在: 直接返回 */
    if (!Strings.isNullOrEmpty(entityClassName) && ENTITY_DEFINITION_MAP.containsKey(entityClassName)) {
      entityDefinition = ENTITY_DEFINITION_MAP.get(entityClassName);
      if (null != entityDefinition) {
        return entityDefinition;
      }
    }

    /* 定义不存在: 定义之并返回 */
    try {
      Class<?> entityClass = Class.forName(entityClassName);
      entityDefinition = new EntityDefinition(entityClass, entityFieldMappings);
      ENTITY_DEFINITION_MAP.put(entityClassName, entityDefinition);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    return entityDefinition;
  }

  /**
   * 返回Getter方法对应的属性名称和其他meta信息
   *
   * @param getter
   * @param <T>
   * @return
   */
  public static <T> EntityPropertyOrMethodMeta getPropertyMetaFromGetter(EntityGetter<T> getter) {
    SerializedLambda lambda = getSerializedLambda(getter);
    String methodName = lambda.getImplMethodName();

    /* 方法校验 */
    if (!methodName.startsWith("get") && !methodName.startsWith("is")) {
      throw new RuntimeException("Not getter method: " + methodName);
    }

    String prefix = "";
    if (methodName.startsWith("get")) {
      prefix = "get";
    }
    if (methodName.startsWith("is")) {
      prefix = "is";
    }

    String className = lambda.getImplClass().replace("/", ".");
    EntityDefinition entityDefinition = getEntityDefinition(className);
    String propertyName = uncapitalize(methodName.replace(prefix, ""));
    String fieldName = null != entityDefinition ? entityDefinition.getPropertyFieldMap().get(propertyName) : null;

    return new EntityPropertyOrMethodMeta(className, methodName, propertyName, fieldName);
  }

  /**
   * 缓存函数接口的Lambda表达式
   *
   * @param fn
   * @return
   */
  private synchronized static SerializedLambda getSerializedLambda(Serializable fn) {
    SerializedLambda lambda = ENTITY_CLASS_LAMBDA_CACHE.get(fn.getClass());
    try {
      if (null == lambda) {
        Method method = fn.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(Boolean.TRUE);
        lambda = (SerializedLambda) method.invoke(fn);
        ENTITY_CLASS_LAMBDA_CACHE.put(fn.getClass(), lambda);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    return lambda;
  }

  /**
   * Uncapitalizes a String, changing the first character to lower case.
   *
   * @param str
   * @return
   */
  private static String uncapitalize(String str) {
    final int strLen = null == str ? 0 : str.length();
    if (strLen == 0) {
      return str;
    }

    final int firstCodepoint = str.codePointAt(0);
    final int newCodePoint = Character.toLowerCase(firstCodepoint);
    if (firstCodepoint == newCodePoint) {
      // already capitalized
      return str;
    }

    // cannot be longer than the char array
    final int[] newCodePoints = new int[strLen];
    int outOffset = 0;
    // copy the first codepoint
    newCodePoints[outOffset++] = newCodePoint;
    for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
      final int codepoint = str.codePointAt(inOffset);
      // copy the remaining ones
      newCodePoints[outOffset++] = codepoint;
      inOffset += Character.charCount(codepoint);
    }
    return new String(newCodePoints, 0, outOffset);
  }

}
