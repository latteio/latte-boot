package io.latte.boot.support.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * JsonUtils
 *
 * @author : wugz
 * @since : 2021/10/10
 */
public final class JsonUtils {
  private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * 构造函数
   */
  private JsonUtils() {
  }

  /**
   * 返回ObjectMapper实例
   *
   * @return ObjectMapper
   */
  public static ObjectMapper getMapper() {
    return objectMapper;
  }

  /**
   * 对象转JSON字符串
   *
   * @param obj 对象
   * @param <T> 对象类型
   * @return 字符串
   */
  public static <T> String fromObject(T obj) {
    if (null == obj) {
      return null;
    }

    try {
      return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      logger.error("Parse object to string: {}", e.getMessage());
      return "";
    }
  }

  /**
   * JSON字符串转对象
   *
   * @param jsonString json字符串
   * @return 对象
   */
  public static <T> T toObject(String jsonString, Class<T> clazz) {
    if (null == jsonString || jsonString.isEmpty()) {
      return null;
    }

    try {
      return objectMapper.readValue(jsonString, clazz);
    } catch (Exception e) {
      logger.error("Parse string to object: {}", e.getMessage());
    }
    return null;
  }

  /**
   * JSON字符串转Map
   *
   * @param jsonString json字符串
   * @return HashMap
   */
  public static HashMap<String, Object> toMap(String jsonString) {
    return toObject(jsonString, HashMap.class);
  }

}
