package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class MSHarrisonCountyEParser extends DispatchH06Parser {
  
  public MSHarrisonCountyEParser() {
    super("HARRISON COUNTY", "MS");
  }
  
  @Override
  public String getFilter() {
    return "FDALERTS@GULFPORT-MS.GOV";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (!data.strApt.isEmpty()) {
      data.strAddress = stripFieldEnd(data.strAddress, ' '+data.strApt);
    }
    return true;
  }

}
