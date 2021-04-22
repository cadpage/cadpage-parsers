package net.anei.cadpage.parsers.WV;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;


public class WVPocahontasCountyParser extends DispatchA19Parser {

  public WVPocahontasCountyParser() {
    super(CITY_CODES, "POCAHONTAS COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "@pocahontasemergency.com,@alert.active911.com";
  }

  private static Properties CITY_CODES = buildCodeTable(new String[] {
      "ARB", "ARBOVALE",
      "BAR", "BARTOW",
      "BH",  "BEARD HEIGHTS",
      "BOY", "BOYER",
      "BRO", "BROWNSBURG",
      "BS",  "BIG SPRINGS",
      "BUC", "BUCKEYE",
      "CAM", "CAMPBELLTOWN",
      "CAS", "CASS",
      "CL",  "CLOVER LICK",
      "COU", "",
      "DEN", "DENMAR",
      "DM",  "DROOP MOUNTAIN",
      "DUN", "DUNMORE",
      "DUR", "DURBIN",
      "EDR", "EDRAY",
      "FRA", "FRANK",
      "FRO", "FROST",
      "GB",  "GREEN BANK",
      "HIL", "HILLSBORO",
      "HUN", "HUNTERSVILLE",
      "JAC", "JACOX",
      "LIN", "LINWOOD",
      "LOB", "LOBELIA",
      "MAC", "MACE",
      "MAR", "MARLINTON",
      "MP",  "MILL POINT",
      "MS",  "MINNEHAHASPRING",
      "RIM", "RIMEL",
      "SB",  "STONY BOTTOM",
      "SEE", "SEEBERT",
      "SLA", "SLATYFORK",
      "SNO", "SNOWSHOE",
      "THO", "THORNWOOD",
      "WOO", "WOODROW",
      "WR",  "WILLIAMS RIVER"

  });
}
