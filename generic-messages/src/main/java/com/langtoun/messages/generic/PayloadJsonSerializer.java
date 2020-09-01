package com.langtoun.messages.generic;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.langtoun.messages.annotations.CustomTypeEncoding;
import com.langtoun.messages.annotations.TypeDefinition;
import com.langtoun.messages.annotations.TypeProperty;
import com.langtoun.messages.types.CustomTypeCodec;
import com.langtoun.messages.types.FieldEncodingType;
import com.langtoun.messages.util.SerializationUtil;

/**
 * JSON serializer for types that implement the {@link SerializablePayload}
 * interface.
 *
 */
public class PayloadJsonSerializer extends JsonSerializer<SerializablePayload> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void serialize(final SerializablePayload payload, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    serialize(payload, gen);
  }

  private static void serialize(final SerializablePayload value, final JsonGenerator gen) throws IOException {
    /*
     * check that the type is annotated with TypeDefinition
     */
    final TypeDefinition typeDefinition = SerializationUtil.getTypeDefinition(value);
    if (typeDefinition != null) {
      if (SerializationUtil.usesCustomTypeEncoding(typeDefinition.encoding())) {
        gen.writeString(serializeCustomEncoding(value, typeDefinition));
      } else {
        serializePayload(value, typeDefinition, gen);
      }
    } else {
      throw new IllegalArgumentException(
          String.format("type[%s] must be annotated with @TypeDefinition", value.getClass().getTypeName()));
    }
  }

  public static String serializeCustomEncoding(final SerializablePayload value) {
    /*
     * check that the type is annotated with TypeDefinition
     */
    final TypeDefinition typeDefinition = SerializationUtil.getTypeDefinition(value);
    if (typeDefinition != null) {
      return serializeCustomEncoding(value, SerializationUtil.getTypeDefinition(value));
    } else {
      throw new IllegalArgumentException(
          String.format("type[%s] must be annotated with @TypeDefinition", value.getClass().getTypeName()));
    }
  }

  private static String serializeCustomEncoding(final SerializablePayload value, final TypeDefinition typeDefinition) {
    final StringBuilder encoded = new StringBuilder();
    if (typeDefinition.encoding().codec() == CustomTypeCodec.GQL) {
      /*
       * process using the GQL serializer
       */
      throw new UnsupportedOperationException("TODO: implement the GQL codec"); // TODO: implement the GQL codec
    } else if (typeDefinition.encoding().codec() == CustomTypeCodec.STD) {
      /*
       * process using the prefix, suffix, field separator, and key / value separator
       * specified in the annotation
       */
      serializeCustomEncoding(value, typeDefinition, encoded);
    } else {
      throw new IllegalArgumentException(String.format("cannot serialize type[%s] with unknown custom codec[%s]",
          value.getClass().getTypeName(), typeDefinition.encoding().codec().customCodec));
    }
    return encoded.toString();
  }

  private static void serializePayload(final SerializablePayload value, final TypeDefinition typeDefinition,
      final JsonGenerator gen) throws IOException {
    if (value != null) {
      /*
       * open a new object
       */
      gen.writeStartObject();
      /*
       * retrieve the fields annotated with TypeProperty and compute the field order
       */
      final Map<String, Pair<Field, TypeProperty>> fieldProperties = SerializationUtil
          .getHierarchyFieldsWithTypeProperty(value.getClass());
      final String[] fieldOrder = SerializationUtil.computeFieldOrder(typeDefinition, fieldProperties);
      /*
       * iterate over the fields to be serialized
       */
      for (final String fieldName : fieldOrder) {
        final Pair<Field, TypeProperty> fieldProperty = fieldProperties.get(fieldName);
        final Field field = fieldProperty.getKey();
        final TypeProperty property = fieldProperty.getValue();
        if (List.class.isAssignableFrom(field.getType())) {
          writeArrayValues(property.jsonName(), SerializationUtil.getListValue(value, field), property.required(), gen);
        } else {
          final Object fieldValue = SerializationUtil.getValue(value, field);
          if (fieldValue instanceof SerializablePayload) {
            gen.writeFieldName(property.jsonName());
            serialize((SerializablePayload) fieldValue, gen);
          } else {
            writeScalarValue(property.jsonName(), fieldValue, property.required(), gen);
          }
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
    if (value instanceof SerializablePayload) {
      serialize((SerializablePayload) value, gen);
    } else if (value instanceof String) {
      gen.writeString((String) value);
    } else if (value instanceof Integer) {
      gen.writeNumber((Integer) value);
    } else if (value instanceof Double) {
      gen.writeNumber((Double) value);
    } else if (value instanceof Boolean) {
      gen.writeBoolean((Boolean) value);
    } else {
      throw new IllegalStateException(
          String.format("attempt to serialize unknown value type[%s]", value.getClass().getSimpleName()));
    }
  }

  private static StringBuilder serializeCustomEncoding(final SerializablePayload value, final TypeDefinition typeDefinition,
      final StringBuilder encoded) {
    final CustomTypeEncoding typeEncoding = typeDefinition.encoding();
    /*
     * write out the prefix if present
     */
    if (!typeEncoding.prefix().isEmpty()) {
      encoded.append(typeEncoding.prefix());
    }
    /*
     * encode each field in the order specified in the computed field order
     */
    final Map<String, Pair<Field, TypeProperty>> fieldProperties = SerializationUtil
        .getHierarchyFieldsWithTypeProperty(value.getClass());
    final String[] fieldOrder = SerializationUtil.computeFieldOrder(typeDefinition, fieldProperties);
    /*
     * iterate over the fields that are to be encoded
     */
    int index = 0;
    for (final String fieldName : fieldOrder) {
      final Pair<Field, TypeProperty> fieldProperty = fieldProperties.get(fieldName);
      final Field field = fieldProperty.getKey();
      final TypeProperty property = fieldProperty.getValue();
      final Class<?> fieldType = field.getType();
      final boolean repeated = List.class.isAssignableFrom(fieldType);

      String encodedValue = null;

      if (SerializablePayload.class.isAssignableFrom(fieldType)) { // --- complex type ---
        final FieldEncodingType fieldEncoding = property.encoding();
        if (fieldEncoding != null) {
          final Object fieldValue = SerializationUtil.getValue(value, field);
          if (fieldEncoding == FieldEncodingType.XML || fieldEncoding == FieldEncodingType.XML_URLENCODED) {
            try {
              final JAXBContext jaxbContext = SerializationUtil.getJaxbContextFor(fieldType);
              final Marshaller marshaller = jaxbContext.createMarshaller();
              marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
              final StringWriter writer = new StringWriter();
              marshaller.marshal(fieldValue, writer);
              encodedValue = writer.toString();
              if (fieldEncoding == FieldEncodingType.XML_URLENCODED) {
                encodedValue = URLEncoder.encode(encodedValue, "UTF-8");
              }
            } catch (JAXBException | UnsupportedEncodingException e) {
              throw new IllegalArgumentException(
                  String.format("unable to serialize an field value of type[%s]", fieldType.getTypeName()), e);
            }
          } else if (fieldEncoding == FieldEncodingType.JSON || fieldEncoding == FieldEncodingType.JSON_URLENCODED
              || fieldEncoding == FieldEncodingType.BASE64) {
            try {
              encodedValue = objectMapper.writeValueAsString(fieldValue);
              if (fieldEncoding == FieldEncodingType.JSON_URLENCODED) {
                encodedValue = URLEncoder.encode(encodedValue, "UTF-8");
              } else if (fieldEncoding == FieldEncodingType.JSON_URLENCODED) {
                encodedValue = Base64.encodeBase64URLSafeString(encodedValue.getBytes(StandardCharsets.UTF_8));
              }
            } catch (JsonProcessingException | UnsupportedEncodingException e) {
              throw new IllegalArgumentException(String.format("unable to serialize an instance of type[%s], field[%s]",
                  value.getClass().getTypeName(), field.getName()), e);
            }
          } else {
            throw new IllegalArgumentException(String.format("unable to serialize an instance of type[%s], field[%s]",
                value.getClass().getTypeName(), field.getName()));
          }
        } else {
          throw new IllegalArgumentException(
              String.format("unable to serialize an instance of type[%s], field[%s] - no encoding defined",
                  value.getClass().getTypeName(), field.getName()));
        }
      } else { // --- simple type ---
        Object object = null;
        if (repeated) {
          object = SerializationUtil.getListValue(value, field);
        } else {
          object = SerializationUtil.getValue(value, field);
        }
        if (object != null) {
          encodedValue = object.toString();
        }
      }
      /*
       * write out a fieldSep if present and required
       */
      if (index > 0 && typeEncoding.fieldSep().length > 0) {
        encoded.append(typeEncoding.fieldSep()[0]);
      }
      /*
       * if the keyValSep has been specified write out the field name followed by the
       * keyValSep
       */
      if (!typeEncoding.keyValSep().isEmpty()) {
        encoded.append(property.originalName()).append(typeEncoding.keyValSep());
      }
      /*
       * regardless of the encoding components that were / were not specified, the
       * encoded value must always be written
       */
      encoded.append(encodedValue);
      index++;
    }
    /*
     * write out the suffix if present
     */
    if (!typeEncoding.suffix().isEmpty()) {
      encoded.append(typeEncoding.suffix());
    }
    return encoded;
  }

}
