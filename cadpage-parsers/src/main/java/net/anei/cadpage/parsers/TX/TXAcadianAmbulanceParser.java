package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.general.XXAcadianAmbulanceParser;

/*
Acadian Ambulance, TX

*/

public class TXAcadianAmbulanceParser extends XXAcadianAmbulanceParser {

  public TXAcadianAmbulanceParser() {
    super("TX");
  }
  
  @Override
  public String getLocName() {
    return "Acadian Ambulance, TX";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
  
  @Override
  public String getFilter() {
    return "commcenteraustin@acadian.com," + super.getFilter();
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "/ ");
    body = stripFieldStart(body, "AUS CAD SMTP Paging / ");
    return super.parseMsg(body, data);
  }
}
