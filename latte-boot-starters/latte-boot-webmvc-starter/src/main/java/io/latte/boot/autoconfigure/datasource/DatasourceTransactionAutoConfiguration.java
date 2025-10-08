package io.latte.boot.autoconfigure.datasource;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据源事务自动配置器
 * (启用Spring事务管理)
 *
 * @author : wugz
 * @since : 2021/7/22
 */
@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
public class DatasourceTransactionAutoConfiguration {

  public DatasourceTransactionAutoConfiguration() {
  }

}
