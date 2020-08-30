package com.langtoun.messages.types.gen.cars;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.annotations.CustomTypeEncoding;
import com.langtoun.messages.annotations.TypeProperty;
import com.langtoun.messages.generic.PayloadJsonDeserializer;
import com.langtoun.messages.generic.PayloadJsonSerializer;
import com.langtoun.messages.types.FieldEncodingType;
import com.langtoun.messages.types.SerializablePayload;

/**
 * Surrogate for a generated type that implements {@link SerializablePayload}
 * and is annotated with {@link CustomTypeEncoding} and {@link TypeProperty}.
 * 
 */
@JsonSerialize(using = PayloadJsonSerializer.class, as = CustomComplexCar.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = CustomComplexCar.class)
@CustomTypeEncoding(prefix = "<<<", suffix = ">>>", fieldSep = "|", keyValSep = "=")
public class CustomComplexCar extends SimpleCar {

  @TypeProperty(required = true, originalName = "engine", encoding = FieldEncodingType.JSON)
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
    return PayloadJsonSerializer.serializeCustomEncoding(this);
  }

}
