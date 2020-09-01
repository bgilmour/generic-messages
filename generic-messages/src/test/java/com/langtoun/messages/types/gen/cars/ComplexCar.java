package com.langtoun.messages.types.gen.cars;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.annotations.AwsFieldProperty;
import com.langtoun.messages.annotations.AwsTypeDefinition;
import com.langtoun.messages.annotations.FieldOrder;
import com.langtoun.messages.generic.AwsComplexTypeJsonDeserializer;
import com.langtoun.messages.generic.AwsComplexTypeJsonSerializer;

/**
 * Surrogate for a generated type that extends {@link SimpleCar}.
 *
 */
@JsonSerialize(using = AwsComplexTypeJsonSerializer.class, as = ComplexCar.class)
@JsonDeserialize(using = AwsComplexTypeJsonDeserializer.class, as = ComplexCar.class)
// @Format-Off
@AwsTypeDefinition(
  fieldOrder = @FieldOrder({
    "colour", "type", "rightHandDrive", "engine"
  })
)
// @Format-On
public class ComplexCar extends SimpleCar {

  @AwsFieldProperty(required = true)
  private CarEngine engine;

  public ComplexCar() {
    // do nothing
  }

  public ComplexCar(final String colour, final String type, final Boolean rightHandDrive, final CarEngine engine) {
    super(colour, type, rightHandDrive);
    this.engine = engine;
  }

  public CarEngine getEngine() { return engine; }

  public void setEngine(final CarEngine engine) { this.engine = engine; }

  @Override
  public String toString() {
    return super.toString() + " [" + engine + "]";
  }

}
