package io.latte.boot.security;

import io.latte.boot.web.http.message.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 认证用户
 *
 * @author : wugz
 * @since : 2021/7/20
 */
public class AuthenticationUser extends User {
  /* 业务明细信息 */
  public static final String UUID_TEMPLATE = "{0}_{1}_{2}";
  private String uuid;
  private String appId;
  private String appClient;

  /* 用户基本信息 */
  private String id;
  private String username;
  private String password;
  private String mobile;
  private String email;
  private String idcard;
  private Integer isLocked;
  private Integer isEnabled;

  /* 用户角色集/用户权限集/是否匿名用户 */
  private Collection<GrantedAuthority> authorities = new ArrayList<>();
  private Collection<GrantedAuthority> permissions = new ArrayList<>();
  private boolean anonymous = false;

  /**
   * 构造函数
   */
  public AuthenticationUser() {
    this(
        AnonymousUser.INSTANCE.getAppId(),
        AnonymousUser.INSTANCE.getAppClient(),
        AnonymousUser.INSTANCE.getId(),
        AnonymousUser.INSTANCE.getUsername(),
        AnonymousUser.INSTANCE.getPassword(),
        AnonymousUser.INSTANCE.getAuthorities(),
        AnonymousUser.INSTANCE.getPermissions(),
        true);
  }

  /**
   * 构造函数
   *
   * @param appId       应用标识
   * @param appClient   应用端
   * @param id          用户id
   * @param username    用户账号
   * @param password    用户密码
   * @param authorities 用户角色集
   * @param permissions 用户权限集
   */
  public AuthenticationUser(String appId,
                            String appClient,
                            String id,
                            String username,
                            String password,
                            Collection<? extends GrantedAuthority> authorities,
                            Collection<? extends GrantedAuthority> permissions) {
    this(appId,
        appClient,
        id,
        username,
        password,
        authorities,
        permissions,
        false);
  }

  /**
   * 构造函数
   *
   * @param appId       应用标识
   * @param appClient   应用端
   * @param id          用户id
   * @param username    用户账号
   * @param password    用户密码
   * @param authorities 用户角色集
   * @param permissions 用户权限集
   * @param anonymous   是否匿名
   */
  public AuthenticationUser(String appId,
                            String appClient,
                            String id,
                            String username,
                            String password,
                            Collection<? extends GrantedAuthority> authorities,
                            Collection<? extends GrantedAuthority> permissions,
                            boolean anonymous) {
    super(username, password, true, true, true, true, authorities);
    this.id = id;
    this.appClient = appClient;
    this.appId = appId;
    createOrUpdateUUID();
    this.username = username;
    this.password = password;
    this.isLocked = 0;
    this.isEnabled = 1;
    if (null != authorities && !authorities.isEmpty()) {
      this.authorities.addAll(authorities);
    }
    if (null != permissions && !permissions.isEmpty()) {
      this.permissions.addAll(permissions);
    }
    this.anonymous = anonymous;
  }

  /**
   * 设置uuid
   */
  private void createOrUpdateUUID() {
    this.uuid = MessageFormat.format(UUID_TEMPLATE, id, appClient, appId);
  }

  public String uuid() {
    return uuid;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
    createOrUpdateUUID();
  }

  public String getAppClient() {
    return appClient;
  }

  public void setAppClient(String appClient) {
    this.appClient = appClient;
    createOrUpdateUUID();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
    createOrUpdateUUID();
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getIdcard() {
    return idcard;
  }

  public void setIdcard(String idcard) {
    this.idcard = idcard;
  }

  public Integer getIsLocked() {
    return isLocked;
  }

  public void setIsLocked(Integer isLocked) {
    this.isLocked = isLocked;
  }

  public Integer getIsEnabled() {
    return isEnabled;
  }

  public void setIsEnabled(Integer isEnabled) {
    this.isEnabled = isEnabled;
  }

  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Collection<UserAuthority> authorities) {
    if (null != authorities && !authorities.isEmpty()) {
      this.authorities.clear();
      this.authorities.addAll(authorities);
    }
  }

  public Collection<GrantedAuthority> getPermissions() {
    return permissions;
  }

  public void setPermissions(Collection<UserAuthority> permissions) {
    if (null != permissions && !permissions.isEmpty()) {
      this.permissions.clear();
      this.permissions.addAll(permissions);
    }
  }

  public boolean isAnonymous() {
    return anonymous;
  }

  public void setAnonymous(boolean anonymous) {
    this.anonymous = anonymous;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("uuid: ").append(uuid).append(", ");
    builder.append("appId: ").append(appId).append(", ");
    builder.append("appClient: ").append(appClient).append(", ");
    builder.append("id: ").append(id).append(", ");
    builder.append("username: ").append(username).append(", ");
    builder.append("password: ").append(Constants.PROTECTED).append(", ");
    builder.append("mobile: ").append(mobile).append(", ");
    builder.append("email: ").append(email).append(", ");
    builder.append("idcard: ").append(idcard).append(", ");
    builder.append("isLocked: ").append(isLocked).append(", ");
    builder.append("isEnabled: ").append(isEnabled).append(", ");
    builder.append("authorities: ").append(Constants.PROTECTED).append(", ");
    builder.append("permissions: ").append(Constants.PROTECTED).append(", ");
    builder.append("anonymous: ").append(anonymous);
    builder.append("}");
    return builder.toString();
  }

}
