package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXEllisCountyEParser extends DispatchA19Parser {

  public TXEllisCountyEParser() {
    super("ELLIS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

}
