package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;


public class SCMcCormickCountyParser extends DispatchA74Parser {
  
  public SCMcCormickCountyParser() {
    super("MCCORMICK COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "Dispatch@McCormickE911.info";
  }
}
