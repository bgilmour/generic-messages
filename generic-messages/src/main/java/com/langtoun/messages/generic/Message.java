package com.langtoun.messages.generic;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.langtoun.messages.generic.Message.MessageJsonDeserializer;
import com.langtoun.messages.generic.Message.MessageJsonSerializer;
import com.langtoun.messages.properties.MessageProperty;
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

  static {
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private T payload;

  protected Message() {
    // create using factory method
  }

  public T getPayload() {
    return payload;
  }

  public void setPayload(final T payload) {
    this.payload = payload;
  }

  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  public static <T extends SerializablePayload> Message<T> from(final T payload) {
    final Message<T> message = new Message<>();
    message.payload = payload;
    return message;
  }

  public static <T extends SerializablePayload> Message<T> from(final String json, final TypeReference<Message<T>> typeReference) {
    try {
      return OBJECT_MAPPER.readValue(json, typeReference);
    } catch (final JsonProcessingException e) {
      throw new IllegalArgumentException("unable to de-serialize an instance of " + typeReference.getType());
    }
  }

  static class MessageJsonSerializer extends JsonSerializer<Message<SerializablePayload>> {

    @Override
    public void serialize(final Message<SerializablePayload> value, final JsonGenerator gen, final SerializerProvider serializers)
        throws IOException {
      JsonSerializationUtil.serialize(value.getPayload(), gen, serializers);
    }

  }

  static class MessageJsonDeserializer extends JsonDeserializer<Message<SerializablePayload>> implements ContextualDeserializer {

    private JavaType javaType;

    public MessageJsonDeserializer() {

    }

    public MessageJsonDeserializer(final JavaType javaType) {
      this.javaType = javaType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Message<SerializablePayload> deserialize(final JsonParser parser, final DeserializationContext context)
        throws IOException, JsonProcessingException {
      @SuppressWarnings("unused")
      final JsonNode node = parser.getCodec().readTree(parser);

      try {
        final Message<SerializablePayload> message = (Message<SerializablePayload>) javaType.getRawClass().newInstance();
        final SerializablePayload payload = (SerializablePayload) javaType.containedType(0).getRawClass().newInstance();

        System.out.println("<begin>");
        System.out.println("signature = " + javaType.getGenericSignature());
        System.out.println("javaType  = " + javaType.toString());
        System.out.println("class     = " + message.getClass());
        System.out.println("payload   = " + payload.getClass());

        int i = 0;
        for (MessageProperty property : payload.getProperties()) {
          System.out.println("property[" + (i++) + "] = " + property);
        }
        System.out.println("<end>");

        message.setPayload(null /* deserialize(node) */);
        return message;
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

  }

}
