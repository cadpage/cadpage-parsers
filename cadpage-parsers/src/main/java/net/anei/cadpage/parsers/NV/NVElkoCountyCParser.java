package net.anei.cadpage.parsers.NV;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NVElkoCountyCParser extends DispatchA19Parser {

  public NVElkoCountyCParser() {
    super("ELKO COUNTY", "NV");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
