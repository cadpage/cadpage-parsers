package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COMontezumaCountyAParser extends DispatchA55Parser {

  public  COMontezumaCountyAParser() {
    this("MONTEZUMA COUNTY");
  }

  COMontezumaCountyAParser(String defCity) {
    super(defCity, "CO");
  }

  @Override
  public String getFilter() {
    return "cadalerts@messaging.eforcesoftware.net";
  }
}
