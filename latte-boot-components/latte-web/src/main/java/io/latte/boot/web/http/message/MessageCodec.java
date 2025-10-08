package io.latte.boot.web.http.message;

import org.springframework.http.HttpStatus;

/**
 * MessageCodec
 *
 * @author : wugz
 * @since : 2022/8/22
 */
public enum MessageCodec {
  // 缺省成功: status=200
  OK(HttpStatus.OK.value(), "platform.default.success", null),
  AUTHORIZED(HttpStatus.OK.value(), "platform.auth-authorized", null),
  CREATED(HttpStatus.CREATED.value(), null, null),

  // status=400
  BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), null, null),
  // status=401
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "platform.auth.unauthorized", null),
  // status=403
  FORBIDDEN(HttpStatus.FORBIDDEN.value(), "platform.auth.forbidden", null),
  // status=404
  NOT_FOUND(HttpStatus.NOT_FOUND.value(), "platform.default.not-found", null),
  // status=429
  REQUEST_BUSY(HttpStatus.TOO_MANY_REQUESTS.value(), "platform.auth.request-busy", null),

  // 缺省失败错误: status=500
  DEFAULT_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "platform.default.failure", null),
  // 缺省失败错误: status=500
  NOT_APPLICABLE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "platform.default.not-applicable", null),
  // 缺省失败错误: status=500
  NOT_ALLOWED_IP(HttpStatus.INTERNAL_SERVER_ERROR.value(), "platform.default.not-allowed-ip", null),
  // 校验失败错误: status=500
  VALIDATION_FAILURE(DEFAULT_FAILURE.status(), "platform.validation.failure", null),
  // Is null: status=500
  VALIDATION_IS_NULL(DEFAULT_FAILURE.status(), "platform.validation.is-null", null),
  // Is empty: status=500
  VALIDATION_IS_EMPTY(DEFAULT_FAILURE.status(), "platform.validation.is-empty", null),
  // 存在子代节点错误: status=500
  VALIDATION_EXISTS_CHILDREN(DEFAULT_FAILURE.status(), "platform.validation.exists-children", null),
  // 无效记录错误: status=500
  VALIDATION_UNAVAILABLE_RECORD(DEFAULT_FAILURE.status(), "platform.validation.unavailable-record", null),
  // 业务逻辑判定失败: status=500
  VALIDATION_BIZ_LOGIC_JUDGMENT_FAILURE(DEFAULT_FAILURE.status(), "platform.validation.biz-logic-judgment-failure", null),

  // status=500
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "platform.default.internal-error", null),
  CODE_GENERATED_FAILURE(540, "platform.auth.code-generated-failure", null),
  USERNAME_NOTFOUND(541, "platform.auth.username-notfound", null),
  ACCOUNT_LOCKED(542, "platform.auth.account-locked", null),
  ACCOUNT_EXPIRED(543, "platform.auth.account-expired", null),
  ACCOUNT_DISABLED(544, "platform.auth.account-disabled", null),
  BAD_CREDENTIALS(545, "platform.auth.bad-credentials", null),
  CREDENTIALS_EXPIRED(546, "platform.auth.credentials-expired", null),
  AUTH_ID_EMPTY(547, "platform.auth.auth-id-empty", null),
  AUTH_ID_INVALID(548, "platform.auth.auth-id-invalid", null),
  CODE_EMPTY(549, "platform.auth.code-empty", null),
  CODE_EXPIRED(550, "platform.auth.code-expired", null),
  CODE_VALIDATE_FAILURE(551, "platform.auth.code-validate-failure", null),
  USER_DETAILS_AUTH_FAILURE(559, "platform.auth.user-details-auth-failure", null),
  SESSION_AUTH_FAILURE(560, "platform.auth.session-failure", null),
  ;

  private final int status;
  private final String key;
  private final String desc;

  MessageCodec(int status, String key, String desc) {
    this.status = status;
    this.key = key;
    this.desc = desc;
  }

  public static MessageCodec statusOf(int status) {
    for (MessageCodec codec : values()) {
      if (codec.status() == status) {
        return codec;
      }
    }
    return DEFAULT_FAILURE;
  }

  /**
   * status
   *
   * @return
   */
  public int status() {
    return status;
  }

  /**
   * msg key
   *
   * @return
   */
  public String key() {
    return key;
  }

  /**
   * msg desc
   *
   * @return
   */
  public String desc() {
    return desc;
  }
}
