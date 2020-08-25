package com.langtoun.messages.types.gen.cars;

import static com.langtoun.messages.types.properties.ScalarProperty.newScalarProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.MessageProperty;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
public class CarFeature implements SerializablePayload {

  private String name;
  private Double price;

  public CarFeature() {

  }

  public CarFeature(final String name, final Double price) {
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(final Double price) {
    this.price = price;
  }

  @Override
  public List<MessageProperty> getProperties() {
    return new ArrayList<>(
        Arrays.asList(newScalarProperty("name", "name", "name", true, () -> getName(), o -> setName((String) o), String.class),
            newScalarProperty("price", "price", "price", false, () -> getPrice(), o -> setPrice((Double) o), Double.class)));
  }

  @Override
  public String toString() {
    return name + (price != null ? " @ " + price : "");
  }

}
