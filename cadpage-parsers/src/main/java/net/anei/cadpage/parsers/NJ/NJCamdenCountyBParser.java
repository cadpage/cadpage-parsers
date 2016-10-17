package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class NJCamdenCountyBParser extends SmartAddressParser {
  
  private static final int FLAG_ST_FLD_REQ = 0;

  public NJCamdenCountyBParser() {
    super("CAMDEN COUNTY", "NJ");
    setFieldList("CALL ADDR APT INFO");
  }
  
  @Override
  public String getFilter() {
    return "ccfsup@camdencounty.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CCCAlert")) return false;
    
    parseAddress(StartType.START_CALL, FLAG_ST_FLD_REQ, body, data);
    if (data.strAddress.length() == 0) return false;
    data.strSupp = getLeft();
    return true;
  }
}
