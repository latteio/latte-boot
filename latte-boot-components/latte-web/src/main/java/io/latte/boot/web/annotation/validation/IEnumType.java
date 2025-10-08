package io.latte.boot.web.annotation.validation;

import java.io.Serializable;

/**
 * IEnumType
 *
 * @author : wugz
 * @since : 2022/3/10
 */
public interface IEnumType<T> extends Serializable {
  /**
   * 返回编码
   *
   * @return
   */
  T getCode();
}
