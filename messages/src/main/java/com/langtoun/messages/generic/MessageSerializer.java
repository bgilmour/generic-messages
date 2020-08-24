package com.langtoun.messages.generic;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.langtoun.messages.properties.ListProperty;
import com.langtoun.messages.properties.MessageProperty;
import com.langtoun.messages.properties.ScalarProperty;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.util.SerializationUtil;

public class MessageSerializer extends JsonSerializer<Message<SerializablePayload>> {

  @Override
  public void serialize(Message<SerializablePayload> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    serialize(value.getPayload(), gen, serializers);
  }

  private static void serialize(SerializablePayload value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value != null) {
      gen.writeStartObject();
      for (MessageProperty property : value.getProperties()) {
        final boolean repeated = property instanceof ListProperty;
        if (repeated) {
          final ListProperty listProperty = (ListProperty) property;
          SerializationUtil.writeArrayValues(listProperty.getJsonName(), null, listProperty.isRequired(), gen);
        } else {
          final ScalarProperty scalarProperty = (ScalarProperty) property;
          final Object propertyValue = scalarProperty.getGetter().get();
          if (propertyValue instanceof SerializablePayload) {
            gen.writeFieldName(scalarProperty.getJsonName());
            serialize((SerializablePayload) propertyValue, gen, serializers);
          } else {
            SerializationUtil.writeScalarValue(property.getJsonName(), propertyValue, property.isRequired(), gen);
          }
        }
      }
      gen.writeEndObject();
    }
  }

}
