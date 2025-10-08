package io.latte.boot.mapper.provider;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.entity.cmd.EntityUpdateCommand;
import io.latte.boot.mapper.provider.impl.BaseUpdateMapperProvider;
import io.latte.boot.mapper.statement.entity.Update;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * BaseUpdateMapper
 *
 * @author : wugz
 * @since : 2018/4/22
 */
public interface BaseUpdateMapper<E extends BaseEntity> {
  /**
   * 按实体更新
   *
   * @param entity 更新实体
   * @return
   */
  @UpdateProvider(type = BaseUpdateMapperProvider.class, method = "updateSelectiveByPrimary")
  Integer updateSelectiveById(EntityUpdateCommand entity);

  /**
   * 按实体包装器更新
   *
   * @param entityUpdate 更新实体
   * @return
   */
  @UpdateProvider(type = BaseUpdateMapperProvider.class, method = "updateBy")
  Integer updateBy(Update<E> entityUpdate);

  /**
   * 按实体包装器更新
   *
   * @param entityUpdate 更新实体
   * @return
   */
  @UpdateProvider(type = BaseUpdateMapperProvider.class, method = "updateSelectiveBy")
  Integer updateSelectiveBy(Update<E> entityUpdate);

}
