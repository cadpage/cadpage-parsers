package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class KSOsageCountyParser extends DispatchA19Parser {

  public KSOsageCountyParser() {
    super("OSAGE COUNTY", "KS");
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
