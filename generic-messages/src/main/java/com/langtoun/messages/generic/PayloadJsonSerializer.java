package com.langtoun.messages.generic;

import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_BASE64;
import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_GQL;
import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_JSON;
import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_JSON_URLENCODED;
import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_XML;
import static com.langtoun.messages.types.CustomEncodingContext.CUSTOM_ENCODING_XML_URLENCODED;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.langtoun.messages.types.CustomEncodingContext;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.ListProperty;
import com.langtoun.messages.types.properties.PayloadProperty;
import com.langtoun.messages.types.properties.ScalarProperty;

/**
 * JSON serializer for types that implement the {@link SerializablePayload}
 * interface.
 *
 */
public class PayloadJsonSerializer extends JsonSerializer<SerializablePayload> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static final Map<Class<?>, JAXBContext> jaxbContexts = new HashMap<>();

  @Override
  public void serialize(final SerializablePayload value, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    serialize(value, gen);
  }

  private static void serialize(final SerializablePayload value, final JsonGenerator gen) throws IOException {
    if (!value.getCustomEncodingContext().usesCustomEncoder()) {
      serializePayload(value, gen);
    } else {
      gen.writeString(serializeCustomEncoding(value));
    }
  }

  public static String serializeCustomEncoding(final SerializablePayload value) {
    final StringBuilder encoded = new StringBuilder();
    if (CUSTOM_ENCODING_GQL.equals(value.getCustomEncodingContext().getTypeEncoding())) {

    } else {
      serializeCustomEncoding(value, value.getCustomEncodingContext(), encoded);
    }
    return encoded.toString();
  }

  private static void serializePayload(final SerializablePayload payload, final JsonGenerator gen) throws IOException {
    if (payload != null) {
      gen.writeStartObject();
      for (final PayloadProperty property : payload.getProperties()) {
        if (property instanceof ListProperty) {
          final ListProperty listProperty = (ListProperty) property;
          writeArrayValues(listProperty.getJsonName(), listProperty.getGetter().get(), listProperty.isRequired(), gen);
        } else {
          final ScalarProperty scalarProperty = (ScalarProperty) property;
          final Object propertyValue = scalarProperty.getGetter().get();
          if (propertyValue instanceof SerializablePayload) {
            gen.writeFieldName(scalarProperty.getJsonName());
            serialize((SerializablePayload) propertyValue, gen);
          } else {
            writeScalarValue(property.getJsonName(), propertyValue, property.isRequired(), gen);
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
      throw new IOException("attempt to serialize unknown type: " + value.getClass().getSimpleName());
    }
  }

  private static StringBuilder serializeCustomEncoding(final SerializablePayload value, final CustomEncodingContext context,
      final StringBuilder encoded) {
    if (context.getPrefix() != null) {
      encoded.append(context.getPrefix());
    }

    int index = 0;
    for (final PayloadProperty property : value.getProperties()) {
      final boolean repeated = property instanceof ListProperty;
      final String ramlName = property.getName(); // TODO: we need to add getRamlName()
      final Class<?> valueType = property.getValueType();

      String encodedValue = null;

      if (SerializablePayload.class.isAssignableFrom(valueType)) { // complex type
        final String typeEncoding = property.getTypeEncoding();
        if (typeEncoding != null) {
          final Object fieldValue = property instanceof ScalarProperty ? ((ScalarProperty) property).getGetter().get()
              : ((ListProperty) property).getGetter().get();
          if (CUSTOM_ENCODING_XML.equals(typeEncoding) || CUSTOM_ENCODING_XML_URLENCODED.equals(typeEncoding)) {
            try {
              final JAXBContext jaxbContext = getJaxbContextFor(valueType);
              final Marshaller marshaller = jaxbContext.createMarshaller();
              marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
              final StringWriter writer = new StringWriter();
              marshaller.marshal(fieldValue, writer);
              encodedValue = writer.toString();
              if (CUSTOM_ENCODING_XML_URLENCODED.equals(typeEncoding)) {
                encodedValue = URLEncoder.encode(encodedValue, "UTF-8");
              }
            } catch (JAXBException | UnsupportedEncodingException e) {
              throw new IllegalArgumentException("unable to serialize an instance of " + valueType.getTypeName(), e);
            }
          } else if (CUSTOM_ENCODING_JSON.equals(typeEncoding) || CUSTOM_ENCODING_JSON_URLENCODED.equals(typeEncoding)
              || CUSTOM_ENCODING_BASE64.equals(typeEncoding)) {
            try {
              encodedValue = objectMapper.writeValueAsString(fieldValue);
              if (CUSTOM_ENCODING_JSON_URLENCODED.equals(typeEncoding)) {
                encodedValue = URLEncoder.encode(encodedValue, "UTF-8");
              } else if (CUSTOM_ENCODING_BASE64.equals(typeEncoding)) {
                encodedValue = Base64.encodeBase64URLSafeString(encodedValue.getBytes(StandardCharsets.UTF_8));
              }
            } catch (JsonProcessingException | UnsupportedEncodingException e) {
              throw new IllegalArgumentException(
                  "unable to serialize an instance of " + value.getClass().getTypeName() + ", field " + property.getName(), e);
            }
          } else {
            throw new IllegalArgumentException("unable to serialize an instance of " + value.getClass().getTypeName() + ", field "
                + property.getName() + " - unknown encoding " + typeEncoding);
          }
        } else {
          throw new IllegalArgumentException("unable to serialize an instance of " + value.getClass().getTypeName() + ", field "
              + property.getName() + " - no encoding defined");
        }

      } else { // simple type
        Object object = null;
        if (repeated) {
          object = ((ListProperty) property).getGetter().get();
        } else {
          object = ((ScalarProperty) property).getGetter().get();
        }
        if (object != null) {
          encodedValue = object.toString();
        }
      }

      if (index > 0 && !context.getSeparators().isEmpty()) {
        encoded.append(context.getSeparators().get(0));
      }
      if (context.getKeyValueSeparator() != null) {
        encoded.append(ramlName).append(context.getKeyValueSeparator());
      }
      encoded.append(encodedValue);
      index++;
    }

    if (context.getSuffix() != null) {
      encoded.append(context.getSuffix());
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
