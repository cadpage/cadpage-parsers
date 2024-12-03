package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

public class MSTateCountyAParser extends DispatchA49Parser {
  
  public MSTateCountyAParser() {
    super("TATE COUNTY", "MS");
  }
  
  @Override
  public String getFilter() {
    return "cadpage@e9.com";
  }

}
