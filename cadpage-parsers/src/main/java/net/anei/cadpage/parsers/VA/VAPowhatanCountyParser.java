package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;


public class VAPowhatanCountyParser extends DispatchDAPROParser {
  
  public VAPowhatanCountyParser() {
    super(CITY_CODE_TABLE, "POWHATAN COUNTY", "VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@powhatansheriff.net";
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
      "ALLERGIC REACTION",
      "ASSIST CITIZEN",
      "BACK/NECK PAIN",
      "BLEED/UNCONTROLLED",
      "BREATHING DIFFICULTY",
      "CARDIAC/RESPIRATORY/FULL ARRES",
      "CHEST PAIN/CARDIAC OVER 30",
      "CHEST PAIN/CARDIAC UNDER 30",
      "CHOKING/AIR EXCHANGE CHILD",
      "DIABETIC",
      "DOMESTIC/FAMILY VIOLENCE NEEDI",
      "FALLS LESS THAN 10 FEET",
      "HEAD/NECK INJURY",
      "MVA/DEER",
      "MVA/ENTRAPMENT",
      "MVA/INJURIES",
      "MVA/MOTOR VEHICLE ACCIDENT",
      "MUTUAL AID",
      "RECKLESS DRIVER",
      "SEIZURES/CONVULSIONS/ACTIVE",
      "SICK CALL",
      "STROKE",
      "STRUCTURE FIRE",
      "SUICIDE/THREAT/ATTEMPT",
      "TRAUMA/MAJOR",
      "UNCONSCIOUS/ALTERED LEVEL CONS",
      "VEHICLE FIRE ON",
      "WARRANT SERVICE"
      
  );
  
  private static final Properties CITY_CODE_TABLE =
    buildCodeTable(new String[]{
 
        "BEA", "BEAUMONT",
        "BRO", "POWHATAN",
        "FOX", "POWHATAN",
        "FRE", "POWHATAN",
        "GEN", "POWHATAN",
        "HIG", "POWHATAN",
        "HOL", "POWHATAN",
        "LAK", "POWHATAN",
        "MAT", "POWHATAN",
        "MID", "MIDLOTHIAN",
        "MIL", "POWHATAN",
        "MON", "POWHATAN",
        "OUT", "OUT OF COUNTY",
        "POW", "POWHATAN",
        "RED", "RED LANE",
        "SPE", "POWHATAN",
        "TAM", "MIDLOTHIAN",
        "WAL", "POWHATAN",

        
    });
}