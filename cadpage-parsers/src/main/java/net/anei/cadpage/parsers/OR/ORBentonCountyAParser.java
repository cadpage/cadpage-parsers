package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.MsgInfo.Data;

//  Map pages can be found at http://maps.co.benton.or.us/gisdata/Address/FireMapBooks/

public class ORBentonCountyAParser extends ORBentonCountyBaseParser {
  
  public ORBentonCountyAParser() {
    super("BENTON COUNTY",
          "INC:CALL! NAT:CODE? ADD:ADDR! APT:APT CITY:CITY! X:X MAP:MAP CFS:ID DIS:UNIT+");
  }
  
  @Override
  public String getFilter() {
    return "Corvallis Alerts,alerts@corvallis.ealertgov.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // See if this is a standard alert
    if (!subject.equals("Corvallis Alert") || !body.startsWith("INC:")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    
    fixAddress(data);
    return true;
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      parseAddress(p.get('['), data);
      data.strCity = p.get(']');
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, ",", field);
    }
  }
}
