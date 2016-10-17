package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class SCMarionCountyParser extends DispatchB2Parser {

  public SCMarionCountyParser() {
    super("E911CENTRALMARIONCOUNTY:", CITY_LIST, "MARION COUNTY", "SC");
    setupCallList(CALL_LIST);
  }
  
  @Override
  public String getFilter() {
    return "tcooper@marionsc.gov";
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "MEDICAL ALARM",
      "BRUSH FIRE",
      "RESPIRATORY DISTRESS",
      "MOTOR VEHICLE ACCIDENT",
      "FIRE ALARM COMMERCIAL",
      "CARDIAC ARREST",
      "PAIN",
      "FIRE ALARM RESIDENTIAL",
      "STRUCTURE FIRE",
      "INVESTIGATION",
      "UNCONSCIOUS-UNREPONSIVE",
      "STRUCTURE FIRE (COMMERCIAL)"
  );

  private static final String[] CITY_LIST = new String[]{
    "ARIEL CROSSROAD",
    "BRITTONS NECK",
    "CENTENARY",
    "FRIENDSHIP",
    "GRESHAM",
    "MARION",
    "MULLINS",
    "NICHOLS",
    "RAINS",
    "SELLERS",
    "TEMPERANCE HILL"
  };
}
