package io.latte.boot.webmvc.infrastructure.adapter.impl;

import io.latte.boot.webmvc.infrastructure.adapter.DatasourceDateTimeAdapter;

import java.time.LocalDateTime;

/**
 * DefaultDatasourceDateTimeAdapter
 *
 * @author : wugz
 * @since : 2025/9/17
 */
public class DefaultDatasourceDateTimeAdapter implements DatasourceDateTimeAdapter {
  public DefaultDatasourceDateTimeAdapter() {
  }

  public Object now() {
    return LocalDateTime.now();
  }
}
