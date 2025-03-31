package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class NCCravenCountyFParser extends DispatchA57Parser {
  
  public NCCravenCountyFParser() {
    super("CRAVEN COUNTY", "NC");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@havelocknc.us";
  }
}
