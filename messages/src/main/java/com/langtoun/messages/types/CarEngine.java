package com.langtoun.messages.types;

import static com.langtoun.messages.generic.ScalarProperty.newScalarProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.langtoun.messages.generic.MessageProperty;
import com.langtoun.messages.generic.SerializablePayload;

public class CarEngine implements SerializablePayload {

  private Integer cylinders; // required
  private String fuelType; // optional

  public CarEngine() {

  }

  public CarEngine(final Integer cylinders, final String fuelType) {
    this.cylinders = cylinders;
    this.fuelType = fuelType;
  }

  public Integer getCylinders() {
    return cylinders;
  }

  public void setCylinders(final Integer cylinders) {
    this.cylinders = cylinders;
  }

  public String getFuelType() {
    return fuelType;
  }

  public void setFuelType(final String fuelType) {
    this.fuelType = fuelType;
  }

  @Override
  public List<MessageProperty> getProperties() {
    return new ArrayList<>(Arrays.asList(
        newScalarProperty("cylinders", "cylinders", "cylinders", true, () -> getCylinders(), o -> setCylinders((Integer) o),
            Integer.class),
        newScalarProperty("fuelType", "fuelType", "fuelType", false, () -> getFuelType(), o -> setFuelType((String) o),
            String.class)));
  }

  @Override
  public String toString() {
    return cylinders + " cyl" + (fuelType != null ? " " + fuelType : "");
  }

}
