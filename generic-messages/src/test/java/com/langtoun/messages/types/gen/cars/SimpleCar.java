package com.langtoun.messages.types.gen.cars;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.annotations.FieldOrder;
import com.langtoun.messages.annotations.TypeDefinition;
import com.langtoun.messages.annotations.TypeProperty;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.generic.SerializablePayload;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
@JsonSerialize(using = PayloadJsonSerializer.class, as = SimpleCar.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = SimpleCar.class)
// @Format-Off
@TypeDefinition(
  fieldOrder = @FieldOrder({
    "colour", "type", "rightHandDrive"
  })
)
// @Format-On
public class SimpleCar implements SerializablePayload {

  @TypeProperty(required = true, jsonName = "colour")
  private String colour;

  @TypeProperty(required = true, jsonName = "type")
  private String type;

  @TypeProperty(jsonName = "rhs")
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
