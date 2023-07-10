package net.anei.cadpage.parsers.WV;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WVJeffersonCountyParser extends DispatchA19Parser {

  public WVJeffersonCountyParser() {
    super(CITY_CODES, "JEFFERSON COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "CAD911@jeffersoncountywv.org";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "HFU",  "Shannondale",
      "RN",   "Ranson"
  });
}
