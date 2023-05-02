package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COMontezumaCountyParser extends DispatchA55Parser {

  public  COMontezumaCountyParser() {
    this("MONTEZUMA COUNTY");
  }

  COMontezumaCountyParser(String defCity) {
    super(defCity, "CO");
  }

  @Override
  public String getFilter() {
    return "cadalerts@messaging.eforcesoftware.net";
  }

  @Override
  public String getAliasCode() {
    return "COMontezumaCounty";
  }
}
