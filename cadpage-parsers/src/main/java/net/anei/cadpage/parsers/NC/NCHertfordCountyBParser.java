
package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;

public class NCHertfordCountyBParser extends DispatchSouthernPlusParser {

  public NCHertfordCountyBParser() {
    super(CITY_LIST, "HERTFORD COUNTY", "NC",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR_LEAD_PLACE | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE2 | DSFLG_X |DSFLG_NAME | DSFLG_PHONE | DSFLG_ID | DSFLG_TIME);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "AHOSKIE COFIELD",
        "BENTHALL BRIDGE",
        "DR MARTIN LUTHER KING JR",
        "EARLY STATION",
        "FLEA HILL",
        "JERNIGAN AIRPORT",
        "JIM HARDY",
        "LEE HARMOND",
        "LIVERMAN MILL",
        "MENOLA ST JOHN",
        "MORRIS FORD",
        "NEWSOME GROVE",
        "QUAIL RIDGE",
        "ST JOHN MILLENNIUM",
        "TRI COUNTY AIRPORT"
    );
    removeWords("APT", "BLDG", "RCH");
  }

  @Override
  public String getFilter() {
    return "@hertfordcountync.gov,777";
  }

  private static final Pattern PREFIX_PTN = Pattern.compile("[A-Za-z0-9 ]+: *");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("HWY", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = PREFIX_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strAddress.startsWith("LANDING ZONE ")) {
      data.strPlace = append("LANDING ZONE", " - ", data.strPlace);
      data.strAddress = data.strAddress.substring(13).trim();
    }
    data.strAddress = stripSector(data.strAddress, data);
    data.strApt = stripSector(data.strApt, data);
    if (data.strApt.length() > 0) {
      if (ADDR_APT_PTN.matcher(data.strApt).matches()) {
        data.strAddress = append(data.strAddress, " ", data.strApt);
      } else {
        data.strPlace = append(data.strPlace, " - ", data.strApt);
      }
      data.strApt = "";
    }
    if (data.strCity.equalsIgnoreCase("AHOSIE")) data.strCity = "AHOSKIE";
    return true;
  }

  private static String stripSector(String field, Data data) {
    if (data.strMap.length() > 0) return field;
    int pt = field.indexOf(" - ");
    if (pt >= 0) {
      data.strMap = field.substring(pt+3).trim();
      return field.substring(0,pt).trim();
    }
    if (field.startsWith("- ")) {
      data.strMap = field.substring(2).trim();
      return "";
    }
    return field;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "MAP CITY");
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("UNION")) return "AHOSKIE";
    return city;
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "050 ACCIDENT",
      "077 FIRE CALL",
      "078 ALARM",
      "137 HELICOPTER",
      "ACCIDENT VEHICLE/PD",
      "ACCIDENT VEHICLE/PI",
      "ACCIDENT VEHICLE/NO INJURY",
      "EMS (CARDIAC)",
      "EMS (MEDICAL)",
      "EMS (INJURY)",
      "EMS (OTHER)",
      "EMS (UNKNOWN)",
      "FIRE (BRUSH)",
      "FIRE (OTHER)",
      "FIRE (RESIDENTAL)",
      "FIRE (UNKNOWN)",
      "FIRE (VEHICLE)",
      "FIRE SMOKE ALARM"
  );

  private static final String[] CITY_LIST = new String[]{

    "AHOSKIE",
    "AHOSIE",   // Misspelled
      "ST JOHN",
      "UNION",
    "COFIELD",
    "COMO",
    "HARRELLSVILLE",
    "MILLENNIUM",
    "MURFREESBORO",
    "WINTON",

    // Bertie County
    "AULANDER",
    "COLERAIN",

    // Northampton County
    "WOODLAND"

  };
}
