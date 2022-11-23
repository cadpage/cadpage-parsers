package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class OzarkCountyParser extends DispatchA33Parser {

  public OzarkCountyParser() {
    super("OZARK COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }
}
