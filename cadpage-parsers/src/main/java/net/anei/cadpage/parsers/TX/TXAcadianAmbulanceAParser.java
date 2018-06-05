package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.general.XXAcadianAmbulanceParser;

/*
Acadian Ambulance, TX

*/

public class TXAcadianAmbulanceAParser extends XXAcadianAmbulanceParser {

  public TXAcadianAmbulanceAParser() {
    super("TX");
  }
  
  @Override
  public String getLocName() {
    return "Acadian Ambulance, LA";
  }
  
  @Override
  public String getFilter() {
    return "commcenteraustin@acadian.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "/ ");
    body = stripFieldStart(body, "AUS CAD SMTP Paging / ");
    return super.parseMsg(body, data);
  }
}
