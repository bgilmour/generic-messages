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
@JsonSerialize(using = PayloadJsonSerializer.class, as = CarEngine.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = CarEngine.class)
public class CarEngine implements SerializablePayload {

  private Integer cylinders; // required
  private String fuelType; // optional

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
  public List<PayloadProperty> getProperties() {
    return new ArrayList<>(Arrays.asList(
        ScalarProperty.Builder.newBuilder("cylinders", "cylinders", "cylinders", true, Integer.class)
            .addGetter(() -> getCylinders()).addSetter(o -> setCylinders((Integer) o)).build(),
        ScalarProperty.Builder.newBuilder("fuelType", "fuelType", "fuelType", false, String.class).addGetter(() -> getFuelType())
            .addSetter(o -> setFuelType((String) o)).build()));
  }

  @Override
  public String toString() {
    return cylinders + " cyl" + (fuelType != null ? " " + fuelType : "");
  }

}
