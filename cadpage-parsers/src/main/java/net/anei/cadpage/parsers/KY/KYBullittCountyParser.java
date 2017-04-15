package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class KYBullittCountyParser extends DispatchB2Parser {

  public KYBullittCountyParser() {
    super("BULLITT CO 911:||BULLITT_CO_911:", CITY_LIST, "BULLITT COUNTY", "KY");
    setupCallList(CALL_LIST);
    setupSaintNames("CLAIRE");
    removeWords("LOT");
  }
  
  public String getFilter() {
    return "@c-msg.net";
  }
  
  private static final Pattern ADDR_AT_PTN = Pattern.compile("@|\\bAT(?!&T)\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = ADDR_AT_PTN.matcher(field).replaceAll("/");
    if (!super.parseAddrField(field, data)) return false;
    if (data.strApt.startsWith("-")) {
      String place = data.strApt.substring(1).trim();
      data.strPlace = append(data.strPlace, " - ", place);
      data.strApt = "";
    }
    return true;
  }
  
  @Override
  public int getExtraParseAddressFlags() {
    return FLAG_RECHECK_APT;
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 CALL / HANG UP",
      "911 CALL (STATIC ON LINE)",
      "ABANDONED VEHICLE / NOT STOLEN",
      "ADMINISTRATIVE",
      "ADMINISTRATIVE DUTIES",
      "ALARM / BURGLAR",
      "ALARM / FIRE",
      "ALARM / MEDICAL",
      "ALARM / PANIC/HOLD UP",
      "ALCOHOL INTOXICATED",
      "ALLERGIC REACTION",
      "ANIMAL COMPLAINTS",
      "ASSAULT",
      "ASSIST",
      "ATTEMPT TO LOCATE",
      "BACK INJURY",
      "BLEEDING",
      "BRUSH/GRASS/MULCH/WOODS",
      "BURGLARY REPORT",
      "BURN VICTIM",
      "CALL SUBJECT",
      "CALL SUBJECT CALL FOR",
      "CARDIAC ARREST",
      "CHEST PAIN",
      "CHILD LOCKED IN VEHICLE",
      "CODE ENFORCEMENT",
      "COMMUNITY POLICING",
      "COURT",
      "CRIMINAL MISCHIEF",
      "DARE SCHOOL DETAIL",
      "DEBRIS IN ROADWAY",
      "DECEASED SUBJECT",
      "DIABETIC EMERGENCY",
      "DIFFICULTY BREATHING",
      "DISORDERLY PERSON",
      "DISPUTE",
      "DOMESTIC",
      "DRUG INVESTIGATION",
      "DRUNK DRIVER",
      "ELECTRICUTION",
      "EMS CALL",
      "EXTRA PATROL",
      "FALL WITH INJURY",
      "FIRE DEPARTMENT CALL",
      "FIRE INVESTIGATION",
      "FIRE / STRUCTURE",
      "FIRE / VEHICLE",
      "FOLLOW-UP",
      "HARASSMENT",
      "HEADACHE",
      "HEART PROBLEMS",
      "HIT & RUN",
      "HOUSE WATCH",
      "ILLEGAL OR UNATTENDED FIRE",
      "INJURED PERSON",
      "INJURY ACCIDENT",
      "INVESTIGATION",
      "LAW ENFORCEMENT CALL",
      "LINES DOWN POWER/PHONE/CABLE",
      "LOUD PERSON OR PARTY",
      "MEET WITH SUBJECT",
      "MEET WITH SUBJECT CALL FOR",
      "MENTAL PERSON",
      "MISSING PERSON",
      "MITIGATION",
      "MOTORIST ASSIST",
      "NATURAL OR LP GAS LEAK",
      "NO INJURY ACCIDENT",
      "OPEN DOOR",
      "OUT OF CONTROL JUVENILE",
      "OVERDOSE",
      "PAPER SERVICE",
      "PARKING VIOLATION",
      "PEDESTRIAN STOP",
      "PHYSICAL ABUSE",
      "PROPERTY DAMAGE REPORT",
      "PROWLER",
      "PT ASSIST NOT INJURED",
      "PURSUIT / FOOT - VEHICLE",
      "ROAD RAGE",
      "SEARCH / RESCUE",
      "SEIZURE",
      "SERVICE OF EPO",
      "SEXUAL ABUSE",
      "SHOOTING",
      "SHOPLIFTING",
      "SICK PERSON",
      "SMOKE INVESTIGATION",
      "SMOKE INVESTIGATION",
      "SPEEDING COMPLAINT",
      "STABBING",
      "SUBJECT TRANSPORT",
      "SUBJECT W/GUN",
      "SUICIDAL SUBJECT",
      "SUICIDE ATTEMPT",
      "SUSPICIOUS VEHICLE",
      "SUSP INDIVIDUAL",
      "SUSP INDIVIDUAL (CLI)",
      "TEST",
      "THEFT REPORT",
      "THEFT REPORT HICKORY",
      "THREATENING",
      "TOW LOT ADMINISTRATION",
      "TRAFFIC CONTROL",
      "TRAFFIC HAZARD (INCLD WATER)",
      "TRAFFIC STOP (CLI)",
      "TRANSFER JEWISH SOUTH",
      "TRASH/DUMPSTER FIRE",
      "TREE DOWN",
      "UNCONCIOUS PERSON",
      "WARRANT DETAIL",
      "WATER RESCUE",
      "WELFARE CHECK"
  );
  
  private static final String[] CITY_LIST =new String[]{
      "BROOKS", 
      "BROWNINGTON", 
      "CLERMONT", 
      "FOX CHASE", 
      "HEBRON ESTATES", 
      "HILLVIEW", 
      "HUNTERS HOLLOW" ,
      "LEBANON JUNCTION", 
      "MOUNT WASHINGTON", 
      "MT WASHINGTON", 
      "PIONEER VILLAGE", 
      "SHEPHERDSVILLE",
      "SOLITUDE", 
      "TAYLORSVILLE",
    
      "LOUISVILLE",
      "LOUISVILLLE",  // Misspelled
      
      // Hardin County
      "WEST POINT",
      
      // Nelson County
      "COXS CREEK"
  };
}
