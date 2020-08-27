package com.langtoun.messages.types.gen.cars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.PayloadProperty;
import com.langtoun.messages.types.properties.ScalarProperty;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
@JsonSerialize(using = PayloadJsonSerializer.class, as = CarFeature.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = CarFeature.class)
public class CarFeature implements SerializablePayload {

  private String name; // required
  private Double price; // optional

  public CarFeature() {
    // do nothing
  }

  public CarFeature(final String name, final Double price) {
    this.name = name;
    this.price = price;
  }

  public String getName() { return name; }

  public void setName(final String name) { this.name = name; }

  public Double getPrice() { return price; }

  public void setPrice(final Double price) { this.price = price; }

  @Override
  public List<PayloadProperty> getProperties() {
    return new ArrayList<>(Arrays.asList(
        ScalarProperty.Builder.newBuilder("name", "name", "name", true, String.class).addGetter(() -> getName())
            .addSetter(o -> setName((String) o)).build(),
        ScalarProperty.Builder.newBuilder("price", "price", "price", false, Double.class).addGetter(() -> getPrice())
            .addSetter(o -> setPrice((Double) o)).build()));
  }

  @Override
  public String toString() {
    return name + (price != null ? " @ " + price : "");
  }

}
