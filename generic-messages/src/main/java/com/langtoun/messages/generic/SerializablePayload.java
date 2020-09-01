package com.langtoun.messages.generic;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Interface for payload types that are to be handled by generic serializers and
 * deserializers.
 */
@JsonSerialize(using = PayloadJsonSerializer.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class)
public interface SerializablePayload {

}
