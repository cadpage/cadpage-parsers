package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class VANelsonCountyParser extends DispatchA19Parser {

  public VANelsonCountyParser() {
    super(CITY_CODES, "NELSON COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "spflex@nelsoncounty.org";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AFT", "AFTON",
      "AMH", "AMHERST",
      "ARR", "ARRINGTON",
      "FAB", "FABER",
      "GLA", "GLADSTONE",
      "HOW", "HOWARDSVILLE",
      "LOV", "LOVINGSTON",
      "LYN", "LYNDHURST",
      "MON", "MONTEBELLO",
      "NEL", "NELLEYSFORD",
      "PIN", "PINEY RIVER",
      "ROS", "ROSELAND",
      "SCH", "SCHUYLER",
      "SHI", "SHIPMAN",
      "TOA", "GLADSTONE",    // ????
      "TYR", "TRYRO",
      "VES", "VESUVIUS",
      "WAG", "LYNDHURST",    // ???
      "WIN", "WINGINA"

  });

}
