package net.anei.cadpage.parsers.AZ;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class AZMohaveCountyParser extends DispatchA19Parser {

  public AZMohaveCountyParser() {
    super("MOHAVE COUNTY", "AZ");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("Unincorporated")) data.strCity = "";
    return true;
  }

}
