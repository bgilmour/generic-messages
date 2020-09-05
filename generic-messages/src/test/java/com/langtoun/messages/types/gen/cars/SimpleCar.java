package com.langtoun.messages.types.gen.cars;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.annotations.AwsFieldProperty;
import com.langtoun.messages.annotations.AwsTypeDefinition;
import com.langtoun.messages.annotations.FieldOrder;
import com.langtoun.messages.generic.AwsComplexTypeJsonDeserializer;
import com.langtoun.messages.generic.AwsComplexTypeJsonSerializer;
import com.langtoun.messages.types.AwsComplexType;

/**
 * Surrogate for a generated type that extends {@link AwsComplexType}.
 *
 */
@JsonSerialize(using = AwsComplexTypeJsonSerializer.class, as = SimpleCar.class)
@JsonDeserialize(using = AwsComplexTypeJsonDeserializer.class, as = SimpleCar.class)
// @Format-Off
@AwsTypeDefinition(
  fieldOrder = @FieldOrder({
    "colour", "type", "rightHandDrive"
  })
)
// @Format-On
public class SimpleCar extends AwsComplexType {

  @AwsFieldProperty(required = true, originalName = "colour")
  private String colour;

  @AwsFieldProperty(required = true, originalName = "type")
  private String type;

  @AwsFieldProperty(originalName = "rightHandDrive")
  private Boolean rightHandDrive;

  public SimpleCar() {
    // do nothing
  }

  public SimpleCar(final String colour, final String type, final Boolean rightHandDrive) {
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
    return colour + " " + type + (rightHandDrive == null ? "" : rightHandDrive ? " (rhd)" : " (lhd)");
  }

}
