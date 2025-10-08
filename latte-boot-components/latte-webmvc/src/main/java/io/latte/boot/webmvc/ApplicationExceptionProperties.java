package io.latte.boot.webmvc;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ApplicationExceptionProperties
 *
 * @author : wugz
 * @since : 2022/4/7
 */
@ConfigurationProperties(prefix = "application.exception")
public class ApplicationExceptionProperties {
  private boolean useTrace;

  public ApplicationExceptionProperties() {
    this.useTrace = false;
  }

  public boolean isUseTrace() {
    return useTrace;
  }

  public void setUseTrace(boolean useTrace) {
    this.useTrace = useTrace;
  }
}
