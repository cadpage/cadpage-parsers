package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSRooksCountyParser extends DispatchA25Parser {
  
  public KSRooksCountyParser() {
    super("ROOKS COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "EnterpolCADAlerts@Ruraltel.net";
  }
}
