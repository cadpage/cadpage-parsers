package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Grant County, WV (A) 
 */
public class WVGrantCountyAParser extends DispatchA48Parser {
  
  private static final Pattern UNIT_PTN = Pattern.compile("\\b(?:[A-Z]{2}\\d+[A-Z]?|[A-Z]{1,3}EMS|77\\d|FIRST_ENERGY|SOUTHG)\\b"); 
  
  public WVGrantCountyAParser() {
    super(CITY_LIST, "GRANT COUNTY", "WV", FieldType.PLACE_X, A48_OPT_CODE, UNIT_PTN);
    setupCallList(CALL_CODE);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "CAD@hardynet.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    body = body.replace(" WV - -", "").replace("FIRST ENERGY", "FIRST_ENERGY");
    
    Matcher match = UNIT_PTN.matcher(body);
    int lastCommaPt = body.indexOf(',');
    int pt = -1;
    while (match.find()) pt = match.end();
    String info = null;
    if (pt > lastCommaPt && pt < body.length() && body.charAt(pt) == ' ') {
      info = body.substring(pt+1).trim();
      body = body.substring(0, pt);
    }
    
    if (!super.parseMsg(subject, body, data)) return false;
    
    if (info != null) data.strSupp = append(data.strSupp, "\n", info);
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " INFO";
  }
  
  @Override
  public String adjustMapAddress(String address) {
    address = address.replace("PAPERBACK MAPLE ST", "MAPLE ST");
    return super.adjustMapAddress(address);
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BAYARD CEMETERY",
      "BIG HILL",
      "CHERRY RIDGE",
      "CLARKS VIEW",
      "GEORGE WASHIGNTON",
      "GEORGE WASHINGTON",
      "HENRY DOBBIN",
      "HIGH POINT",
      "HIGH VALLEY",
      "JOHNSON RUN",
      "JORDAN RUN",
      "KUHN MINE",
      "LAUREL RUN",
      "LUNICE CREEK",
      "MILL CREEK",
      "MISS LIZZY",
      "MOUNTAIN VIEW",
      "NOBLE FIR",
      "PAPERBACK MAPLE",
      "PATTERSON CREEK",
      "PETERSBURG GAP",
      "RED BARN",
      "SKY VALLEY",
      "SMOKE HOLE",
      "WELTON ORCHARD",
      "WINDY HILL",
      "YUCKY RUN"
  };
  
  private static final CodeSet CALL_CODE = new CodeSet(
      "BLEEDNOTRAUMA",
      "BREATHDIFF",
      "BREATHING DIFFICULTY/TROUBLE BREATHING/SHORT OF BREATH-SOB/DIFFICULTY BREATHING",
      "BRUSH FIRE/GRASS FIRE/WOODS FIRE",
      "BRUSH FIRE/GRASS FIRE/WOODS FIRE HARDY CO",
      "CHESTHEART",
      "COALARM",
      "CONTROLLED BURN",
      "DIABETIC EMERGENCY / LOW BLOOD SUGAR / HIGH BLOOD SUGAR / GLUCOSE LEVEL",
      "ELECTRICAL FIRE",
      "EMSASSIST",
      "EMSTRANS",
      "ESTBY",
      "FALL",
      "FIRE ALARM/AUTOMATIC FIRE ALARM/COMMERCIAL FIRE ALARM/RESIDENTIAL FIRE ALARM",
      "FIRE STANDBY",
      "FLUEFIRE",
      "LANDING ZONE SETUP",
      "LMISC",
      "LZSET",
      "MENTAL",
      "MISC",
      "MISC CALL NOT LAW ENFORCEMENT",
      "MOTOR VEHICLE CRASH WITH INJURY OR ENTRAPMET ACCIDENT",
      "MUT AID EMS",
      "MVC",
      "MVCINJ",
      "PROCESS",
      "SEIZURE",
      "SICK",
      "SMELL ODOR-GAS OUTDOORS",
      "SMOKE INVESTIGATION OUTDOORS",
      "STROKE",
      "STROKE/SLURRED SPEECH/DROOPING",
      "STRUCTURE FIRE",
      "TEST CALL",
      "TRAUMA",
      "TRAUMATIC INJURY",
      "UNCONS UNRESPONSIVE",
      "UNCONSCIOUS/UNRESPONSIVE/",
      "UNCONSCIOUS/UNRESPONSIVE/SYNCOPE",
      "VEHICLE ACCIDENT-NO INJURY OR ENTRAPMENT MOTOR VEHCLE CRASH",
      "VEHICLE FIRE",
      "WELFARE"
  );

  private static final String[] CITY_LIST = new String[]{

      //CITY
      "PETERSBURG",

      //TOWN
      "BAYARD",

      //UNINCORPORATED COMMUNITIES
      "ARTHUR",
      "BISMARCK",
      "CABINS",
      "DOBBIN",
      "DORCAS",
      "FAIRFAX",
      "FORMAN",
      "GORMANIA",
      "GREENLAND",
      "HENRY",
      "HOPEVILLE",
      "LAHMANSVILLE",
      "MAYSVILLE",
      "MEDLEY",
      "MOUNT STORM",
      "OLD ARTHUR",
      "SCHERR",
      "WILLIAMSPORT",
      "WILSONIA",
          
      // Mineral County
      "BURLINGTON",
      "KEYSER",
      "NEW CREEK"

  };
}
