package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;



public class OHBrownCountyParser extends DispatchA1Parser {

  public OHBrownCountyParser() {
    super(CITY_LIST, "BROWN COUNTY", "OH");
    setupCities(CITY_CODES);
  }

  @Override
  public String getFilter() {
    return "browncommctr@roadrunner.com,browncommctr@browncountyohio.gov";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCross.startsWith("N/A & N/A")) data.strCross = "";
    else if (data.strCross.startsWith("N/A & ")) {
      data.strCross = data.strCross.substring(6).trim();
    }
    else if (data.strCross.endsWith(" & N/A")) {
      data.strCross = data.strCross.substring(0,data.strCross.length()-6).trim();
    }
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

        // Village
        "ABERDEEN",
        "FAYETTEVILLE",
        "GEORGETOWN",
        "HAMERSVILLE",
        "HIGGINSPORT",
        "MT ORAB",
        "RIPLEY",
        "RUSSELLVILLE",
        "SARDINIA",
        "CENSUS-DESIGNATED PLACES",
        "LAKE LORELEI",
        "LAKE WAYNOKA",
        "ST MARTIN",

        //Unincorporated communities
        "ARNHEIM",
        "ASH RIDGE",
        "BARDWELL",
        "BOUDES FERRY",
        "BROWNSTOWN",
        "CENTERVILLE",
        "CHASETOWN",
        "CROSSTOWN",
        "DECATUR",
        "EASTWOOD",
        "ELLSBERRY",
        "FEESBURG",
        "FINCASTLE",
        "FIVEMILE",
        "GREENBUSH",
        "HIETT",
        "LEVANNA",
        "LOCUST RIDGE",
        "MACON",
        "MAPLE",
        "NEALS CORNER",
        "NEEL",
        "NEW HARMONY",
        "NEW HOPE",
        "REDOAK",
        "UPPER FIVEMILE",
        "VERA CRUZ",
        "WAHLSBURG",
        "WHITE OAK",
        "WHITE OAK VALLEY",

        // Townships
        "BYRD TWP",
        "BYRD",
        "CLARK TWP",
        "CLARK",
        "EAGLE TWP",
        "EAGLE",
        "FRANKLIN TWP",
        "FRANKLIN",
        "GREEN TWP",
        "GREEN",
        "HUNTINGTON TWP",
        "HUNTINGTON",
        "JACKSON TWP",
        "JACKSON",
        "JEFFERSON TWP",
        "JEFFERSON",
        "LEWIS TWP",
        "LEWIS",
        "PERRY TWP",
        "PERRY",
        "PIKE TWP",
        "PIKE",
        "PLEASANT TWP",
        "PLEASANT",
        "SCOTT TWP",
        "SCOTT",
        "STERLING TWP",
        "STERLING",
        "UNION TWP",
        "UNION",
        "WASHINGTON TWP",
        "WASHINGTON",

        // Adams County
        "ADAMS CO",
        "WEST UNION",

        // Clinton county
        "CLINTON CO",
        "BLANCHESTER",

        // Mason County (KY)
        "MASON CO",
        "MAYSVILLE"

  };

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "FAY", "FAYETTEVILLE"
  });
}
