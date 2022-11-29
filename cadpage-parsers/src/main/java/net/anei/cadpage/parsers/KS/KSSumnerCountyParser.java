package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSSumnerCountyParser extends DispatchA25Parser {

  public KSSumnerCountyParser() {
    super("SUMNER COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "sumner911cad@co.sumner.ks.us,donotreply@mulvane.us,CAD@mulvane.us,notsowpdd1@hotmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("County")) data.strCity = "";
    return true;
  }

}
