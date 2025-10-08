package io.latte.boot.web.http.message;

import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Objects;

/**
 * AppMessageContext
 *
 * @author : wugz
 * @since : 2022/10/29
 */
public class AppMessageContext {
  private static volatile MessageContext INSTANCE;
  private MessageSourceSupport messageSourceSupport;

  /**
   * 构造函数
   *
   * @param messageSource
   */
  public AppMessageContext(MessageSource messageSource) {
    if (INSTANCE == null) {
      synchronized (AppMessageContext.class) {
        if (INSTANCE == null) {
          INSTANCE = new MessageContext(messageSource);
          this.messageSourceSupport = new MessageUtils(INSTANCE);
        }
      }
    }
  }

  /**
   * 绑定语言
   *
   * @param locale
   */
  public static void setLocale(Locale locale) {
    INSTANCE.setLocale(Objects.requireNonNull(locale));
  }

  private MessageSourceSupport getMessageSourceSupport() {
    return messageSourceSupport;
  }
}
