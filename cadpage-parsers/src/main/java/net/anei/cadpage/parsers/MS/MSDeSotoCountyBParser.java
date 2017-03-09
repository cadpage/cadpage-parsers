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
        "CHARLIE MOORES",
        "COLONIAL HILLS",
        "GREAT OAKS",
        "LAKE SHORE",
        "MARKET PLACE",
        "MOSS POINT",
        "OAK CHASE",
        "PLUM POINT",
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
      "FALLS",
      "LIFTING ASSISTANCE NO INJURIES",
      "MEDICAL ALARM",
      "MUTUAL AID DESOTO COUNTY",
      "MUTUAL AID HERNANDO",
      "RESIDENTIAL FIRE ALARM",
      "SEIZURES/CONVULSIONS",
      "SICK PARTY",
      "SMELL OF SMOKE INSIDE",
      "STROKE - CVA - TIA",
      "SUICIDE-THREAT",
      "SUSPICIOUS PERSON",
      "TEST CALL",
      "TRAFFIC STOP",
      "UNCONSCIOUS PARTY",
      "VISIBLE SMOKE OUTSIDE"
  );
}
