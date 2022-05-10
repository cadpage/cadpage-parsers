package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class GAPolkCountyParser extends DispatchA78Parser {
  
  public GAPolkCountyParser() {
    super("POLK COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@Polk911alerts.com";
  }

}
