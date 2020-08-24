package com.langtoun.messages.generic;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = Message.Serializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message<T extends SerializablePayload> {

  private T payload;

  private Message() {
    // create using factory method
  }

  public T getPayload() {
    return payload;
  }

  public static <T extends SerializablePayload> Message<T> newMessage(T payload) {
    final Message<T> message = new Message<T>();
    message.payload = payload;
    return message;
  }

  static class Serializer extends JsonSerializer<Message<SerializablePayload>> {

    @Override
    public void serialize(Message<SerializablePayload> value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
      serialize(value.getPayload(), gen, serializers);
    }

    private static void serialize(SerializablePayload value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value != null) {
        gen.writeStartObject();
        for (MessageProperty property : value.getProperties()) {
          final boolean repeated = property instanceof ListProperty;
          if (repeated) {
            final ListProperty listProperty = (ListProperty) property;
            writeArrayValues(listProperty.getJsonName(), null, listProperty.isRequired(), gen);
          } else {
            final ScalarProperty scalarProperty = (ScalarProperty) property;
            final Object propertyValue = scalarProperty.getGetter().get();
            if (propertyValue instanceof SerializablePayload) {
              gen.writeFieldName(scalarProperty.getJsonName());
              serialize((SerializablePayload) propertyValue, gen, serializers);
            } else {
              writeScalarValue(property.getJsonName(), propertyValue, property.isRequired(), gen);
            }
          }
        }
        gen.writeEndObject();
      }
    }

    private static void writeScalarValue(String fieldName, Object value, boolean required, JsonGenerator gen) throws IOException {
      if (value == null && required) {
        // write a null
        gen.writeNullField(fieldName);
      } else if (value != null) {
        // write the value
        gen.writeFieldName(fieldName);
        writeScalarValue(value, gen);
      }
    }

    private static void writeScalarValue(Object value, JsonGenerator gen) throws IOException {
      if (value instanceof String) {
        gen.writeString((String) value);
      } else if (value instanceof Integer) {
        gen.writeNumber((Integer) value);
      } else if (value instanceof Double) {
        gen.writeNumber((Double) value);
      } else {
        throw new IOException("attempt to serialize unknown type: " + value.getClass().getSimpleName());
      }
    }

    @SuppressWarnings("unused")
    private static void writeArrayValues(List<Object> array, boolean required, JsonGenerator gen) throws IOException {
      writeArrayValues(null, array, required, gen);
    }

    private static void writeArrayValues(String fieldName, List<Object> array, boolean required, JsonGenerator gen)
        throws IOException {
      if (required || (array != null && !array.isEmpty())) {
        if (fieldName != null) {
          gen.writeFieldName(fieldName);
        }
        gen.writeStartArray();
      }
      if (array != null) {
        for (Object item : array) {
          // write the item
          if (item == null) {
            gen.writeNull();
          } else {
            writeScalarValue(item, gen);
          }
        }
      }
      if (required || (array != null && !array.isEmpty())) {
        gen.writeEndArray();
      }
    }

  }

  class Deserializer extends JsonDeserializer<SerializablePayload> {

    @Override
    public SerializablePayload deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      return null;
    }

  }

}
