package io.latte.boot.webmvc.registrar;

import io.latte.boot.mapper.EntityUtils;
import io.latte.boot.mapper.MapperEntityProperties;
import io.latte.boot.mapper.annotation.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * EntityScannerRegistrar
 *
 * @author : wugz
 * @since : 2021/6/15
 */
public class EntityScannerRegistrar implements IComponentScannerRegistrar {
  private final Logger logger = LoggerFactory.getLogger(EntityScannerRegistrar.class);
  private final MapperEntityProperties mapperEntityProperties;

  public EntityScannerRegistrar(MapperEntityProperties mapperEntityProperties) {
    this.mapperEntityProperties = mapperEntityProperties;
  }

  public void scanAndRegister() {
    logger.debug("Start scanning entities ...");

    ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
    componentProvider.addIncludeFilter((@NonNull MetadataReader metadataReader, @NonNull MetadataReaderFactory metadataReaderFactory) -> metadataReader.getAnnotationMetadata().hasAnnotation(Entity.class.getName()));

    String[] basePackages = mapperEntityProperties.getBasePackages().split(",");
    for (String basePackage : basePackages) {
      Set<BeanDefinition> beanDefinitions = componentProvider.findCandidateComponents(basePackage);
      for (BeanDefinition definition : beanDefinitions) {
        EntityUtils.registerAndGetEntityDefinition(definition.getBeanClassName(), mapperEntityProperties.getFieldMappings());
        if (logger.isDebugEnabled()) {
          logger.debug("Scanned entity: {}", definition.getBeanClassName());
        }
      }
    }
  }
}
