package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class MSDeSotoCountyBParser extends DispatchB2Parser {

  public MSDeSotoCountyBParser() {
    super("DISPATCH:", MSDeSotoCountyAParser.CITY_LIST, "DESOTO COUNTY", "MS", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "ANSLEY PARK",
        "CEDAR LAKE",
        "CEDAR POINT",
        "CHARLIE MOORES",
        "COLONIAL HILLS",
        "GREAT OAKS",
        "LAKE SHORE",
        "MARKET PLACE",
        "MOSS POINT",
        "OAK CHASE",
        "PECAN GROVE",
        "PECAN HILL",
        "PLUM POINT",
        "SHADOW OAKS",
        "SLEEPY HOLLOW",
        "STAR LANDING",
        "SWINNEA RIDGE",
        "WE ROSS",
        "WINNERS CIRCLE"
    );
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@southaven.org";
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ACCIDENT W/INJURIES",
      "ALTERED MENTAL STATUS",
      "ASSAULT-SIMPLE",
      "BLOOD PRESSURE",
      "CARDIAC/RESPITORY ARREST",
      "CHEST PAIN",
      "COMMERCIAL FIRE ALARM",
      "COMPLAINT-RECKLESS DRIVING",
      "DIABETIC PROBLEMS",
      "DIFFICULTY BREATHING",
      "DISTURBANCE",
      "DOA/DEATH INVESTIGATION",
      "GENERAL COMPLAINT OF PAIN",
      "FAINTED (NOW CONSCIOUS)",
      "FALLS",
      "LIFTING ASSISTANCE NO INJURIES",
      "MEDICAL ALARM",
      "MUTUAL AID DESOTO COUNTY",
      "MUTUAL AID HERNANDO",
      "MUTUAL AID HORN LAKE",
      "RESIDENTIAL FIRE ALARM",
      "SEIZURES/CONVULSIONS",
      "SERVICE CALL",
      "SICK PARTY",
      "SMELL OF SMOKE INSIDE",
      "STROKE - CVA - TIA",
      "SUICIDE-ATTEMPT",
      "SUICIDE-THREAT",
      "SUSPICIOUS PERSON",
      "TEST CALL",
      "TRAFFIC STOP",
      "UNCONSCIOUS PARTY",
      "VISIBLE SMOKE OUTSIDE",
      "WELFARE CONCERN"
  );
}
