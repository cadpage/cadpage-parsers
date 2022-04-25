package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class GACrispCountyParser extends DispatchA78Parser {

  public GACrispCountyParser() {
    super("CRISP COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "donotreply@Crisp911alerts.com";
  }

}
