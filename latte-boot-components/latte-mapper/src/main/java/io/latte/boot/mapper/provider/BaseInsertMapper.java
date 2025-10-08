package io.latte.boot.mapper.provider;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.provider.impl.BaseInsertMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;

/**
 * BaseInsertMapper
 *
 * @author : wugz
 * @since : 2018/4/22
 */
public interface BaseInsertMapper<E extends BaseEntity> {
  /**
   * 数据插入(所有字段)
   *
   * @param entity 插入实体
   * @return
   */
  @InsertProvider(type = BaseInsertMapperProvider.class, method = "insert")
  Integer insert(E entity);

  /**
   * 数据插入(选择性字段)
   *
   * @param entity 插入实体
   * @return
   */
  @InsertProvider(type = BaseInsertMapperProvider.class, method = "insertSelective")
  Integer insertSelective(E entity);
}
