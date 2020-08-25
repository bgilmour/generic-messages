package com.langtoun.messages.runtime.generic;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.langtoun.messages.compilation.properties.ListProperty;
import com.langtoun.messages.compilation.properties.MessageProperty;
import com.langtoun.messages.compilation.properties.ScalarProperty;
import com.langtoun.messages.compilation.types.SerializablePayload;

/**
 * Utility class used by the generic message JSON deserializer.
 *
 */
public class JsonDeserializationUtil {

  private JsonDeserializationUtil() {
    // static utility class
  }

  public static SerializablePayload deserialize(final SerializablePayload payload, JsonNode node,
      final DeserializationContext context) {
    for (MessageProperty property : payload.getProperties()) {
      // find each property in turn and populate the payload, creating new payload
      // objects as the deserializer walks the node tree
      final JsonNode field = node.findValue(property.getJsonName());
      if (property instanceof ListProperty) {
        if (node.isArray()) {
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
