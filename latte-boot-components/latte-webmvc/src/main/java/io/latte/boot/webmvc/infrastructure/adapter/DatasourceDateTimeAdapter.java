package io.latte.boot.webmvc.infrastructure.adapter;

/**
 * DatasourceDateTimeAdapter
 *
 * @author : wugz
 * @since : 2025/9/17
 */
public interface DatasourceDateTimeAdapter {
  /**
   * 获取系统当前时间
   *
   * @return 系统当前时间
   */
  Object now();
}
