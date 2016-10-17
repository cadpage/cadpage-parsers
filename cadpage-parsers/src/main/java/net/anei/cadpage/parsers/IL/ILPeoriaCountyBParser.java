package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;



public class ILPeoriaCountyBParser extends DispatchA49Parser {
  
  public ILPeoriaCountyBParser() {
    super("PEORIA COUNTY", "IL");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.startsWith("MUTUAL AID")) {
      if (data.strCall.length() == 0) {
        data.strCall = data.strAddress;
      } else {
        data.strSupp = append(data.strAddress, "\n", data.strAddress);
      }
      data.strAddress = "";
      parseAddress(StartType.START_ADDR, data.strSupp, data);
      data.strSupp = getLeft();
    }
    return true;
  }

  @Override
  public String getFilter() {
    return "firepage@peoriagov.org";
  }
}
