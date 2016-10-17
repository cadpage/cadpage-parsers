package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAFairfaxCountyBParser extends FieldProgramParser {
  
  public VAFairfaxCountyBParser() {
    super(CITY_CODES, "FAIRFAX COUNTY", "VA", 
          "LOCATION:ADDR/S! EVENT_TYPE:CALL! EVENT_#:ID! FIRE_BOX:BOX! TALKGROUP:CH! Disp:UNIT");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Information")) return false;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt >= 0) {
        data.strPlace = stripFieldStart(field.substring(pt+1).trim(), "@");
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALEX CITY",      "ALEXANDER",
      "CHAN",           "CHANTILY",
      "FLCH",           "FALLS CHURCH"
  });
}
