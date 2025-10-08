package io.latte.boot.autoconfigure.datasource;

import io.latte.boot.mapper.MapperEntityProperties;
import io.latte.boot.webmvc.infrastructure.adapter.DatasourceDateTimeAdapter;
import io.latte.boot.webmvc.infrastructure.adapter.impl.DefaultDatasourceDateTimeAdapter;
import io.latte.boot.webmvc.infrastructure.adapter.impl.DerbyDatasourceDateTimeAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DatatablePropertiesAdaptationAutoConfiguration
 *
 * @author : wugz
 * @since : 2025/9/17
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = {MapperEntityProperties.class})
public class DatatablePropertiesAdaptationAutoConfiguration {
  private final MapperEntityProperties mapperEntityProperties;

  public DatatablePropertiesAdaptationAutoConfiguration(MapperEntityProperties mapperEntityProperties) {
    this.mapperEntityProperties = mapperEntityProperties;
  }

  @Bean
  @ConditionalOnExpression(value = "'${application.mapper.entities.dialect:default}'.equalsIgnoreCase('default')")
  public DatasourceDateTimeAdapter defaultDatasourceDateTimeAdapter() {
    return new DefaultDatasourceDateTimeAdapter();
  }

  @Bean
  @ConditionalOnExpression(value = "'${application.mapper.entities.dialect:default}'.equalsIgnoreCase('org.hibernate.dialect.DerbyDialect')")
  public DatasourceDateTimeAdapter derbyDatasourceDateTimeAdapter() {
    return new DerbyDatasourceDateTimeAdapter();
  }
}
