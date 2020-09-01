package com.langtoun.messages.types;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.AwsComplexTypeJsonDeserializer;
import com.langtoun.messages.generic.AwsComplexTypeJsonSerializer;

/**
 * Base class for objects that are (de-)serializable with
 * {@link AwsComplexTypeJsonDeserializer} and {@link AwsComplexTypeJsonSerializer}.
 *
 */
@JsonSerialize(using = AwsComplexTypeJsonSerializer.class)
@JsonDeserialize(using = AwsComplexTypeJsonDeserializer.class)
public class AwsComplexType {

  protected AwsComplexType() {
    // protected base class constructor
  }

}
