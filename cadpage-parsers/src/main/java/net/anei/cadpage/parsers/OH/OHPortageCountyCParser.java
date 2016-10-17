package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Portage County, OH (C) 
 */
public class OHPortageCountyCParser extends SmartAddressParser {
  
  private static final Pattern MARKER = Pattern.compile("MANTUA[- ]+", Pattern.CASE_INSENSITIVE);

  public OHPortageCountyCParser() {
    super("PORTAGE COUNTY", "OH");
    setFieldList("ADDR APT CALL INFO");
  }

  @Override
  public String getFilter() {
    return "mantuafire@gmail.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end());
    
    // Hopefully, there is a address followed by a - separator
    int pt = body.indexOf("- ");
    if (pt >= 0) {
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, body.substring(0,pt).trim(), data);
      data.strCall = body.substring(pt+2).trim();
      return true;
    }
    
    // No such luck, back to using the address parser to identify an address
    Result res = parseAddress(StartType.START_CALL, body);
    if (res.isValid()) {
      res.getData(data);
      String left = res.getLeft();
      if (data.strCall.length() == 0) data.strCall = left;
      else data.strSupp = left;
      return true;
    }
    
    // If that did not work, report this as a general alert
    data.strCall = "GENERAL ALERT";
    data.strPlace = body;
    return true;
  };
  
  @Override
  public String postAdjustMapAddress(String addr) {
    return OHPortageCountyParser.fixMapAddress(addr);
  }
}
