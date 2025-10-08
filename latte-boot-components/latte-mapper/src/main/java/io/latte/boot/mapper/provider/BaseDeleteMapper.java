package io.latte.boot.mapper.provider;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.provider.impl.BaseDeleteMapperProvider;
import io.latte.boot.mapper.statement.entity.Delete;
import org.apache.ibatis.annotations.DeleteProvider;

/**
 * BaseDeleteMapper
 *
 * @author : wugz
 * @since : 2018/4/22
 */
public interface BaseDeleteMapper<E extends BaseEntity> {
  /**
   * 按实体删除
   *
   * @param whereEntity where条件
   * @return
   */
  @DeleteProvider(type = BaseDeleteMapperProvider.class, method = "delete")
  Integer delete(E whereEntity);

  /**
   * 按实体包装器删除
   *
   * @param entityDelete where条件
   * @return
   */
  @DeleteProvider(type = BaseDeleteMapperProvider.class, method = "deleteBy")
  Integer deleteBy(Delete<E> entityDelete);

  /**
   * 按主键删除
   *
   * @param whereEntity where条件
   * @return
   */
  @DeleteProvider(type = BaseDeleteMapperProvider.class, method = "deleteByPrimary")
  Integer deleteById(E whereEntity);
}
