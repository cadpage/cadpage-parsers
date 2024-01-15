package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class IDTetonCountyParser extends DispatchA55Parser{

  public IDTetonCountyParser() {
    super("TETON COUNTY", "ID");
  }

  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }
}