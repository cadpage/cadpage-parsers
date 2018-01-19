package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class GAHartCountyParser extends SmartAddressParser {
  
  public GAHartCountyParser() {
    super("HART COUNTY", "GA");
    setupCallList(CALL_LIST);
    setupMultiWordStreets("JUD COLE");
    setFieldList("CODE CALL ADDR APT X NAME");
  }
  
  @Override
  public String getFilter() {
    return "HART_COUNTY_911@hartcountyga.gov";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    if (!body.startsWith("HART_COUNTY_911:")) return false;
    body = body.substring(16).trim();
    
    int pt = body.indexOf(' ');
    if (pt < 0) return false;
    data.strCode = body.substring(0, pt).trim();
    body = body.substring(pt+1).trim();
    
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_CROSS_FOLLOWS, body, data);
    if (data.strAddress.length() == 0) return false;
    
    body = getLeft();
    parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, body, data);
    
    data.strName = cleanWirelessCarrier(getLeft());
    return true;
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "CITY FIRE",
      "GAS LEAK"
  ); 
}
