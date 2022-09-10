package net.anei.cadpage.parsers.NC;


import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Gates County, NC
 */

public class NCGatesCountyParser extends DispatchSouthernParser {

  public NCGatesCountyParser() {
    super(CALL_LIST, CITY_LIST, "GATES COUNTY", "NC", DSFLAG_CROSS_NAME_PHONE | DSFLAG_FOLLOW_CROSS);
    setCallPtn("\\S+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    setupMultiWordStreets(
        "ACORN HILL",
        "ADAM SWAMP",
        "GATES BANK",
        "HORACE CARTER",
        "JOE HENRY",
        "MILL POND",
        "ROCKY HOCK",
        "SAND BANKS",
        "SILVER SPRINGS",
        "UNION BRANCH"
    );
  }

  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace(" TRLR PARK", " TR PARK");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("0 ")) body = body.substring(2).trim();
    if (!super.parseMsg(body, data)) return false;
    if (data.strApt.startsWith("- ")) {
      data.strMap = data.strApt.substring(2);
      data.strApt = "";
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("APT", "APT MAP");
  }

  private static final String[] CITY_LIST = new String[] {
    //City
    "GATESVILLE",

    // Townships
    "GATESVILLE",
    "HALL",
    "REYNOLDSON",
    "HASLETT",
    "HOLLY GROVE",
    "HUNTERS MILL",
    "MINTONSVILLE",

    // County Districts
    "GATESVILLE",
    "EURE",
    "GATES",
    "SUNBURY",
    "HOBBSVILLE",

    // Unincorporated
    "CORAPEAKE",
    "EASON CROSSROADS",
    "EURE",
    "HOFLERS FORK",
    "GATES",
    "MINTONSVILLE",
    "SELWIN",
    "TARHEEL",

    // Perquimans County
    "BELVIDERE",
    "HERTFORD",
    "WINFALL",

    // Chowan County
    "EDENTON"

  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "Breathing Problems",
      "MVC with Injury",
      "Non Emergency Transport",
      "Vehicle Fire"
  );

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "19 ROBERT ROBINSON LN",                "+36.458840,-76.673580",
      "22 ROBERT ROBINSON LN",                "+36.458330,-76.673730"
  });
}
