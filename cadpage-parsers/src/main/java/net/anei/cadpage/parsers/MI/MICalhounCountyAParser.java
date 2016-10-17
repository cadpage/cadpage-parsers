package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;


public class MICalhounCountyAParser extends DispatchPrintrakParser {
  
  public MICalhounCountyAParser() {
    super(MICalhounCountyParser.CITY_CODES, "CALHOUN COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "postmaster@calhouncountymi.gov,mcfd12@gmail.com,CCCDA@calhouncountymi.gov";
  }
  
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Not sure if CCCDA is a separate dispatch center or is massaging pages from
    // county dispatch.  But we can unmassage them so they work
    if (body.startsWith("CCCDA:")) {
      body = body.substring(6).trim();
      int pt1 = body.indexOf(" *: ");
      if (pt1 >= 0) {
        int pt2 = body.indexOf(" *: ", pt1+4);
        if (pt2 < 0) return false;
        body = body.substring(0,pt1) + " TYP: " + body.substring(pt1+4,pt2) + " AD: " + body.substring(pt2+4);
      }
    }
    if (!super.parseMsg(body, data)) return false;
    
    MICalhounCountyParser.cleanup(data);
    return true;
  }
}
