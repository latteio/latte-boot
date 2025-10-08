package io.latte.boot.support.web;

import com.google.common.base.Strings;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * MapUtils
 *
 * @author : wugz
 * @since : 2016/4/14
 */
public final class MapUtils {
  private static Logger logger = LoggerFactory.getLogger(MapUtils.class);

  /**
   * 构造函数
   */
  private MapUtils() {
  }

  /**
   * 获得参数Map
   *
   * @param request
   * @return
   */
  public static Map<String, Object> getParameterMap(HttpServletRequest request) {
    Map<String, Object> params = getParametersStartingWith(request, null);
    Map<String, Object> payLoadParams = getFromPayload(request);
    if (payLoadParams.size() > 0) {
      params.putAll(payLoadParams);
    }
    return params;
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
    Enumeration<String> paramNames = request.getParameterNames();
    Map<String, Object> params = new TreeMap<String, Object>();
    if (null == prefix || prefix.isEmpty()) {
      prefix = "";
    }
    while (paramNames != null && paramNames.hasMoreElements()) {
      String paramName = paramNames.nextElement();
      if ("".equals(prefix) || paramName.startsWith(prefix)) {
        String unprefixed = paramName.substring(prefix.length());
        String[] values = request.getParameterValues(paramName);
        if (values == null || values.length == 0) {
          // Do nothing, no values found at all.
        } else if (values.length > 1) {
          params.put(unprefixed, values);
        } else {
          params.put(unprefixed, values[0]);
        }
      }
    }
    return params;
  }

  /**
   * 从request payload里获取参数
   *
   * @param request
   * @return
   */
  private static Map<String, Object> getFromPayload(HttpServletRequest request) {
    Map<String, Object> body = new TreeMap<String, Object>();
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader = null;
    // 从流中读取参数
    try {
      InputStream inputStream = request.getInputStream();
      if (inputStream != null) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] charBuffer = new char[1024];
        int bytesRead = -1;
        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
          stringBuilder.append(charBuffer, 0, bytesRead);
        }
      }
      String bodyString = stringBuilder.toString();
      if (!Strings.isNullOrEmpty(bodyString)) {
        // 非JSON
        String[] params = bodyString.split("&");
        for (String param : params) {
          String[] values = param.split("=");
          if (values.length == 2) {
            body.put(values[0], values[1]);
          } else if (values.length == 1) {
            body.put(values[0], null);
          }
        }
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage());
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException ex) {
          logger.error(ex.getMessage());
        }
      }
    }
    return body;
  }

  /**
   * 将(逗号分割的)字符串转Map
   *
   * @param params
   * @return
   */
  public static Map<String, Object> getParamsMap(String params) {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    if (null != params && params.contains(",")) {
      String[] paramsString = params.split(",");
      for (int e = 0; e < paramsString.length; e += 2) {
        if ((e + 1) < paramsString.length) {
          returnMap.put(paramsString[e], paramsString[e + 1]);
        } else {
          returnMap.put(paramsString[e], null);
        }
      }
    }
    return returnMap;
  }

  /**
   * 将url键值对转换成Map
   *
   * @param url
   * @return
   */
  public static Map<String, Object> fromURL(String url) {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    if (null != url) {
      String[] urls = url.split("[\\{\\?&\\}]");
      String key = null, val = null;
      for (String u : urls) {
        if (null != u && !u.isEmpty()) {
          String[] values = u.split("=");
          try {
            if (values.length == 2) {
              key = URLDecoder.decode(values[0], "UTF-8");
              val = URLDecoder.decode(values[1], "UTF-8");
            } else if (values.length == 1) {
              key = URLDecoder.decode(values[0], "UTF-8");
              val = null;
            }
            returnMap.put(key, val);
          } catch (Exception ignored) {
          }
        }
      }
    }
    return returnMap;
  }

  /**
   * 将bean包装成map
   *
   * @param bean
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> from(Object bean) {
    if (null != bean) {
      if (bean instanceof Map) {
        return new HashMap<>((Map<String, Object>) bean);
      } else {
        return new JSONObject(bean).toMap();
      }
    }
    return new HashMap<>();
  }

  /**
   * 创建builder
   *
   * @return
   */
  public static <K, V> MapBuilder<K, V> newBuilder() {
    return new MapBuilder<K, V>();
  }

  /**
   * 创建builder
   *
   * @param map
   * @return
   */
  public static <K, V> MapBuilder<K, V> newBuilder(Map<K, V> map) {
    return new MapBuilder<K, V>(map);
  }

  /**
   * MapBuilder
   *
   * @author : wugz
   * @since : 2016/4/14
   */
  public static class MapBuilder<K, V> implements Serializable {
    private Map<K, V> map;

    /**
     * 构造函数
     */
    public MapBuilder() {
      this(new HashMap<K, V>());
    }

    /**
     * 构造函数
     *
     * @param map
     */
    public MapBuilder(Map<K, V> map) {
      this.map = null != map ? map : new HashMap<K, V>();
    }

    /**
     * 设置参数
     *
     * @param key
     * @param value
     * @return
     */
    public MapBuilder<K, V> put(K key, V value) {
      this.map.put(key, value);
      return this;
    }

    /**
     * 返回Map
     *
     * @return
     */
    public Map<K, V> map() {
      return this.map;
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return
     */
    public int size() {
      return this.map.size();
    }

    /**
     * 创建Json
     *
     * @return
     */
    public String toJson() {
      if (this.map.size() > 0) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Map.Entry<K, V> entry : this.map.entrySet()) {
          builder.append("\"").append(entry.getKey()).append("\"").append(":").append("\"").append(entry.getValue()).append("\"");
          builder.append(", ");
        }
        return new StringBuilder(builder.subSequence(0, builder.lastIndexOf(","))).append("}").toString();
      }
      return "{}";
    }
  }
}
