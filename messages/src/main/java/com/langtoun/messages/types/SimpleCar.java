package com.langtoun.messages.types;

import static com.langtoun.messages.generic.ScalarProperty.newScalarProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.langtoun.messages.generic.MessageProperty;
import com.langtoun.messages.generic.SerializablePayload;

public class SimpleCar implements SerializablePayload {

  private String colour; // required
  private String type; // required

  public SimpleCar() {

  }

  public SimpleCar(final String colour, final String type) {
    this.colour = colour;
    this.type = type;
  }

  public String getColour() {
    return colour;
  }

  public void setColour(final String colour) {
    this.colour = colour;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  @Override
  public List<MessageProperty> getProperties() {
    return new ArrayList<>(Arrays.asList(
        newScalarProperty("colour", "colour", "colour", true, () -> getColour(), o -> setColour((String) o), String.class),
        newScalarProperty("type", "type", "type", true, () -> getType(), o -> setType((String) o), String.class)));
  }

  @Override
  public String toString() {
    return colour + " " + type;
  }

}
