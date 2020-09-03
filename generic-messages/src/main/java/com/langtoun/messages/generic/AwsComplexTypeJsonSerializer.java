package com.langtoun.messages.generic;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.langtoun.messages.annotations.AwsFieldProperty;
import com.langtoun.messages.annotations.AwsTypeDefinition;
import com.langtoun.messages.types.AwsComplexType;
import com.langtoun.messages.util.SerializationHelper;

/**
 * JSON serializer for types that extend the {@link AwsComplexType} base class.
 *
 */
public class AwsComplexTypeJsonSerializer extends JsonSerializer<AwsComplexType> {

  @Override
  public void serialize(final AwsComplexType value, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    serialize(value, gen);
  }

  private static void serialize(final AwsComplexType value, final JsonGenerator gen) throws IOException {
    /*
     * check that the type is annotated with TypeDefinition
     */
    final AwsTypeDefinition typeDefinition = SerializationHelper.getTypeDefinition(value);
    if (typeDefinition != null) {
      if (!SerializationHelper.usesCustomTypeEncoding(value.getClass())) {
        serializeComplexType(value, typeDefinition, gen);
      } else {
        throw new IllegalArgumentException(
            String.format("the @TypeDefinition annotation must not specify custom encoding parameters for type[%s]",
                value.getClass().getTypeName()));
      }
    } else {
      throw new IllegalArgumentException(
          String.format("type[%s] must be annotated with @TypeDefinition", value.getClass().getTypeName()));
    }
  }

  private static void serializeComplexType(final AwsComplexType value, final AwsTypeDefinition typeDefinition,
      final JsonGenerator gen) throws IOException {
    if (value != null) {
      /*
       * open a new object
       */
      gen.writeStartObject();
      /*
       * retrieve the fields annotated with TypeProperty and compute the field order
       */
      final Map<String, Pair<Field, AwsFieldProperty>> fieldProperties = SerializationHelper
          .computeFieldProperties(value.getClass());
      final String[] fieldOrder = SerializationHelper.computeFieldOrder(typeDefinition, fieldProperties);
      /*
       * iterate over the fields to be serialized
       */
      for (final String fieldName : fieldOrder) {
        final Pair<Field, AwsFieldProperty> fieldProperty = fieldProperties.get(fieldName);
        if (fieldProperty != null) {
          final Field field = fieldProperty.getKey();
          final AwsFieldProperty property = fieldProperty.getValue();
          if (List.class.isAssignableFrom(field.getType())) {
            writeArrayValues(fieldName, SerializationHelper.getListValue(value, field), property.required(), gen);
          } else {
            final Object fieldValue = SerializationHelper.getValue(value, field);
            if (fieldValue instanceof AwsComplexType) {
              gen.writeFieldName(fieldName);
              serialize((AwsComplexType) fieldValue, gen);
            } else {
              writeScalarValue(fieldName, fieldValue, property.required(), gen);
            }
          }
        } else {
          throw new IllegalStateException(
              String.format("failed to retrieve field property info during serialization of type[%s], field[%s]",
                  value.getClass().getTypeName(), fieldName));
        }
      }
      /*
       * close the object
       */
      gen.writeEndObject();
    }
  }

  private static void writeArrayValues(final String fieldName, final List<Object> array, final boolean required,
      final JsonGenerator gen) throws IOException {
    if (required || !CollectionUtils.isEmpty(array)) {
      if (fieldName != null) {
        gen.writeFieldName(fieldName);
      }
    }
    writeArrayValues(array, required, gen);
  }

  private static void writeArrayValues(final List<Object> array, final boolean required, final JsonGenerator gen)
      throws IOException {
    if (required || !CollectionUtils.isEmpty(array)) {
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

  private static void writeScalarValue(final String fieldName, final Object value, final boolean required, final JsonGenerator gen)
      throws IOException {
    if (value == null && required) {
      /*
       * write a null
       */
      gen.writeNullField(fieldName);
    } else if (value != null) {
      /*
       * write the value
       */
      gen.writeFieldName(fieldName);
      writeScalarValue(value, gen);
    }
  }

  private static void writeScalarValue(final Object value, final JsonGenerator gen) throws IOException {
    if (value instanceof AwsComplexType) {
      serialize((AwsComplexType) value, gen);
    } else if (value instanceof String) {
      gen.writeString((String) value);
    } else if (value instanceof Long) {
      gen.writeNumber((Long) value);
    } else if (value instanceof Integer) {
      gen.writeNumber((Integer) value);
    } else if (value instanceof Double) {
      gen.writeNumber((Double) value);
    } else if (value instanceof Float) {
      gen.writeNumber((Float) value);
    } else if (value instanceof Boolean) {
      gen.writeBoolean((Boolean) value);
    } else {
      throw new IllegalStateException(
          String.format("attempt to serialize unknown value type[%s]", value.getClass().getSimpleName()));
    }
  }

}
