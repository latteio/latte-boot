package io.latte.boot.mapper;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据实体映射相关配置
 *
 * @author : wugz
 * @since : 2025/9/17
 */
@ConfigurationProperties(prefix = "application.mapper.entities")
public class MapperEntityProperties {
  private String dialect;
  private String basePackages;
  private MapperEntityFieldMappings fieldMappings;

  public MapperEntityProperties() {
    this.dialect = "default";
    this.basePackages = "";
    this.fieldMappings = new MapperEntityFieldMappings();
  }

  public String getDialect() {
    return dialect;
  }

  public void setDialect(String dialect) {
    this.dialect = dialect;
  }

  public String getBasePackages() {
    return basePackages;
  }

  public void setBasePackages(String basePackages) {
    this.basePackages = basePackages;
  }

  public MapperEntityFieldMappings getFieldMappings() {
    return fieldMappings;
  }

  public void setFieldMappings(MapperEntityFieldMappings fieldMappings) {
    this.fieldMappings = fieldMappings;
  }
}
