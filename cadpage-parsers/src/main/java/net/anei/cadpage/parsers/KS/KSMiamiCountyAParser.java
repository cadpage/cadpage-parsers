package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KSMiamiCountyAParser extends SmartAddressParser {

  public KSMiamiCountyAParser() {
    super(CITY_LIST, "MIAMI COUNTY", "KS");
    setFieldList("CALL ADDR APT CITY ST PLACE X ID");
    setupMultiWordStreets(MWORD_STREETS);
    setupCallList(CALL_LIST);
    removeWords("PLAZA");
  }

  @Override
  public String getFilter() {
    return "noreply@sheriffmiamicountyks.gov";
  }

  private static final Pattern MASTER = Pattern.compile("Incident in progress (.*) (\\d{4}-\\d{6})");
  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("([A-Z ]+), ([A-Z]{2})(?: \\d{5})?\\b *(.*)");
  private static final Pattern CITY_ZIP_PTN = Pattern.compile("([A-Z ]+) \\d{5}\\b *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).trim();
    data.strCallId = match.group(2);

    Parser p = new Parser(body);
    String callAddr = p.get(',');
    String cityX = p.get();
    parseAddress(StartType.START_CALL, FLAG_ANCHOR_END, callAddr.replace(" AT ", " & "), data);
    if (data.strAddress.isEmpty()) {
      data.strAddress = data.strCall;
      data.strCall = "ALERT";
    }
    else if (data.strCall.endsWith("/") || data.strCall.endsWith("&")) {
      data.strAddress = append(data.strCall.substring(0, data.strCall.length()-1).trim(), " & ", data.strAddress);
      data.strCall = "ALERT";
    }
    else if (data.strCall.isEmpty()) {
      data.strCall = "ALERT";
    }

    if ((match = CITY_ST_ZIP_PTN.matcher(cityX)).matches()) {
      data.strCity = match.group(1).trim();
      data.strState = match.group(2);
      cityX = match.group(3);
    }
    else if ((match = CITY_ZIP_PTN.matcher(cityX)).matches()) {
      data.strCity = match.group(1).trim();
      cityX = match.group(2).trim();
    }
    else {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, cityX, data);
      cityX = getLeft();
    }

    if (!cityX.isEmpty()) {
      String xExt = "";
      int pt = cityX.lastIndexOf('(');
      if (pt >= 0) {
        xExt = cityX.substring(pt);
        cityX = cityX.substring(0,pt).trim();
      }
      parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS | FLAG_ANCHOR_END, cityX, data);
      data.strCross = append(data.strCross, " ", xExt);
    }
    return true;
  }

  private static final String[] MWORD_STREETS = new String[] {
      "BETHEL CHURCH",
      "BROWN CIRCLE",
      "CEDAR NILES",
      "COLD WATER SPRINGS",
      "HEDGE LANE",
      "INDUSTRIAL PARK",
      "KANSAS CITY",
      "LAKE MIOLA",
      "MAIN BUILDING",
      "MISSION BELLEVIEW",
      "OSAWATOMIE PARKER",
      "PLEASANT VALLEY",
      "PLUM CREEK",
      "ROHRER HEIGHTS",
      "STATE HOSPITAL",
      "STATE LINE",
      "STATE PARK CAMPGROUND"
  };

  private static final CodeSet CALL_LIST = new CodeSet(new String[] {
      "911 MISDIAL / HANGUP",
      "ALARM CALL",
      "ALERT",
      "ANIMAL COMPLAINT",
      "ANIMAL CONTROL",
      "ASSIST OUTSIDE AGENCY",
      "AUTOMATIC FIRE ALARM (FIRE)",
      "BATTERY",
      "CHECK WELFARE",
      "CHILD IN NEED OF CARE",
      "CITIZEN ASSIST",
      "CIVIL MATTER",
      "CIVIL PAPER",
      "CIVIL STANDBY",
      "DEBRIS IN ROADWAY",
      "DISTURBANCE",
      "EMS ASSIST",
      "EMS ASSIST (FIRE)",
      "EXTRA PATROL",
      "FIELD/GRASS FIRE (FIRE)",
      "FOLLOW UP",
      "FUNERAL ESCORT",
      "GAS LEAK (FIRE)",
      "GRASS FIRE (FIRE)",
      "HARASSMENT",
      "HOUSE FIRE",
      "INJURY ACCIDENT (FIRE)",
      "INVESTIGATION (FIRE)",
      "JUVENILE ACTIVITY",
      "LINES DOWN (FIRE)",
      "LIVESTOCK OUT",
      "MISCELLANEOUS",
      "MOTORIST ASSIST",
      "NOISE COMPLAINT",
      "NON INJURY ACCIDENT",
      "OTHER FIRE",
      "PARKING COMPLAINT",
      "PEDESTRIAN CHECK",
      "PROPERTY DAMAGE",
      "SUICIDAL SUBJECT",
      "SUPICIOUS ACTIVITY",
      "SUSPICIOUS PERSON",
      "SUSPICIOUS VEHICLE",
      "TEST CALL",
      "THEFT",
      "TRAFFIC HAZARD",
      "TRAFFIC STOP",
      "Traffic Stop",
      "UNWANTED SUBJECT",
      "VEHICLE CHECK",
      "VEHICLE FIRE",
      "VEHICLE LOCKOUT",
      "WARRANT"
  });

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "FONTANA",
      "LOUISBURG",
      "OSAWATOMIE",
      "PAOLA",
      "SPRING HILL",

      // Unincorporated communities
      "BEAGLE",
      "BLOCK",
      "BUCYRUS",
      "HILLSDALE",
      "JINGO",
      "NEW LANCASTER",
      "SOMERSET",
      "STANTON",
      "WAGSTAFF",
      "WEA",

      // Franklin County
      "LANE",

      // Linn County
      "LINN COUNTY",
      "LA CYGNE",

      // Miami County
      "MIAMI COUNTY"
  };
}
