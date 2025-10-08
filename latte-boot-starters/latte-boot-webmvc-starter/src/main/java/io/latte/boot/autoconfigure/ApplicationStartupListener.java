package io.latte.boot.autoconfigure;

import io.latte.boot.webmvc.registrar.DomainModelEndpointScannerRegistrar;
import io.latte.boot.webmvc.registrar.EntityScannerRegistrar;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 应用容器启动完成监听器
 *
 * @author : wugz
 * @since : 2025/9/19
 */
@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
  private final DomainModelEndpointScannerRegistrar domainModelEndpointScannerRegistrar;
  private final EntityScannerRegistrar entityScannerRegistrar;

  public ApplicationStartupListener(
      DomainModelEndpointScannerRegistrar domainModelEndpointScannerRegistrar,
      EntityScannerRegistrar entityScannerRegistrar) {
    this.domainModelEndpointScannerRegistrar = domainModelEndpointScannerRegistrar;
    this.entityScannerRegistrar = entityScannerRegistrar;
  }

  public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
    domainModelEndpointScannerRegistrar.scanAndRegister();
    entityScannerRegistrar.scanAndRegister();
  }
}
