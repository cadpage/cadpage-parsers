package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA78Parser;

public class GAMcIntoshCountyParser extends DispatchA78Parser {
  
  public GAMcIntoshCountyParser() {
    super("MCINTOSH COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@WIREGRASSE911alerts.com";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("SMITHBERRY", "SMITH-BERRY");
    
  }
}
