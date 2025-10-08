package io.latte.boot.mapper;

/**
 * 自定义数据实体的对应字段, 以适应不同场景下的表结构设计规范
 *
 * @author : wugz
 * @since : 2024/10/3
 */
public class MapperEntityFieldMappings implements EntityFieldMappings {
  private String id;
  private String parentId;
  private String parentPath;
  private String sortNo;
  private String remark;
  private String version;
  private String createUser;
  private String createTime;
  private String createIp;
  private String updateUser;
  private String updateTime;
  private String updateIp;
  private String secretLevel;
  private String isDeleted;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getParentPath() {
    return parentPath;
  }

  public void setParentPath(String parentPath) {
    this.parentPath = parentPath;
  }

  public String getSortNo() {
    return sortNo;
  }

  public void setSortNo(String sortNo) {
    this.sortNo = sortNo;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getCreateIp() {
    return createIp;
  }

  public void setCreateIp(String createIp) {
    this.createIp = createIp;
  }

  public String getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public String getUpdateIp() {
    return updateIp;
  }

  public void setUpdateIp(String updateIp) {
    this.updateIp = updateIp;
  }

  public String getSecretLevel() {
    return secretLevel;
  }

  public void setSecretLevel(String secretLevel) {
    this.secretLevel = secretLevel;
  }

  public String getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(String isDeleted) {
    this.isDeleted = isDeleted;
  }
}
