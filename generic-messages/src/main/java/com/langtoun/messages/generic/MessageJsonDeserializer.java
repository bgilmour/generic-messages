package com.langtoun.messages.generic;

import java.io.IOException;
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
import com.langtoun.messages.types.properties.MessageProperty;
import com.langtoun.messages.types.properties.ScalarProperty;

/**
 * JSON deserializer for types that implement the {@link SerializablePayload}
 * interface.
 *
 */
public class MessageJsonDeserializer extends JsonDeserializer<SerializablePayload> implements ContextualDeserializer {

  private JavaType javaType;

  public MessageJsonDeserializer() {

  }

  public MessageJsonDeserializer(final JavaType javaType) {
    this.javaType = javaType;
  }

  @Override
  public SerializablePayload deserialize(final JsonParser parser, final DeserializationContext context)
      throws IOException, JsonProcessingException {
    final JsonNode rootNode = parser.getCodec().readTree(parser);

    try {
      final SerializablePayload payload = (SerializablePayload) javaType.getRawClass().newInstance();

      System.out.println("<begin>");
      System.out.println("  signature = " + javaType.getGenericSignature());
      System.out.println("  javaType  = " + javaType.toString());
      System.out.println("  payload   = " + payload.getClass());
      System.out.println("  payload   = " + payload.getClass().getTypeName());
      System.out.println("  properties:");
      int i = 0;
      for (final MessageProperty property : payload.getProperties()) {
        System.out.println("    property[" + (i++) + "] = " + property);
      }
      System.out.println("<end>");
      System.out.println("deserialize : " + javaType.getTypeName());
      return deserializePayload(payload, rootNode, "/", "  ", context);
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalArgumentException("unable to de-serialize an instance of " + javaType.getTypeName());
    }
  }

  @Override
  public JsonDeserializer<?> createContextual(final DeserializationContext context, final BeanProperty property)
      throws JsonMappingException {
    final JavaType javaType = context.getContextualType() != null ? context.getContextualType() : property.getMember().getType();
    return new MessageJsonDeserializer(javaType);
  }

  private static SerializablePayload deserializePayload(final SerializablePayload payload, final JsonNode root,
      final String nodePath, final String indent, final DeserializationContext context) {
    for (final MessageProperty property : payload.getProperties()) {
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
            final Class<?> itemType = listProperty.getItemType();
            // deserialize the array values
            final ArrayNode arrayNode = (ArrayNode) field;
            final List<Object> items = new ArrayList<>();
            final Iterator<JsonNode> iter = arrayNode.elements();
            while (iter.hasNext()) {
              JsonNode node = iter.next();
              try {
                items.add(itemType.newInstance());
              } catch (InstantiationException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            }
            listProperty.getSetter().accept(items);
          } else {
            throw new IllegalStateException(nodePath + fieldName + ": failed to process list property");
          }
        } else if (property instanceof ScalarProperty) {
          final ScalarProperty scalarProperty = (ScalarProperty) property;
          // deserialize the scalar value (may be simple or complex)
          if (field instanceof ObjectNode) {
            System.out.println(indent + fieldName + "{}");
            final ObjectNode objectNode = (ObjectNode) field;
            if (SerializablePayload.class.isAssignableFrom(valueType)) {
              try {
                scalarProperty.getSetter().accept(deserializePayload((SerializablePayload) valueType.newInstance(), field,
                    nodePath + fieldName + "/", indent + "  ", context));
              } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(
                    nodePath + fieldName + ": failed to instantiate payload object of type: " + valueType.getCanonicalName());
              }
            } else {
              throw new IllegalStateException(nodePath + fieldName + ": object node not appropriate for expected property type: "
                  + valueType.getCanonicalName());
            }
          } else if (field instanceof ValueNode) {
            System.out.println(indent + fieldName);
            final ValueNode valueNode = (ValueNode) field;
//          valueNode.
          } else {
            throw new IllegalStateException(nodePath + fieldName + ": failed to process scalar property");
          }
        } else {
          throw new IllegalStateException(nodePath + fieldName + ": failed to process property");
        }
      } else if (property.isRequired()) {

      } else {
        System.out.println(indent + fieldName + " - optional (not present)");
      }
    }
    return payload;
  }

}
