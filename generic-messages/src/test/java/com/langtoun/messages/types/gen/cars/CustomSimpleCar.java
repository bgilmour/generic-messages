package com.langtoun.messages.types.gen.cars;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.annotations.CustomTypeEncoding;
import com.langtoun.messages.annotations.FieldOrder;
import com.langtoun.messages.annotations.AwsTypeDefinition;
import com.langtoun.messages.annotations.AwsFieldProperty;
import com.langtoun.messages.generic.AwsComplexTypeJsonDeserializer;
import com.langtoun.messages.generic.AwsComplexTypeJsonSerializer;
import com.langtoun.messages.types.AwsComplexType;
import com.langtoun.messages.types.FieldEncodingType;

/**
 * Surrogate for a generated type that extends {@link AwsComplexType} and is
 * annotated with {@link AwsTypeDefinition} and {@link AwsFieldProperty}.
 *
 */
@JsonSerialize(using = AwsComplexTypeJsonSerializer.class, as = CustomSimpleCar.class)
@JsonDeserialize(using = AwsComplexTypeJsonDeserializer.class, as = CustomSimpleCar.class)
// @Format-Off
@AwsTypeDefinition(
  fieldOrder = @FieldOrder({
    "colour", "type", "rightHandDrive"
  }),
  encoding = @CustomTypeEncoding(
    prefix = "<<<", suffix = ">>>", fieldSep = "|", keyValSep = "="
  )
)
// @Format-On
public class CustomSimpleCar extends AwsComplexType {

  @AwsFieldProperty(required = true, originalName = "colour", encoding = FieldEncodingType.JSON)
  private String colour;

  @AwsFieldProperty(required = true, originalName = "type", encoding = FieldEncodingType.XML)
  private String type;

  @AwsFieldProperty(originalName = "right_hand_drive", encoding = FieldEncodingType.XML_URLENCODED)
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
    return AwsComplexTypeJsonSerializer.serializeCustomEncoding(this);
  }

}
