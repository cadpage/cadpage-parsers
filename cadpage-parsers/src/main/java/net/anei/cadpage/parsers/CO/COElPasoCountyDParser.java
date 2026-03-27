package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COElPasoCountyDParser extends DispatchA55Parser {

  public COElPasoCountyDParser() {
    super("EL PASO COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
}
