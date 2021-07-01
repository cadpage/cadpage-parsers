package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALBaldwinCountyBParser extends DispatchSouthernParser {

  public ALBaldwinCountyBParser() {
    super(CITY_LIST, "BALDWIN COUNTY", "AL",
          DSFLG_ADDR | DSFLG_OPT_X | DSFLG_ID | DSFLG_TIME);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("Foley Sta. 30")) data.strSource = subject;

    body = body.replace("https://www.ssmap.link/cad?", "https://maps.google.com/?q=");
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "BAY MINETTE",
    "DAPHNE",
    "FAIRHOPE",
    "FOLEY",
    "GULF SHORES",
    "ORANGE BEACH",
    "ROBERTSDALE",
    "SPANISH FORT",

    // Towns
    "ELBERTA",
    "LOXLEY",
    "MAGNOLIA SPRINGS",
    "PERDIDO BEACH",
    "SILVERHILL",
    "SUMMERDALE",

    // Census-designated place
    "BON SECOUR",
    "LILLIAN",
    "PERDIDO",
    "POINT CLEAR",
    "STAPLETON",
    "STOCKTON",

    // Unincorporated areas
    "BARNWELL",
    "BATTLES WHARF",
    "BELFOREST",
    "BROMLEY",
    "CLAY CITY",
    "CROSSROADS",
    "ELSANOR",
    "FORT MORGAN",
    "GASQUE",
    "JOSEPHINE",
    "LATHAM",
    "LITTLE RIVER",
    "MALBIS",
    "MARLOW",
    "MIFLIN",
    "MONTROSE",
    "OAK",
    "ONO ISLAND",
    "OYSTER BAY",
    "PINE GROVE",
    "RABUN",
    "SEACLIFF",
    "SEMINOLE",
    "SWIFT",
    "WOLF BAY",
    "YELLING SETTLEMENT"
  };

}
