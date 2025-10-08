package io.latte.boot.web.http.message;

import org.springframework.util.StringUtils;

/**
 * MessageUtils
 *
 * @author : wugz
 * @since : 2022/1/20
 */
public class MessageUtils implements MessageSourceSupport {
  private static MessageContext messageContext;

  /**
   * 构造函数
   *
   * @param messageContext
   */
  public MessageUtils(MessageContext messageContext) {
    MessageUtils.messageContext = messageContext;
  }

  /**
   * 获取默认成功编码
   *
   * @return
   */
  public static int getDefaultSuccessStatus() {
    return MessageCodec.OK.status();
  }

  /**
   * 获取默认成功消息
   *
   * @return
   */
  public static String getDefaultSuccessMsg() {
    return getMessage(MessageCodec.OK);
  }

  /**
   * 获取默认失败编码
   *
   * @return
   */
  public static int getDefaultFailureStatus() {
    return MessageCodec.DEFAULT_FAILURE.status();
  }

  /**
   * 获取默认失败消息
   *
   * @return
   */

  public static String getDefaultFailureMsg() {
    return getMessage(MessageCodec.DEFAULT_FAILURE);
  }

  /**
   * 获取默认校验失败消息
   *
   * @return
   */
  public static String getDefaultValidateFailureMsg() {
    return getMessage(MessageCodec.VALIDATION_FAILURE);
  }

  /**
   * 获取默认存在子代消息
   *
   * @return
   */
  public static String getDefaultExistsChildrenMsg() {
    return getMessage(MessageCodec.VALIDATION_EXISTS_CHILDREN);
  }

  /**
   * 获取默认无效记录消息
   *
   * @return
   */
  public static String getDefaultUnavailableRecordMsg() {
    return getMessage(MessageCodec.VALIDATION_UNAVAILABLE_RECORD);
  }

  /**
   * 返回消息
   *
   * @param key
   * @return
   */
  private static String getMessage(String key) {
    return messageContext.getMessageSource()
        .getMessage(key, null, messageContext.getLocale());
  }

  /**
   * 返回消息
   *
   * @param messageCodec
   * @return
   */
  public static String getMessage(MessageCodec messageCodec) {
    return StringUtils.hasText(messageCodec.key()) ?
        messageContext.getMessageSource()
            .getMessage(messageCodec.key(), null, messageContext.getLocale()) : null;
  }

}
