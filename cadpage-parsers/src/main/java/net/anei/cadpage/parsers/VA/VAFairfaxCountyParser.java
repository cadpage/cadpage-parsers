package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAFairfaxCountyParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile("/ *\n");
  
  public VAFairfaxCountyParser() {
    super("FAIRFAX COUNTY", "VA",
           "Box:BOX! Event:CALL! Adrs:ADDR! Note:INFO Ch:CH");
    setBreakChar(' ');
  }
  
  @Override
  public String getFilter() {
    return "EAN_23NQ@ean.fairfaxcounty.gov,EAN";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\nSent on:");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    if (body.length() < 4) return false;
    if (!body.startsWith("Box")) return false;
    setBreakChar(body.charAt(3));
    return parseFields(DELIM.split(body), 3, data);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (city.length() > 3) {
          data.strCity = city;
        } else {
          field = field + ' ' + city;
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
}
