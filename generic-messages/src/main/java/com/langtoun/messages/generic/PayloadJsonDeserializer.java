package com.langtoun.messages.generic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.ListProperty;
import com.langtoun.messages.types.properties.PayloadProperty;
import com.langtoun.messages.types.properties.ScalarProperty;

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
  public SerializablePayload deserialize(final JsonParser parser, final DeserializationContext context)
      throws IOException, JsonProcessingException {
    System.out.println("deserialize : " + javaType.getTypeName());
    try {
      final JsonNode rootNode = parser.getCodec().readTree(parser);
      final SerializablePayload payload = (SerializablePayload) javaType.getRawClass().getConstructor().newInstance();
      return deserializePayload(payload, rootNode, "/", "  ");
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
        | SecurityException e) {
      throw new IllegalArgumentException("unable to deserialize an instance of " + javaType.getTypeName(), e);
    }
  }

  @Override
  public JsonDeserializer<?> createContextual(final DeserializationContext context, final BeanProperty property)
      throws JsonMappingException {
    final JavaType javaType = context.getContextualType() != null ? context.getContextualType() : property.getMember().getType();
    return new PayloadJsonDeserializer(javaType);
  }

  private static SerializablePayload deserializePayload(final SerializablePayload payload, final JsonNode root,
      final String nodePath, final String indent) {
    for (final PayloadProperty property : payload.getProperties()) {
      // find each property in turn and populate the payload, creating new payload
      // objects as the deserializer walks the node tree
      final String fieldName = property.getJsonName();
      final Class<?> valueType = property.getValueType();
      final JsonNode field = root.findValue(fieldName);
      if (field != null) {
        if (property instanceof ListProperty) {
          if (field instanceof ArrayNode) {
            System.out.println(indent + fieldName + "[]");
            final ListProperty listProperty = (ListProperty) property;
            listProperty.getSetter().accept(readArrayValues((ArrayNode) field, listProperty, nodePath, indent + "  "));
          } else {
            throw new IllegalStateException(nodePath + fieldName + ": failed to process list property");
          }
        } else if (property instanceof ScalarProperty) {
          final ScalarProperty scalarProperty = (ScalarProperty) property;
          // deserialize the scalar value (may be simple or complex)
          scalarProperty.getSetter().accept(readScalarValue(field, fieldName, valueType, nodePath, indent));
        } else {
          throw new IllegalStateException(nodePath + fieldName + ": failed to process property");
        }
      } else if (property.isRequired()) {
        System.out.println(indent + fieldName + " - required (not present)");
        throw new IllegalStateException(nodePath + fieldName + ": missing required property");
      } else {
        System.out.println(indent + fieldName + " - optional (not present)");
      }
    }
    return payload;
  }

  private static List<Object> readArrayValues(final ArrayNode arrayNode, final ListProperty property, final String nodePath,
      final String indent) {
    final String fieldName = property.getJsonName();
    final Class<?> itemType = property.getItemType();
    final Iterator<JsonNode> iter = arrayNode.elements();
    if (iter.hasNext()) {
      int i = 0;
      List<Object> items = new ArrayList<>();
      while (iter.hasNext()) {
        final JsonNode node = iter.next();
        final String itemName = fieldName + "[" + i + "]";
        items.add(readScalarValue(node, itemName, itemType, nodePath, indent));
        i++;
      }
      return items;
    }
    return null;
  }

  private static Object readScalarValue(final JsonNode node, final String fieldName, final Class<?> valueType,
      final String nodePath, final String indent) {
    if (node instanceof ObjectNode) {
      System.out.println(indent + fieldName + "{}");
      return readComplexValue((ObjectNode) node, fieldName, valueType, nodePath, indent);
    } else if (node instanceof ValueNode) {
      System.out.println(indent + fieldName);
      final ValueNode valueNode = (ValueNode) node;
      return readSimpleValue(valueNode, fieldName, valueType);
    } else {
      throw new IllegalStateException(nodePath + fieldName + ": failed to process scalar property");
    }
  }

  private static Object readComplexValue(final ObjectNode objectNode, final String fieldName, final Class<?> valueType,
      final String nodePath, final String indent) {
    if (SerializablePayload.class.isAssignableFrom(valueType)) {
      try {
        return deserializePayload((SerializablePayload) valueType.getConstructor().newInstance(), objectNode,
            nodePath + fieldName + "/", indent + "  ");
      } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
          | SecurityException e) {
        throw new IllegalStateException(
            nodePath + fieldName + ": failed to instantiate payload object of type: " + valueType.getCanonicalName(), e);
      }
    } else {
      throw new IllegalStateException(
          nodePath + fieldName + ": object node not appropriate for expected property type: " + valueType.getCanonicalName());
    }
  }

  private static Object readSimpleValue(final ValueNode valueNode, final String fieldName, final Class<?> valueType) {
    if (valueNode.isNull()) {
      return null;
    } else if (valueNode.isIntegralNumber() && Integer.class.isAssignableFrom(valueType)) {
      return valueNode.asInt();
    } else if (valueNode.isFloatingPointNumber() && Double.class.isAssignableFrom(valueType)) {
      return valueNode.asDouble();
    } else if (valueNode.isBoolean() && Boolean.class.isAssignableFrom(valueType)) {
      return valueNode.asBoolean();
    } else {
      return valueNode.asText();
    }
  }

}
