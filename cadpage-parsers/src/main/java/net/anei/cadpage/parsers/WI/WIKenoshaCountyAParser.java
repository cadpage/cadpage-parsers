package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class WIKenoshaCountyAParser extends SmartAddressParser {
  
  private static final Pattern MARKER = Pattern.compile("^(?:From: )?[A-Z]{2,3}\\d{3} +#:");
  
  public WIKenoshaCountyAParser() {
    this("KENOSHA COUNTY", "WI");
  }
  
  WIKenoshaCountyAParser(String defCity, String defState) {
    super(defCity, defState);
    setFieldList("ID CALL ADDR PLACE INFO");
  }
  
  @Override
  public String getAliasCode() {
    return "WIKenoshaCounty";
  }
  
  @Override
  public String getFilter() {
    return "@kccjs.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    body = body.substring(match.end()).trim();
    
    Parser p = new Parser(body);
    data.strCallId = p.get(' ');
    data.strCall = p.get(" at ");
    parseAddress(StartType.START_ADDR, p.get(" Rem: "), data);
    data.strPlace = getLeft();
    if (data.strAddress.length() == 0) return false;
    data.strSupp = p.get();
    return true;
  }
}
