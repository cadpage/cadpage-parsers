package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;


public class GALamarCountyParser extends DispatchA78Parser {

  public GALamarCountyParser() {
    super("LAMAR COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "CAD@ssialerts.com";
  }
}

