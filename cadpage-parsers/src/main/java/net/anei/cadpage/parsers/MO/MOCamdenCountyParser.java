package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOCamdenCountyParser extends DispatchA33Parser {
  public MOCamdenCountyParser() {
    super("CAMDEN COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "DISPATCH@CAMDENSO-MO.US,DISPATCH@OMNIGO.COM,noreply@omnigo.com";
  }
}
