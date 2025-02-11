package net.anei.cadpage.parsers.IN;


import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

/**
 * Wabash County, IN
 */
public class INWabashCountyParser extends DispatchA24Parser {

  public INWabashCountyParser() {
    super("WABASH COUNTY", "IN");
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    int pt = body.indexOf(" - CALL:");
    if (pt < 0) return false;
    data.strSource = body.substring(0,pt).trim();
    body = body.substring(pt+3);
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
