package net.anei.cadpage.parsers.MO;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOBuchananCountyAParser extends FieldProgramParser {

  // City code table shared with MOBuchananCountyA

  public MOBuchananCountyAParser() {
    super(CITY_CODES, "BUCHANAN COUNTY", "MO",
          "CALL:CALL! PLACE:ADDRCITY/S6! LATITUDE:GPS1! LONGITUDE:GPS2! ID:ID! RECEIVED_DATE/TIME:SKIP! DISPATCH_TIME:TIME MAP:MAP! UNIT:UNIT! COMMON_NAME:PLACE! ( CLOSEST:X! | CLOSEST_INTERSECTION:X! ) INFO:INFO!");
  }

  @Override
  public String getFilter() {
    return "pddistribution@ci.st-joseph.mo.us,pddistribution@stjoemo.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("(PAGE)")) return false;
    int pt = body.indexOf("\nEXTERNAL EMAIL");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body,  data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll("  +", " ");
      int pt = field.lastIndexOf(" APT #");
      if (pt >= 0) {
        data.strApt = field.substring(pt+6).trim();
        field = field.substring(0,pt).trim();
      }
      field = stripFieldEnd(field, "/");
      field = field.replace('@', '&');
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

  static Properties CITY_CODES = buildCodeTable(new String[]{
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
