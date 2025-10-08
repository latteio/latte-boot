package io.latte.boot.web.http.message;

import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Objects;

/**
 * MessageContext
 *
 * @author : wugz
 * @since : 2022/10/29
 */
public class MessageContext {
  private ThreadLocal<Locale> messageLang = new ThreadLocal<>();
  private MessageSource messageSource;

  /**
   * 构造函数
   *
   * @param messageSource
   */
  MessageContext(MessageSource messageSource) {
    this.messageLang.set(Locale.SIMPLIFIED_CHINESE);
    this.messageSource = messageSource;
  }

  /**
   * 获取语言配置
   *
   * @return
   */
  public MessageSource getMessageSource() {
    return messageSource;
  }

  /**
   * 获取语言
   *
   * @return
   */
  public Locale getLocale() {
    return messageLang.get();
  }

  /**
   * 绑定语言
   *
   * @param locale
   */
  public void setLocale(Locale locale) {
    messageLang.set(Objects.requireNonNull(locale));
  }
}
