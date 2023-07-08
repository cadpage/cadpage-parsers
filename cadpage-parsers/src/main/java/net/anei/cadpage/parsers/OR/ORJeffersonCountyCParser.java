package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ORJeffersonCountyCParser extends DispatchA19Parser {

  public ORJeffersonCountyCParser() {
    super("JEFFERSON COUNTY", "OR");
  }

  public ORJeffersonCountyCParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }
  
  @Override
  public String getAliasCode() {
    return "ORJeffersonCountyC";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
