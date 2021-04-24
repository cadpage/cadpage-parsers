package net.anei.cadpage.parsers.ID;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDGemCountyAParser extends DispatchA19Parser {

  public IDGemCountyAParser() {
    super(CITY_CODES, "GEM COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "EM", "EMMETT"
  });

}
