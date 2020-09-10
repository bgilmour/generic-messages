package com.langtoun.messages.types.gen.cars;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.langtoun.messages.annotations.AwsFieldProperty;
import com.langtoun.messages.annotations.AwsTypeDefinition;
import com.langtoun.messages.annotations.CustomTypeEncoding;
import com.langtoun.messages.annotations.FieldOrder;
import com.langtoun.messages.generic.AwsComplexTypeJsonDeserializer;
import com.langtoun.messages.generic.AwsCustomEncodingSerializer;
import com.langtoun.messages.types.FieldEncodingType;

/**
 * Surrogate for a generated type that extends {@link ComplexCar}.
 *
 */
@JsonSerialize(using = AwsCustomEncodingSerializer.class, as = CustomComplexCarWithFeatures.class)
@JsonDeserialize(using = AwsComplexTypeJsonDeserializer.class, as = CustomComplexCarWithFeatures.class)
//@Format-Off
@AwsTypeDefinition(
  fieldOrder = @FieldOrder({
    "colour", "type", "rightHandDrive", "engine", "features"
  }),
  encoding = @CustomTypeEncoding(
    prefix = "<<<", suffix = ">>>", fieldSep = "|", keyValSep = "="
  )
)
//@Format-On
public class CustomComplexCarWithFeatures extends CustomComplexCar {

  @AwsFieldProperty(originalName = "features", index = 4, required = true, encoding = FieldEncodingType.BASE64)
  private final List<CarFeature> features = new ArrayList<>();

  public CustomComplexCarWithFeatures() {
    // do nothing
  }

  public CustomComplexCarWithFeatures(final String colour, final String type, final Boolean rightHandDrive,
      final CarEngine engine) {
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
    return AwsCustomEncodingSerializer.serializeCustomEncoding(this);
  }

}
