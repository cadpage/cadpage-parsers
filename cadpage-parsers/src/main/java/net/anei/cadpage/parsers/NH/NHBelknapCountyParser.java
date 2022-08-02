package net.anei.cadpage.parsers.NH;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchArchonixParser;

public class NHBelknapCountyParser extends DispatchArchonixParser {

  public NHBelknapCountyParser() {
    super(CITY_CODES, "BELKNAP COUNTY", "NH");
  }

  @Override
  public String getFilter() {
    return "cccademail@LRMFA.org";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "05", "CENTER HARBOR",
      "14", "MEREDITH",
      "15", "MOULTONBOROUGH",
      "19", "SANBORNTON",
      "20", "SANDWICH"
  });

}
