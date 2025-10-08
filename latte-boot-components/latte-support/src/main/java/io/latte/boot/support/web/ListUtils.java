package io.latte.boot.support.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ListUtils
 *
 * @author : wugz
 * @since : 2016/4/14
 */
public final class ListUtils {
  /**
   * 私有构造函数
   */
  private ListUtils() {
  }

  /**
   * 新建list
   *
   * @return
   */
  public static <T> ListBuilder<T> newBuilder() {
    return new ListBuilder<T>();
  }

  /**
   * 新建list
   *
   * @param list
   * @return
   */
  public static <T> ListBuilder<T> newBuilder(List<T> list) {
    return new ListBuilder<T>(list);
  }

  /**
   * 以指定元素初始化
   *
   * @param elements
   * @return
   */
  @SafeVarargs
  public static <T> ListBuilder<T> asList(T... elements) {
    ListBuilder<T> list = new ListBuilder<T>();
    if (null != elements && elements.length > 0) {
      for (T element : elements) {
        list.push(element);
      }
    }
    return list;
  }

  /**
   * 列表连接
   *
   * @param list
   * @param separator
   * @return
   */
  public static <T> String join(List<T> list, String separator) {
    ListBuilder<T> ol = new ListBuilder<T>(list);
    return ol.join(separator);
  }

  /**
   * 列表连接
   *
   * @param list
   * @param separator
   * @param joinField
   * @return
   */
  public static <T> String join(List<T> list, String separator, JoinField<T> joinField) {
    ListBuilder<T> ol = new ListBuilder<T>(list);
    return ol.join(separator, joinField);
  }

  /**
   * 连接字段
   *
   * @author : wugz
   * @since : 2016/4/14
   */
  public interface JoinField<T> {
    Object field(T element);
  }

  /**
   * ListBuilder
   *
   * @author : wugz
   * @since : 2016/4/14
   */
  public static class ListBuilder<T> implements Serializable {
    private List<T> list;

    /**
     * 构造函数
     */
    public ListBuilder() {
      this(new ArrayList<T>());
    }

    /**
     * 构造函数
     *
     * @param list
     */
    public ListBuilder(List<T> list) {
      this.list = null != list ? list : new ArrayList<T>();
    }

    /**
     * 添加元素
     *
     * @param data
     * @return
     */
    public ListBuilder<T> push(T data) {
      this.list.add(data);
      return this;
    }

    /**
     * 列表连接
     *
     * @param separator
     * @return
     */
    public String join(String separator) {
      StringBuilder builder = new StringBuilder();
      if (this.list.size() > 0) {
        builder.append(list.get(0));
        for (int e = 1; e < list.size(); e++) {
          builder.append(separator);
          builder.append(list.get(e));
        }
      }
      return builder.toString();
    }

    public String join(String separator, JoinField<T> joinField) {
      StringBuilder builder = new StringBuilder();
      if (this.list.size() > 0) {
        builder.append(null != joinField ? joinField.field(list.get(0)) : list.get(0));
        for (int e = 1; e < list.size(); e++) {
          builder.append(separator);
          builder.append(null != joinField ? joinField.field(list.get(e)) : list.get(e));
        }
      }
      return builder.toString();
    }

    /**
     * 返回list
     *
     * @return
     */
    public List<T> list() {
      return this.list;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return
     */
    public int size() {
      return this.list.size();
    }

    public String toString() {
      return this.list.toString();
    }
  }
}
