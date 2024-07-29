package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAOrangeCountyParser extends FieldProgramParser {

  public CAOrangeCountyParser() {
    super(CITY_CODES, "ORANGE COUNTY", "CA",
          "( DATETIME CALL ADDR APT APT CITY? MAP UNIT ID " +
          "| ADDR APT APT CITY MAP UNIT " +
          ") SRC! GPS/d PLACE END");
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
      "BDU", "SAN BERNARDINO COUNTY",
      "BOL", "BOLSA CHICA",
      "BPK", "BUENA PARK",
      "BRE", "BREA",
      "BRU", "BREA",
      "CCU", "COTO DE CAZA",
      "CHO", "CHINO HILLS",
      "CMU", "COSTA MESA",
      "CNF", "CLEVELAND NATIONAL FOREST",
      "COR", "CORONA",
      "COS", "COSTA MESA",
      "CYP", "CYPRESS",
      "DPT", "DANA POINT",
      "EBU", "EMERALD BAY",
      "EMU", "EL MODENA",
      "FLU", "FULLERTON",
      "FUL", "FULLERTON",
      "FVU", "FOUNTAIN VALLEY",
      "FVY", "FOUNTAIN VALLEY",
      "GGU", "GARDEN GROVE",
      "GGV", "GARDEN GROVE",
      "HTB", "HUNTINGTON BEACH",
      "IRU", "IRVINE",
      "IRV", "IRVINE",
      "JWA", "JOHN WAYNE AIRPORT",
      "LAB", "LAGUNA BEACH",
      "LAC", "LOS ANGELES COUNTY",
      "LAF", "LOS ALAMITOS",
      "LAP", "LA PALMA",
      "LDU", "LADERA RANCH",
      "LFU", "LAS FLORES",
      "LGH", "LAGUNA HILLS",
      "LGN", "LAGUNA NIGUEL",
      "LGU", "LAGUNA",
      "LGW", "LAGUNA WOODS",
      "LHB", "LA HABRA",
      "LHU", "LA HABRA",
      "LKF", "LAKE FOREST",
      "LOB", "LONG BEACH",
      "LOS", "JOINT FORCES TRAINING BASE",
      "MCE", "MARINE CORPS - EL TORO",
      "MCP", "MARINE CORPS - CAMP PENDLETON",
      "MCU", "MIDWAY CITY",
      "MJU", "MODJESKA CANYON",
      "MVO", "MISSION VIEJO",
      "NCU", "NEWPORT COAST",
      "NPB", "NEWPORT BEACH",
      "NVA", "NAVAL - JFTB",
      "NVW", "NAVAL WEAPONS - SEAL BEACH",
      "OHU", "ORTEGA HIGHWAY",
      "OLU", "ORANGE OLIVE",
      "OPU", "ORANGE PARK ACRES",
      "ORC", "ORANGE COUNTY",
      "ORG", "ORANGE",
      "ORU", "ORANGE",
      "PLA", "PLACENTIA",
      "PLU", "PLACENTIA",
      "RMU", "RANCHO MISSION VIEJO",
      "RSM", "RANCHO SANTA MARGARITA",
      "RSU", "ROSSMOOR",
      "RVC", "RIVERSIDE COUNTY",
      "SBU", "SUNSET BEACH AND SEAL BEACH",
      "SCL", "SAN CLEMENTE",
      "SCP", "SAN JUAN CAPISTRANO",
      "SCU", "SANTIAGO CANYON",
      "SDC", "SAN DIEGO COUNTY",
      "SHU", "SANTA ANA HEIGHTS",
      "SJU", "SAN JUAN CAPISTRANO",
      "SLB", "SEAL BEACH",
      "SLU", "SILVERADO CANYON",
      "STA", "SANTA ANA",
      "STN", "STANTON",
      "STU", "STANTON",
      "TBU", "TRABUCO CANYON",
      "TST", "TUSTIN",
      "TSU", "TUSTIN",
      "UCI", "UNIVERSITY OF CALIFORNIA IRVINE",
      "VPK", "VILLA PARK",
      "WST", "WESTMINSTER",
      "YBL", "YORBA LINDA",
      "YLU", "YORBA LINDA"

  });
}
