package com.langtoun.messages.types.gen.cars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.types.CustomEncodingContext;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.PayloadProperty;
import com.langtoun.messages.types.properties.ScalarProperty;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
@JsonSerialize(using = PayloadJsonSerializer.class, as = SimpleCar.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = SimpleCar.class)
public class SimpleCar implements SerializablePayload {

  private String colour; // required
  private String type; // required
  private Boolean rightHandDrive; // optional

  private CustomEncodingContext context; // ignore

  private List<PayloadProperty> properties = new ArrayList<>(Arrays.asList(
      ScalarProperty.Builder.newBuilder("colour", "colour", "colour", true, String.class).addGetter(() -> getColour())
          .addSetter(o -> setColour((String) o)).build(),
      ScalarProperty.Builder.newBuilder("type", "type", "type", true, String.class).addGetter(() -> getType())
          .addSetter(o -> setType((String) o)).build(),
      ScalarProperty.Builder.newBuilder("rightHandDrive", "right_hand_drive", "rightHandDrive", false, Boolean.class)
          .addGetter(() -> getRightHandDrive()).addSetter(o -> setRightHandDrive((Boolean) o)).build()));

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
  public List<PayloadProperty> getProperties() { return properties; }

  // TODO: remove for generated types - complete hack for the development
  // environment
  public void setPropertyTypeEncoding(int index, String typeEncoding) {
    properties.get(index).setTypeEncoding(typeEncoding);
  }

  @Override
  public CustomEncodingContext getCustomEncodingContext() {
    if (context == null) {
      return DEFAULT_CONTEXT;
    }
    return context;
  }

  public void setCustomEncodingContext(final CustomEncodingContext context) { this.context = context; }

  @Override
  public String toString() {
    if (getCustomEncodingContext().usesCustomEncoder()) {
      return PayloadJsonSerializer.serializeCustomEncoding(this);
    }
    return colour + " " + type + (rightHandDrive == null ? "" : rightHandDrive ? " (rhd)" : " (lhd)");
  }

}
