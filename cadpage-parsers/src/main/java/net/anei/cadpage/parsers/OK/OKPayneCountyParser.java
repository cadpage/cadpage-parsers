package net.anei.cadpage.parsers.OK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class OKPayneCountyParser extends SmartAddressParser {
  
  public OKPayneCountyParser() {
    super("PAYNE COUNTY", "OK");
    setFieldList("CALL PLACE ADDR APT CITY ST INFO");
    setupCallList(CALL_LIST);
    setupMultiWordStreets("BUSH CREEK");
  }
  
  @Override
  public String getFilter() {
    return "fireadmin@stillwater.org";
  }
  
  private static final Pattern MASTER = Pattern.compile("911:(.*)From CAD User:[A-Z]+ on Device:\\d+");
  private static final Pattern CALL_PTN = Pattern.compile("[- A-Z]+");
  private static final Pattern CITY_ST_PTN = Pattern.compile("([ A-Z]+), *([A-Z]{2})");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).trim();
    
    Parser p = new Parser(body);
    String addr = p.get("  ");
    if (CALL_PTN.matcher(addr).matches()) {
      data.strCall = addr;
      addr = p.get("  ");
      if (addr.length() == 0) {
        addr = data.strCall;
        data.strCall = "";
      }
    }
    data.strSupp = p.get();
    
    StartType st = (data.strCall.length() == 0 ? StartType.START_CALL_PLACE : StartType.START_PLACE);
    parseAddress(st, FLAG_AT_BOTH | FLAG_ANCHOR_END, addr, data);
    if (data.strAddress.length() == 0) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    
    match = CITY_ST_PTN.matcher(data.strPlace);
    if (match.matches()) {
      data.strCity = match.group(1).trim();
      data.strState = match.group(2);
      data.strPlace = "";
    }
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT WITH INJURY",
      "AMBULANCE CALL",
      "FIRE ALARM - GENERAL",
      "MISC FIRE",
      "PASSENGER VEH FIRE",
      "RURAL GRASS FIRE",
      "SMOKE INV COMMERCIAL",
      "SMOKE INVEST OUTSIDE",
      "STRUCTURE FIRE - RES"
  );
}
