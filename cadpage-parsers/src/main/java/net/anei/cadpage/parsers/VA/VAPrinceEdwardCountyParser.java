package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Prince Edward County, VA
 */
public class VAPrinceEdwardCountyParser extends DispatchDAPROParser {
  
  public VAPrinceEdwardCountyParser() {
    super(CITY_CODE_TABLE, "PRINCE EDWARD COUNTY", "VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@farmvilleva.com";
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
      "ABDOMINAL/BACK PAIN",
      "ACCIDENT NO INJURY",
      "ACCIDENT NO INJURY BUSH",
      "ACCIDENT NO INJURY PRINCE",
      "ACCIDENT WITH INJURIES",
      "ACCIDENT WITH INJURIES PRINCE",
      "BRUSH / FOREST FIRE",
      "CHEST PAINS",
      "COMMERCIAL STRUCTURE FIRE",
      "COMMERCIAL STRUCTURE FIRE HSC /",
      "DIABETIC",
      "DIFFICULTY BREATHING",
      "DISABLED VEHICLE",
      "FALL",
      "FIRE ALARM",
      "FIRE (UNKNOWN)",
      "FULL CARDIAC ARREST",
      "HAZMAT/SPILL",
      "MEDICAL ALARM",
      "ODOR INVESTIGATION",
      "PUBLIC ASSISTANCE REQUEST",
      "PUBLIC ASSISTANCE (RESCUE)",
      "RESCUE CALL",
      "SEIZURES",
      "SHOOTING",
      "SICK",
      "SMOKE INVESTIGATION",
      "SMOKE INVESTIGATION (OUTSIDE)",
      "STROKE",
      "STRUCTURE FIRE",
      "TRASH FIRE",
      "TRASH FIRE (RUBBISH)",
      "TREE DOWN",
      "UNCONSCIOUS",
      "VEHICLE/EQUIPMENT FIRE"
  );
  
  private static final Properties CITY_CODE_TABLE =
    buildCodeTable(new String[]{
        "FAR", "FARMVILLE",
        "MEH", "MEHERRIN",
        "PAM", "PAMPLIN CITY",
        "PRO", "PROSPECT",
        "RIC", "RICE"
    });
}