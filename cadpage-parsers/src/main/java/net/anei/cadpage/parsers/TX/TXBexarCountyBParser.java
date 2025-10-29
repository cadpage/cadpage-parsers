package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXBexarCountyBParser extends DispatchA19Parser {

  public TXBexarCountyBParser() {
    super("BEXAR COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com,FRN-liveoaktx@getrave.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

}
