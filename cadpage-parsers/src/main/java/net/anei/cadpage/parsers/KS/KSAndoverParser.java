package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGlobalDispatchParser;



public class KSAndoverParser extends DispatchGlobalDispatchParser {

  public KSAndoverParser() {
    super("ANDOVER", "KS");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "BLACK LOCUST",
        "CEDAR RIDGE",
        "HARVEST RIDGE",
        "HEATHER LAKE",
        "KERRY LYNN",
        "SANTA FE LAKE",
        "WAGON WHEEL"
    );
  }
  
  @Override
  public String getFilter() {
    return "DispatchMail@andoverks.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if(!subject.equals("CAD Call")) return false;
    
    body = body.replace('\n', ' ');
    if (!super.parseMsg(subject, body, data)) return false;
    
    if (data.strCity.endsWith(" KS")) {
      data.strCity = data.strCity.substring(0,data.strCity.length()-3).trim();
    }
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "911 OPEN LINE/HANG-UP",
      "ALARM - ENTRY",
      "ANIMAL - AT LARGE",
      "ANIMAL - NEGLECT",
      "ANIMAL - NOISE",
      "ASSAULT - THREAT",
      "ASSIST - CITIZEN",
      "ASSIST - LAW ENFORCEMENT",
      "CHECK WELFARE",
      "CIVIL MATTER",
      "DISTURBANCE - DOMESTIC VIOLENCE",
      "DOCUMENTATION",
      "FIRE - AIRCRAFT EMERGENCY",                                                                              
      "FIRE - ALARM COMMERCIAL",
      "FIRE - ALARM RESIDENTIAL",
      "FIRE - BRUSH/GRASS FIRE",                                                                                
      "FIRE - BUILDING FIRE RESIDENTIAL",                                                                       
      "FIRE - CHECK A FIRE THAT IS OUT",
      "FIRE - CHECK ELEC WIRING/APPLIANCE",
      "FIRE - CO DETECTOR ACTIVATION",
      "FIRE - FUEL WASH DOWN",
      "FIRE - HAZ-MAT RESPONSE",                                                                                
      "FIRE - LANDING ZONE",
      "FIRE - MEDICAL RESPONSE",
      "FIRE - NATURAL GAS ODOR",
      "FIRE - POWER LINES DOWN/ARCING",
      "FIRE - PUBLIC ASSIST",
      "FIRE - RESCUE RESPONSE",
      "FIRE - SMOKE CHECK OUTSIDE",
      "FIRE - SMOKE DETECTOR ACTIVATION",
      "FIRE - TRASH FIRE/RUBBISH",
      "FIRE - VEHICLE FIRE",
      "FRAUD/FORGERY - REPORT",
      "JUVENILE COMPLAINT",
      "MISSING PERSON - RUNAWAY",
      "MVA - INJURY",
      "MVA - NON-INJURY",
      "MVA - PEDESTRIAN",
      "OTHER - NOT CLASSIFIED",
      "SECURITY CHECK",
      "SUICIDE THREAT/ATTEMPT",
      "SUSPICIOUS ACTIVITY",
      "THEFT - REPORT",
      "TRAFFIC - ABANDONED VEH",
      "TRAFFIC - ASSIST",
      "TRAFFIC - COMPLAINT",
      "TRAFFIC - LOCKOUT",
      "TRAFFIC - PARKING COMPLAINT",
      "TRAFFIC - STOP"
  );
}
