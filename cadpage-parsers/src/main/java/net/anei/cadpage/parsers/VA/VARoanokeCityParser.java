package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;

/**
 * Roanoke City, VA
 */
public class VARoanokeCityParser extends DispatchPrintrakParser {
  
  public VARoanokeCityParser() {
    super("ROANOKE CITY", "VA");
  }
  
  @Override
  public String getFilter() {
    return "e911@roanokeva.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("E-911")) return false;
    
    // Regular Printrak format
    if (body.startsWith("PRI:")) return super.parseMsg(body, data);
    setFieldList("CALL ADDR APT INFO");
    
    int pt = body.indexOf(" [Att");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    //  Structure fires have their own special format
    if (body.startsWith("F-STRUCTURE FIRE ")) {
      data.strCall = body.substring(0,16);
      parseAddress(StartType.START_ADDR, body.substring(17).trim(), data);
      data.strSupp = getLeft();
      return true;
    }
    
    // Otherwise fallback to old format, that may or may not still be in use
    if (body.length() < 21 || body.charAt(20) != ' ') return false;
    data.strCall = body.substring(0,20).trim();
    body = body.substring(21).trim();
    
    pt = body.indexOf(" Original Location :");
    if (pt >= 0) {
      parseAddress(body.substring(0,pt).trim(), data);
      data.strSupp = body.substring(pt+20).trim();
    } else {
      parseAddress(StartType.START_ADDR, body, data);
      data.strSupp = getLeft();
    }
    return true;
  }
}
