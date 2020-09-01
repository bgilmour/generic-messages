package com.langtoun.messages.types.gen.cars;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.annotations.CustomTypeEncoding;
import com.langtoun.messages.annotations.FieldOrder;
import com.langtoun.messages.annotations.AwsTypeDefinition;
import com.langtoun.messages.annotations.AwsFieldProperty;
import com.langtoun.messages.generic.AwsComplexTypeJsonDeserializer;
import com.langtoun.messages.generic.AwsComplexTypeJsonSerializer;
import com.langtoun.messages.types.FieldEncodingType;

/**
 * Surrogate for a generated type that extends {@link CustomSimpleCar} and is
 * annotated with {@link AwsTypeDefinition} and {@link AwsFieldProperty}.
 * 
 */
@JsonSerialize(using = AwsComplexTypeJsonSerializer.class, as = CustomComplexCar.class)
@JsonDeserialize(using = AwsComplexTypeJsonDeserializer.class, as = CustomComplexCar.class)
// @Format-Off
@AwsTypeDefinition(
  fieldOrder = @FieldOrder({
    "colour", "type", "rightHandDrive", "engine"
  }),
  encoding = @CustomTypeEncoding(
    prefix = "<<<", suffix = ">>>", fieldSep = "|", keyValSep = "="
  )
)
// @Format-On
public class CustomComplexCar extends SimpleCar {

  @AwsFieldProperty(required = true, originalName = "engine", encoding = FieldEncodingType.JSON)
  private CarEngine engine;

  public CustomComplexCar() {
    // do nothing
  }

  public CustomComplexCar(final String colour, final String type, final Boolean rightHandDrive, final CarEngine engine) {
    super(colour, type, rightHandDrive);
    this.engine = engine;
  }

  public CarEngine getEngine() { return engine; }

  public void setEngine(final CarEngine engine) { this.engine = engine; }

  @Override
  public String toString() {
    return AwsComplexTypeJsonSerializer.serializeCustomEncoding(this);
  }

}
