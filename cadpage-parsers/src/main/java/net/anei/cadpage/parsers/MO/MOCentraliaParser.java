package net.anei.cadpage.parsers.MO;


import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class MOCentraliaParser extends DispatchA25Parser {

  public MOCentraliaParser() {
    super("CENTRAILIA", "MO");
  }
  
  @Override
  public String getFilter() {
    return "EnterpolAlerts@centraliapd.org";  
  }
 
}
