package io.latte.boot.webmvc.infrastructure.adapter.impl;

import io.latte.boot.webmvc.infrastructure.adapter.DatasourceDateTimeAdapter;

/**
 * DerbyDatasourceDateTimeAdapter
 *
 * @author : wugz
 * @since : 2025/9/17
 */
public class DerbyDatasourceDateTimeAdapter implements DatasourceDateTimeAdapter {
  public DerbyDatasourceDateTimeAdapter() {
  }

  public Object now() {
    return new java.sql.Timestamp(System.currentTimeMillis());
  }
}
