package io.latte.boot.webmvc;

import io.latte.boot.security.ApplicationRedissionProperties;
import io.latte.boot.security.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ApplicationProperties
 *
 * @author : wugz
 * @since : 2022/3/30
 */
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
  private String name;
  private String code;
  private String version;
  private ApplicationRedissionProperties redis;
  private SecurityProperties security;
  private ApplicationExceptionProperties exception;

  /**
   * 构造函数
   */
  public ApplicationProperties() {
    this.redis = new ApplicationRedissionProperties();
    this.security = new SecurityProperties();
    this.exception = new ApplicationExceptionProperties();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public ApplicationRedissionProperties getRedis() {
    return redis;
  }

  public void setRedis(ApplicationRedissionProperties redis) {
    this.redis = redis;
  }

  public SecurityProperties getSecurity() {
    return security;
  }

  public void setSecurity(SecurityProperties security) {
    this.security = security;
  }

  public ApplicationExceptionProperties getException() {
    return exception;
  }

  public void setException(ApplicationExceptionProperties exception) {
    this.exception = exception;
  }

}
