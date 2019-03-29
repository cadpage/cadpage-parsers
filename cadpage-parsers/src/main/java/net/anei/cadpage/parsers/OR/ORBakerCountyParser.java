package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORBakerCountyParser extends FieldProgramParser {
  
  public ORBakerCountyParser() {
    super(CITY_CODES, "BAKER COUNTY", "OR", 
          "ADDR CITY DATE TIME UNIT UNIT UNIT UNIT CALL! CALL/S+? MAP END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }
  
  @Override
  public String getFilter() {
    return "pager@baker911.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.length() > 50 && body.charAt(50) == '~') {
      body = body.substring(0, 50) + body.substring(51);
    }
    return parseFields(body.split("~"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) new MyUnitField();
    if (name.equals("MAP")) return new MapField("\\d{3,4}");
    return super.getField(name);
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("/")) return;
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
  
  private static final Pattern STREET_PTN = Pattern.compile("\\bSTREET\\b");
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    address = address.toUpperCase();
    address = STREET_PTN.matcher(address).replaceAll("ST");
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "337 2ND ST",                           "+44.766027,-117.169473"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BAK",  "BAKER CITY",
      "BRI",  "BRIDGEPORT",
      "DUR",  "DURKEE",
      "GRA",  "GRANITE",
      "GRE",  "GREENHORN",
      "HAI",  "HAINES",
      "HAL",  "HALFWAY",
      "HER",  "HEREFORD",
      "HUN",  "HUNTINGTON",
      "ONT",  "ONTARIO",
      "OXB",  "OXBOW",
      "NOR",  "NORTH POWDER",
      "RIC",  "RICHLAND",
      "SUM",  "SUMPTER",
      "UNI",  "UNITY"
  });

}
