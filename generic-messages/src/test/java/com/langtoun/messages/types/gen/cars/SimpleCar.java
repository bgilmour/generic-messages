package com.langtoun.messages.types.gen.cars;

import static com.langtoun.messages.types.properties.ScalarProperty.newScalarProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.MessageJsonDeserializer;
import com.langtoun.messages.generic.MessageJsonSerializer;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.MessageProperty;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
@JsonSerialize(using = MessageJsonSerializer.class, as = SimpleCar.class)
@JsonDeserialize(using = MessageJsonDeserializer.class, as = SimpleCar.class)
public class SimpleCar implements SerializablePayload {

  private String colour; // required
  private String type; // required
  private Boolean rightHandDrive; // optional

  public SimpleCar() {

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

  public void setRightHandDrive(Boolean rightHandDrive) { this.rightHandDrive = rightHandDrive; }

  @Override
  public List<MessageProperty> getProperties() {
    return new ArrayList<>(Arrays.asList(
        newScalarProperty("colour", "colour", "colour", true, () -> getColour(), o -> setColour((String) o), String.class),
        newScalarProperty("type", "type", "type", true, () -> getType(), o -> setType((String) o), String.class),
        newScalarProperty("rightHandDrive", "right_hand_drive", "rightHandDrive", false, () -> getRightHandDrive(),
            o -> setRightHandDrive((Boolean) o), Boolean.class)));
  }

  @Override
  public String toString() {
    return colour + " " + type + (rightHandDrive == null ? "" : rightHandDrive ? " (rhd)" : " (lhd)");
  }

}
