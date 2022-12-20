package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAMontgomeryCountyBParser extends FieldProgramParser {

  public VAMontgomeryCountyBParser() {
    super("MONTGOMERY COUNTY", "VA",
          "ADDR:ADDRCITY! GPS:GPS? CALL:CALL! PLACE:PLACE! INFO:INFO! INFO/N+ X:X! UNIT:UNIT! PRI:PRI! ID:ID! END");
  }

  @Override
  public String getFilter() {
    return "@nrv911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n<end>");
    if  (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains(", -")) {
        data.strAddress = field;
      } else {
        super.parse(field, data);
      }
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ";");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field,  data);
    }
  }

  @Override
  public String adjustMapCity(String city) {
    String tmp = MAP_CITY_TABLE.getProperty(city.toUpperCase());
    if (tmp != null) city = tmp;
    return city;
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "VIRGINIA TECH", "Blacksburg"
  });

}
