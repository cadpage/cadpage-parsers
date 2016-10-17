package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;


public class MNFreebornCountyParser extends DispatchA27Parser {
  
  public MNFreebornCountyParser() {
    super("FREEBORN COUNTY", "MN", "\\d{3,4}[A-Z]{1,3}|[A-Z]+\\d+");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
