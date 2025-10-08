package io.latte.boot.mapper.statement.entity.impl;

import io.latte.boot.mapper.statement.entity.WhereExists;

/**
 * EntityQueryWhereExists
 *
 * @author : wugz
 * @since : 2023/7/26
 */
public class EntityQueryWhereExists<E, T> {
  private final EntityQuery<E> entityQuery;
  private final EntityQueryWhereBuilder<E> entityQueryWhereBuilder;
  private final WhereExists whereExists;

  /**
   * 构造函数
   *
   * @param entityQuery
   * @param entityQueryWhereBuilder
   * @param whereExists
   */
  public EntityQueryWhereExists(EntityQuery<E> entityQuery,
                                EntityQueryWhereBuilder<E> entityQueryWhereBuilder,
                                WhereExists whereExists) {
    this.entityQuery = entityQuery;
    this.entityQueryWhereBuilder = entityQueryWhereBuilder;
    this.whereExists = whereExists;
    this.entityQuery.getEntityAliasMap()
        .put(whereExists.getEntityClass().getName(), whereExists.getEntityAlias());
  }

  public EntityQueryWhereExistsBuilder<E, T> where() {
    return new EntityQueryWhereExistsBuilder<>(this.entityQuery,
        this.entityQueryWhereBuilder,
        this.whereExists);
  }
}
