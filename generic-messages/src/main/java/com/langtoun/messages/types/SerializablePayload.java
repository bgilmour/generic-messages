package com.langtoun.messages.types;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.types.properties.PayloadProperty;

/**
 * Interface for payload types that are to be handled by generic serializers and
 * deserializers.
 */
@JsonSerialize(using = PayloadJsonSerializer.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class)
public interface SerializablePayload {

  static final CustomEncodingContext DEFAULT_CONTEXT = CustomEncodingContext.Builder.newBuilder().build();

  /**
   * Provide the configuration for the properties that are to be transported in
   * the generic message.
   *
   * @return a list of {@link PayloadProperty} objects
   */
  List<PayloadProperty> getProperties();

  /**
   * Is the top level type a list.
   * 
   * @return {@code true} is the type is a list, defaults to {@code false}
   */
  default boolean isListType() { return false; }

  /**
   * Retrieve a context object containing the prefix, suffix, and separators to be
   * used when processing a custom encoding.
   * 
   * @return a context object if a custom encoding is to be used, defaults to
   *         {@code null}
   */
  default CustomEncodingContext getCustomEncodingContext() { return DEFAULT_CONTEXT; }

  /**
   * Retrieve the name of the type encoding that will guide the encoding process.
   * 
   * @return the name of the type encoding, defaults to {@code null}
   */
  default String getTypeEncoding() { return null; }

}
