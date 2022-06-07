package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class GAMonroeCountyParser extends DispatchA78Parser {

  public GAMonroeCountyParser() {
    super("MONROE COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "donotreply@MonroeCountySOalerts.com";
  }

}
