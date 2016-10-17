package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class KYCallowayCountyParser extends SmartAddressParser {
  
  private static final Pattern CODE_PTN = Pattern.compile("^(\\d{4}) +");
  
  public KYCallowayCountyParser() {
    super("CALLOWAY COUNTY", "KY");
    setFieldList("PHONE CODE CALL ADDR APT NAME");
  }
  
  @Override
  public String getFilter() {
    return "CALLOWAY_COUNTY_911@callkyso.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    if (!body.startsWith("CALLOWAY_COUNTY_911:")) return false;
    body = body.substring(20).trim();
    
    if (body.startsWith("Return Phone:")) {
      body = body.substring(13).trim();
      int pt = body.indexOf(' ');
      if (pt < 0) return false;
      data.strPhone = body.substring(0,pt);
      body = body.substring(pt+1).trim();
    }
    Matcher match = CODE_PTN.matcher(body);
    if (match.find()) {
      data.strCode = match.group(1);
      body = body.substring(match.end());
    }
    
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, body, data);
    data.strName = cleanWirelessCarrier(getLeft());
    
    return true;
  }
}
