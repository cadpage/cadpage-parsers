package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class PADelawareCountyAParser extends FieldProgramParser {
  
  public PADelawareCountyAParser() {
    super(CITY_CODES, "DELAWARE COUNTY", "PA",
           "( TIME CALL ADDR/S! X | UPDATE? ADDR/S1! X/Z? UNIT! GPS? ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "station55@comcast.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() > 0) body = "[" + subject + "] " + body;
    return parseFields(body.split("\\*"), data);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(",@");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+2).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new UnitField("(?:[A-Z]+\\d+|\\d{3}[A-Z]?)(?:/.*)?", true);
    if (name.equals("GPS")) return new GPSField("([-+]\\d+\\.\\d+ +[-+]\\d+\\.\\d+)\\b.*", true);
    if (name.equals("UPDATE")) return new SkipField("\\[Update\\]", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ME",    "MEDIA",
      "SP",    "SPRINGFIELD TWP",
      "U",     "UPPER PROVIDENCE TWP",
      "UP",    "UPPER PROVIDENCE TWP"
  });
}
