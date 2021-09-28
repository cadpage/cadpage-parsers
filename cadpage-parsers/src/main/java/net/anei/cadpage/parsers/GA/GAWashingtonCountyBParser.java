package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class GAWashingtonCountyBParser extends DispatchA78Parser {

  public GAWashingtonCountyBParser() {
    super("WASHINGTON COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "CAD@ssialerts.com";
  }
}
