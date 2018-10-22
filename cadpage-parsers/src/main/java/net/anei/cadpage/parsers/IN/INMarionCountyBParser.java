package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class INMarionCountyBParser extends DispatchA52Parser {
  
  public INMarionCountyBParser() {
    super("MARION COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "MotorolaCAD@page.indy.gov";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("Motorola CAD:")) return false;
    body = body.substring(13).trim();
    return super.parseMsg(body, data);
  }

}
