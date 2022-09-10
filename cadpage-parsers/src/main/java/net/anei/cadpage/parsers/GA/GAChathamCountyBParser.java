package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class GAChathamCountyBParser extends DispatchA78Parser {

  public GAChathamCountyBParser() {
    super("CHATHAM COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "donotreply@TybeeIslandPoliceDepartmentalerts.com";
  }

}
