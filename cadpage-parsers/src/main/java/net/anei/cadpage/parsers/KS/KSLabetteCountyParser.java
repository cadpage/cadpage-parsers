package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;

public class KSLabetteCountyParser extends DispatchBCParser {
  
  public KSLabetteCountyParser() {
    super("LABETTE COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "DISPATCH@LABETTECOUNTY.COM,LABETTECOUNTY@OMNIGO.COM";
  }
}
