package io.latte.boot.webmvc;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;

/**
 * XssStringConverter
 *
 * @author : wugz
 * @since : 2021/09/26
 */
public class XssStringConverter implements Converter<String, String> {
  /**
   * Convert the source object of type to target type
   *
   * @param source
   * @return
   */
  public String convert(@Nullable String source) {
    return null != source ? HtmlUtils.htmlEscape(source, StandardCharsets.UTF_8.name()) : null;
  }
}
