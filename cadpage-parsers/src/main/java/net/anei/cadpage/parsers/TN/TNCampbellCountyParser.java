package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchGeoconxParser;


public class TNCampbellCountyParser extends DispatchGeoconxParser {
  
  public TNCampbellCountyParser() {
    super("CAMPBELL COUNTY", "TN", GCX_FLG_NAME_PHONE);
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (! super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("LAFOLLETTE")) data.strCity = "LA FOLLETTE";
    
    // Dispatch requests caller name not be included in result
    data.strName = "";
    return true;
  }

  @Override
  public String getFilter() {
    return "@911email.net,@911email.org";
  }

  @Override
  protected boolean isAddress(String field) {
    if (field.equals("LMCER")) return true;
    return super.isAddress(field);
  }
  
}
