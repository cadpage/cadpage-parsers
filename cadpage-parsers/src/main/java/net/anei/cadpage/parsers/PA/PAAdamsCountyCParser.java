package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA9Parser;

public class PAAdamsCountyCParser extends DispatchA9Parser {
  
  public PAAdamsCountyCParser() {
    super("ADAMS COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "911@co.armstrong.pa.us";
  }

}
