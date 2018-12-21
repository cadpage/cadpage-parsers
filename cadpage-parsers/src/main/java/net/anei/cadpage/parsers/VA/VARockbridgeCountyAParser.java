package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Rockbridge County, VA
 */
public class VARockbridgeCountyAParser extends DispatchDAPROParser {
  
  public VARockbridgeCountyAParser() {
    super(CITY_CODE_TABLE, "ROCKBRIDGE COUNTY", "VA");
    setupCallList(CALL_SET);
    setupMultiWordStreets(
        "MAURY RIVER",
        "MOUNTAIN VIEW",
        "OLD BUENA VISTA");
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@Rockregional911.org,MAILBOX@lexingtonva.gov";
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
      "9-1-1 HANGUP WITH NO CONTACT",
      "ABDOMINAL PAIN",
      "ACCIDENT FIRE DEPT ONLY",
      "ACCIDENT NO INJURY",
      "ACCIDENT WITH INJURY",
      "ALLERGIC REACTION",
      "BRUSH FIRE GREATER THAN 25' AW",
      "BRUSH FIRE GREATER THAN 50' AW",
      "CHEST PAINS/CARDIAC",
      "CHIMNEY FIRE",
      "CHOKING",
      "COMMERCIAL NATURAL GAS/PROPANE",
      "COMMERCIAL VEHICLE FIRE",
      "DEBRIS IN ROADWAY",
      "DIABETIC",
      "DIZZY, SICK, ETC.",
      "EMS SERVICE CALL",
      "FALL",
      "FIRE ALARM",
      "FIRE SERVICE CALL",
      "FLUID LEAK FROM VEHICLE NO INJ",
      "FRACTURE OR DISLOCATION",
      "HAZARDOUS MATERIALS CALL",
      "HEMMORHAGING",
      "LACERATION",
      "MEDICAL ALARM/LIFELINE ALARM",
      "MENTAL HEALTH SUBJECT/TRANSPOR",
      "OVERDOSE",
      "PAIN NOT RESULTING FORM AN INJ",
      "POISONING",
      "PUBLIC WORKS AFTER HOURS CALL",
      "SEIZURES",
      "SERVICE CALL",
      "SHORT OF BREATH/DIFF BREATHING",
      "STROKE",
      "STRUCTURE FIRE",
      "SWIFT WATER TEAM CALL",
      "TECHNICAL RESCUE CALL",
      "TEST CALL FOR ECC",
      "UNKNOWN MEDICAL EMERGENCY",
      "UNRESPONSIVE",
      "VEHICLE FIRE",
      "WEATHER RELATED CALL W/ NO DIS",
      "WEATHER RELATED FIRE DISPATCH",
      "WIRE DOWN/TRANSFORMER FIRE"
      
  );
  
  private static final Properties CITY_CODE_TABLE =
    buildCodeTable(new String[]{
        "BUE", "BUENA VISTA",
        "LEX", "LEXINGTON",
        "GOS", "GOSHEN",
        "GLA", "GLASGOW",
        "FAI", "FAIRFIELD",
        "NAT", "NATURAL BRIDGE",
        "RAP", "RAPHINE",
        "BRO", "BROWNSBURG"
    });
}