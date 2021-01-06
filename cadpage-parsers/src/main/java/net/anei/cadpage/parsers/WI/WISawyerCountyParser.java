package net.anei.cadpage.parsers.WI;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WISawyerCountyParser extends DispatchA19Parser {

  public WISawyerCountyParser() {
    super(CITY_CODES, "SAWYER COUNTY", "WI");
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
      "BAS", "BASS LAKE",
      "CHA", "HAYWARD",
      "HAY", "HAYWARD",
      "LBA", "BASS LAKE",
      "LEN", "LENROOT",
      "LHA", "HAYWARD",
      "LHU", "HUNTER",
      "LSA", "SAND LAKE",
      "OJI", "OJIBWA",
      "RAD", "RADISSON",
      "SPI", "SPIDER LAKE",
      "VEX", "EXELAND"
  });

}
