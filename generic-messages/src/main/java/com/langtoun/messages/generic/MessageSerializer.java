package com.langtoun.messages.generic;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.util.SerializationUtil;

public class MessageSerializer extends JsonSerializer<Message<SerializablePayload>> {

  @Override
  public void serialize(Message<SerializablePayload> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    SerializationUtil.serialize(value.getPayload(), gen, serializers);
  }

}
