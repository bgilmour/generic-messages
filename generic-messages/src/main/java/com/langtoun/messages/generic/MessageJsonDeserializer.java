package com.langtoun.messages.generic;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.langtoun.messages.types.SerializablePayload;

/**
 * JSON de-serializer for generic messages that works with
 * {@link SerializablePayload} objects.
 */
class MessageJsonDeserializer extends JsonDeserializer<SerializablePayload> {

  @Override
  public SerializablePayload deserialize(final JsonParser parser, final DeserializationContext context)
      throws IOException, JsonProcessingException {
    return null;
  }

}
