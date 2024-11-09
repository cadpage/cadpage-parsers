package net.anei.cadpage.parsers.WA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WAWhitmanCountyBParser extends DispatchA19Parser {

  public WAWhitmanCountyBParser() {
    this("WHITMAN COUNTY", "WA");
  }

  WAWhitmanCountyBParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com,whitcomrapidnotification@whitcom.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
