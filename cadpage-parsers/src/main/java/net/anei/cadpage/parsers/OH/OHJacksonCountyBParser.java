package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class OHJacksonCountyBParser extends DispatchA19Parser {

  public OHJacksonCountyBParser() {
    super("JACKSON COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "jacksonflexmsi@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
