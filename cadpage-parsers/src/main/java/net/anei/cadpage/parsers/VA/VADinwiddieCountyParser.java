package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;



public class VADinwiddieCountyParser extends DispatchB2Parser {
  
  private static final Pattern MASTER1 = Pattern.compile("911-CENTER:(?:\\d+ *>|EVENT:).*");
  private static final Pattern MASTER2 = Pattern.compile("911-CENTER:(\\d+) +(.*)");
  private static final Pattern PHONE_PTN = Pattern.compile("\\b\\d{10}\\b");
  
  public VADinwiddieCountyParser() {
    super("911-CENTER:", CITY_LIST, "DINWIDDIE COUNTY", "VA");
    setupCallList(CALL_LIST);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // There are two different formats we have to deal with :(
    if (MASTER1.matcher(body).matches()) {
      return super.parseMsg(body, data);
    }
    
    Matcher match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("CODE CALL ADDR APT PHONE X NAME");
      data.strCode = match.group(1);
      body = match.group(2);
      
      String left;
      match = PHONE_PTN.matcher(body);
      if (match.find()) {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_CITY | FLAG_ANCHOR_END, body.substring(0,match.start()).trim(), data);
        data.strPhone = match.group();
        left = body.substring(match.end()).trim();
      } else {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_CITY, body, data);
        left = getLeft();
      }
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_NO_CITY, left);
      if (res.isValid()) {
        res.getData(data);
        left = res.getLeft();
      }
      data.strName = left;
      return true;
    }
    return false;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN/PROBLEM",                                                                                                                                           
      "ALLERGIC/HIVE/MEDICATION REACT",
      "BREATHING DIFFICULTY",
      "CHEST PAIN",
      "COMMERICAL FIRE ALARM",
      "CONVULSIONS/SEIZURES",
      "DIABETIC PROBLEM",
      "GAS LEAK (INSIDE OF STRUCTURE)",
      "GENERAL ALERT",
      "MUTUAL AID",
      "MVA W/INJURIES",
      "MVA WITH HAZARDS",
      "RESIDENTIAL FIRE ALARM",
      "RUN REPORT",
      "SICK",
      "STANDBY/PUBLIC SERVICE",
      "TRAUMA WITH INJURY",
      "UNCONSCIOUS/UNRESPONSIVE",
      "UNKNOWN FIRE",
      "UNKNOWN PROBLEM/MAN DOWN"
 );
  
  private static final String[] CITY_LIST = new String[]{

    // Towns
    "MCKENNEY",

    // Unincorporated communities
    "AMMON",
    "CARSON",
    "CHURCH ROAD",
    "DEWITT",
    "DARVILS",
    "DINWIDDIE",
    "FORD",
    "SUTHERLAND",
    "WILSONS",
    
    // Independent cities
    "PETERSBURG"

  };
}
