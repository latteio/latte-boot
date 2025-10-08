package io.latte.boot.webmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;

import java.util.List;

/**
 * Jackson2HttpMessageConverterCustomizer
 *
 * @author : wugz
 * @since : 2021/12/13
 */
public class Jackson2HttpMessageConverterCustomizer {
  private final ObjectMapper jacksonObjectMapper;

  /**
   * 构造函数
   */
  public Jackson2HttpMessageConverterCustomizer(ObjectMapper jacksonObjectMapper) {
    this.jacksonObjectMapper = jacksonObjectMapper;
  }

  /**
   * 替换默认MappingJackson2HttpMessageConverter配置
   *
   * @param converters 转换器集
   */
  public void customize(List<HttpMessageConverter<?>> converters) {
    converters.forEach(converter -> {
      if (converter instanceof AllEncompassingFormHttpMessageConverter) {
        ((AllEncompassingFormHttpMessageConverter) converter).getPartConverters().forEach(mc -> {
          if (mc instanceof MappingJackson2HttpMessageConverter c) {
            c.setObjectMapper(jacksonObjectMapper);
          }
        });
      } else if (converter instanceof MappingJackson2HttpMessageConverter c) {
        c.setObjectMapper(jacksonObjectMapper);
      }
    });
  }

  public ObjectMapper getObjectMapper() {
    return jacksonObjectMapper;
  }
}
