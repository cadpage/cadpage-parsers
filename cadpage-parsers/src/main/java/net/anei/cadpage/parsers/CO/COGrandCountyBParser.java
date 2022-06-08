package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class COGrandCountyBParser extends DispatchA19Parser {

  public COGrandCountyBParser() {
    super("GRAND COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

}
