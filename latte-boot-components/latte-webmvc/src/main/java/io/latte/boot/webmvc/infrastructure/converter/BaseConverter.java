package io.latte.boot.webmvc.infrastructure.converter;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.entity.cmd.EntityCommand;

import java.util.List;

/**
 * BaseConverter
 *
 * @author : wugz
 * @since : 2022/2/22
 */
public interface BaseConverter<E extends BaseEntity, C extends EntityCommand, T> {
  /**
   * cmd转entity
   *
   * @param cmd
   * @return
   */
  E convertCommand(C cmd);

  /**
   * 实体转DO类型
   *
   * @param entity
   * @return
   */
  T convertEntity(E entity);

  /**
   * 实体集转DO集
   *
   * @param entities
   * @return
   */
  List<T> convertEntities(List<E> entities);
}
