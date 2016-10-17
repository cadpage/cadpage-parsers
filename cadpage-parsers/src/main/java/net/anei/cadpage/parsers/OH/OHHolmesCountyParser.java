package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class OHHolmesCountyParser extends DispatchA19Parser {

  public OHHolmesCountyParser() {
    super("HOLMES COUNTY", "OH");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject,  body, data)) return false;
    if (data.strPriority.equals("0")) data.strPriority = "";
    return true;
  }
  
  @Override
  public String getFilter() {
    return "911@holmescountysheriff.org";
  }
}
