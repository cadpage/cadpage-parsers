package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCVanceCountyParser extends SmartAddressParser {
  
  private static final Pattern MASTER = Pattern.compile("VanceCounty911:(?:LineCount=\\d+ +)?(?:(\\d{4}-\\d{6}) +)?(.*?)(?: +Line\\d+=)*");
  private static final Pattern MASTER2 = Pattern.compile("([^,]+) +([,A-Z0-9]+.*)");
  
  public NCVanceCountyParser() {
    super(CITY_LIST, "VANCE COUNTY", "NC");
    setFieldList("ID ADDR APT CITY CALL NAME UNIT");
  }
  
  @Override
  public String getFilter() {
    return "VanceCounty911@vancecounty.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    // Check for truncated Line=nn
    int pt = body.lastIndexOf(' ');
    if (pt >= 0) {
      if ("Line=".startsWith(body.substring(pt+1))) body = body.substring(0,pt).trim();
    }
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = getOptGroup(match.group(1));
    body = match.group(2).trim();
    
    match = MASTER2.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).replace("//", "/");
    data.strUnit = match.group(2);
    
    parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT, body, data);
    body = getLeft();
    if (body.length() == 0) return false;
    
    String call = CALL_SET.getCode(body);
    if (call != null) {
      data.strCall = call;
      data.strName = cleanWirelessCarrier(body.substring(call.length()).trim());
    } else {
      data.strCall = body;
    }
    return true;
  }
  
  private CodeSet CALL_SET = new CodeSet(
      "ACCIDENT PERSONAL INJURY",
      "BREATHING PROBLEMS",
      "CHIMNEY FIRE",
      "FIRE ALARM",
      "MEDICAL",
      "MISSING PERSON",
      "STRUCTURE FIRE"
  );
  
  private static final String[] CITY_LIST = new String[]{
    "HENDERSON",
    "KITTRELL",
    "MANSON",
    "MIDDLEBURG",
    "OXFORD",
    "SOUTH HENDERSON"
  };
}
