package io.latte.boot.mapper.annotation;

/**
 * ValidateType
 *
 * @author : wugz
 * @since : 2018/4/23
 */
public enum ValidateType {
  /**
   * 忽略(不校验)
   */
  IGNORED,
  /**
   * 非NULL
   */
  NOT_NULL,
  /**
   * 非NULL且非""
   */
  NOT_EMPTY;
}