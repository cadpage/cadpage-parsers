package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class COChaffeeCountyParser extends DispatchA64Parser {


  public COChaffeeCountyParser() {
    super("CHAFFEE COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
}
