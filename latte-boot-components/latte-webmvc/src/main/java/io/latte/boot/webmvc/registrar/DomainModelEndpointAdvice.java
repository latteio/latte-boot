package io.latte.boot.webmvc.registrar;

import io.latte.boot.webmvc.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * DomainModelEndpointAdvice
 *
 * @author : wugz
 * @since : 2025/12/2
 */
public class DomainModelEndpointAdvice {
  private final Logger logger = LoggerFactory.getLogger(DomainModelEndpointAdvice.class);
  private final ApplicationProperties applicationProperties;

  public DomainModelEndpointAdvice(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  public void beforeInvoke(Object target, Method method, Object[] args) {
    if (applicationProperties.getSecurity().getHttpRequest().isUseTrace()) {
      logger.debug("\n ===> [Request method]: {}::{}\n ===> [Request params]: {}", target.getClass().getName(), method.getName(), args);
    }
  }

  public void afterInvoke() {
  }
}
