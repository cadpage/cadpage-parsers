package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;




public class NYMontgomeryCountyParser extends SmartAddressParser {
  
  private static final Pattern SUBJ_PTN = Pattern.compile("[A-Z]{4} *\\d+");
  private static final Pattern DATE_TIME_PTN = Pattern.compile(" (\\d\\d/\\d\\d/\\d{4}) (\\d+)\\b");
  private static final Pattern GPS_PTN = Pattern.compile("(?: +[-+]?\\d+\\.\\d+){2}$");
  private static final Pattern CITY_TRIM_PTN = Pattern.compile(" (?:CITY|VILLAGE)$");
  
  public NYMontgomeryCountyParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "NY");
    setFieldList("SRC CALL PLACE ADDR CITY X DATE TIME INFO GPS");
  }
  
  @Override
  public String getFilter() {
    return "paging@impact-sys.com,messaging@iamresponding.com,impact@impact.co.Montgomery.NY.us,2183500166";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (SUBJ_PTN.matcher(subject).matches()) data.strSource = subject;
    Matcher match = DATE_TIME_PTN.matcher(body);
    if (!match.find()) return false;
    data.strDate = match.group(1);
    String time = match.group(2);
    data.strTime = time.substring(0,2) + ':' + time.substring(2,4);
    String info = body.substring(match.end()).trim();
    body = body.substring(0,match.start()).trim();
    
    match = GPS_PTN.matcher(info);
    if (match.find()) {
      setTrimmedGPSLoc(match.group().trim(), data);
      info = info.substring(0,match.start()).trim();
    }
    data.strSupp = info;
    
    StartType st = StartType.START_CALL;
    int pt = body.indexOf("  ");
    if (pt >= 0) {
      data.strCall = body.substring(0,pt);
      body = body.substring(pt+2).trim();
      st = StartType.START_PLACE;
    }
    else {
      for (String call : CALL_LIST) {
        if (body.startsWith(call)) {
          data.strCall = call;
          body = body.substring(call.length()).trim();
          st = StartType.START_PLACE;
          break;
        }
      }
    }
    
    parseAddress(st, FLAG_CROSS_FOLLOWS, body, data);
    data.strCross = getLeft();
    
    data.strAddress = data.strAddress.replace("SHOP CT", "SHOPPING CTR");
    
    if (data.strCity.startsWith("V ") || data.strCity.startsWith("T ")) {
      data.strCity = data.strCity.substring(2).trim();
    }
    match = CITY_TRIM_PTN.matcher(data.strCity);
    if (match.find()) data.strCity = data.strCity.substring(0,match.start()).trim();
    return true;
  }
 
  @Override
  public String adjustMapAddress(String addr) {
    return CTR_PTN.matcher(addr).replaceAll("CENTER");
  }
  private static final Pattern CTR_PTN = Pattern.compile("\\bCTR\\b");
 
  
  private static final String[] CALL_LIST = new String[] {
    "EMS - EMS CALL",
    "FIRE - CO CALL",
    "FIRE - SERVICE",
    "FIRE - STRUCTURE",
    "MOTOR VEHICLE ACCIDENT"
  };
  
  static final String[] CITY_LIST = new String[] {
    "AMES",
    "AMES VILLAGE",
    "V AMES",
    "AMSTERDAM",
    "AMSTERDAM CITY",
    "C AMSTERDAM",
    "T AMSTERDAM",
    "CANAJOHARIE",
    "CANAJOHARIE VILLAGE",
    "T CANAJOHARIE",
    "V CANAJOHARIE",
    "CHARLESTON",
    "FLORIDA",
    "FONDA",
    "FONDA VILLAGE",
    "V FONDA",
    "FORT JOHNSON",
    "FORT JOHNSON VILLAGE",
    "V FORT JOHNSON",
    "FORT PLAIN",
    "FORT PLAIN VILLAGE",
    "V FORT PLAIN",
    "FULTONVILLE",
    "FULTONVILLE VILLAGE",
    "V FULTONVILLE",
    "GLEN",
    "T GLEN",
    "HAGAMAN",
    "HAGAMAN VILLAGE",
    "V HAGAMAN",
    "MINDEN",
    "MOHAWK",
    "T MOHAWK",
    "NELLISTON",
    "NELLISTON VILLAGE",
    "V NELLISTON",
    "PALATINE",
    "PALATINE BRIDGE",
    "PALATINE BRIDGE VILLAGE",
    "V PALATINE BRIDGE",
    "ROO",
    "ST JOHNSVILLE",
    "ST JOHNSVILLE VILLAGE",
    "T ST JOHNSVILLE",
    "V ST JOHNSVILLE"
  };
}
	