package com.langtoun.messages.types.gen.cars;

import static com.langtoun.messages.types.properties.ScalarProperty.newScalarProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.generic.MessageJsonDeserializer;
import com.langtoun.messages.generic.MessageJsonSerializer;
import com.langtoun.messages.types.SerializablePayload;
import com.langtoun.messages.types.properties.MessageProperty;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}.
 *
 */
@JsonSerialize(using = MessageJsonSerializer.class, as = CarEngine.class)
@JsonDeserialize(using = MessageJsonDeserializer.class, as = CarEngine.class)
public class CarEngine implements SerializablePayload {

  private Integer cylinders; // required
  private String fuelType; // optional

  public CarEngine() {

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
