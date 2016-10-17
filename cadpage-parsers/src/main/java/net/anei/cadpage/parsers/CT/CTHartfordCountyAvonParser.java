package net.anei.cadpage.parsers.CT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class CTHartfordCountyAvonParser extends SmartAddressParser {
  
  private static final Pattern SRC_PTN = Pattern.compile("^([A-Z]{4}) / ");
  private static final Pattern UNIT_PTN = Pattern.compile("(?: +(?:[A-Z]{0,3} ?\\d+))+$");
  private static final Pattern CROSS_DELIM_PTN = Pattern.compile(" Cross: | NA\\b");
  
  public CTHartfordCountyAvonParser() {
    super("AVON", "CT");
  }
  
  public CTHartfordCountyAvonParser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState);
    setFieldList("SRC ID CALL ADDR CITY MAP X UNIT DATE TIME");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    body = body.replaceAll("  +", " ");
    Matcher match = SRC_PTN.matcher(body);
    if (match.find()) {
      data.strSource = match.group(1);
      body = body.substring(match.end()).trim();
    }
    
    match = UNIT_PTN.matcher(body);
    if (!match.find()) return false;
    data.strUnit = match.group().trim();
    body = body.substring(0,match.start()).trim();
    
    int flags = FLAG_CROSS_FOLLOWS;
    match = CROSS_DELIM_PTN.matcher(body);
    if (match.find()) {
      flags = FLAG_ANCHOR_END;
      data.strCross = body.substring(match.end()).trim();
      body = body.substring(0,match.start()).trim();
    }
    parseAddress(StartType.START_CALL, flags, body, data);
    if (data.strAddress.length() == 0) return false;
    if (flags == FLAG_CROSS_FOLLOWS) data.strCross = getLeft();
    
    return true;
  }
}
