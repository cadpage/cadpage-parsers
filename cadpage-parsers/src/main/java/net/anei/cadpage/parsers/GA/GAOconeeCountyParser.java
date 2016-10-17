package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class GAOconeeCountyParser extends SmartAddressParser {
  
  public GAOconeeCountyParser() {
    super("OCONEE COUNTY", "GA");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (!body.startsWith("OCSO E911:")) return false;

    // Skip everything up to first colon
    int ipt = body.indexOf(':');
    if (ipt >= 0) body = body.substring(ipt+1).trim();

    // Skip return phone number if there is one
    if (body.toUpperCase().startsWith("RETURN PHONE:")) {
      body = body.substring(13).trim();
      ipt = body.indexOf(' ');
      if (ipt <= 0) return false;
      data.strPhone = body.substring(0, ipt);
      body = body.substring(ipt+1).trim();
    }
    
    // Now try to find call description and address
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ, body, data);
    body = getLeft();
    
    // Next should be a 7 digit numeric value that we want to skip
    ipt = body.indexOf(' ');
    if (ipt < 0) return true;
    if (NUMERIC.matcher(body.substring(0,ipt)).matches()) {
      body = body.substring(ipt+1).trim();
    }
    
    // Everything else is a cross street
    data.strCross = body;
    
    return true;
  }
}
