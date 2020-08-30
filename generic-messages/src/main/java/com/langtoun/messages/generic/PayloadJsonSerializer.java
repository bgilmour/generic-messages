package com.langtoun.messages.generic;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.langtoun.messages.annotations.CustomTypeEncoding;
import com.langtoun.messages.annotations.TypeProperty;
import com.langtoun.messages.types.CustomTypeEncoder;
import com.langtoun.messages.types.FieldEncodingType;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.util.SerializationUtil;

/**
 * JSON serializer for types that implement the {@link SerializablePayload}
 * interface.
 *
 */
public class PayloadJsonSerializer extends JsonSerializer<SerializablePayload> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static final Map<Class<?>, JAXBContext> jaxbContexts = new HashMap<>();

  @Override
  public void serialize(final SerializablePayload payload, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    serialize(payload, gen);
  }

  private static void serialize(final SerializablePayload payload, final JsonGenerator gen) throws IOException {
    final CustomTypeEncoding typeEncoding = SerializationUtil.getCustomTypeEncoding(payload);
    if (typeEncoding != null) {
      gen.writeString(serializeCustomEncoding(payload, typeEncoding));
    } else {
      serializePayload(payload, gen);
    }
  }

  public static String serializeCustomEncoding(final SerializablePayload value) {
    return serializeCustomEncoding(value, SerializationUtil.getCustomTypeEncoding(value));
  }

  private static String serializeCustomEncoding(final SerializablePayload value, final CustomTypeEncoding typeEncoding) {
    final StringBuilder encoded = new StringBuilder();
    if (typeEncoding != null) {
      if (typeEncoding.encoder() == CustomTypeEncoder.GQL) {
        // process using the GQL serializer
        // TODO: implement the GQL serializer
      } else {
        // process using the prefix, suffix, field separator, and key / value separator
        // specified in the annotation
        serializeCustomEncoding(value, typeEncoding, encoded);
      }
    } else {
      throw new IllegalStateException("");
    }
    return encoded.toString();
  }

  private static void serializePayload(final SerializablePayload payload, final JsonGenerator gen) throws IOException {
    if (payload != null) {
      gen.writeStartObject();
      for (final Entry<Field, TypeProperty> fieldProperty : SerializationUtil.getFieldProperties(payload).entrySet()) {
        final Field field = fieldProperty.getKey();
        final TypeProperty property = fieldProperty.getValue();
        if (List.class.isAssignableFrom(field.getType())) {
          writeArrayValues(property.jsonName(), SerializationUtil.getListValue(payload, field), property.required(), gen);
        } else {
          final Object fieldValue = SerializationUtil.getValue(payload, field);
          if (fieldValue instanceof SerializablePayload) {
            gen.writeFieldName(property.jsonName());
            serialize((SerializablePayload) fieldValue, gen);
          } else {
            writeScalarValue(property.jsonName(), fieldValue, property.required(), gen);
          }
        }
      }
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
      // write a null
      gen.writeNullField(fieldName);
    } else if (value != null) {
      // write the value
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

  private static StringBuilder serializeCustomEncoding(final SerializablePayload payload, final CustomTypeEncoding typeEncoding,
      final StringBuilder encoded) {
    if (!typeEncoding.prefix().isEmpty()) {
      encoded.append(typeEncoding.prefix());
    }

    int index = 0;
    for (final Entry<Field, TypeProperty> fieldProperty : SerializationUtil.getFieldProperties(payload).entrySet()) {
      final Field field = fieldProperty.getKey();
      final TypeProperty property = fieldProperty.getValue();
      final Class<?> fieldType = field.getType();
      final boolean repeated = List.class.isAssignableFrom(fieldType);

      String encodedValue = null;

      if (SerializablePayload.class.isAssignableFrom(fieldType)) { // --- complex type ---
        final FieldEncodingType fieldEncoding = property.encoding();
        if (fieldEncoding != null) {
          final Object fieldValue = SerializationUtil.getValue(payload, field);
          if (fieldEncoding == FieldEncodingType.XML || fieldEncoding == FieldEncodingType.XML_URLENCODED) {
            try {
              final JAXBContext jaxbContext = getJaxbContextFor(fieldType);
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
                  payload.getClass().getTypeName(), field.getName()), e);
            }
          } else {
            throw new IllegalArgumentException(String.format("unable to serialize an instance of type[%s], field[%s]",
                payload.getClass().getTypeName(), field.getName()));
          }
        } else {
          throw new IllegalArgumentException(
              String.format("unable to serialize an instance of type[%s], field[%s] - no encoding defined",
                  payload.getClass().getTypeName(), field.getName()));
        }
      } else { // --- simple type ---
        Object object = null;
        if (repeated) {
          object = SerializationUtil.getListValue(payload, field);
        } else {
          object = SerializationUtil.getValue(payload, field);
        }
        if (object != null) {
          encodedValue = object.toString();
        }
      }

      if (index > 0 && !typeEncoding.fieldSep().isEmpty()) {
        encoded.append(typeEncoding.fieldSep());
      }
      if (!typeEncoding.keyValSep().isEmpty()) {
        encoded.append(property.originalName()).append(typeEncoding.keyValSep());
      }
      encoded.append(encodedValue);
      index++;
    }

    if (!typeEncoding.suffix().isEmpty()) {
      encoded.append(typeEncoding.suffix());
    }
    return encoded;
  }

  private static JAXBContext getJaxbContextFor(final Class<?> clazz) {
    return jaxbContexts.computeIfAbsent(clazz, c -> {
      try {
        return JAXBContext.newInstance(c);
      } catch (final JAXBException e) {
        throw new IllegalStateException(e);
      }
    });
  }

}
