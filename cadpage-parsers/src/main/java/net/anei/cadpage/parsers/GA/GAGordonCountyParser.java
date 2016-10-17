package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA17Parser;


public class GAGordonCountyParser extends DispatchA17Parser {
  
  public GAGordonCountyParser() {
    super("GORDON COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "gordon911cad@gmail.com";
  }

}
