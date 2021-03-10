package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class KSWoodsonCountyParser extends DispatchA33Parser {

  public KSWoodsonCountyParser() {
    super("WOODSON COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCross.equals("KS")) {
      data.strState = data.strCross;
      data.strCross = "";
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace(" X ", " ST X ");
  }

}
