package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Contra Costa County, CA
 */
public class CAContraCostaCountyAParser extends SmartAddressParser {
  
  public CAContraCostaCountyAParser() {
    super(CITY_LIST, "CONTRA COSTA COUNTY", "CA");
    setFieldList("CALL ADDR APT CITY UNIT INFO");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "ARNOLD INDUSTRIAL",
        "CONTRA COSTA",
        "DEER HILL",
        "FARM BUREAU",
        "GOLDEN RAIN",
        "FARM BUREAU",
        "KIRKER PASS",
        "OAK GROVE",
        "PORT CHICAGO",
        "RIDGE PARK",
        "TERRA CALIFORNIA",
        "THE TREES",
        "YGNACIO VALLEY",
        "WILLOW PASS"
    );
  }
  
  @Override
  public String getFilter() {
    return "btucad@fire.ca.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("INFO PAGE ")) return false;
    body = body.substring(10).trim();
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, body, data);
    if (data.strCity.length() == 0) return false;
    Parser p = new Parser(getLeft());
    data.strUnit = p.get(' ');
    data.strSupp = p.get();
    return data.strUnit.length() > 0;
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "ANTIOCH",
    "BRENTWOOD",
    "CLAYTON",
    "CONCORD",
    "DANVILLE",
    "EL CERRITO",
    "HERCULES",
    "LAFAYETTE",
    "MARTINEZ",
    "MORAGA",
    "OAKLEY",
    "ORINDA",
    "PINOLE",
    "PITTSBURG",
    "PLEASANT HILL",
    "RICHMOND",
    "SAN PABLO",
    "SAN RAMON",
    "WALNUT CREEK",

    // Census-designated places
    "ACALANES RIDGE",
    "ALAMO",
    "ALHAMBRA VALLEY",
    "BAY POINT",
    "BETHEL ISLAND",
    "BLACKHAWK",
    "BYRON",
    "CAMINO TASSAJARA",
    "CASTLE HILL",
    "CLYDE",
    "CONTRA COSTA CENTRE",
    "CROCKETT",
    "DIABLO",
    "DISCOVERY BAY",
    "EAST RICHMOND HEIGHTS",
    "EL SOBRANTE",
    "KENSINGTON",
    "KNIGHTSEN",
    "MOUNTAIN VIEW",
    "NORRIS CANYON",
    "NORTH GATE",
    "NORTH RICHMOND",
    "PACHECO",
    "PORT COSTA",
    "RELIEZ VALLEY",
    "RODEO",
    "ROLLINGWOOD",
    "SAN MIGUEL",
    "SHELL RIDGE",
    "TARA HILLS",
    "VINE HILL",

    // Unincorporated communities
    "BAYVIEW-MONTALVIN",
    "CANYON",
    "HASFORD HEIGHTS",

    // Ghost towns
    "NORTONVILLE",
    "POINT OF TIMBER LANDING",
    "PORT CHICAGO",
    "SOMERSVILLE"
   
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACC-BICYCLE",
      "ACC-VEH",
      "AIRPORT ALRT 3-CRASH",
      "ALARM-MEDICAL",
      "EMS-CHARLIE",
      "EMS-DELTA",
      "EMS-ECHO",
      "*EMS-PD C3",
      "*EMS-PEND PROQA",
      "FIRE-STRUC COMML",
      "FIRE-STRUC-RES"
  );
}
