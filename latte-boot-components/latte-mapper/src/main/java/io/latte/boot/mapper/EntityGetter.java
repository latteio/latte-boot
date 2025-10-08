package io.latte.boot.mapper;

import java.io.Serializable;

/**
 * EntityGetter
 *
 * @author : wugz
 * @since : 2021/6/29
 */
@FunctionalInterface
public interface EntityGetter<T> extends Serializable {
  Object get(T source);
}