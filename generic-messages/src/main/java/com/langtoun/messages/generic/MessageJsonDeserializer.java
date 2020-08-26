package com.langtoun.messages.generic;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
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
      System.out.println("signature = " + javaType.getGenericSignature());
      System.out.println("javaType  = " + javaType.toString());
      System.out.println("payload   = " + payload.getClass());

      int i = 0;
      for (final MessageProperty property : payload.getProperties()) {
        System.out.println("property[" + (i++) + "] = " + property);
      }
      System.out.println("<end>");

      return deserializePayload(payload, rootNode, context);
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
      final DeserializationContext context) {
    for (final MessageProperty property : payload.getProperties()) {
      // find each property in turn and populate the payload, creating new payload
      // objects as the deserializer walks the node tree
      final JsonNode field = root.findValue(property.getJsonName());
      if (property instanceof ListProperty) {
        if (field.isArray()) {
          final ListProperty listProperty = (ListProperty) property;
          // deserialize the array values
        } else {
          throw new IllegalStateException("failed to process list property " + property.getJsonName());
        }
      } else if (property instanceof ScalarProperty) {
        final ScalarProperty scalarProperty = (ScalarProperty) property;
        // deserialize the scalar value (may be simple or complex)
      } else {
        throw new IllegalStateException("failed to process property " + property.getJsonName());
      }
    }
    return payload;
  }

}
