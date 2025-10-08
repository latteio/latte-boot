package io.latte.boot.mapper.provider;

import io.latte.boot.entity.BaseEntity;

/**
 * BaseMapper
 *
 * @author : wugz
 * @since : 2018/4/15
 */
public interface BaseMapper<E extends BaseEntity, T>
    extends BaseInsertMapper<E>,
    BaseUpdateMapper<E>,
    BaseDeleteMapper<E>,
    BaseSelectMapper<E, T>,
    BaseSQLMapper {
}
