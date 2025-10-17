package io.latte.boot.webmvc.repository;

import com.google.common.base.Strings;
import io.latte.boot.entity.BaseEntity;
import io.latte.boot.entity.TreeEntity;
import io.latte.boot.entity.cmd.EntityCommand;
import io.latte.boot.entity.cmd.EntityInsertCommand;
import io.latte.boot.entity.cmd.EntityUpdateCommand;
import io.latte.boot.entity.query.PageQuery;
import io.latte.boot.mapper.EntityFields;
import io.latte.boot.mapper.MapperEntityFieldMappings;
import io.latte.boot.mapper.MapperEntityProperties;
import io.latte.boot.mapper.provider.BaseMapper;
import io.latte.boot.mapper.statement.entity.impl.EntityQuery;
import io.latte.boot.support.validate.Validate;
import io.latte.boot.support.web.IdUtils;
import io.latte.boot.web.exception.ThrowableFailure;
import io.latte.boot.web.http.message.MessageUtils;
import io.latte.boot.webmvc.infrastructure.adapter.DatasourceDateTimeAdapter;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AbstractRepository
 *
 * @author : wugz
 * @since : 2021/9/14
 */
public abstract class AbstractRepository<M extends BaseMapper<E, T>, E extends BaseEntity, C extends EntityCommand, Q extends PageQuery, T>
    implements IRepository<C, Q, T>, ThrowableFailure {
  protected final M mapper;
  private final Class<E> entityClass;
  private final Class<C> commandObjectClass;
  private final Class<Q> queryObjectClass;
  private final Class<T> dataObjectClass;
  @Resource
  private MapperEntityProperties mapperEntityProperties;
  @Resource
  protected DatasourceDateTimeAdapter datasourceDateTimeAdapter;

  /**
   * 构造函数
   */
  public AbstractRepository(M mapper) {
    this.mapper = mapper;
    ResolvableType type = ResolvableType.forClass(this.getClass()).getSuperType();
    this.entityClass = (Class<E>) type.getGeneric(1).resolve();
    this.commandObjectClass = (Class<C>) type.getGeneric(2).resolve();
    this.queryObjectClass = (Class<Q>) type.getGeneric(3).resolve();
    this.dataObjectClass = (Class<T>) type.getGeneric(4).resolve();
  }

  /**
   * 返回实体类型对象
   *
   * @return
   */
  public E instantiateEntityClass() {
    return BeanUtils.instantiateClass(entityClass);
  }

  /**
   * 返回实体命令类型对象
   *
   * @return
   */
  public C instantiateCommandObjectClass() {
    return BeanUtils.instantiateClass(commandObjectClass);
  }

  /**
   * 返回实体查询类型对象
   *
   * @return
   */
  public Q instantiateQueryObjectClass() {
    return BeanUtils.instantiateClass(queryObjectClass);
  }

  /**
   * 返回实体DO类型对象
   *
   * @return
   */
  public T instantiateDataObjectClass() {
    return BeanUtils.instantiateClass(dataObjectClass);
  }

  /**
   * 保存实体
   *
   * @param insertCommand
   * @param insertOperator
   * @return
   */
  public E save(EntityInsertCommand insertCommand, String insertOperator) {
    /* 1.参数校验 */
    Validate.requireNonNull(insertCommand, "insertCommand is null");

    /* 2.新增: 支持无主键和有主键两种情况: */
    E entity = instantiateEntityClass();
    BeanUtils.copyProperties(insertCommand, entity);

    /* 2.1 若无主键传入时, 自动生成主键 */
    if (!this.hasPrimaryKey(entity)) {
      entity.setId(IdUtils.getId());
    }

    /* 2.2 自动生成数据版本, 创建人, 创建日期等信息: */
    /* 创建日期如果没传则自动填充系统当前时间 */
    entity.setVersion(1);
    entity.setCreateUser(insertOperator);
    if (null == entity.getCreateTime()) {
      entity.setCreateTime(datasourceDateTimeAdapter.now());
    }

    /* 2.3 如果是 TreeEntity 类型数据,则生成 parentPath 属性 */
    if (entity instanceof TreeEntity
        && null != ((TreeEntity) entity).getParentId()) {
      ((TreeEntity) entity).setParentPath(this.parentPath(((TreeEntity) entity).getParentId()));
    }

    /* 3.执行新增, 返回结果 */
    int result = mapper.insertSelective(entity);
    if (result == 0) {
      this.throwFailure(MessageUtils.getDefaultFailureMsg());
    }

    return entity;
  }

  /**
   * 保存实体
   *
   * @param updateCommand
   * @param updateOperator
   */
  public E updateById(EntityUpdateCommand updateCommand, String updateOperator) {
    /* 1.参数校验 */
    /* 1.1参数校验 */
    Validate.requireNonNull(updateCommand, "updateCommand is null");
    Validate.requireNonEmpty(updateCommand.getId(), "updateCommand.getId() is null");

    /* 2.自动填充参数: 更新人及更新日期 */
    updateCommand.setUpdateUser(updateOperator);
    updateCommand.setUpdateTime(datasourceDateTimeAdapter.now());

    /* 2.1自动填充参数: 如果是 TreeEntity 类型数据, 则生成 parentPath 属性 */
    if (updateCommand instanceof TreeEntity
        && null != ((TreeEntity) updateCommand).getParentId()) {
      ((TreeEntity) updateCommand).setParentPath(this.parentPath(((TreeEntity) updateCommand).getParentId()));
    }

    /* 3.执行保存, 返回结果 */
    int result = mapper.updateSelectiveById(updateCommand);
    if (result == 0) {
      this.throwFailure(MessageUtils.getDefaultFailureMsg());
    }

    return mapper.selectById(updateCommand.getId());
  }

  /**
   * 删除实体
   *
   * @param deleteCommand
   * @param deleteOperator
   * @return
   */
  public Integer delete(Object deleteCommand, String deleteOperator) {
    /* 参数校验 */
    Validate.requireNonNull(deleteCommand, "deleteCommand is null");

    /* 如果是TreeEntity类型数据, 则检查是否存在子代 */
    E entity = instantiateEntityClass();
    BeanUtils.copyProperties(deleteCommand, entity);
    if (entity instanceof TreeEntity) {
      if (existChildren(entity.getId())) {
        throwFailure(MessageUtils.getDefaultExistsChildrenMsg());
      }
    }

    /* 执行删除 */
    /* 当执行逻辑删除时, updateUser 和 updateTime 作为更新属性不参与where组合 */
    entity.setUpdateUser(deleteOperator);
    entity.setUpdateTime(datasourceDateTimeAdapter.now());
    Integer result = this.hasPrimaryKey(entity) ? mapper.deleteById(entity) : mapper.delete(entity);
    if (result == 0) {
      this.throwFailure(MessageUtils.getDefaultFailureMsg());
    }

    return result;
  }

  /**
   * 检测是否包含主键
   *
   * @param entity
   * @return
   */
  protected boolean hasPrimaryKey(E entity) {
    return (null != entity && !Strings.isNullOrEmpty(entity.getId()));
  }

  /**
   * 拼接 parentPath
   *
   * @param parentId
   * @return
   */
  protected String parentPath(String parentId) {
    if (Strings.isNullOrEmpty(parentId)) {
      return null;
    }

    List<String> list = new ArrayList<String>();
    /* 查找父节点 */
    E entity = mapper.selectById(parentId);
    if (null != entity) {
      /* 递归遍历 */
      list.add(parentId);
      this.parentPath(((TreeEntity) entity).getParentId(), list);
    } else {
      list.add(parentId);
    }
    /* 拼接 */
    StringBuilder builder = new StringBuilder();
    if (list.size() > 0) {
      Collections.reverse(list);
      builder.append(list.get(0));
      for (int e = 1; e < list.size(); e++) {
        builder.append(",");
        builder.append(list.get(e));
      }
    }
    return builder.toString();
  }

  /**
   * 查找节点的所有父节点(祖先节点)的路由路径
   *
   * @param parentId
   * @param list
   */
  private void parentPath(String parentId, List<String> list) {
    /* 查找父节点 */
    E entity = mapper.selectById(parentId);
    if (null != entity) {
      /* 递归遍历 */
      list.add(parentId);
      this.parentPath(((TreeEntity) entity).getParentId(), list);
    } else {
      list.add(parentId);
    }
  }

  /**
   * 返回目标节点的所有后代
   *
   * @param id          目标节点id
   * @param includeThis 是否包含目标节点
   * @return
   */
  protected List<String> queryChildren(String id, boolean includeThis) {
    Validate.requireTrue(TreeEntity.class.isAssignableFrom(entityClass),
        "Entity type illegal, must be subclass of TreeEntity.");

    List<String> childrenList = new ArrayList<>();
    if (includeThis) {
      childrenList.add(id);
    }

    queryChildren(childrenList, id);
    return childrenList;
  }

  /**
   * 遍历节点的后代
   *
   * @param childrenList
   * @param id
   */
  private void queryChildren(final List<String> childrenList, final String id) {
    MapperEntityFieldMappings entityFields = mapperEntityProperties.getFieldMappings();
    EntityQuery<E> query = new EntityQuery<>(entityClass, "et");
    String fieldParentId = null != entityFields.getParentId()
        ? entityFields.getParentId()
        : EntityFields.PARENT_ID;
    query.where().and("et." + fieldParentId + " = #{parentId}", id);
    final List<E> children = mapper.selectListBy(query);
    if (children.size() > 0) {
      for (E child : children) {
        childrenList.add(child.getId());
        queryChildren(childrenList, child.getId());
      }
    }
  }

  /**
   * 检查节点是否存在子代
   *
   * @param parentId
   * @return
   */
  private boolean existChildren(String parentId) {
    E entity = instantiateEntityClass();
    ((TreeEntity) entity).setParentId(parentId);
    Long count = mapper.selectCount(entity);
    return count > 0;
  }

}
