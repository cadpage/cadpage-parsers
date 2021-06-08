package net.anei.cadpage.parsers.IN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INLakeCountyBParser extends DispatchA19Parser {

  public INLakeCountyBParser() {
    super(CITY_CODES, "LAKE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "flex@lcec911.org";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BRH", "BURNS HARBOR",
      "BRK", "BROOK",
      "BVS", "BEVERLY SHORES",
      "CED", "CEDAR LAKE",
      "CHE", "CHESTERTON",
      "CRO", "CROWN POINT",
      "DEM", "DEMOTTE",
      "DNA", "DUNE ACRES",
      "DYE", "DYER",
      "EC", " EAST CHICAGO",
      "GA1", "GARY",
      "GA2", "GARY",
      "GA3", "GARY",
      "GA4", "GARY",
      "GA5", "GARY",
      "GA6", "GARY",
      "GA7", "GARY",
      "GAR", "GARY",
      "GLD", "GOODLAND",
      "GRI", "GRIFFITH",
      "HA1", "HAMMOND",
      "HA2", "HAMMOND",
      "HA3", "HAMMOND",
      "HA4", "HAMMOND",
      "HA5", "HAMMOND",
      "HA6", "HAMMOND",
      "HAM", "HAMMOND",
      "HEB", "HEBRON",
      "HIG", "HIGHLAND",
      "HOB", "HOBART",
      "IND", "INDIANAPOLIS",
      "KOU", "Kouts",
      "KTD", "KENTLAND",
      "LAF", "Lafayette",
      "LAK", "LAKE STATION",
      "LAP", "LAPORTE",
      "LER", "LEROY",
      "LKS", "LAKE STATION",
      "LKV", "LAKE VILLAGE",
      "LOG", "LOGANSPORT",
      "LOW", "LOWELL",
      "MER", "MERRILLVILLE",
      "MIC", "MICHIGAN CITY",
      "MOR", "MOROCCO",
      "MTR", "MT AYR",
      "MUN", "MUNSTER",
      "NCH", "NEW CHICAGO",
      "NCL", "NEW CARLISLE",
      "OGD", "OGDEN DUNES",
      "PLA", "PLAINFIELD",
      "PNS", "PINES",
      "POR", "PORTAGE",
      "PTR", "PORTER",
      "REM", "REMINGTON",
      "REN", "RENSSELAER",
      "SBD", "SOUTH BEND",
      "SCH", "SCHERERVILLE",
      "SCN", "SCHNEIDER",
      "SHL", "SHELBY",
      "STJ", "ST JOHN",
      "TER", "TERRE HAUTE",
      "VAL", "VALPARAISO",
      "WHE", "WHEATFIELD",
      "WHI", "WHITING",
      "WIN", "WINFIELD"
  });
}
