package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class ILKaneCountyCParser extends SmartAddressParser {
  
  private static final Pattern DIR_STREET_NO_PTN = Pattern.compile(" (\\d+[NSEW]\\d? ?\\d+) ");
  private static final Pattern DIR_STREET_NO_PTN2 = Pattern.compile("(\\d+[NSEW]\\d? \\d+) ");
  private static final Pattern X_ST_PTN1 = Pattern.compile("([ A-Z]+/[A-Z]+)  +");
  private static final Pattern X_ST_PTN2 = Pattern.compile("([ A-Z]+/[A-Z]+) ");
  
  
  public ILKaneCountyCParser() {
    super(ILKaneCountyParser.CITY_LIST, "KANE COUNTY", "IL");
    setFieldList("CALL ADDR APT PLACE CITY X GPS INFO");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "ARBOR CREEK",
        "ARMY TRAIL",
        "BIG TIMBER",
        "BOWES BEND",
        "CLOVER FIELD",
        "COUNTRY CLUB",
        "DIAMOND HEAD",
        "FOX MILL",
        "HIDDEN OAKS",
        "NORTH JAMES",
        "SILVER GLEN",
        "WILLIAM CULLEN BRYANT");
  }
  
  @Override
  public String getFilter() {
    return "shfradio@co.kane.il.us";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("!")) return false;
    
    // They have some weird street numbers that throw the smart address parser
    // for a loop.  So check for them first.
    StartType st = StartType.START_CALL;
    int flags = FLAG_START_FLD_REQ;
    String dirStreetNo = null;
    Matcher match = DIR_STREET_NO_PTN.matcher(body);
    if (match.find()) {
      st = StartType.START_ADDR;
      flags = 0;
      data.strCall = body.substring(0,match.start()).trim();
      dirStreetNo = match.group(1);
      body = "999999" + body.substring(match.end(1));
    }
    
    else {
      int pt = body.indexOf(" <UNKNOWN> ");
      if (pt >= 0) {
        data.strCall = body.substring(0,pt).trim();
        data.strAddress = "<UNKNOWN>";
        data.strSupp = body.substring(11).trim();
        return true;
      }
    }

    // They like verbose highway names
    body = body.replace(" UNITED STATES HIGHWAY ", " US ");
    body = body.replace(" ILLINOIS ROUTE ", " IL ");
    
    match = GPS_PATTERN.matcher(body);
    if (match.find()) {
      setGPSLoc(match.group(), data);
      data.strSupp = body.substring(match.end()).trim();
      body = body.substring(0,match.start()).trim();
    }
    
    
    // Lets see what we can do...
    parseAddress(st, flags | FLAG_IMPLIED_INTERSECT | FLAG_IGNORE_AT| FLAG_CROSS_FOLLOWS | FLAG_PAD_FIELD_EXCL_CITY, body, data);
    if (!isValidAddress() && data.strCity.length() == 0) return false;
    data.strPlace = getPadField();
    body = getLeft();
    if (dirStreetNo != null && data.strAddress.startsWith("999999 ")) {
      data.strAddress = dirStreetNo + data.strAddress.substring(6);
    }
    
    // Next is the cross street. But it is a bit chancy.  Let's see what we can do 
    // with this.
    do {
      if (data.strGPSLoc.length() > 0) {
        data.strCross = body;
        break;
      }
      
      if (body.startsWith("No Cross Streets Found ")) {
        data.strSupp = body.substring(23).trim();
        break;
      }
      
      match = X_ST_PTN1.matcher(body);
      if (match.lookingAt()) {
        data.strCross = match.group(1).trim();
        data.strSupp = body.substring(match.end()).trim();
        break;
      }
      
      match = X_ST_PTN2.matcher(body);
      if (match.lookingAt()) {
        data.strCross = match.group(1).trim();
        data.strSupp = body.substring(match.end()).trim();
        break;
      }
      
      Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, body);
      if (res.isValid()) {
        res.getData(data);
        data.strSupp = res.getLeft();
        if (data.strSupp.length() == 0) {
          data.strSupp = data.strCross;
          data.strCross = "";
        }
        break;
      }
      
      data.strSupp = body;
        
    } while (false);
    
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    Matcher match = DIR_STREET_NO_PTN2.matcher(addr);
    if (match.lookingAt()) {
      addr = match.group(1).replace(" ", "") + addr.substring(match.end(1));
    }
    while (addr.startsWith("0")) addr = addr.substring(1).trim();
    return addr;
  }
 
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "AMBULANCE CALL",
      "FIELD FIRE",
      "FIRE ALARM",
      "FIRE CALL",
      "MUTUAL AID REQUEST",
      "STRUCTURE FIRE",
      "VEHICLE FIRE"
  );
}
