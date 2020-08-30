package com.langtoun.messages.types.gen.cars;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.annotations.CustomTypeEncoding;
import com.langtoun.messages.annotations.TypeProperty;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.types.FieldEncodingType;
import com.langtoun.messages.types.SerializablePayload;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}
 * and is annotated with {@link CustomTypeEncoding} and {@link TypeProperty}.
 *
 */
@JsonSerialize(using = PayloadJsonSerializer.class, as = CustomSimpleCar.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = CustomSimpleCar.class)
@CustomTypeEncoding(prefix = "<<<", suffix = ">>>", fieldSep = "|", keyValSep = "=")
public class CustomSimpleCar implements SerializablePayload {

  @TypeProperty(required = true, originalName = "colour", encoding = FieldEncodingType.JSON)
  private String colour;

  @TypeProperty(required = true, originalName = "type", encoding = FieldEncodingType.XML)
  private String type;

  @TypeProperty(originalName = "rightHandDrive", encoding = FieldEncodingType.XML_URLENCODED)
  private Boolean rightHandDrive;

  public CustomSimpleCar() {
    // do nothing
  }

  public CustomSimpleCar(final String colour, final String type, final Boolean rightHandDrive) {
    this.colour = colour;
    this.type = type;
    this.rightHandDrive = rightHandDrive;
  }

  public String getColour() { return colour; }

  public void setColour(final String colour) { this.colour = colour; }

  public String getType() { return type; }

  public void setType(final String type) { this.type = type; }

  public Boolean getRightHandDrive() { return rightHandDrive; }

  public void setRightHandDrive(final Boolean rightHandDrive) { this.rightHandDrive = rightHandDrive; }

  @Override
  public String toString() {
    return PayloadJsonSerializer.serializeCustomEncoding(this);
  }

}
