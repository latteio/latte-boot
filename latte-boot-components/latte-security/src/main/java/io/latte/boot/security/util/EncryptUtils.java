package io.latte.boot.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * EncryptUtils
 *
 * @author : wugz
 * @since : 2018/7/19
 */
public final class EncryptUtils {
  private static volatile BCryptPasswordEncoder passwordEncoder = null;

  /**
   * 私有构造函数
   */
  private EncryptUtils() {
  }

  /**
   * passwordEncoder
   *
   * @return
   */
  public static BCryptPasswordEncoder passwordEncoder() {
    if (null == passwordEncoder) {
      synchronized (EncryptUtils.class) {
        if (null == passwordEncoder) {
          passwordEncoder = new BCryptPasswordEncoder();
        }
      }
    }

    return passwordEncoder;
  }

  /**
   * 加密明文
   *
   * @param plainText 明文
   * @return
   */
  public static String encode(String plainText) {
    return passwordEncoder().encode(plainText);
  }

  /**
   * 密码匹配
   *
   * @param plainText  明文
   * @param cipherText 密文
   * @return
   */
  public static boolean matches(String plainText, String cipherText) {
    return passwordEncoder().matches(plainText, cipherText);
  }
}
