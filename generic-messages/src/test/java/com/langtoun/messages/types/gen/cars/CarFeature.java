package com.langtoun.messages.types.gen.cars;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.annotations.FieldOrder;
import com.langtoun.messages.annotations.AwsTypeDefinition;
import com.langtoun.messages.annotations.AwsFieldProperty;
import com.langtoun.messages.generic.AwsComplexTypeJsonDeserializer;
import com.langtoun.messages.generic.AwsComplexTypeJsonSerializer;
import com.langtoun.messages.types.AwsComplexType;

/**
 * Surrogate for a generated type that extends {@link AwsComplexType}.
 *
 */
@JsonSerialize(using = AwsComplexTypeJsonSerializer.class, as = CarFeature.class)
@JsonDeserialize(using = AwsComplexTypeJsonDeserializer.class, as = CarFeature.class)
// @Format-Off
@AwsTypeDefinition(
  fieldOrder = @FieldOrder({
    "name", "price"
    }))
// @Format-On
public class CarFeature extends AwsComplexType {

  @AwsFieldProperty(required = true, jsonName = "name")
  private String name;

  @AwsFieldProperty(jsonName = "price")
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
