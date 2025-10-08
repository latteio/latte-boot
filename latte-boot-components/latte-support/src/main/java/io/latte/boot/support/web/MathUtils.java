package io.latte.boot.support.web;

import io.latte.boot.support.validate.Validate;

import java.security.SecureRandom;

/**
 * MathUtils
 *
 * @author : wugz
 * @since : 2022/11/22
 */
public class MathUtils {
  private static final SecureRandom random = new SecureRandom();

  private MathUtils() {
  }

  private volatile static MathUtils INSTANCE = getInstance();

  private static MathUtils getInstance() {
    if (null == INSTANCE) {
      synchronized (MathUtils.class) {
        if (null == INSTANCE) {
          INSTANCE = new MathUtils();
        }
      }
    }
    return INSTANCE;
  }

  /**
   * 生成随机数
   *
   * @param length 数值长度
   * @return
   */
  public static String randomInt(int length) {
    Validate.requireTrue(length > 0, "length must be positive");
    String code;
    do {
      int number = random.nextInt((int) Math.pow(10, length));
      code = String.valueOf(number);
    } while (code.length() != length);
    return code;
  }


}
