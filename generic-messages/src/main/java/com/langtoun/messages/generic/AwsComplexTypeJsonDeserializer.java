package com.langtoun.messages.generic;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.common.collect.Streams;
import com.langtoun.messages.annotations.CustomTypeEncoding;
import com.langtoun.messages.annotations.AwsTypeDefinition;
import com.langtoun.messages.annotations.AwsFieldProperty;
import com.langtoun.messages.types.AwsComplexType;
import com.langtoun.messages.types.CustomTypeCodec;
import com.langtoun.messages.types.FieldEncodingType;
import com.langtoun.messages.util.SerializationUtil;

/**
 * JSON deserializer for types that extend the {@link AwsComplexType} base
 * class.
 *
 */
public class AwsComplexTypeJsonDeserializer extends JsonDeserializer<AwsComplexType> implements ContextualDeserializer {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private JavaType javaType;

  public AwsComplexTypeJsonDeserializer() {

  }

  public AwsComplexTypeJsonDeserializer(final JavaType javaType) {
    this.javaType = javaType;
  }

  @Override
  public JsonDeserializer<?> createContextual(final DeserializationContext context, final BeanProperty property)
      throws JsonMappingException {
    final JavaType javaType = context.getContextualType() != null ? context.getContextualType() : property.getMember().getType();
    return new AwsComplexTypeJsonDeserializer(javaType);
  }

