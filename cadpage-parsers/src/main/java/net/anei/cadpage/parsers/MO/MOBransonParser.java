package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA44Parser;

public class MOBransonParser extends DispatchA44Parser {

  public MOBransonParser() {
    super("BRANSON", "MO");
  }
  
  @Override
  public String getFilter() {
    return "codydispatch@bransonmo.gov";
  }
}
