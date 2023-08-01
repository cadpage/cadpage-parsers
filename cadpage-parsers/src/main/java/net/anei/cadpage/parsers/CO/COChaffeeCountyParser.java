package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COChaffeeCountyParser extends DispatchA55Parser {


  public COChaffeeCountyParser() {
    super("CHAFFEE COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }
}
