package com.langtoun.messages.generic;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.langtoun.messages.types.SerializablePayload;

public class PayloadXmlAdapter extends XmlAdapter<String, SerializablePayload> {

  @Override
  public SerializablePayload unmarshal(String v) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String marshal(SerializablePayload v) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
