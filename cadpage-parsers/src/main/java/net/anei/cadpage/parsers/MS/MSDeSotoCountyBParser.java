package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class MSDeSotoCountyBParser extends DispatchB2Parser {

  public MSDeSotoCountyBParser() {
    super("DISPATCH:", MSDeSotoCountyAParser.CITY_LIST, "DESOTO COUNTY", "MS");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "ANSLEY PARK",
        "MOSS POINT",
        "SWINNEA RIDGE",
        "WINNERS CIRCLE"
   );
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@southaven.org";
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT W/INJURIES",
      "ASSAULT-SIMPLE",
      "BLOOD PRESSURE",
      "CARDIAC/RESPITORY ARREST",
      "CHEST PAIN",
      "DIFFICULTY BREATHING",
      "FALLS",
      "LIFTING ASSISTANCE NO INJURIES",
      "MEDICAL ALARM",
      "MUTUAL AID DESOTO COUNTY",
      "SEIZURES/CONVULSIONS",
      "SICK PARTY",
      "SMELL OF SMOKE INSIDE",
      "STROKE - CVA - TIA",
      "SUICIDE-THREAT",
      "SUSPICIOUS PERSON",
      "TRAFFIC STOP",
      "UNCONSCIOUS PARTY"
  );
}
