package io.latte.boot.webmvc.repository;

import io.latte.boot.entity.cmd.EntitiesCommand;
import io.latte.boot.entity.cmd.EntityCommand;
import io.latte.boot.entity.cmd.IdCommand;
import io.latte.boot.entity.query.PageQuery;
import io.latte.boot.web.context.UserContext;

import java.util.List;

/**
 * IRepository
 *
 * @author : wugz
 * @since : 2022/2/22
 */
public interface IRepository<C extends EntityCommand, Q extends PageQuery, T> {
  /**
   * 返回列表数据
   *
   * @param params 查询参数
   * @return
   */
  List<T> selectList(Q params);

  /**
   * 保存数据
   *
   * @param context 上下文参数
   * @param cmd     数据参数
   * @return
   */
  Boolean save(UserContext context, C cmd);

  /**
   * 保存数据
   *
   * @param context 上下文参数
   * @param cmd     数据参数
   * @return
   */
  T saveAndReturn(UserContext context, C cmd);

  /**
   * 更新数据
   *
   * @param context 上下文参数
   * @param cmd     数据参数
   * @return
   */
  Boolean updateById(UserContext context, C cmd);

  /**
   * 删除数据
   *
   * @param context 上下文参数
   * @param cmd     数据参数
   * @return
   */
  Boolean delete(UserContext context, IdCommand cmd);

  /**
   * 批量操作
   *
   * @param context
   * @param entities
   * @return
   */
  Integer batchSave(UserContext context, EntitiesCommand<C> entities);

  /**
   * 返回数据条数
   *
   * @param params 查询参数
   * @return 数据条数
   */
  Long selectCount(Q params);

  /**
   * 返回实体命令类型对象
   *
   * @return
   */
  C instantiateCommandObjectClass();

  /**
   * 返回实体查询类型对象
   *
   * @return
   */
  Q instantiateQueryObjectClass();

  /**
   * 返回实体DO类型对象
   *
   * @return
   */
  T instantiateDataObjectClass();
}
