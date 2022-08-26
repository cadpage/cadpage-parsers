package net.anei.cadpage.parsers.NV;

import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class NVRenoParser extends DispatchA57Parser {
  
  public NVRenoParser() {
    super("RENO", "NV");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@renoairport.com";
  }
}
