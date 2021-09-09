package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA29Parser;

public class INCassCountyParser extends DispatchA29Parser {

  public INCassCountyParser() {
    super(CITY_LIST, "CASS COUNTY", "IN");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "e911.pagegate@co.cass.in.us";
  }

  private static final Pattern MARKER = Pattern.compile("\\S+\\s+DISPATCH\\s+");
  private static final Pattern DIR_OF_PTN = Pattern.compile("\\b([NSEW])/O\\b");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    String s1 = subject;
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = "DISPATCH:" + body.substring(match.end());
    body = DIR_OF_PTN.matcher(body).replaceAll("$1O");
    if (!super.parseMsg(body, data)) return false;
    data.strSource = s1;
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ALARM",
      "ACCIDENT (INJURIES)",
      "ACCIDENT (PROPERTY DAMAGE)",
      "ACCIDENT (UNKNOWN)",
      "ASSIST ANOTHER AGENCY",
      "ATTEMPT TO LOCATE",
      "DISORDERLY CONDUCT",
      "DOMESTIC COMPLAINT",
      "FIRE ALARM",
      "FIRE FIELD",
      "FIRE-STRUCTURE",
      "FIRE-VEHICLE",
      "GAS ODOR",
      "GAS SPILL",
      "INTOXICATED PERSON",
      "LIFT ASSIST",
      "LIFTING ASSIST",
      "MEDICAL",
      "SUICIDAL SUBJECT",
      "SUSPICIOUS CIRCUMSTANCES",
      "TRAFFIC STOP",
      "TREES DOWN"
  );

  private static final String[] MWORD_STREET_LIST = new String[]{
      "DEER CREEK"
  };

  private static final String[] CITY_LIST = new String[]{

      //CITY

          "LOGANSPORT",

      //TOWNS

          "GALVESTON",
          "ONWARD",
          "ROYAL CENTER",
          "WALTON",
          "TWELVE MILE",
          "LUCERNE",
          "YOUNG AMERICA",

      //CENSUS-DESIGNATED PLACE

          "GRISSOM AFB",

      //UNINCORPORATED PLACES

          "ADAMSBORO",
          "ANOKA",
          "CLYMERS",
          "DEACON",
          "DUNKIRK",
          "GEORGETOWN",
          "HOOVER",
          "KENNETH",
          "LAKE CICOTT",
          "LEWISBURG",
          "LINCOLN",
          "LUCERNE",
          "METEA",
          "MIAMI BEND",
          "MOUNT PLEASANT",
          "NEW WAVERLY",
          "POTAWATOMI POINT",
          "TWELVE MILE",
          "YOUNG AMERICA",

      //EXTINCT TOWNS

          "CIRCLEVILLE",
          "TABERVILLE",

      //TOWNSHIPS

          "ADAMS",
          "BETHLEHEM",
          "BOONE",
          "CLAY",
          "CLINTON",
          "DEER CREEK",
          "EEL",
          "HARRISON",
          "JACKSON",
          "JEFFERSON",
          "MIAMI",
          "NOBLE",
          "TIPTON",
          "WASHINGTON",

      // Howard County
          "KOKOMO"


  };
}
