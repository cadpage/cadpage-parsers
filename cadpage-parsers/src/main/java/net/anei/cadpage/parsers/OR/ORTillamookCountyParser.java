package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ORTillamookCountyParser extends FieldProgramParser {

  public ORTillamookCountyParser() {
    super("TILLAMOOK COUNTY", "OR",
          "DATETIME CALL ADDRCITY PLACE INFO/N+? ID GPS1 GPS2! END");
  }

  @Override
  public String getFilter() {
    return "@tillamook911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("!")) return false;
    int pt = body.indexOf("\nCONFIDENTIALITY");
    if (pt >= 0)  body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d");
    if (name.equals("ID")) return new IdField("CFS# *(.*)");
    return super.getField(name);
  }

  @Override
  public String adjustMapCity(String city) {
    String city2 = CITY_PLACE_TABLE.getProperty(city.toUpperCase());
    if (city2 != null) city = city2;
    return city;
  }
  private static final Properties CITY_PLACE_TABLE = buildCodeTable(new String[]{
      "BLAINE",           "Beaver",
      "SANDLAKE",         "Cloverdale",
      "CASCADE HEAD",     "Siuslaw National Forest"
  });
}
