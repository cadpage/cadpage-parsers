package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

/**
 * Jasper County, IN
 */

public class INJasperCountyParser extends DispatchA19Parser {

  public INJasperCountyParser() {
    super(CITY_CODES, "JASPER COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "noreply@jaspercountypolice.com";
  }

  private static final Pattern DIR_PTN = Pattern.compile("[NSEW]B");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (! super.parseMsg(subject, body, data)) return false;
    if (DIR_PTN.matcher(data.strPlace).matches()) {
      data.strAddress = append(data.strAddress, " ", data.strPlace);
      data.strPlace = "";
    }
    return true;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AND", "ANDERSON",
      "ANG", "ANGOLA",
      "BAT", "BATTLE GROUND",
      "BLO", "BLOOMINGTON",
      "BOO", "BOONE GROVE",
      "BOS", "BOSWELL",
      "BRK", "BROOKSTON",
      "BRO", "BROOK",
      "BUF", "BUFFALO",
      "CED", "CEDAR LAKE",
      "CHA", "CHALMERS",
      "CHE", "CHESTERTON",
      "COV", "COVINGTON",
      "CRA", "CRAWFORDSVILLE",
      "CRO", "CROWN POINT",
      "DAY", "DAYTON",
      "DEM", "DEMOTTE",
      "DYE", "DYER",
      "EAR", "EARL PARK",
      "EAS", "EAST CHICAGO",
      "EVA", "EVANSVILLE",
      "FAI", "FAIR OAKS",
      "FOW", "FOWLER",
      "FRA", "FRANCESVILLE",
      "FRN", "FRANKFORT",
      "GAR", "GARY",
      "GOO", "GOODLAND",
      "GOS", "GOSHEN",
      "GRI", "GRIFFITH",
      "HAM", "HAMMOND",
      "HEB", "HEBRON",
      "HIG", "HIGHLAND",
      "HOB", "HOBART",
      "IND", "INDIANAPOLIS",
      "KEN", "KENTLAND",
      "KNO", "KNOX",
      "KOK", "KOKOMO",
      "KOU", "KOUTS",
      "LAE", "LAKE STATION",
      "LAF", "LAFAYETTE",
      "LAK", "LAKE VILLAGE",
      "LAP", "LAPORTE",
      "LOW", "LOWELL",
      "MAR", "MARION",
      "MED", "MEDARYVILLE",
      "MER", "MERRILLVILLE",
      "MNC", "MUNCIE",
      "MNT", "MONTICELLO",
      "MON", "MONON",
      "MOR", "MOROCCO",
      "MOU", "MOUNT AYR",
      "MUN", "MUNSTER",
      "NOB", "NOBLESVILLE",
      "NOR", "NORTH JUDSON",
      "OTT", "OTTERBEIN",
      "OXF", "OXFORD",
      "POR", "PORTAGE",
      "REM", "REMINGTON",
      "REN", "RENSSELAER",
      "REY", "REYNOLDS",
      "RIC", "RICHMOND",
      "ROC", "ROCHESTER",
      "ROS", "ROSELAWN",
      "SAI", "SAINT JOHN",
      "SAN", "SAN PIERRE",
      "SBD", "SOUTH BEND",
      "SCH", "SCHERERVILLE",
      "SCN", "SCHNEIDER",
      "SHE", "SHELBY",
      "TEF", "TEFFT",
      "TER", "TERRE HAUTE",
      "THA", "THAYER",
      "VAL", "VALPARAISO",
      "WAR", "WARSAW",
      "WHE", "WHEATFIELD",
      "WHI", "WHITING",
      "WHL", "WHEELER",
      "WIL", "WILLIAMSPORT",
      "WIN", "WINAMAC",
      "WLA", "WEST LAFAYETTE",
      "WOL", "WOLCOTT"
  });
}