  @Override
  public AwsComplexType deserialize(final JsonParser parser, final DeserializationContext context)
      throws IOException, JsonProcessingException {
    System.out.println("deserialize : " + javaType.getTypeName());
    /*
     * check that the type is annotated with TypeDefinition
     */
    final AwsTypeDefinition typeDefinition = SerializationUtil.getTypeDefinition(javaType.getRawClass());
    if (typeDefinition != null) {
      try {
        final JsonNode rootNode = parser.getCodec().readTree(parser);
        final AwsComplexType value = (AwsComplexType) javaType.getRawClass().getConstructor().newInstance();
        return deserializeComplexType(value, rootNode, typeDefinition);
      } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
          | SecurityException e) {
        throw new IllegalArgumentException(String.format("unable to deserialize an instance of type[%s]", javaType.getTypeName()),
            e);
      }
    } else {
      throw new IllegalArgumentException(String.format("type[%s] must be annotated with @TypeDefinition", javaType.getTypeName()));
    }
  }

  private static AwsComplexType deserializeComplexType(final AwsComplexType value, final JsonNode rootNode,
      final AwsTypeDefinition typeDefinition) {
    if (SerializationUtil.usesCustomTypeEncoding(typeDefinition.encoding())) {
      return deserializeCustomEncoding(value, rootNode, typeDefinition);
    }
    return deserializeComplexType(value, rootNode, typeDefinition, "/", "  ");
  }

  private static AwsComplexType deserializeCustomEncoding(final AwsComplexType value, final JsonNode rootNode,
      final AwsTypeDefinition typeDefinition) {
    if (typeDefinition.encoding().codec() == CustomTypeCodec.GQL) {
      /*
       * process using the GQL serializer
       */
      // TODO: implement the GQL serializer
      return null;
    } else if (typeDefinition.encoding().codec() == CustomTypeCodec.STD) {
      /*
       * process using the prefix, suffix, field separator, and key / value separator
       * specified in the annotation
       */
      return deserializeCustomEncoding(value, rootNode.asText(), typeDefinition);
    } else {
      throw new IllegalArgumentException(String.format("cannot deserialize type[%s] with unknown custom codec[%s]",
          value.getClass().getTypeName(), typeDefinition.encoding().codec().customCodec));
    }
  }

  private static AwsComplexType deserializeComplexType(final AwsComplexType value, final JsonNode rootNode,
      final AwsTypeDefinition typeDefinition, final String nodePath, final String indent) {
    /*
     * retrieve the fields annotated with TypeProperty and compute the field order
     */
    final Map<String, Pair<Field, AwsFieldProperty>> fieldProperties = SerializationUtil
        .getHierarchyFieldsWithTypeProperty(value.getClass());
    final String[] fieldOrder = SerializationUtil.computeFieldOrder(typeDefinition, fieldProperties);
    /*
     * iterate over the fields to be deserialized
     */
    for (final String fieldName : fieldOrder) {
      final Pair<Field, AwsFieldProperty> fieldProperty = fieldProperties.get(fieldName);
      if (fieldProperty != null) {
        final Field field = fieldProperty.getKey();
        final AwsFieldProperty property = fieldProperty.getValue();
        /*
         * find each property in turn and populate the payload, creating new payload
         * objects as the deserializer walks the node tree
         */
        final String jsonName = property.jsonName();
        final Class<?> fieldType = field.getType();
        final JsonNode jsonField = rootNode.findValue(jsonName);
        if (jsonField != null) {
          if (List.class.isAssignableFrom(fieldType)) {
            if (jsonField instanceof ArrayNode) {
              System.out.println(indent + jsonName + "[]");
              SerializationUtil.setValue(value, readArrayValues((ArrayNode) jsonField, field, property, nodePath, indent + "  "),
                  field);
            } else {
              throw new IllegalStateException(
                  String.format("%s%s: failed to process list property for type[%s], field[%s] (JSON property name = %s)", nodePath,
                      jsonName, value.getClass().getTypeName(), field.getName(), jsonName));
            }
          } else {
            /*
             * deserialize the scalar value (may be simple or complex)
             */
            SerializationUtil.setValue(value, readScalarValue(jsonField, fieldName, field, nodePath, indent), field);
          }
        } else if (property.required()) {
          System.out.println(indent + jsonName + " - required (not present)");
          throw new IllegalStateException(String.format("%s%s: missing required property for type[%s], field[%s]", nodePath,
              fieldName, value.getClass().getTypeName(), field.getName()));
        } else {
          System.out.println(indent + jsonName + " - optional (not present)");
        }
      }
    }
    return value;
  }

  private static List<Object> readArrayValues(final ArrayNode arrayNode, final Field field, final AwsFieldProperty property,
      final String nodePath, final String indent) {
    final String fieldName = property.jsonName();
    final Iterator<JsonNode> iter = arrayNode.elements();
    if (iter.hasNext()) {
      int i = 0;
      List<Object> items = new ArrayList<>();
      while (iter.hasNext()) {
        final JsonNode node = iter.next();
        final String itemName = fieldName + "[" + i + "]";
        items.add(readScalarValue(node, itemName, field, nodePath, indent));
        i++;
      }
      return items;
    }
    return null;
  }

  private static Object readScalarValue(final JsonNode node, final String fieldName, Field field, final String nodePath,
      final String indent) {
    if (node instanceof ObjectNode) {
      System.out.println(indent + fieldName + "{}");
      return readComplexValue((ObjectNode) node, fieldName, field, nodePath, indent);
    } else if (node instanceof ValueNode) {
      System.out.println(indent + fieldName);
      final ValueNode valueNode = (ValueNode) node;
      return SerializationUtil.coerceFromNode(valueNode, fieldName);
    } else {
      throw new IllegalStateException(
          String.format("%s%s: failed to process scalar property for type[%s], field[%s]", nodePath, fieldName));
    }
  }

  private static Object readComplexValue(final ObjectNode objectNode, final String fieldName, Field field, final String nodePath,
      final String indent) {
    final Class<?> fieldType = SerializationUtil.getValueType(field);
    if (AwsComplexType.class.isAssignableFrom(fieldType)) {
      try {
        final AwsComplexType value = (AwsComplexType) fieldType.getConstructor().newInstance();
        return deserializeComplexType(value, objectNode, SerializationUtil.getTypeDefinition(fieldType), nodePath + fieldName + "/",
            indent + "  ");
      } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
          | SecurityException e) {
        throw new IllegalStateException(
            String.format("%s%s: failed to instantiate new type[%s]", nodePath, fieldName, fieldType.getTypeName()), e);
      }
    } else {
      throw new IllegalStateException(String.format("%s%s: object node not appropriate for expected property type[%s], field[%s]",
          nodePath, fieldName, fieldType.getTypeName(), field.getName()));
    }
  }

  private static AwsComplexType deserializeCustomEncoding(final AwsComplexType value, final String encoded,
      final AwsTypeDefinition typeDefinition) {
    final String valueTypeName = value.getClass().getTypeName();
    String remaining = encoded;
    final CustomTypeEncoding typeEncoding = typeDefinition.encoding();
    /*
     * check and consume the prefix
     */
    if (!typeEncoding.prefix().isEmpty()) {
      remaining = consumeRequiredTokenAtStart(remaining, typeEncoding.prefix(), () -> {
        return String.format("encoded[%s] must have prefix[%s]", valueTypeName, typeEncoding.prefix());
      });
    }
    /*
     * check and consume the suffix
     */
    if (!typeEncoding.suffix().isEmpty()) {
      remaining = consumeRequiredTokenAtEnd(remaining, typeEncoding.suffix(), () -> {
        return String.format("encoded[%s] must have suffix[%s]", valueTypeName, typeEncoding.suffix());
      });
    }
    /*
     * decode each field in the order specified in the computed field order
     */
    final Map<String, Pair<Field, AwsFieldProperty>> fieldProperties = SerializationUtil
        .getHierarchyFieldsWithTypeProperty(value.getClass());
    final String[] fieldOrder = SerializationUtil.computeFieldOrder(typeDefinition, fieldProperties);
    /*
     * split the remaining encoded message into encoded fields (the number of fields
     * must match the number of entries in the computed field order)
     */
    final String[] encodedFields = remaining.split(SerializationUtil.createSeparatorPattern(typeEncoding.fieldSep()));
    /*
     * create a map of field name and encoded field value using the field order if
     * no key value separator is specified
     */
    final Map<String, String> encodedFieldMap = new HashMap<>();
    final String keyValSep = typeEncoding.keyValSep();

    Streams.forEachPair(Stream.of(encodedFields), IntStream.range(0, encodedFields.length).boxed(), (encodedField, i) -> {
      if (!keyValSep.isEmpty()) {
        int keyValSepIndex = encodedField.indexOf(keyValSep);
        if (keyValSepIndex > 0) {
          encodedFieldMap.put(encodedField.substring(0, keyValSepIndex).trim(),
              encodedField.substring(keyValSepIndex + keyValSep.length()).trim());
        } else {
          throw new IllegalArgumentException(
              String.format("no key/value separator[%s] found in entry[%s] for type[%s]", keyValSep, encodedField, valueTypeName));
        }
      } else {
        encodedFieldMap.put(fieldOrder[i], encodedField);
      }
    });

    /*
     * iterate over the fields that are to be decoded
     */
    String encodedField = null;
    for (final String fieldName : fieldOrder) {
      /*
       * skip over any fields that the preprocessor has determined are to be ignored
       */
//      if (!ignoredField) { // due to preprocessor directives

      final Pair<Field, AwsFieldProperty> fieldProperty = fieldProperties.get(fieldName);
      final Field field = fieldProperty.getKey();
      final AwsFieldProperty property = fieldProperty.getValue();
      final Class<?> fieldType = field.getType();
      final boolean repeated = List.class.isAssignableFrom(fieldType);

      try {
        if (keyValSep.isEmpty()) {
          // TODO: work out wtf is going on here
        } else {
          /*
           * retrieve the encoded field and take action if it's a missing required field
           */
          encodedField = encodedFieldMap.get(fieldName);
          if (encodedField == null && property.required()) {
            throw new IllegalArgumentException(
                String.format("failed to find an entry for required field[%s] in type[%s]", fieldName, valueTypeName));
          }
          if (encodedField != null) {
            final FieldEncodingType fieldEncoding = property.encoding();
            if (fieldEncoding == FieldEncodingType.XML_URLENCODED || fieldEncoding == FieldEncodingType.JSON_URLENCODED) {
              encodedField = URLDecoder.decode(encodedField, "UTF-8").trim();
            } else {
              encodedField = encodedField.trim();
            }
            if (encodedField.startsWith("\"") && encodedField.endsWith("\"")) {
              encodedField = encodedField.substring(1, encodedField.length() - 1);
            }
            if (fieldEncoding != FieldEncodingType.UNKNOWN) {
              if (fieldEncoding == FieldEncodingType.XML || fieldEncoding == FieldEncodingType.XML_URLENCODED) {
                final JAXBContext jaxbContext = SerializationUtil.getJaxbContextFor((Class<?>) (repeated ? List.class : fieldType));
                final InputStream xmlStream = new java.io.ByteArrayInputStream(encodedField.getBytes());
                final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                SerializationUtil.setValue(valueTypeName, unmarshaller.unmarshal(xmlStream), field);
                // SerializationUtil.setValue(com.avaloq.front.common.util.SafeXMLUtils.safeUnmarshal(xmlStream,
                // jaxbContext), field);
              } else if (fieldEncoding == FieldEncodingType.JSON || fieldEncoding == FieldEncodingType.JSON_URLENCODED) {
                SerializationUtil.setValue(value,
                    objectMapper.readValue(encodedField, (Class<?>) (repeated ? List.class : fieldType)), field);
              } else if (fieldEncoding == FieldEncodingType.BASE64) {
                final Base64 base64Url = new Base64(true);
                encodedField = new String(base64Url.decode(encodedField));
                SerializationUtil.setValue(value,
                    objectMapper.readValue(encodedField, (Class<?>) (repeated ? List.class : fieldType)), field);
              }
            } else {
              // Try to dynamically infer the encoding from the contents
              if (encodedField.startsWith("{") || encodedField.startsWith("[")) {
                SerializationUtil.setValue(value,
                    objectMapper.readValue(encodedField, (Class<?>) (repeated ? List.class : fieldType)), field);
              } else if (encodedField.startsWith("<")) {
                javax.xml.bind.JAXBContext jaxbContext = SerializationUtil
                    .getJaxbContextFor((Class<?>) (repeated ? List.class : fieldType));
                java.io.InputStream xmlStream = new java.io.ByteArrayInputStream(encodedField.getBytes());
                final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                SerializationUtil.setValue(valueTypeName, unmarshaller.unmarshal(xmlStream), field);
                // SerializationUtil.setValue(com.avaloq.front.common.util.SafeXMLUtils.safeUnmarshal(xmlStream,
                // jaxbContext), field);
              } else {
                if (!AwsComplexType.class.isAssignableFrom(fieldType)) {
                  SerializationUtil.setValue(valueTypeName, SerializationUtil.coerceFromString(encodedField, fieldType), field);
                } else {
                  throw new IllegalArgumentException(String
                      .format("unable to deserialize an instance of type[%s] from encoded field[%s]", valueTypeName, encodedField));
                }
              }
            }
          }

        }

      } catch (Exception e) {
        if (e instanceof IllegalArgumentException) {
          throw (IllegalArgumentException) e;
        } else {
          throw new IllegalArgumentException(
              String.format("failed to decode property[%s] for type[%s], field[%s]", encodedField, valueTypeName, fieldName), e);
        }
      }

//      } // end of ignoredField
    }
    return value;
  }

  private static String consumeRequiredTokenAtStart(final String encoded, final String token, Supplier<String> exceptionMessage) {
    if (encoded.startsWith(token)) {
      return encoded.substring(token.length());
    }
    throw new IllegalStateException(exceptionMessage.get());
  }

  private static String consumeRequiredTokenAtEnd(final String encoded, final String token, Supplier<String> exceptionMessage) {
    if (encoded.endsWith(token)) {
      return encoded.substring(0, encoded.length() - token.length());
    }
    throw new IllegalStateException(exceptionMessage.get());
  }

}
