package io.latte.boot.mapper;

import com.google.common.base.CaseFormat;
import io.latte.boot.mapper.annotation.*;
import io.latte.boot.support.web.MapUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.*;

/**
 * EntityDefinition
 *
 * @author : wugz
 * @since : 2018/4/24
 */
public final class EntityDefinition implements Serializable {
  /* 实体类class */
  private final Class<?> entityClass;
  private final EntityFieldMappings entityFieldMappings;
  /* 属性和字段映射关系表(带@Property注解, 且注解的field属性有值) */
  private final Map<String, String> propertyFieldMap = new HashMap<>();
  /* 持久化属性字段 */
  private final List<EntityProperty> persistentProperties = new ArrayList<>();
  /* 查询属性字段 */
  private final List<EntityPropertySelect> selectProperties = new ArrayList<>();
  /* 实体对应的表名 */
  private String tableName;
  /* 实体对应的表的别名 */
  private String tableAlias;
  /* 缺省字段集 */
  private EntityProperty propertyId;
  private EntityProperty propertyVersion;
  private EntityProperty propertyCreateUser;
  private EntityProperty propertyCreateTime;
  private EntityProperty propertyCreateIp;
  private EntityProperty propertyUpdateUser;
  private EntityProperty propertyUpdateTime;
  private EntityProperty propertyUpdateIp;
  private EntityProperty propertyIsDeleted;
  private boolean logicalDelete = true;

  /**
   * 构造函数
   *
   * @param entityClass
   * @param entityFieldMappings
   */
  public EntityDefinition(Class<?> entityClass, EntityFieldMappings entityFieldMappings) {
    Objects.requireNonNull(entityClass, "entityClass is null");

    this.entityFieldMappings = entityFieldMappings;
    this.entityClass = entityClass;
    if (null != entityClass.getAnnotation(Entity.class)) {
      this.logicalDelete = entityClass.getAnnotation(Entity.class).logicalDelete();
    } else if (null != entityClass.getSuperclass().getAnnotation(Entity.class)) {
      this.logicalDelete = entityClass.getSuperclass().getAnnotation(Entity.class).logicalDelete();
    } else {
      for (Class<?> inter : entityClass.getInterfaces()) {
        if (null != inter.getAnnotation(Entity.class)) {
          this.logicalDelete = inter.getAnnotation(Entity.class).logicalDelete();
          break;
        }
      }
    }
    this.tableName();
    this.tableAlias();
    this.declaredProperties();
  }

  /**
   * 返回实体类
   *
   * @return
   */
  public Class<?> getEntityClass() {
    return entityClass;
  }

  /**
   * 返回表名
   *
   * @return
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * 返回表的别名
   *
   * @return
   */
  public String getDefaultTableAlias() {
    return tableAlias;
  }

  /**
   * 获取主键
   *
   * @return
   */
  public EntityProperty getPropertyId() {
    return propertyId;
  }

  public EntityProperty getPropertyVersion() {
    return propertyVersion;
  }

  public EntityProperty getPropertyCreateUser() {
    return propertyCreateUser;
  }

  public EntityProperty getPropertyCreateTime() {
    return propertyCreateTime;
  }

  public EntityProperty getPropertyCreateIp() {
    return propertyCreateIp;
  }

  public EntityProperty getPropertyUpdateUser() {
    return propertyUpdateUser;
  }

  public EntityProperty getPropertyUpdateTime() {
    return propertyUpdateTime;
  }

  public EntityProperty getPropertyUpdateIp() {
    return propertyUpdateIp;
  }

  public EntityProperty getPropertyIsDeleted() {
    return propertyIsDeleted;
  }

  /**
   * 返回属性及属性对应字段关系表
   *
   * @return
   */
  public Map<String, String> getPropertyFieldMap() {
    return propertyFieldMap;
  }

  /**
   * 返回持久化属性
   *
   * @return
   */
  public List<EntityProperty> getPersistentProperties() {
    return persistentProperties;
  }

  /**
   * 返回持久化属性
   *
   * @return
   */
  public Map<String, EntityProperty> getPersistentPropertiesMap() {
    Map<String, EntityProperty> map = new HashMap<String, EntityProperty>();
    for (EntityProperty property : persistentProperties) {
      map.put(property.getProperty(), property);
    }
    return map;
  }

  /**
   * 返回查询的属性集
   *
   * @return
   */
  public List<EntityPropertySelect> getSelectProperties() {
    return selectProperties;
  }

  /**
   * 返回查询的属性集
   *
   * @return
   */
  public Map<String, EntityPropertySelect> getSelectPropertiesMap() {
    Map<String, EntityPropertySelect> map = new HashMap<String, EntityPropertySelect>();
    for (EntityPropertySelect property : selectProperties) {
      map.put(property.getProperty(), property);
    }
    return map;
  }

