package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class GAJeffersonCountyParser extends SmartAddressParser {

  public GAJeffersonCountyParser() {
      super("JEFFERSON COUNTY", "GA");
      setupCallList(CALL_LIST);
      setupMultiWordStreets(MWORD_STREETS);
      setFieldList("CODE CALL ADDR APT PHONE NAME");
  }
  
  private static final Pattern PHONE_PTN = Pattern.compile("(.*) (\\d{10}) *(.*)");
  private static final Pattern V_NN_PTN = Pattern.compile("\\bV(\\d+)\\b", Pattern.CASE_INSENSITIVE);
  
  public boolean parseMsg(String body, Data data) {
    
    if (!body.startsWith("JEFFERSONCOUNTYCENTRAL:")) return false;
    body = body.substring(23).trim();
    
    int pt = body.indexOf(' ');
    if (pt < 0) return false;
    data.strCode = body.substring(0,pt);
    body = body.substring(pt+1).trim();

    body = body.replace('@', '&');
    body = V_NN_PTN.matcher(body).replaceAll("$1");
    Matcher match = PHONE_PTN.matcher(body);
    if (match.matches()) {
      String addr = match.group(1).trim();
      data.strPhone = match.group(2);
      data.strName = match.group(3);
      
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, addr, data);
    }
    
    else {
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, body, data);
      data.strName = getLeft();
    }
    return true;
  }
  
  public static CodeSet CALL_LIST = new CodeSet(
      "AUTO ACCIDENT",
      "AUTO ACCIDENT WITH INJURIES",
      "BRUSH FIRE",
      "DIABETIC PROBLEMS",
      "FAINTING / UNRESPOSIVE",
      "FIRE ALARM",
      "HELICOPTER LANDING",
      "MOTORCYCLE ACCIDENT",
      "RESPIRATORY DISTRESS",
      "STRUCTURE FIRE",
      "TRAINING",
      "WEATHER RELATED"
  );
  
  public static String[] MWORD_STREETS = new String[]{
    "CLARKS MILL",
    "EDEN CHURCH",
    "GAMBLE SCHOOL",
    "GEORGE WILLIAMS",
    "LINCOLN PARK",
    "MOSLEY CHAPEL",
    "MOXLEY BARTOW",
    "NOAH STATION",
    "OAK GROVE",
    "OLD STAPLETON",
    "SAND VALLEY",
    "WILLIAMS BRIDGE"
  };
}
