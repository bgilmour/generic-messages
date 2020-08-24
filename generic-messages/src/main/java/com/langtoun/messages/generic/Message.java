package com.langtoun.messages.generic;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.langtoun.messages.generic.Message.MessageJsonDeserializer;
import com.langtoun.messages.generic.Message.MessageJsonSerializer;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.util.JsonSerializationUtil;

/**
 * Generic message container.
 *
 * @param <T> the generic type representing the payload
 */
@JsonSerialize(using = MessageJsonSerializer.class)
@JsonDeserialize(using = MessageJsonDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message<T extends SerializablePayload> {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final TypeFactory TYPE_FACTORY;

  static {
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    TYPE_FACTORY = OBJECT_MAPPER.getTypeFactory();
  }

  private T payload;

  private Message() {
    // create using factory method
  }

  public static <T extends SerializablePayload> Message<T> from(final T payload) {
    final Message<T> message = new Message<>();
    message.payload = payload;
    return message;
  }

  public static <T extends SerializablePayload> Message<T> from(final String json, TypeReference<Message<T>> typeReference) {
    // final Message<T> message = new Message<>();
    try {
      return OBJECT_MAPPER.readValue(json, typeReference);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("unable to de-serialize an instance of " + typeReference.toString());
    }
    // return message;
  }

  public T getPayload() {
    return payload;
  }

  public void setPayload(T payload) {
    this.payload = payload;
  }

  static class MessageJsonSerializer extends JsonSerializer<Message<SerializablePayload>> {

    @Override
    public void serialize(final Message<SerializablePayload> value, final JsonGenerator gen, final SerializerProvider serializers)
        throws IOException {
      JsonSerializationUtil.serialize(value.getPayload(), gen, serializers);
    }

  }

  static class MessageJsonDeserializer extends JsonDeserializer<Message<SerializablePayload>> {

    @Override
    public Message<SerializablePayload> deserialize(final JsonParser parser, final DeserializationContext context)
        throws IOException, JsonProcessingException {
      JsonNode node = parser.getCodec().readTree(parser);

      JavaType jt = TYPE_FACTORY.constructType(SerializablePayload.class);
      System.out.println("signature = " + jt.getGenericSignature());

      // SerializablePayload object =

      // return OBJECT_MAPPER.readValue(node.asText(), SerializablePayload.class);

      final Message<SerializablePayload> message = new Message<>();

      return message;
    }

  }

}
