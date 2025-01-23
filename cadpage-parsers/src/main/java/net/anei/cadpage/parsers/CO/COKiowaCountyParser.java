package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COKiowaCountyParser extends DispatchA55Parser {

  public COKiowaCountyParser() {
    super("KIOWA COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "reports@messagingvzw.eforcesoftware.net";
  }

}
