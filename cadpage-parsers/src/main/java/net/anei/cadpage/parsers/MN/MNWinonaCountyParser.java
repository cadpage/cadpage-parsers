package net.anei.cadpage.parsers.MN;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;


public class MNWinonaCountyParser extends DispatchA43Parser {

  public MNWinonaCountyParser() {
    super("WINONA COUNTY", "MN");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "21121 HWY 61",                         "+44.024670,-91.589470",
      "21530 HWY 61",                         "+44.024980,-91.569740",
      "21884 HWY 61",                         "+44.020540,-91.551310",
      "22194 HWY 61",                         "+44.019500,-91.531390",
      "22486 HWY 61",                         "+44.014090,-91.513110",
      "22750 HWY 61",                         "+44.006910,-91.496290",
      "23030 HWY 61",                         "+44.004870,-91.476850",
      "23562 HWY 61",                         "+43.996680,-91.461310",
      "24168 HWY 61",                         "+43.989820,-91.443470",
      "24760 HWY 61",                         "+43.980570,-91.430420",
      "25482 HWY 61",                         "+43.968390,-91.420270"
  });
}
