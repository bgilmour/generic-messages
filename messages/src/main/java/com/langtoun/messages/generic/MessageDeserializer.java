package com.langtoun.messages.generic;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.langtoun.messages.types.SerializablePayload;

class MessageDeserializer extends JsonDeserializer<SerializablePayload> {

  @Override
  public SerializablePayload deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    return null;
  }

}
