package io.latte.boot.mapper;

import java.io.Serializable;

/**
 * EntityFields
 *
 * @author : wugz
 * @since : 2018/4/24
 */
public interface EntityFields extends Serializable {
  /**
   * 数据库字段命名
   */
  String ID = "id";
  String IDS = "ids";
  String PARENT_ID = "parent_id";
  String PARENT_PATH = "parent_path";
  String SORT_NO = "sort_no";
  String REMARK = "remark";
  String VERSION = "version";
  String CREATE_USER = "create_user";
  String CREATE_TIME = "create_time";
  String CREATE_IP = "create_ip";
  String UPDATE_USER = "update_user";
  String UPDATE_TIME = "update_time";
  String UPDATE_IP = "update_ip";
  String SECRET_LEVEL = "secret_level";
  String IS_DELETED = "is_deleted";
  String WHERE_PROPERTY_PREFIX = "where_property_prefix_";
  String WHERE_PROPERTY_EXCLUDES = "where_property_excludes_";

  /**
   * 数据库字段驼峰命名
   */
  class CamelCase {
    public static final String ID = "id";
    public static final String IDS = "ids";
    public static final String PARENT_ID = "parentId";
    public static final String PARENT_PATH = "parentPath";
    public static final String SORT_NO = "sortNo";
    public static final String REMARK = "remark";
    public static final String VERSION = "version";
    public static final String CREATE_USER = "createUser";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATE_IP = "createIp";
    public static final String UPDATE_USER = "updateUser";
    public static final String UPDATE_TIME = "updateTime";
    public static final String UPDATE_IP = "updateIp";
    public static final String SECRET_LEVEL = "secretLevel";
    public static final String IS_DELETED = "isDeleted";
  }
}
