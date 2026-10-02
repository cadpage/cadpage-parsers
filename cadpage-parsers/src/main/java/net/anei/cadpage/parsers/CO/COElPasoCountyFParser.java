package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COElPasoCountyFParser extends DispatchA55Parser {

  public COElPasoCountyFParser() {
    super("EL PASO COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
}
