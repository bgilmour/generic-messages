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
@JsonSerialize(using = AwsComplexTypeJsonSerializer.class, as = CarEngine.class)
@JsonDeserialize(using = AwsComplexTypeJsonDeserializer.class, as = CarEngine.class)
// @Format-Off
@AwsTypeDefinition(
  fieldOrder = @FieldOrder({
    "cylinders", "fuelType"
  })
)
// @Format-On
public class CarEngine extends AwsComplexType {

  @AwsFieldProperty(originalName = "cylinders", index = 0, required = true)
  private Integer cylinders;

  @AwsFieldProperty(originalName = "fuelType", index = 1)
  private String fuelType;

  public CarEngine() {
    // do nothing
  }

  public CarEngine(final Integer cylinders, final String fuelType) {
    this.cylinders = cylinders;
    this.fuelType = fuelType;
  }

  public Integer getCylinders() { return cylinders; }

  public void setCylinders(final Integer cylinders) { this.cylinders = cylinders; }

  public String getFuelType() { return fuelType; }

  public void setFuelType(final String fuelType) { this.fuelType = fuelType; }

  @Override
  public String toString() {
    return cylinders + " cyl" + (fuelType != null ? " " + fuelType : "");
  }

}
