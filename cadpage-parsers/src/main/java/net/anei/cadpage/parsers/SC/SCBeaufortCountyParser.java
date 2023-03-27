package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class SCBeaufortCountyParser extends DispatchA27Parser {

  public SCBeaufortCountyParser() {
    super("BEAUFORT COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }

}
