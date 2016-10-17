package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class IDTetonCountyParser extends DispatchA64Parser{

  public IDTetonCountyParser() {
    this("TETON COUNTY", "ID");
  }

  protected IDTetonCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }

  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }
}