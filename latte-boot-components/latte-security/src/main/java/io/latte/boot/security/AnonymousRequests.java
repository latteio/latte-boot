package io.latte.boot.security;

import java.util.ArrayList;
import java.util.List;

/**
 * AnonymousRequests
 *
 * @author : wugz
 * @since : 2021/8/20
 */
public class AnonymousRequests {
  private final List<String> anon;

  /**
   * 构造函数
   */
  public AnonymousRequests() {
    this.anon = new ArrayList<>();
  }

  public AnonymousRequests addRequest(String... antPatterns) {
    for (String antPattern : antPatterns) {
      if (!this.anon.contains(antPattern)) {
        this.anon.add(antPattern);
      }
    }
    return this;
  }

  public AnonymousRequests addRequest(List<String> anon) {
    if (null != anon) {
      addRequest(anon.toArray(new String[0]));
    }
    return this;
  }

  public List<String> get() {
    return anon;
  }

  public String[] getArray() {
    return this.anon.toArray(new String[0]);
  }

  public int size() {
    return this.anon.size();
  }
}
