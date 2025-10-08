package io.latte.boot.mapper.provider;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.mapper.provider.impl.BaseSelectMapperProvider;
import io.latte.boot.mapper.statement.entity.Query;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

/**
 * BaseSelectMapper
 *
 * @author : wugz
 * @since : 2018/4/22
 */
public interface BaseSelectMapper<E extends BaseEntity, T> {
  /**
   * 查询数量
   *
   * @param entity 查询实体
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectCount")
  Long selectCount(E entity);

  /**
   * 查询数量
   *
   * @param entityQuery 查询实体
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectCountBy")
  Long selectCountBy(Query<E> entityQuery);

  /**
   * 查询数量
   *
   * @param id 查询主键
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectCountByPrimary")
  Long selectCountById(Serializable id);

  // ---------- select & return entity(s) ---------- //

  /**
   * 查询实体
   *
   * @param entity 查询实体
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectOne")
  E selectOne(E entity);

  /**
   * 查询实体
   *
   * @param entityQuery 查询实体
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectOneBy")
  E selectOneBy(Query<E> entityQuery);

  /**
   * 查询实体
   *
   * @param id 查询主键
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectOneByPrimary")
  E selectById(Serializable id);

  /**
   * 获取查询列表
   *
   * @param entity 查询实体
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectList")
  List<E> selectList(E entity);

  /**
   * 获取查询列表
   *
   * @param entityQuery 查询实体
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectListBy")
  List<E> selectListBy(Query<E> entityQuery);

  // ---------- select & return object(s) ---------- //

  /**
   * 查询实体
   *
   * @param entity 查询实体
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectOne")
  T selectObject(E entity);

  /**
   * 查询实体
   *
   * @param entityQuery 查询实体
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectOneBy")
  T selectObjectBy(Query<E> entityQuery);

  /**
   * 查询实体
   *
   * @param id 查询主键
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectOneByPrimary")
  T selectObjectById(Serializable id);

  /**
   * 获取查询列表
   *
   * @param entity 查询实体
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectList")
  List<T> selectObjects(E entity);

  /**
   * 获取查询列表
   *
   * @param entityQuery 查询实体
   * @return
   */
  @SelectProvider(type = BaseSelectMapperProvider.class, method = "selectListBy")
  List<T> selectObjectsBy(Query<E> entityQuery);
}
