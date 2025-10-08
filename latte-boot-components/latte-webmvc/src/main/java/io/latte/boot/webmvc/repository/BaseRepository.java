package io.latte.boot.webmvc.repository;

import io.latte.boot.entity.BaseEntity;
import io.latte.boot.entity.TreeEntity;
import io.latte.boot.entity.cmd.EntitiesCommand;
import io.latte.boot.entity.cmd.EntityCommand;
import io.latte.boot.entity.cmd.IdCommand;
import io.latte.boot.entity.query.PageQuery;
import io.latte.boot.mapper.provider.BaseMapper;
import io.latte.boot.mapper.statement.entity.Query;
import io.latte.boot.support.validate.Validate;
import io.latte.boot.web.context.UserContext;
import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.message.MessageUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * BaseRepository
 *
 * @author : wugz
 * @since : 2022/2/22
 */
public abstract class BaseRepository<M extends BaseMapper<E, T>, E extends BaseEntity, C extends EntityCommand, Q extends PageQuery, T>
    extends AbstractRepository<M, E, C, Q, T> {

  /**
   * 构造函数
   *
   * @param mapper
   */
  public BaseRepository(M mapper) {
    super(mapper);
  }

  /**
   * 创建并返回通用查询语句
   *
   * @param params 查询参数
   * @return 查询语句
   */
  protected abstract Query<E> createQuery(Q params);

  /**
   * 返回列表数据
   *
   * @param params 查询参数
   * @return
   */
  public List<T> selectList(Q params) {
    Query<E> entityQuery = Validate.requireNonNull(createQuery(params));
    return mapper.selectObjectsBy(entityQuery);
  }

  /**
   * 返回数据条数
   *
   * @param params 查询参数
   * @return 数据条数
   */
  public Long selectCount(Q params) {
    Query<E> entityQuery = Validate.requireNonNull(createQuery(params));
    return mapper.selectCountBy(entityQuery);
  }

  /**
   * 返回数据条数
   *
   * @param query 查询语句
   * @return 数据条数
   */
  protected Long selectCount(Query<E> query) {
    Query<E> entityQuery = Validate.requireNonNull(query);
    return mapper.selectCountBy(entityQuery);
  }

  /**
   * 保存数据
   *
   * @param context 上下文参数
   * @param cmd     数据参数
   * @return
   */
  public Boolean save(UserContext context, C cmd) {
    return Validate.requireTrue(
        null != saveAndReturn(context, cmd),
        MessageUtils.getDefaultFailureMsg());
  }

  /**
   * 保存数据
   *
   * @param context 上下文参数
   * @param cmd     数据参数
   * @return
   */
  public T saveAndReturn(UserContext context, C cmd) {
    /* 1.参数校验 */
    Validate.requireTrue(
        this.validateBeforeSave(context, cmd),
        MessageUtils.getMessage(MessageCodec.VALIDATION_BIZ_LOGIC_JUDGMENT_FAILURE));

    /* 2.保存数据 */
    E result = this.save(cmd, context.getUser().getId());
    T retDTO = instantiateDataObjectClass();
    BeanUtils.copyProperties(result, retDTO);
    return retDTO;
  }

  /**
   * 更新数据
   *
   * @param context 上下文参数
   * @param cmd     数据参数
   * @return
   */
  public Boolean updateById(UserContext context, C cmd) {
    /* 1.参数校验 */
    Validate.requireNonEmpty(cmd.getId(),
        MessageUtils.getMessage(MessageCodec.VALIDATION_IS_EMPTY));

    Validate.requireTrue(
        this.validateBeforeUpdate(context, cmd),
        MessageUtils.getMessage(MessageCodec.VALIDATION_BIZ_LOGIC_JUDGMENT_FAILURE));

    /* 2.更新数据 */
    E result = this.updateById(cmd, context.getUser().getId());
    return Validate.requireTrue(
        null != result,
        MessageUtils.getDefaultFailureMsg());
  }

  /**
   * 删除数据
   *
   * @param context 上下文参数
   * @param cmd     数据参数
   * @return
   */
  public Boolean delete(UserContext context, IdCommand cmd) {
    E entity = Validate.requireNonNull(
        mapper.selectById(cmd.getId()),
        MessageUtils.getDefaultUnavailableRecordMsg());

    /* 判断是否树形结构表 */
    if (entity instanceof TreeEntity) {
      E query = instantiateEntityClass();
      ((TreeEntity) query).setParentId(cmd.getId());
      Validate.requireTrue(mapper.selectCount(query) == 0,
          MessageUtils.getDefaultExistsChildrenMsg());
    }

    Integer result = mapper.deleteById(entity);
    return Validate.requireTrue(
        result > 0,
        MessageUtils.getDefaultFailureMsg());
  }

  /**
   * 批量操作
   *
   * @param context  上下文参数
   * @param entities 数据参数
   * @return
   */
  public Integer batchSave(UserContext context, EntitiesCommand<C> entities) {
    EntitiesCommand<C> entitiesInternal = Validate.requireNonNull(entities,
        MessageUtils.getMessage(MessageCodec.VALIDATION_IS_EMPTY));

    AtomicInteger total = new AtomicInteger(0);
    if (null != entitiesInternal.getInsertEntities()) {
      for (C insertEntity : entitiesInternal.getInsertEntities()) {
        this.save(insertEntity, context.getUser().getId());
        total.getAndAdd(1);
      }
    }

    if (null != entitiesInternal.getUpdateEntities()) {
      for (C updateEntity : entitiesInternal.getUpdateEntities()) {
        this.updateById(updateEntity, context.getUser().getId());
        total.getAndAdd(1);
      }
    }

    if (null != entitiesInternal.getDeleteEntities()) {
      for (C deleteEntity : entitiesInternal.getDeleteEntities()) {
        total.getAndAdd(this.delete(deleteEntity, context.getUser().getId()));
      }
    }

    return total.get();
  }

  /**
   * 保存前验证
   *
   * @param context 上下文参数
   * @param cmd     数据参数
   * @return
   */
  protected boolean validateBeforeSave(UserContext context, C cmd) {
    return true;
  }

  /**
   * 更新前验证
   *
   * @param context 上下文参数
   * @param cmd     数据参数
   * @return
   */
  protected boolean validateBeforeUpdate(UserContext context, C cmd) {
    return true;
  }

}
