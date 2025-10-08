package io.latte.boot.autoconfigure.registrar;

import io.latte.boot.mapper.MapperEntityProperties;
import io.latte.boot.webmvc.ApplicationProperties;
import io.latte.boot.webmvc.registrar.DomainModelEndpointScannerRegistrar;
import io.latte.boot.webmvc.registrar.EntityScannerRegistrar;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * ComponentScannerRegistrarAutoConfiguration
 *
 * @author : wugz
 * @since : 2025/9/25
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = {MapperEntityProperties.class, ApplicationProperties.class})
public class ComponentScannerRegistrarAutoConfiguration {
  @Bean
  public DomainModelEndpointScannerRegistrar domainModelEndpointScannerRegistrar(ApplicationContext applicationContext,
                                                                                 ApplicationProperties applicationProperties,
                                                                                 @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping requestMappingHandlerMapping) {
    return new DomainModelEndpointScannerRegistrar(
        applicationContext,
        applicationProperties,
        requestMappingHandlerMapping);
  }

  @Bean
  public EntityScannerRegistrar entityScannerRegistrar(MapperEntityProperties mapperEntityProperties) {
    return new EntityScannerRegistrar(mapperEntityProperties);
  }
}
