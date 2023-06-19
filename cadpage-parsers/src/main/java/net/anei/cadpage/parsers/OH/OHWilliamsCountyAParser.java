package net.anei.cadpage.parsers.OH;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OHWilliamsCountyAParser extends DispatchA19Parser {

  public OHWilliamsCountyAParser() {
    super(CITY_CODES, "WILLIAMS COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "BryanCAD,info@sundance-sys.com";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BRY", "BRYAN"
  });
}
