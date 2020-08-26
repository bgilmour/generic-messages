package com.langtoun.messages.generic;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.ListProperty;
import com.langtoun.messages.types.properties.MessageProperty;
import com.langtoun.messages.types.properties.ScalarProperty;

/**
 * JSON serializer for types that implement the {@link SerializablePayload}
 * interface.
 *
 */
public class MessageJsonSerializer extends JsonSerializer<SerializablePayload> {

  @Override
  public void serialize(final SerializablePayload value, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    serializePayload(value, gen, serializers);
  }

  private static void serializePayload(final SerializablePayload payload, final JsonGenerator gen,
      final SerializerProvider serializers) throws IOException {
    if (payload != null) {
      gen.writeStartObject();
      for (final MessageProperty property : payload.getProperties()) {
        if (property instanceof ListProperty) {
          final ListProperty listProperty = (ListProperty) property;
          writeArrayValues(listProperty.getJsonName(), listProperty.getGetter().get(), listProperty.isRequired(), gen, serializers);
        } else {
          final ScalarProperty scalarProperty = (ScalarProperty) property;
          final Object propertyValue = scalarProperty.getGetter().get();
          if (propertyValue instanceof SerializablePayload) {
            gen.writeFieldName(scalarProperty.getJsonName());
            serializePayload((SerializablePayload) propertyValue, gen, serializers);
          } else {
            writeScalarValue(property.getJsonName(), propertyValue, property.isRequired(), gen, serializers);
          }
        }
      }
      gen.writeEndObject();
    }
  }

  private static void writeArrayValues(final String fieldName, final List<Object> array, final boolean required,
      final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
    if (required || !CollectionUtils.isEmpty(array)) {
      if (fieldName != null) {
        gen.writeFieldName(fieldName);
      }
    }
    writeArrayValues(array, required, gen, serializers);
  }

  private static void writeArrayValues(final List<Object> array, final boolean required, final JsonGenerator gen,
      final SerializerProvider serializers) throws IOException {
    if (required || !CollectionUtils.isEmpty(array)) {
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

  private static void writeScalarValue(final String fieldName, final Object value, final boolean required, final JsonGenerator gen,
      final SerializerProvider serializers) throws IOException {
    if (value == null && required) {
      // write a null
      gen.writeNullField(fieldName);
    } else if (value != null) {
      // write the value
      gen.writeFieldName(fieldName);
      writeScalarValue(value, gen, serializers);
    }
  }

  private static void writeScalarValue(final Object value, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    if (value instanceof SerializablePayload) {
      serializePayload((SerializablePayload) value, gen, serializers);
    } else if (value instanceof String) {
      gen.writeString((String) value);
    } else if (value instanceof Integer) {
      gen.writeNumber((Integer) value);
    } else if (value instanceof Double) {
      gen.writeNumber((Double) value);
    } else if (value instanceof Boolean) {
      gen.writeBoolean((Boolean) value);
    } else {
      throw new IOException("attempt to serialize unknown type: " + value.getClass().getSimpleName());
    }
  }

}
