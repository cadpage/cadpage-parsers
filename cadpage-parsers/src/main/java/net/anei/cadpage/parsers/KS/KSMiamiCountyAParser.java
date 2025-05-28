package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

public class KSMiamiCountyAParser extends SmartAddressParser {

  public KSMiamiCountyAParser() {
    super(CITY_LIST, "MIAMI COUNTY", "KS");
    setFieldList("ADDR APT CITY ST PLACE X CALL UNIT ID");
    setupMultiWordStreets(MWORD_STREETS);
    setupCallList(CALL_LIST);
    removeWords("PLAZA");
  }

  @Override
  public String getFilter() {
    return "noreply@sheriffmiamicountyks.gov";
  }

  private static final Pattern TRAIL_ID_PTN = Pattern.compile("\\b\\d{4}-\\d{6}$");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile("(?:\\b(?:[A-Z]+PAGER|\\d{3,4}|[A-Z]{1,3}\\d{1,3})(?:; )?)+$");
  private static final Pattern TRAIL_CALL_PTN = Pattern.compile("\\b([ A-Z]+(?: \\(FIRE\\))?)$");
  private static final Pattern LEAD_CITY_ST_PTN = Pattern.compile("([A-Z ]+), *([A-Z]{2})(?: +\\d{5})? +");
  private static final Pattern LEAD_CITY_ZIP_PTN = Pattern.compile("([A-Z ]+?) +\\d{5}\\b *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("DISPATCHED INCIDENT")) return false;

    Matcher match = TRAIL_ID_PTN.matcher(body);
    if (!match.find()) return false;
    body = body.substring(0,match.start()).trim();
    data.strCallId = match.group();

    match = TRAIL_UNIT_PTN.matcher(body);
    if (match.find()) {
      body = body.substring(0,match.start()).trim();
      data.strUnit = match.group().replace("; ", ",");
    }

    String call = CALL_LIST.getCode(body);
    if (call != null) {
      data.strCall = call;
      body = body.substring(0, body.length()-call.length()).trim();
    } else {
      match = TRAIL_CALL_PTN.matcher(body);
      if (match.find()) {
        data.strCall = match.group();
        body = body.substring(0, match.start()).trim();
      }
      else data.strCall = "ALERT";
    }

    int pt = body.indexOf(',');
    if (pt >= 0) {
      parseAddress(body.substring(0, pt).trim(), data);
      body = body.substring(pt+1).trim();

      match = LEAD_CITY_ST_PTN.matcher(body);
      if (match.lookingAt()) {
        data.strCity = match.group(1).trim();
        data.strState = match.group(2);
        body = body.substring(match.end());
      }
      else if ((match = LEAD_CITY_ZIP_PTN.matcher(body)).lookingAt()) {
        data.strCity =  match.group(1).trim();
        body = body.substring(match.end());
      }
      else {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body, data);
        body = getLeft();
      }

      if (body.endsWith(")")) {
        pt = body.indexOf('(');
        if (pt < 0) return false;
        parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS | FLAG_ANCHOR_END, body.substring(0,pt).trim(), data);
        if (data.strCross.isEmpty()) {
          data.strCross = data.strPlace;
          data.strPlace = "";
        }
        data.strCross = append(data.strCross, " ", body.substring(pt));
      } else {
        data.strPlace = body;
      }
    }

    else {
      parseAddress(body, data);
    }
    return true;
  }

  private static final String[] MWORD_STREETS = new String[] {
      "BROWN CIRCLE",
      "INDUSTRIAL PARK",
      "MAIN BUILDING",
      "MISSION BELLEVIEW",
      "PLEASANT VALLEY",
      "STATE HOSPITAL",
      "STATE LINE",
      "STATE PARK CAMPGROUND"
  };

  private static final ReverseCodeSet CALL_LIST = new ReverseCodeSet(new String[] {
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
      "DISTURBANCE",
      "EMS ASSIST",
      "EMS ASSIST (FIRE)",
      "FUNERAL ESCORT",
      "GAS LEAK (FIRE)",
      "GRASS FIRE (FIRE)",
      "HARASSMENT",
      "HOUSE FIRE",
      "INJURY ACCIDENT (FIRE)",
      "JUVENILE ACTIVITY",
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
      "THEFT",
      "TRAFFIC HAZARD",
      "TRAFFIC STOP",
      "Traffic Stop",
      "UNWANTED SUBJECT",
      "VEHICLE FIRE",
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

      // Linn County
      "LINN COUNTY",
      "LA CYGNE",

      // Miami County
      "MIAMI COUNTY"
  };
}
