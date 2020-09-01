package com.langtoun.messages.types.gen.cars;

import java.util.ArrayList;
import java.util.List;

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
@JsonSerialize(using = PayloadJsonSerializer.class, as = ComplexCarWithFeatures.class)
@JsonDeserialize(using = PayloadJsonDeserializer.class, as = ComplexCarWithFeatures.class)
// @Format-Off
@TypeDefinition(
  fieldOrder = @FieldOrder({
    "colour", "type", "rightHandDrive", "engine", "features"
  })
)
// @Format-On
public class ComplexCarWithFeatures extends ComplexCar {

  @TypeProperty(required = true, jsonName = "features")
  private final List<CarFeature> features = new ArrayList<>();

  public ComplexCarWithFeatures() {
    // do nothing
  }

  public ComplexCarWithFeatures(final String colour, final String type, final Boolean rightHandDrive, final CarEngine engine) {
    super(colour, type, rightHandDrive, engine);
  }

  public List<CarFeature> getFeatures() { return features; }

  public void setFeatures(final List<CarFeature> features) {
    this.features.addAll(features);
  }

  public void addFeature(final CarFeature feature) {
    features.add(feature);
  }

  @Override
  public String toString() {
    return super.toString() + " with " + features + "]";
  }

}
