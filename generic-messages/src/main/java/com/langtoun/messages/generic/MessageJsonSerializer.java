package com.langtoun.messages.generic;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.util.JsonSerializationUtil;

/**
 * JSON serializer for generic messages that works with
 * {@link SerializablePayload} objects.
 */
public class MessageJsonSerializer extends JsonSerializer<Message<SerializablePayload>> {

  @Override
  public void serialize(final Message<SerializablePayload> value, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    JsonSerializationUtil.serialize(value.getPayload(), gen, serializers);
  }

}
