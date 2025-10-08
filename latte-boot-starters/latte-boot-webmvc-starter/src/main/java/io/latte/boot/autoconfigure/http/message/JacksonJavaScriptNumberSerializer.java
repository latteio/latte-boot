package io.latte.boot.autoconfigure.http.message;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;

/**
 * JacksonJavaScriptNumberSerializer
 *
 * @author : wugz
 * @since : 2024/9/12
 */
@JacksonStdImpl
public class JacksonJavaScriptNumberSerializer extends NumberSerializer {
  public final static JacksonJavaScriptNumberSerializer INSTANCE = new JacksonJavaScriptNumberSerializer(Number.class);
  private static final Long JS_MAX_SAFE_INTEGER = 9007199254740991L;
  private static final Long JS_MIN_SAFE_INTEGER = -9007199254740991L;
  private static final Double JS_MAX_SAFE_DOUBLE = 9007199254740990.0;
  private static final Double JS_MIN_SAFE_DOUBLE = -9007199254740990.0;

  public JacksonJavaScriptNumberSerializer(Class<? extends Number> handledType) {
    super(handledType);
  }

  public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (Long.class.isAssignableFrom(value.getClass())) {
      if (value.longValue() > JS_MIN_SAFE_INTEGER && value.longValue() < JS_MAX_SAFE_INTEGER) {
        super.serialize(value, gen, serializers);
      } else {
        gen.writeString(value.toString());
      }
    } else if (Double.class.isAssignableFrom(value.getClass())) {
      if (value.doubleValue() > JS_MIN_SAFE_DOUBLE && value.doubleValue() < JS_MAX_SAFE_DOUBLE) {
        super.serialize(value, gen, serializers);
      } else {
        gen.writeString(value.toString());
      }
    }
  }
}

