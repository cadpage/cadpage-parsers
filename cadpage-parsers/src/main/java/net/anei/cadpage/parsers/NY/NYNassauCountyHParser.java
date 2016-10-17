package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYNassauCountyHParser extends NYSuffolkCountyXBaseParser {
  
  public NYNassauCountyHParser() {
    super("NASSAU COUNTY", "NY",
           "CALL PLACENAME ADDR/SXa! CS:X? TOA:TOAP IDP INFO/N+");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Rule out alerts intended for NYNassauCountyFireTracker
    if (subject.equals("FirePage")) return false;
    
    return super.parseMsg(body, data);
  }

  @Override
  public String getFilter() {
    return "scmbackup@verizon.net,cpg.page@gmail.com,wbpaging@optonline.net,paging2@firerescuesystems.xohost.com";
  }
}
