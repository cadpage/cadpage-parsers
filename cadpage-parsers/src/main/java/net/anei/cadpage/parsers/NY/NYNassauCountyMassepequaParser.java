package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYNassauCountyMassepequaParser extends FieldProgramParser {
  
  public NYNassauCountyMassepequaParser() {
    super(CITY_CODES, "NASSAU COUNTY", "NY", 
          "ADDR! Town:CITY! Cross:X! Map:MAP! Block:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "paging@rednmxcad.com,massapequafd@rednmxcad.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("Block#:", "Block:");
    return super.parseFields(body.split(","), data);
  }
  
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt < 0) abort();
      data.strCall = field.substring(0, pt).trim();
      super.parse(field.substring(pt+1).trim(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL " + super.getFieldNames();
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return WESTFIELD_MALL_PTN.matcher(address).replaceAll("1 WESTFIELD MALL");
  }
  private static final Pattern WESTFIELD_MALL_PTN = Pattern.compile("\\b\\d+ +WESTFIELD MALL\\b", Pattern.CASE_INSENSITIVE);
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "E/M",  "EAST MASSAPEQUA",
      "MASS", "MASSAPEQUA",
      "MPK",  "MASSAPEQUA PARK"
  });
}
