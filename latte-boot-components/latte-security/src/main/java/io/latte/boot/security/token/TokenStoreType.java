package io.latte.boot.security.token;

/**
 * Token存储方式
 *
 * @author : wugz
 * @since : 2021/8/15
 */
public enum TokenStoreType {
  /**
   * token信息存本地内存
   */
  MAP,

  /**
   * token信息存redis
   */
  REDIS,

  /**
   * token信息远程存储
   */
  REMOTE;
}
