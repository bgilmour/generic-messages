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
@JsonSerialize(using = PayloadJsonSerializer.class, as = CarFeature.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = CarFeature.class)
// @Format-Off
@TypeDefinition(
  fieldOrder = @FieldOrder({
    "name", "price"
    }))
// @Format-On
public class CarFeature implements SerializablePayload {

  @TypeProperty(required = true, jsonName = "name")
  private String name;

  @TypeProperty(required = true, jsonName = "price")
  private Double price;

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
  public String toString() {
    return name + (price != null ? " @ " + price : "");
  }

}
