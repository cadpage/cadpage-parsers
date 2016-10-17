package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


abstract public class DispatchA21Parser extends MsgParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) (\\d\\d-\\d\\d-\\d{4})");
  private static final Pattern MASTER_PTN = Pattern.compile("\\*[DEU] (\\d{3,5}) ([A-Z0-9]+)/([^,]+) ,([A-Z]+)(?: <[ ,\\d]*>)?(?: \\((.*)\\))? ?(.*)");
  
  private Properties cityCodes;
  private Properties callCodes;
  
  public DispatchA21Parser(Properties cityCodes, String defCity, String defState) {
    this(cityCodes, null, defCity, defState);
  }
  
  public DispatchA21Parser(Properties cityCodes, Properties callCodes, String defCity, String defState) {
    super(defCity, defState);
    setFieldList("TIME DATE ID CODE CALL ADDR APT CITY PLACE UNIT");
    this.cityCodes = cityCodes;
    this.callCodes = callCodes;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strTime = match.group(1);
    data.strDate = match.group(2).replace('-', '/');
    
    match = MASTER_PTN.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    String code = match.group(2);
    if (callCodes == null) {
      data.strCall = code;
    } else {
      data.strCode = code;
      String call = callCodes.getProperty(code);
      if (call == null && (code.startsWith("E") || code.startsWith("F"))) {
        call = callCodes.getProperty(code.substring(1));
      }
      if (call  == null) call = code;
      data.strCall = call;
    }
    String addr = match.group(3).trim();
    int pt = addr.indexOf('@');
    if (pt >= 0) {
      data.strPlace = addr.substring(0,pt).trim();
      addr = addr.substring(pt+1).trim();
    }
    parseAddress(addr, data);
    String city = match.group(4);
    if (cityCodes != null) city = convertCodes(city, cityCodes);
    data.strCity = city;
    data.strPlace = append(data.strPlace, " - ", getOptGroup(match.group(5)));
    data.strUnit = match.group(6).trim();
    
    return true;
  }
}
