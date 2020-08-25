package com.langtoun.messages.runtime.generic;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.langtoun.messages.compilation.properties.ListProperty;
import com.langtoun.messages.compilation.properties.MessageProperty;
import com.langtoun.messages.compilation.properties.ScalarProperty;
import com.langtoun.messages.compilation.types.SerializablePayload;

/**
 * Utility class used by the generic message JSON serializer.
 *
 */
public final class JsonSerializationUtil {

  private JsonSerializationUtil() {
    // static utility class
  }

  public static void serialize(SerializablePayload payload, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (payload != null) {
      gen.writeStartObject();
      for (MessageProperty property : payload.getProperties()) {
        if (property instanceof ListProperty) {
          final ListProperty listProperty = (ListProperty) property;
          writeArrayValues(listProperty.getJsonName(), listProperty.getGetter().get(), listProperty.isRequired(), gen, serializers);
        } else {
          final ScalarProperty scalarProperty = (ScalarProperty) property;
          final Object propertyValue = scalarProperty.getGetter().get();
          if (propertyValue instanceof SerializablePayload) {
            gen.writeFieldName(scalarProperty.getJsonName());
            serialize((SerializablePayload) propertyValue, gen, serializers);
          } else {
            writeScalarValue(property.getJsonName(), propertyValue, property.isRequired(), gen, serializers);
          }
        }
      }
      gen.writeEndObject();
    }
  }

  public static void writeScalarValue(final String fieldName, final Object value, final boolean required, final JsonGenerator gen,
      SerializerProvider serializers) throws IOException {
    if (value == null && required) {
      // write a null
      gen.writeNullField(fieldName);
    } else if (value != null) {
      // write the value
      gen.writeFieldName(fieldName);
      writeScalarValue(value, gen, serializers);
    }
  }

  private static void writeScalarValue(final Object value, final JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (value instanceof SerializablePayload) {
      serialize((SerializablePayload) value, gen, serializers);
    } else if (value instanceof String) {
      gen.writeString((String) value);
    } else if (value instanceof Integer) {
      gen.writeNumber((Integer) value);
    } else if (value instanceof Double) {
      gen.writeNumber((Double) value);
    } else {
      throw new IOException("attempt to serialize unknown type: " + value.getClass().getSimpleName());
    }
  }

  public static void writeArrayValues(final List<Object> array, final boolean required, final JsonGenerator gen,
      SerializerProvider serializers) throws IOException {
    writeArrayValues(null, array, required, gen, serializers);
  }

  public static void writeArrayValues(final String fieldName, final List<Object> array, final boolean required,
      final JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (required || (array != null && !array.isEmpty())) {
      if (fieldName != null) {
        gen.writeFieldName(fieldName);
      }
      gen.writeStartArray();
    }
    if (array != null) {
      for (final Object item : array) {
        // write the item
        if (item == null) {
          gen.writeNull();
        } else {
          writeScalarValue(item, gen, serializers);
        }
      }
    }
    if (required || (array != null && !array.isEmpty())) {
      gen.writeEndArray();
    }
  }

}
