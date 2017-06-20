package net.anei.cadpage.parsers.ZCANS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class ZCANSAnnapolisCountyParser extends SmartAddressParser {
  
  public ZCANSAnnapolisCountyParser() {
    this("ANNAPOLIS COUNTY");
    setupCallList(CALL_LIST);
  }
  
  protected ZCANSAnnapolisCountyParser(String defCity) {
    super(defCity, "NS");
    setFieldList("ADDR APT PLACE CITY CALL");
  }
  
  @Override
  public String getAliasCode() {
    return "ZCANSAnnapolisCounty";
  }
  
  @Override
  public String getFilter() {
    return "valleycommunications@ns.aliantzinc.ca,valley295-u@iamresponding.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    // See if there is a trailing call description
    boolean good = false;
    int pt = body.indexOf(" * ");
    if (pt >= 0) {
      good = true;
      data.strCall = body.substring(pt+3).trim();
      body = body.substring(0,pt).trim();
    }
    
    Parser p = new Parser(body);
    String city = p.getLastOptional(',');
    if (city.length() == 0) {
      data.strCity = p.getLastOptional(" IN ");
    } else {
      parseAptCity(city, data);
      parseAptCity(p.getLastOptional(','), data);
    }
    body = p.get();
    
    // OK, now lets do the address
    StartType st = data.strCall.length() > 0 ? StartType.START_ADDR : StartType.START_CALL;
    int flags = FLAG_ANCHOR_END;
    if (st == StartType.START_CALL) flags |= FLAG_START_FLD_REQ;
    parseAddress(st, flags, body, data);
    return good || isValidAddress();
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:UNIT|APT|RM|ROOM|LOT|#) *(.*)|[A-Z]?\\d+[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  
  private void parseAptCity(String field, Data data) {
    if (field.length() == 0)  return;
    Matcher match = APT_PTN.matcher(field);
    if (match.matches()) {
      String apt = match.group(1);
      if (apt == null) apt = field;
      data.strApt = append(apt, "-", data.strApt);
    } else if (data.strCity.length() == 0) {
      data.strCity = field;
    } else {
      data.strPlace = field;
    }
  }
  
  @Override
  public String adjustMapAddress(String addr, String city, boolean cross) {
    addr = INTERSECT_PTN.matcher(addr).replaceAll("&");
    if (city.equalsIgnoreCase("WILMOT")) {
      addr = HWY1_PTN.matcher(addr).replaceAll("EVANGELINE TRAIL");
    }
    return super.adjustMapAddress(addr);
  }
  
  private static final Pattern INTERSECT_PTN = Pattern.compile("\\bAT (?:THE )?INTERSECTION OF\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern HWY1_PTN = Pattern.compile("\\b(?:HWY|HIGHWAY) +1\\b", Pattern.CASE_INSENSITIVE);
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "2 VEHICLE MVA",
      "AERIAL TO SCENE",
      "ALARM",
      "CAR ON FIRE",
      "CHIMNEY",
      "CHIMNEY FIRE",
      "CO ALARM ACTIVATION",
      "FIRE ALARM",
      "FIRE ALARM SOUNDING",
      "HOUSE FIRE",
      "KITCHEN FIRE",
      "LIFT ASSIST",
      "MEDICAL",
      "MEDICAL APARTMENT A",
      "MEDICAL ARREST",
      "MUTUAL AID HOUSE FIRE",
      "MVA",
      "POWER LINES ON FIRE",
      "SINGLE VEHICLE MVA",
      "SMOKE CONDITION",
      "STRUCTURE",
      "STRUCTURE FIRE",
      "UNKNOWN FIRE",
      "VEHICLE FIRE",
      "VEHICLE ON FIRE"
  );
}
