package net.anei.cadpage.parsers.WI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class WIKenoshaCountyCParser extends DispatchProphoenixParser {
  
  public WIKenoshaCountyCParser() {
    super(CITY_CODES, CITY_LIST, "KENOSHA COUNTY", "WI");
  }
  
  @Override
  public String getFilter() {
    return "CAD@plprairiewi.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return super.parseMsg(body, data);
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      // City
      "KENOSHA",

      // Villages
      "BRISTOL",
      "GENOA CITY",
      "PADDOCK LAKE",
      "PLEASANT PRAIRIE",
      "SALEM LAKES",
      "SOMERS",
      "TWIN LAKES",

      // Towns
      "BRIGHTON",
      "PARIS",
      "RANDALL",
      "SOMERS",
      "WHEATLAND",

      // Census-designated places
      "CAMP LAKE",
      "LILY LAKE",
      "POWERS LAKE",
      "WILMOT",

      // Unincorporated communities

      "BASSETT",
      "BENET LAKE",
      "BERRYVILLE",
      "BRIGHTON",
      "CENTRAL PARK",
      "CHAPIN",
      "FOX RIVER",
      "KELLOGGS CORNERS",
      "KLONDIKE",
      "LAKE SHANGRILA",
      "LIBERTY CORNERS",
      "NEW MUNSTER",
      "PARIS",
      "SALEM OAKS",
      "SALEM",
      "TREVOR",
      "VOLTZ LAKE",

      // Ghost towns/neighborhoods
      "AURORA"
  };
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "VPP", "Pleasant Prairie"
  });
  
}
