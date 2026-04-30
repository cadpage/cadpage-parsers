package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MIVanBurenCountyParser extends DispatchA19Parser {

  public MIVanBurenCountyParser() {
    super("VAN BUREN COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com,FRN-vanburencountymi@email.getrave.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
