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

    int pt = body.indexOf("\n________");
    if (pt >= 0) body = body.substring(0,pt).trim();

    body = DIR_OF_PTN.matcher(body).replaceAll("$1O");
    if (!super.parseMsg(body, data)) return false;
    if (data.strCity.equals("U")) data.strCity = "";
    data.strSource = s1;
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 HANG UP/ MISDIAL",
      "911 CELLPHONE CALL",
      "ABDOMINAL PAIN",
      "ACCIDENT (CAR/DEER)",
      "ALARM",
      "ALARM-TROUBLE",
      "ACCIDENT (INJURIES)",
      "ACCIDENT (PROPERTY DAMAGE)",
      "ACCIDENT (UNKNOWN)",
      "ANIMAL COMPLAINT",
      "ASSIST ANOTHER AGENCY",
      "ATTEMPT TO LOCATE",
      "BURN COMPLAINT",
      "CITIZEN CONTACT/PUBLIC RELATIONS",
      "CO DETECTOR ALARM",
      "DIFFICULTY BREATHING",
      "DISORDERLY CONDUCT",
      "DOMESTIC COMPLAINT",
      "FALLS",
      "FIRE ALARM",
      "FIRE FIELD",
      "FIRE-GENERAL",
      "FIRE-STRUCTURE",
      "FIRE-VEHICLE",
      "GAS ODOR",
      "GAS SPILL",
      "INTOXICATED PERSON",
      "LIFT ASSIST",
      "LIFTING ASSIST",
      "MEDICAL",
      "OUT WITH SUBJECT/S",
      "SUICIDAL SUBJECT",
      "SUSPICIOUS CIRCUMSTANCES",
      "TEST",
      "TRAFFIC STOP",
      "TREES DOWN",
      "UNWANTED GUEST",
      "UTILITIES DOWN",
      "WELFARE CHECK"
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
