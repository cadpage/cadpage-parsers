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
        "PINE VIEW",
        "PRAIRIE CREEK",
        "SANTA FE LAKE",
        "SHADOW ROCK",
        "WAGON WHEEL"
    );
  }

  @Override
  public String getFilter() {
    return "DispatchMail@andoverks.com,@andoverks.gov";
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
      "ALARM - HOLD-UP",
      "ANIMAL - AT LARGE",
      "ANIMAL - NEGLECT",
      "ANIMAL - NOISE",
      "ANIMAL - POUND CHECK",
      "ASSAULT - THREAT",
      "ASSIST - CITIZEN",
      "ASSIST - LAW ENFORCEMENT",
      "BLDG CHECK/OPEN DOOR",
      "CHECK WELFARE",
      "CIVIL MATTER",
      "CODE COMPLIANCE",
      "DISTURBANCE - DOMESTIC VIOLENCE",
      "DISTURBANCE - OTHER",
      "DISTURBANCE - PHYSICAL",
      "DISTURBANCE - VERBAL",
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
      "HARASS BY PHONE",
      "JUVENILE COMPLAINT",
      "LITTERING/TRASH",
      "MISSING PERSON - RUNAWAY",
      "MVA - INJURY",
      "MVA - NON-INJURY",
      "MVA - PEDESTRIAN",
      "NOISE COMPLAINT",
      "OTHER - NOT CLASSIFIED",
      "PROPERTY - FOUND",
      "PROPERTY - LOST",
      "SECURITY CHECK",
      "SUICIDE THREAT/ATTEMPT",
      "SUSPICIOUS ACTIVITY",
      "SUSPICIOUS - PERSON",
      "SUSPICIOUS - VEHICLE",
      "THEFT - REPORT",
      "TRAFFIC - ABANDONED VEH",
      "TRAFFIC - ASSIST",
      "TRAFFIC - COMPLAINT",
      "TRAFFIC - LOCKOUT",
      "TRAFFIC - PARKING COMPLAINT",
      "TRAFFIC - STOP"
  );
}
