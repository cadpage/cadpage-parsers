package net.anei.cadpage.parsers.OR;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA85Parser;

public class ORCoosCountyBParser extends DispatchA85Parser {

  public ORCoosCountyBParser() {
    super(CITY_CODES, "COOS COUNTY", "OR");
  }

  @Override
  public String getFilter() {
    return "FDPaging@coosbay.org";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BAN", "BANDON",
      "BRO", "BROOKINGS",
      "COO", "COOS BAY",
      "COQ", "COQUILLE",
      "GOD", "GOLD BEACH",
      "LAK", "LAKESIDE",
      "LAN", "LANGLOIS",
      "MYR", "MYRTLE POINT",
      "NOR", "NORTH BEND",
      "POR", "PORT ORFORD",
      "POW", "POWERS",
      "REE", "REEDSPORT",
      "ROS", "ROSEBURG"
  });

}