  /**
   * 返回insert的into字段集
   *
   * @param bean
   * @param selective
   * @return
   */
  public List<String> getInsertColumns(Object bean, boolean selective) {
    List<String> properties = new ArrayList<String>();
    Map<String, Object> params = MapUtils.from(bean);
    for (EntityProperty entityProperty : this.persistentProperties) {
      if (selective) {
        if (null != params.get(entityProperty.getProperty())) {
          properties.add(entityProperty.getField());
        }
      } else {
        properties.add(entityProperty.getField());
      }
    }
    return properties;
  }

  /**
   * 返回insert的values集
   */
  public List<String> getInsertValues(Object bean, boolean selective) {
    List<String> properties = new ArrayList<String>();
    Map<String, Object> params = MapUtils.from(bean);
    for (EntityProperty entityProperty : this.persistentProperties) {
      if (selective) {
        if (null != params.get(entityProperty.getProperty())) {
          properties.add(entityProperty.getText());
        }
      } else {
        properties.add(entityProperty.getText());
      }
    }
    return properties;
  }

  /**
   * 获取带有表别名的字段访问
   *
   * @param tableAlias 表的别名
   * @param field      表的字段
   * @return
   */
  public String getTableField(String tableAlias, String field) {
    return MessageFormat.format("{0}.{1}", tableAlias, field);
  }

  /**
   * 是否逻辑删除
   *
   * @return
   */
  public boolean isLogicalDelete() {
    return logicalDelete;
  }

  /**
   * 实体对应的表名
   */
  private void tableName() {
    Entity entity = this.entityClass.getAnnotation(Entity.class);
    Objects.requireNonNull(entity, "entity is null");
    Objects.requireNonNull(entity.table(), "entity.table() is null");

    this.tableName = entity.table();
  }

  /**
   * 实体对应的表的别名
   */
  private void tableAlias() {
    this.tableAlias = "et";
  }

  /**
   * 获取申明字段
   *
   * @return
   */
  private void declaredProperties() {
    /* 添加目标类属性 */
    for (Field field : this.entityClass.getDeclaredFields()) {
      if (!Modifier.isStatic(field.getModifiers())) {
        this.propertiesMapping(field);
      }
    }
  }

