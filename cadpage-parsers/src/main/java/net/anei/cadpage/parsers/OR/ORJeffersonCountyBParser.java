package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchA77Parser;

public class ORJeffersonCountyBParser extends DispatchA77Parser {

  public ORJeffersonCountyBParser() {
    this("JEFFERSON COUNTY", "OR");
  }

  public ORJeffersonCountyBParser(String defCity, String defState) {
    super("Alert", defCity, defState);
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
