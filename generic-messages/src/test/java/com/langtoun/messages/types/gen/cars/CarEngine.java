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
@JsonSerialize(using = PayloadJsonSerializer.class, as = CarEngine.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = CarEngine.class)
// @Format-Off
@TypeDefinition(
  fieldOrder = @FieldOrder({
    "cylinders", "fuelType"
  })
)
// @Format-On
public class CarEngine implements SerializablePayload {

  @TypeProperty(required = true, jsonName = "cyls")
  private Integer cylinders;

  @TypeProperty(jsonName = "fuel")
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
