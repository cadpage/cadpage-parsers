package net.anei.cadpage.parsers.OK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class OKOklahomaCountyAParser extends SmartAddressParser {
  
  private static final Pattern DELIM = Pattern.compile("/{2,}|[-/] ");
  
  public OKOklahomaCountyAParser() {
    super("OKLAHOMA COUNTY", "OK");
    addRoadSuffixTerms("NEST", "VISTA");
    setFieldList("CALL ADDR APT INFO");
  }
  
  @Override
  public String getFilter() {
    return "405261,81370,4058352432,4057960363";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf(" - From: ");
    if (pt < 0) return false;
    body = body.substring(0,pt).trim();
    
    Matcher match = DELIM.matcher(body);
    if (match.find()) {
      String addr = body.substring(0,match.start()).trim();
      body = body.substring(match.end()).trim();
      
      if (match.group().contains("/") && checkAddress(addr) == STATUS_STREET_NAME) {
        match = DELIM.matcher(body);
        if (match.find()) {
          addr = append(addr, " & ", body.substring(0,match.start()).trim());
          body = body.substring(match.end()).trim();
        } else {
          Result res = parseAddress(StartType.START_ADDR, FLAG_IGNORE_AT, body);
          if (res.isValid()) {
            res.getData(data);
            addr = append(addr, " & ", data.strAddress);
            data.strAddress = "";
            body = res.getLeft();
          }
        }
      }
      
      parseAddress(addr, data);
      data.strCall = body;
    }
    
    else {
      parseAddress(StartType.START_CALL, FLAG_IGNORE_AT, body, data);
      if (data.strCall.length() == 0) {
        data.strCall = getLeft();
      } else {
        data.strSupp = getLeft();
      }
    }
    return (data.strCall.length() > 0 && data.strAddress.length() > 0);
  }
}
