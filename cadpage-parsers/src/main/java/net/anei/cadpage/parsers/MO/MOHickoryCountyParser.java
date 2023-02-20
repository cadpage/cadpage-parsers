package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOHickoryCountyParser extends DispatchA33Parser {

  public MOHickoryCountyParser() {
    super("HICKORY COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
