package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;
import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class MOHickoryCountyParser extends DispatchBCParser {

  public MOHickoryCountyParser() {
    super(CITY_LIST, "HICKORY COUNTY", "MO", DispatchA33Parser.A33_X_ADDR_EXT);
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities and Towns
      "CROSS TIMBERS",
      "HERMITAGE",
      "PRESTON",
      "WEAUBLEAU",
      "WHEATLAND",
      "",
      // Unincorporated Communities
      "AVERY",
      "ELKTON",
      "JORDAN",
      "NEMO",
      "PITTSBURG",
      "QUINCY",
      "WHITE CLOUD"
  };
}
