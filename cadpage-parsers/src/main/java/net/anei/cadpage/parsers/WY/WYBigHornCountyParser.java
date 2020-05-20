package net.anei.cadpage.parsers.WY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class WYBigHornCountyParser extends DispatchA20Parser {
  
  public WYBigHornCountyParser() {
    super("BIG HORN COUNTY", "WY");
  }
  
  @Override
  public String getFilter() {
    return "rimspaging@bighorncountywy.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    subject = "Dispatched Call " + subject;
    return super.parseMsg(subject, body, data);
  }

}
