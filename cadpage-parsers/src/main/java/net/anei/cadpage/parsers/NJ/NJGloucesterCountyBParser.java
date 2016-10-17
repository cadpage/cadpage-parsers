package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA5Parser;


/**
 * Gloucester County, NJ (Variant B)
 */
public class NJGloucesterCountyBParser extends DispatchA5Parser {
  
  public NJGloucesterCountyBParser() {
    super("GLOUCESTER COUNTY", "NJ");
  }
  
  @Override
  public String getFilter() {
    return "@S105KD4M.CO.GLOUCESTER.NJ";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("AC EXP")) data.strCity = "";
    return true;
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    return sAddress.replace("AC EXP", "ATLANTIC CITY EXPY");
  }
}
