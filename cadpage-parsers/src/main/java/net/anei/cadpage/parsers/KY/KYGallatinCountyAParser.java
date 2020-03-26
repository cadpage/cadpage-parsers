package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

/**
 * Gallatin County, KY
 */
public class KYGallatinCountyAParser extends DispatchA29Parser {
  
  private static final Pattern US_HWY_PTN = Pattern.compile("\\bU +S\\b", Pattern.CASE_INSENSITIVE);
  
  public KYGallatinCountyAParser() {
    super(CITY_LIST, "GALLATIN COUNTY", "KY");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "CEDAR POINTE",
        "CONCORD CHURCH",
        "DRY CREEK",
        "EAGLE TUNNEL",
        "EAST MAIN",
        "EAST PEARL",
        "KNOX LILLARD",
        "LONE OAK",
        "MAIN CROSS",
        "MEADOW CREEK",
        "MILLERS RIDGE",
        "RED CROSS",
        "ROYAL PINES",
        "STEELES BOTTOM",
        "VERA CRUZ",
        "WEST MAIN",
        "WILLOW POINTE"
    );
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = US_HWY_PTN.matcher(body).replaceAll("US");
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equalsIgnoreCase("GALLATIN COUNTY")) data.strCity = "";
    
    // Sometimes intersections need an implied &
    String addr = data.strAddress;
    if (!addr.contains("&")) {
      data.strAddress = "";
      parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_IMPLIED_INTERSECT | FLAG_NO_IMPLIED_APT | FLAG_ANCHOR_END, addr, data);
    }
    return true;
  }

  @Override
  protected void parseAddress(StartType sType, int flags, String address, Data data) {
    address = address.replace('@', '&');
    super.parseAddress(sType, flags, address, data);
  }

  private static final String[] CITY_LIST = new String[]{
    "GALLATIN COUNTY",
    "GLENCOE",
    "SPARTA",
    "WARSAW",
    
    "BOONE COUNTY",
    "NE VERONA",
    "VERONA",
    
    "CARROLL COUNTY",
    "CARROLLTON",
    "GHENT",
    "SANDERS",
    
    "KENTON COUNTY",
    "ERLANGER"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "1015",
      "1088",
      "4-WHEELER COMPLAINT",
      "ABANDONED VEHICLE",
      "ABDOMINAL PAIN",
      "ALARM-BURGLAR-COMMERCIAL",
      "ALCOHOL INTOXICATION",
      "ANIMAL COMPLAINT",
      "ASSIST ANOTHER AGENCY",
      "ATTEMPT TO LOCATE",
      "ATTEMPT TO SERVE",
      "B  AND  E",
      "BURGLARY-COMMERCIAL",
      "CHEST PAINS",
      "COMPLAINT",
      "CONTROLLED BURN",
      "CUSTODY DISPUTE",
      "CUSTODY EXCHANGE",
      "CRIMINAL MISCHIEF",
      "DIFFICULTY BREATHING",
      "DISABLED VEHICLE",
      "DISPUTE-NEIGHBOR/OTHER",
      "DOG BITE",
      "DOMESTIC COMPLAINT",
      "DRIVING UNDER INFLUENCE",
      "DRUG ACTIVITY",
      "FALL UNDER 6 FEET",
      "FALL UNDER SIX FEET",
      "FIRE ALARM",
      "FIRE-GENERAL",
      "FOLLOW-UP INVESTIGATION",
      "GAS ODOR",
      "GENERAL ILLNESS",
      "EMOTIONAL CRISIS",
      "HARASSING COMMUNICATIONS",
      "HARRASSMENT",
      "HIT AND RUN",
      "JUVENILE PROBLEMS",
      "LACERATION",
      "LIFTING ASSIST",
      "LINES DOWN (ELECTRIC, TELEPHONE, ETC.)",
      "LOCK OUT",
      "LOUD MUSIC COMPLAINT",
      "LOUD NOISE COMP",
      "MEDICAL",
      "MENTAL STATUS CHANGE",
      "MISSING JUVENILE",
      "MOTORIST ASSIST",
      "NON-EMERGENCY TRANSPORT",
      "OUT WITH SUBJECT",
      "PAPER SERVICE",
      "PRISONER TRANSPORT",
      "PROPERTY DAMAGE",
      "RECKLESS DRIVING",
      "REMOVE UNWANTED SUBJECT",
      "REQUEST EXTRA PATROL",
      "REQUEST SPEAK OFFICER",
      "RESCUE/AMBULANCE",
      "SPEEDWAY POLICE",
      "STROKE",
      "SUICIDAL SUBJECT",
      "SUSPICIOUS CIRCUMSTANCES",
      "SUSPICIOUS PERSON",
      "SUSPICIOUS VEHICLE",
      "TERRORISTIC THREATENING",
      "TEST CALL",
      "THEFT",
      "TRAFFIC ACCIDENT INJURIES",
      "TRAFFIC ACCIDENT NON-INJURY",
      "TRAFFIC ACCIDENT-INJURIES",
      "TRAFFIC ACCIDENT - VS DEER",
      "TRAFFIC COMPLAINT",
      "TRAFFIC STOP",
      "UNAUTHORIZED USE OF VEHICLE",
      "UNCONSCIOUS",
      "UNKNOWN PROBLEM",
      "VANDALISM",
      "WEATHER ALERTS",
      "WELFARE CHECK"
  );
}
