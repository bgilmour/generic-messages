package com.langtoun.messages.generic;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.langtoun.messages.annotations.TypeProperty;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.util.SerializationUtil;

/**
 * JSON deserializer for types that implement the {@link SerializablePayload}
 * interface.
 *
 */
public class PayloadJsonDeserializer extends JsonDeserializer<SerializablePayload> implements ContextualDeserializer {

  private JavaType javaType;

  public PayloadJsonDeserializer() {

  }

  public PayloadJsonDeserializer(final JavaType javaType) {
    this.javaType = javaType;
  }

  @Override
  public JsonDeserializer<?> createContextual(final DeserializationContext context, final BeanProperty property)
      throws JsonMappingException {
    final JavaType javaType = context.getContextualType() != null ? context.getContextualType() : property.getMember().getType();
    return new PayloadJsonDeserializer(javaType);
  }

  @Override
  public SerializablePayload deserialize(final JsonParser parser, final DeserializationContext context)
      throws IOException, JsonProcessingException {
    System.out.println("deserialize : " + javaType.getTypeName());
    try {
      final JsonNode rootNode = parser.getCodec().readTree(parser);
      final SerializablePayload payload = (SerializablePayload) javaType.getRawClass().getConstructor().newInstance();
      return deserializePayload(payload, rootNode, "/", "  ");
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
        | SecurityException e) {
      throw new IllegalArgumentException(String.format("unable to deserialize an instance of type[%s]", javaType.getTypeName()), e);
    }
  }

  private static SerializablePayload deserializePayload(final SerializablePayload payload, final JsonNode root,
      final String nodePath, final String indent) {
    for (final Entry<Field, TypeProperty> fieldProperty : SerializationUtil.getFieldProperties(payload).entrySet()) {
      final Field field = fieldProperty.getKey();
      final TypeProperty property = fieldProperty.getValue();
      // find each property in turn and populate the payload, creating new payload
      // objects as the deserializer walks the node tree
      final String fieldName = property.jsonName();
      final Class<?> fieldType = field.getType();
      final JsonNode jsonField = root.findValue(property.jsonName());
      if (field != null) {
        if (List.class.isAssignableFrom(fieldType)) {
          if (jsonField instanceof ArrayNode) {
            System.out.println(indent + fieldName + "[]");
            SerializationUtil.setValue(payload, readArrayValues((ArrayNode) jsonField, field, property, nodePath, indent + "  "),
                field);
          } else {
            throw new IllegalStateException(String.format("%s%s: failed to process list property for type[%s], field[%s]", nodePath,
                fieldName, payload.getClass().getTypeName(), field.getName()));
          }
        } else {
          // deserialize the scalar value (may be simple or complex)
          SerializationUtil.setValue(payload, readScalarValue(jsonField, fieldName, field, property, nodePath, indent), field);
        }
      } else if (property.required()) {
        System.out.println(indent + fieldName + " - required (not present)");
        throw new IllegalStateException(String.format("%s%s: missing required property for type[%s], field[%s]", nodePath,
            fieldName, payload.getClass().getTypeName(), field.getName()));
      } else {
        System.out.println(indent + fieldName + " - optional (not present)");
      }
    }
    return payload;
  }

  private static List<Object> readArrayValues(final ArrayNode arrayNode, final Field field, final TypeProperty property,
      final String nodePath, final String indent) {
    final String fieldName = property.jsonName();
    final Iterator<JsonNode> iter = arrayNode.elements();
    if (iter.hasNext()) {
      int i = 0;
      List<Object> items = new ArrayList<>();
      while (iter.hasNext()) {
        final JsonNode node = iter.next();
        final String itemName = fieldName + "[" + i + "]";
        items.add(readScalarValue(node, itemName, field, property, nodePath, indent));
        i++;
      }
      return items;
    }
    return null;
  }

  private static Object readScalarValue(final JsonNode node, final String fieldName, Field field, TypeProperty property,
      final Class<?> valueType, final String nodePath, final String indent) {
    if (node instanceof ObjectNode) {
      System.out.println(indent + fieldName + "{}");
      return readComplexValue((ObjectNode) node, fieldName, valueType, nodePath, indent);
    } else if (node instanceof ValueNode) {
      System.out.println(indent + fieldName);
      final ValueNode valueNode = (ValueNode) node;
      return readSimpleValue(valueNode, fieldName);
    } else {
      throw new IllegalStateException(
          String.format("%s%s: failed to process scalar property for type[%s], field[%s]", nodePath, fieldName));
    }
  }

  private static Object readComplexValue(final ObjectNode objectNode, final String fieldName, Field field, final String nodePath,
      final String indent) {
    final Class<?> valueType = field.getType();
    if (SerializablePayload.class.isAssignableFrom(valueType)) {
      try {
        return deserializePayload((SerializablePayload) valueType.getConstructor().newInstance(), objectNode,
            nodePath + fieldName + "/", indent + "  ");
      } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
          | SecurityException e) {
        throw new IllegalStateException(
            nodePath + fieldName + "%s%s: failed to instantiate payload object of type: " + valueType.getTypeName(), e);
      }
    } else {
      throw new IllegalStateException(String.format(": object node not appropriate for expected property type[%s], field[%s]",
          nodePath, fieldName, valueType.getTypeName(), field.getName()));
    }
  }

  private static Object readSimpleValue(final ValueNode valueNode, final String fieldName) {
    if (valueNode.isNull()) {
      return null;
    } else if (valueNode.isIntegralNumber()) {
      return valueNode.asInt();
    } else if (valueNode.isFloatingPointNumber()) {
      return valueNode.asDouble();
    } else if (valueNode.isBoolean()) {
      return valueNode.asBoolean();
    } else {
      return valueNode.asText();
    }
  }

}
