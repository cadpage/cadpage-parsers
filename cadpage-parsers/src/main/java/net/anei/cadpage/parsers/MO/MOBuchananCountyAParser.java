package net.anei.cadpage.parsers.MO;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOBuchananCountyAParser extends FieldProgramParser {
  
  public MOBuchananCountyAParser() {
    super("BUCHANAN COUNTY", "MO", 
          "CALL:CALL! PLACE:ADDR! LATITUDE:GPS1! LONGITUDE:GPS2! ID:ID! RECEIVED_DATE/TIME:SKIP! DISPATCH_TIME:TIME! MAP:MAP! UNIT:UNIT! COMMON_NAME:PLACE! CLOSEST_INTERSECTION:X! INFO:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "pddistribution@ci.st-joseph.mo.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("(PAGE)")) return false;
    return super.parseMsg(body,  data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll("  +", " ");
      int pt = field.lastIndexOf(" APT #");
      if (pt < 0) abort();
      data.strApt = field.substring(pt+6).trim();
      field = field.substring(0,pt).trim();
      if (!field.contains("/")) abort();
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = CITY_CODES.getProperty(p.getLast(' '));
      if (city != null) {
        data.strCity = city;
        field = p.get();
      }
      if (!field.equals("No Cross Streets Found")) {
        data.strCross = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "X CITY";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("E911 Info -");
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("LAKE CONTRARY")) city = "ST JOSEPH";
    return city;
  }
  
  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "AGEN", "AGENCY",
      "CH",   "ST JOSEPH",
      "DEAR", "DEARBORN",
      "DEKA", "DE KALB",
      "EAST", "EASTON",
      "EDGE", "EDGERTON",
      "FAUC", "FAUCETT",
      "GOWE", "GOWER",
      "LC",   "LAKE CONTRARY",
      "SANA", "SAN ANTONIO",
      "SJ",   "ST JOSEPH",
      "RUSH", "RUSHVILLE",
      "SUGA", "RUSHVILLE"
  });

}
