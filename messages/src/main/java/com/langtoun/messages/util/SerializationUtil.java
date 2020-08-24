package com.langtoun.messages.util;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;

public final class SerializationUtil {

  private SerializationUtil() {
    // static utility class
  }

  public static void writeScalarValue(final String fieldName, final Object value, final boolean required, final JsonGenerator gen)
      throws IOException {
    if (value == null && required) {
      // write a null
      gen.writeNullField(fieldName);
    } else if (value != null) {
      // write the value
      gen.writeFieldName(fieldName);
      writeScalarValue(value, gen);
    }
  }

  private static void writeScalarValue(final Object value, final JsonGenerator gen) throws IOException {
    if (value instanceof String) {
      gen.writeString((String) value);
    } else if (value instanceof Integer) {
      gen.writeNumber((Integer) value);
    } else if (value instanceof Double) {
      gen.writeNumber((Double) value);
    } else {
      throw new IOException("attempt to serialize unknown type: " + value.getClass().getSimpleName());
    }
  }

  public static void writeArrayValues(final List<Object> array, final boolean required, final JsonGenerator gen)
      throws IOException {
    writeArrayValues(null, array, required, gen);
  }

  public static void writeArrayValues(final String fieldName, final List<Object> array, final boolean required,
      final JsonGenerator gen) throws IOException {
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
          writeScalarValue(item, gen);
        }
      }
    }
    if (required || (array != null && !array.isEmpty())) {
      gen.writeEndArray();
    }
  }

}
