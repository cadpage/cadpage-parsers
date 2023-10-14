package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;

public class COParkCountyBParser extends DispatchA38Parser {

  public COParkCountyBParser() {
    super("PARK COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "parkcodispatch@parkco.us";
  }

}
