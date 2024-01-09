package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAOrangeCountyParser extends FieldProgramParser {

  public CAOrangeCountyParser() {
    super(CITY_CODES, "ORANGE COUNTY", "CA",
          "DATETIME CALL ADDR APT PLACE CITY? MAP UNIT ID SRC! GPS/d END");
  }

  @Override
  public String getFilter() {
    return "tritech911@ocfa.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split(">"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{6}", true);
    return super.getField(name);
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city.toUpperCase(), MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "JOHN WAYNE AIRPORT",    "IRVINE"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANA", "ANAHEIM",
      "AVO", "ALISO VIEJO",
      "BPK", "BUENA PARK",
      "CCU", "COTO DE CAZA",
      "CYP", "CYPRESS",
      "DPT", "DANA POINT",
      "EMU", "ORANGE",
      "FVY", "FOUNTAIN VALLEY",
      "GGV", "GARDEN GROVE",
      "IRV", "IRVINE",
      "JWA", "JOHN WAYNE AIRPORT",
      "LAF", "LOS ALAMITOS",
      "LAP", "LA PALMA",
      "LDU", "LADERA RANCH",
      "LGH", "LAGUNA HILLS",
      "LGN", "LAGUNA NIGUEL",
      "LGW", "LAGUNA WOODS",
      "LKF", "LAKE FOREST",
      "MCU", "MIDWAY CITY",
      "MVO", "MISSION VIEJO",
      "NPB", "NEWPORT BEACH",
      "ORG", "ORANGE",
      "PLA", "PLACENTIA",
      "RSM", "RANCHO SANTA MARGARITA",
      "SCL", "SAN CLEMENTE",
      "SCP", "SAN JUAN CAPISTRANO",
      "SLB", "SEAL BEACH",
      "STA", "SANTA ANA",
      "STU", "ANAHEIM",     // ?????
      "STN", "STANTON",
      "TST", "TUSTIN",
      "TSU", "TUSTIN",
      "WST", "WESTMINSTER",
      "YBL", "YORBA LINDA"
  });
}
