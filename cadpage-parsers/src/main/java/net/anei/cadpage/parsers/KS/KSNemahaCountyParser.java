package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSNemahaCountyParser extends DispatchA25Parser {

  public KSNemahaCountyParser() {
    super("NEMAHA COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "alerts@nemahaso.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("County")) data.strCity = "";
    return true;
  }

}
