package io.latte.boot.mapper.statement.entity;

/**
 * QueryWhere
 *
 * @author : wugz
 * @since : 2024/10/16
 */
public interface QueryWhere<E> extends Where<E> {
  /**
   * and 条件
   *
   * @return
   */
  QueryWhereBuilder<E> and();

  QueryWhere<E> and(String expression, Object value);

  /**
   * or 条件
   *
   * @return
   */
  QueryWhereBuilder<E> or();

  QueryWhere<E> or(String expression, Object value);

  /**
   * endWhere
   *
   * @return
   */
  QuerySelection<E> endWhere();
}
