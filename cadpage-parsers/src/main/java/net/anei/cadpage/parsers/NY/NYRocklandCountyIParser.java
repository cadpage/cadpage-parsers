package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchC02Parser;

public class NYRocklandCountyIParser extends DispatchC02Parser {

  public NYRocklandCountyIParser() {
    super("ROCKLAND COUNTY", "NY");
  }

  @Override
  public String getFilter() {
    return "monseyactive911@gmail.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    int pt = data.strAddress.indexOf(',');
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", data.strAddress.substring(0,pt).trim());
      data.strAddress = data.strAddress.substring(pt+1).trim();
    }
    return true;
  }
}
