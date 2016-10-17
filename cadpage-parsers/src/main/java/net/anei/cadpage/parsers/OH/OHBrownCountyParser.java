package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;



public class OHBrownCountyParser extends DispatchA1Parser {
  
  public OHBrownCountyParser() {
    super("BROWN COUNTY", "OH");
  }
  
  @Override
  public String getFilter() {
    return "browncommctr@roadrunner.com,browncommctr@browncountyohio.gov";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCross.startsWith("N/A & N/A")) data.strCross = "";
    else if (data.strCross.startsWith("N/A & ")) {
      data.strCross = data.strCross.substring(6).trim();
    }
    else if (data.strCross.endsWith(" & N/A")) {
      data.strCross = data.strCross.substring(0,data.strCross.length()-6).trim();
    }
    return true;
  }
}
