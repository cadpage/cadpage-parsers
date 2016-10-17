package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Franklin County, VA
 */
public class VAFranklinCountyAParser extends DispatchDAPROParser {
  
  public VAFranklinCountyAParser() {
    super(CITY_CODE_TABLE, "FRANKLIN COUNTY", "VA");
    setupCallList(CALL_SET);
  }
      
  
  @Override
  public String getFilter() {
    return "mailbox@franklincountyva.org";
  }
  
  private static final CodeSet CALL_SET = new CodeSet(
      "911 HANG UP",
      "ABDOMINAL PAIN",
      "ACCIDENT NO INJURY",
      "ACCIDENT-INJURY",
      "ALLERGIC REACTION",
      "ALTERED MENTAL STATUS",
      "ASSAULT",
      "BACK INJURY",
      "DIABETIC PROBLEMS",
      "DISABLED BOAT",
      "DRUG VIOLATION",
      "DRUNK DRIVER",
      "EMS-ALLERGIC REACTION",
      "EMS-CARDIAC",
      "EMS-CHEST PAIN",
      "EMS-DIABETIC",
      "EMS-DIFFICULTY BREATHING",
      "EMS-DOA",
      "EMS-FAINTING/PASSING OUT",
      "EMS-GEN GENERAL ILLNESS",
      "EMS-HEAD INJURY",
      "EMS-HIGH BLOOD PRESSURE",
      "EMS-LACERATION",
      "EMS-MENTAL",
      "EMS-OTHER/DEFINE",
      "EMS-OVERDOSE",
      "EMS-PATIENT FALLEN",
      "EMS-TRANSPORT",
      "EMS-PATIENT IN PAIN",
      "FIRE-ALARM RESIDENTIAL",
      "FIRE-DUMPSTER",
      "EMS-SEIZURE",
      "EMS-STROKE",
      "EMS-UNCONSCIOUS",
      "FIGHT",
      "FIRE-ALARM COMMERCIAL",
      "FIRE-ALARM RESIDENTIAL",
      "FIRE-BRUSH",
      "FIRE-SMOKE REPORT",
      "FIRE-STRUCTURE",
      "FUEL SPILL",
      "MEDICAL ALARM",
      "HIGH BLOOD PRESSURE",
      "HIT & RUN-INJURY",
      "SHOTS FIRED",
      "THREATS",
      "TREEDOWN",
      "FIRE-UTILITIES",
      "ILLEGAL BURNING COMPLAINT",
      "PUBLIC SERVICE CALL",
      "WELLBEING CHECK"
  );
  
  private static final Properties CITY_CODE_TABLE =
    buildCodeTable(new String[]{
      "BML", "Boones Mill", 
      "CAL", "Callaway",
      "FER", "Ferrum",
      "GLA", "Glade Hill",
      "HEN", "Henry",
      "PEN", "Penhook",   
      "RMT", "Rocky Mount",
      "UHL", "Union Hall",
      "WIR", "Wirtz",   
      "BAS", "Bassett",   
      "HAR", "Hardy",
      "MAR", "Martinsville",
      "MON", "Moneta"   
    });

}