  /**
   * 记录属性和字段映射关系 & 主键属性
   *
   * @param field 字段对象
   */
  private void propertiesMapping(Field field) {
    Property property = field.getAnnotation(Property.class);
    if (null != property && !property.field().isEmpty()) {
      propertyFieldMap.put(field.getName(), property.field());
      if (property.editable()) {
        persistentProperties.add(new EntityProperty(field.getName(), property.field()));
      }
      if (property.selectable()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), property.field()));
      }
      return;
    }

    PropertyId propertyId = field.getAnnotation(PropertyId.class);
    if (null != propertyId && !propertyId.field().isEmpty()) {
      String columnName = null != entityFieldMappings.getId() ? entityFieldMappings.getId() : propertyId.field();
      this.propertyId = new EntityProperty(field.getName(), columnName);
      propertyFieldMap.put(field.getName(), columnName);
      if (propertyId.editable()) {
        persistentProperties.add(this.propertyId);
      }
      if (propertyId.selectable()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), columnName));
      }
      return;
    }

    PropertyVersion propertyVersion = field.getAnnotation(PropertyVersion.class);
    if (null != propertyVersion && !propertyVersion.field().isEmpty()) {
      String columnName = null != entityFieldMappings.getVersion() ? entityFieldMappings.getVersion() : propertyVersion.field();
      this.propertyVersion = new EntityProperty(field.getName(), columnName);
      propertyFieldMap.put(field.getName(), columnName);
      if (propertyVersion.editable()) {
        persistentProperties.add(this.propertyVersion);
      }
      if (propertyVersion.selectable()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), columnName));
      }
      return;
    }

    PropertyCreateUser propertyCreateUser = field.getAnnotation(PropertyCreateUser.class);
    if (null != propertyCreateUser && !propertyCreateUser.field().isEmpty()) {
      String columnName = null != entityFieldMappings.getCreateUser() ? entityFieldMappings.getCreateUser() : propertyCreateUser.field();
      this.propertyCreateUser = new EntityProperty(field.getName(), columnName);
      propertyFieldMap.put(field.getName(), columnName);
      if (propertyCreateUser.editable()) {
        persistentProperties.add(this.propertyCreateUser);
      }
      if (propertyCreateUser.selectable()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), columnName));
      }
      return;
    }

    PropertyCreateTime propertyCreateTime = field.getAnnotation(PropertyCreateTime.class);
    if (null != propertyCreateTime && !propertyCreateTime.field().isEmpty()) {
      String columnName = null != entityFieldMappings.getCreateTime() ? entityFieldMappings.getCreateTime() : propertyCreateTime.field();
      this.propertyCreateTime = new EntityProperty(field.getName(), columnName);
      propertyFieldMap.put(field.getName(), columnName);
      if (propertyCreateTime.editable()) {
        persistentProperties.add(this.propertyCreateTime);
      }
      if (propertyCreateTime.selectable()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), columnName));
      }
      return;
    }

    PropertyCreateIp propertyCreateIp = field.getAnnotation(PropertyCreateIp.class);
    if (null != propertyCreateIp && !propertyCreateIp.field().isEmpty()) {
      String columnName = null != entityFieldMappings.getCreateIp() ? entityFieldMappings.getCreateIp() : propertyCreateIp.field();
      this.propertyCreateIp = new EntityProperty(field.getName(), columnName);
      propertyFieldMap.put(field.getName(), columnName);
      if (propertyCreateIp.editable()) {
        persistentProperties.add(this.propertyCreateIp);
      }
      if (propertyCreateIp.selectable()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), columnName));
      }
      return;
    }

    PropertyUpdateUser propertyUpdateUser = field.getAnnotation(PropertyUpdateUser.class);
    if (null != propertyUpdateUser && !propertyUpdateUser.field().isEmpty()) {
      String columnName = null != entityFieldMappings.getUpdateUser() ? entityFieldMappings.getUpdateUser() : propertyUpdateUser.field();
      this.propertyUpdateUser = new EntityProperty(field.getName(), columnName);
      propertyFieldMap.put(field.getName(), columnName);
      if (propertyUpdateUser.editable()) {
        persistentProperties.add(this.propertyUpdateUser);
      }
      if (propertyUpdateUser.selectable()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), columnName));
      }
      return;
    }

    PropertyUpdateTime propertyUpdateTime = field.getAnnotation(PropertyUpdateTime.class);
    if (null != propertyUpdateTime && !propertyUpdateTime.field().isEmpty()) {
      String columnName = null != entityFieldMappings.getUpdateTime() ? entityFieldMappings.getUpdateTime() : propertyUpdateTime.field();
      this.propertyUpdateTime = new EntityProperty(field.getName(), columnName);
      propertyFieldMap.put(field.getName(), columnName);
      if (propertyUpdateTime.editable()) {
        persistentProperties.add(this.propertyUpdateTime);
      }
      if (propertyUpdateTime.selectable()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), columnName));
      }
      return;
    }

    PropertyUpdateIp propertyUpdateIp = field.getAnnotation(PropertyUpdateIp.class);
    if (null != propertyUpdateIp && !propertyUpdateIp.field().isEmpty()) {
      String columnName = null != entityFieldMappings.getUpdateIp() ? entityFieldMappings.getUpdateIp() : propertyUpdateIp.field();
      this.propertyUpdateIp = new EntityProperty(field.getName(), columnName);
      propertyFieldMap.put(field.getName(), columnName);
      if (propertyUpdateIp.editable()) {
        persistentProperties.add(this.propertyUpdateIp);
      }
      if (propertyUpdateIp.selectable()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), columnName));
      }
      return;
    }

    PropertyIsDeleted propertyIsDeleted = field.getAnnotation(PropertyIsDeleted.class);
    if (null != propertyIsDeleted && !propertyIsDeleted.field().isEmpty()) {
      String columnName = null != entityFieldMappings.getIsDeleted() ? entityFieldMappings.getIsDeleted() : propertyIsDeleted.field();
      this.propertyIsDeleted = new EntityProperty(field.getName(), columnName);
      propertyFieldMap.put(field.getName(), columnName);
      if (propertyIsDeleted.editable()) {
        persistentProperties.add(this.propertyIsDeleted);
      }
      if (propertyIsDeleted.selectable()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), columnName));
      }
      return;
    }

    Transient propSelect = field.getAnnotation(Transient.class);
    if (null != propSelect) {
      if (!propSelect.field().isEmpty()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), propSelect.field(), true));
      } else if (!propSelect.sql().isEmpty()) {
        selectProperties.add(new EntityPropertySelect(field.getName(), propSelect.sql(), true, 1));
      }
      return;
    }

    /* 属性名称转字段名称: 小驼峰转下划线 */
    String fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
    propertyFieldMap.put(field.getName(), fieldName);
    persistentProperties.add(new EntityProperty(field.getName(), fieldName));
    selectProperties.add(new EntityPropertySelect(field.getName(), fieldName));
  }
}
