package com.langtoun.messages.types;

import java.util.BitSet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.AwsComplexTypeJsonDeserializer;
import com.langtoun.messages.generic.AwsComplexTypeJsonSerializer;

/**
 * Base class for objects that are (de-)serializable with
 * {@link AwsComplexTypeJsonDeserializer} and
 * {@link AwsComplexTypeJsonSerializer}.
 *
 */
@JsonSerialize(using = AwsComplexTypeJsonSerializer.class)
@JsonDeserialize(using = AwsComplexTypeJsonDeserializer.class)
public class AwsComplexType {

  BitSet bitMask = new BitSet();

  protected AwsComplexType() {
    // protected base class constructor
  }

  public BitSet getBitMask() { return bitMask; }

  public void setBitMask(BitSet bitMask) { this.bitMask = bitMask; }

  public void setBitMaskField(int index) {
    bitMask.set(index);
  }

}
