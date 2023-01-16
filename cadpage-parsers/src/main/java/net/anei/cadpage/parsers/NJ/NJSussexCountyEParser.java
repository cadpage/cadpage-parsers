package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class NJSussexCountyEParser extends DispatchA19Parser {

  public NJSussexCountyEParser() {
    super("SUSSEX COUNTY", "NJ");
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
