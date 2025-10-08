package io.latte.boot.autoconfigure.http.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.latte.boot.support.web.DateFormatPatterns;
import io.latte.boot.webmvc.Jackson2HttpMessageConverterCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Jackson自动配置器
 *
 * @author : wugz
 * @since : 2022/8/19
 */
@Configuration(proxyBeanMethods = false)
public class JacksonCustomizedAutoConfiguration {
  @Bean
  @Primary
  public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder() {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
    /* 基础设置: 时区与简单格式化 */
    builder.locale(Locale.CHINA);
    builder.timeZone(TimeZone.getTimeZone("GMT+8"));
    builder.simpleDateFormat(DateFormatPatterns.DATE_TIME_FORMAT);

    /* 特性设置: 禁用特性 */
    builder.serializationInclusion(JsonInclude.Include.ALWAYS);
    builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    builder.featuresToDisable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
    builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    builder.featuresToDisable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
    builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    /* 添加自定义序列化器和反序列化器 */
    builder.modules(
        new SimpleModule()
            .addSerializer(Long.class, JacksonJavaScriptNumberSerializer.INSTANCE)
            .addSerializer(Double.class, JacksonJavaScriptNumberSerializer.INSTANCE)
            .addSerializer(BigInteger.class, ToStringSerializer.instance)
            .addSerializer(BigDecimal.class, ToStringSerializer.instance),
        new JavaTimeModule()
            .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateFormatPatterns.DATE_FORMAT)))
            .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateFormatPatterns.TIME_FORMAT)))
            .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateFormatPatterns.DATE_TIME_FORMAT)))
            .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateFormatPatterns.DATE_TIME_FULL_FORMAT)))
            .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateFormatPatterns.DATE_FORMAT)))
            .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateFormatPatterns.TIME_FORMAT)))
            .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateFormatPatterns.DATE_TIME_FORMAT)))
            .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateFormatPatterns.DATE_TIME_FULL_FORMAT)))
    );
    return builder;
  }

  @Bean
  @Primary
  public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
    return jacksonObjectMapperBuilder.createXmlMapper(false).build();
  }

  @Bean
  @ConditionalOnClass(value = {ObjectMapper.class, Jackson2HttpMessageConverterCustomizer.class})
  public Jackson2HttpMessageConverterCustomizer jacksonConverterCustomizer(ObjectMapper jacksonObjectMapper) {
    return new Jackson2HttpMessageConverterCustomizer(jacksonObjectMapper);
  }

}
