package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA17Parser;


public class KYCarrollCountyCParser extends DispatchA17Parser {
  
  public KYCarrollCountyCParser() {
    super("CARROLL COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "Carrollcounty911@carrolltonpd.net,CARROLLCOUNTY911@CARROLLTONPD.NET";
  }

}
