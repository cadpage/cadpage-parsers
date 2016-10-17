package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPremierOneParser;

public class COSanJuanCountyParser extends DispatchPremierOneParser {

  public COSanJuanCountyParser() {
    super("SAN JUAN COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "@CSP.CAD";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("San Juan EMS Notification")) return false;
    return super.parseFields(body.split("\n"), data);
  }
}
