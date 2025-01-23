package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class MIAlpenaCountyAParser extends DispatchB2Parser {
  
  public MIAlpenaCountyAParser() {
    super("ALPENACOUNTY.ORG:",CITY_LIST, "ALPENA COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "ALPENACOUNTY.ORG@alpenacounty.org";
  }

  private static final String[] CITY_LIST = new String[]{
    "ALPENA",
    "BOLTON",
    "CATHRO",
    "HERRON",
    "HILLMAN",
    "HUBBARD LAKE",
    "LACHINE",
    "LEER",
    "OSSINEKE",
    "SPRATT"
  };
}
