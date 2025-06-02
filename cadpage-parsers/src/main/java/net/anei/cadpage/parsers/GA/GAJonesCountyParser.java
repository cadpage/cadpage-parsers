package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class GAJonesCountyParser extends DispatchA78Parser {

  public GAJonesCountyParser() {
    super("JONES COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "donotreply@JonesSOalerts.com";
  }
}
