package net.anei.cadpage.parsers.WV;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class WVLincolnCountyParser extends DispatchB2Parser {
  
  public WVLincolnCountyParser() {
    super("LINCOLN 911:", CITY_LIST, "LINCOLN COUNTY", "WV", B2_OPT_CALL_CODE);
    setupMultiWordStreets(
        "A PARK",
        "BIG LAUREL",
        "DR STEELE FARM",
        "END OF MONDAY",
        "HILL VIEW",
        "INDIAN VALLEY",
        "LINCOLN PANTHER",
        "LOWER MUD RIVER",
        "MARY JANE",
        "MUD RIVER",
        "ONE MILE",
        "PIN OAK",
        "SILVER MINE",
        "SLASH BR BELLOMY",
        "STONES TRAILER",
        "SUGAR TREE",
        "UPPER MUD RIVER",
        "WEST PARK"
    );
    setupCallList(CALL_LIST);
    setupDoctorNames("STEELE");
  }
  
  @Override
  public String getFilter() {
    return "LINCOLN911@e911.org";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = STRAIGHT_FRK_PTN.matcher(addr).replaceAll("STRAIGHT FORK RD");
    addr = SLASH_BR_BELLOMY_RD_PTN.matcher(addr).replaceAll("BELLOMY RD");
    return addr;
  }
  private static final Pattern STRAIGHT_FRK_PTN = Pattern.compile("\\bSTRAIGHT +FO?RK\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SLASH_BR_BELLOMY_RD_PTN = Pattern.compile("\\bSLASH BR BELLOMY RD\\b", Pattern.CASE_INSENSITIVE);
  
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCross = TO_PTN.matcher(data.strCross).replaceAll(" / ");
    return true;
  }
  private static final Pattern TO_PTN = Pattern.compile(" +TO +", Pattern.CASE_INSENSITIVE);
  
  private static CodeSet CALL_LIST = new CodeSet(
      "AUTO ACCIDENT NO INJURIES",
      "AUTO ACCIDENT UNKNOWN INJURIES",
      "AUTO ACCIDENT W/ENTRAPMENT",
      "AUTO ACCIDENT WITH INJURIES",
      "BOX ALARM",
      "BREATHING DIFFICULTY",
      "BRUSH FIRE",
      "CARDIAC ARREST",
      "CHEST PAINS",
      "CODE 3 DIFFICULTY BREATHING",
      "CODE3 UNCONSCIOUS/UNRESPONSIVE",
      "FIRE INVESTIGATION",
      "FLASH FLOOD WARNING",
      "FLOOD WARNING",
      "GAS LEAK",
      "HOUSE FIRE",
      "LANDING ZONE SECURED",
      "LINE DOWN",
      "LOCK OUT WITH ENTRAPMENT",
      "OTHER FIRE CALL",
      "OVERDOSE",
      "PUBLIC ASSISTANCE NON EMERGENC",
      "RELEASED FROM",
      "SHOOTING",
      "SICK UNKNOWN",
      "SMOKE INVESTIGATION",
      "STRUCTURE FIRE",
      "TRAFFIC PROBLEM",
      "TRAILER FIRE",
      "TRAUMA",
      "TREE DOWN",
      "VEHICLE FIRE"
  );
  
  private static final String[] CITY_LIST = new String[]{
    
    // Incorporated cities and towns

    "HAMLIN",
    "WEST HAMLIN",

    // Unincorporated communities

    "ALKOL",
    "ALUM CREEK",
    "ATENVILLE",
    "BIG UGLY",
    "BRANCHLAND",
    "DOLLIE",
    "EDEN PARK",
    "FERRELLSBURG",
    "FOURTEEN",
    "FRY",
    "GILL",
    "GREEN SHOAL",
    "GRIFFITHSVILLE",
    "HARTS",
    "LEET",
    "MIDKIFF",
    "MYRA",
    "PALERMO",
    "PLEASANT VIEW",
    "RANGER",
    "RECTOR",
    "SOD",
    "SPURLOCKVILLE",
    "SUMERCO",
    "SWEETLAND",
    "WARREN",
    "WEWANTA",
    "YAWKEY",
    
    "CABELL COUNTY",
    "CULLODEN",
    
    "PUTNAM COUNTY"
  };
}
