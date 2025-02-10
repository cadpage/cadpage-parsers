package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH06Parser;

public class PADauphinCountyDParser extends DispatchH06Parser {

  public PADauphinCountyDParser() {
    super("DAUPHIN COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "@lcdes.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " Borough");
    return true;
  }
}
