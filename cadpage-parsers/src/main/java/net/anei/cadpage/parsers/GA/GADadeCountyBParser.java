package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

public class GADadeCountyBParser extends DispatchA9Parser {
  
  public  GADadeCountyBParser() {
    super("DADE COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "E911@dadega.com";
  }

}
