package io.latte.boot.security;

import java.util.ArrayList;
import java.util.List;

/**
 * SecurityHttpRequestProperties
 *
 * @author : wugz
 * @since : 2023/5/7
 */
public class SecurityHttpRequestProperties {
  private boolean useTrace;
  private boolean usePermission;
  private String apiPrefix;
  private List<String> apiPermits;

  public SecurityHttpRequestProperties() {
    this.useTrace = true;
    this.usePermission = false;
    this.apiPrefix = "/api";
    this.apiPermits = new ArrayList<>();
  }

  public boolean isUseTrace() {
    return useTrace;
  }

  public void setUseTrace(boolean useTrace) {
    this.useTrace = useTrace;
  }

  public boolean isUsePermission() {
    return usePermission;
  }

  public void setUsePermission(boolean usePermission) {
    this.usePermission = usePermission;
  }

  public String getApiPrefix() {
    return apiPrefix;
  }

  public void setApiPrefix(String apiPrefix) {
    this.apiPrefix = apiPrefix;
  }

  public List<String> getApiPermits() {
    return apiPermits;
  }

  public void setApiPermits(List<String> apiPermits) {
    this.apiPermits = apiPermits;
  }
}
