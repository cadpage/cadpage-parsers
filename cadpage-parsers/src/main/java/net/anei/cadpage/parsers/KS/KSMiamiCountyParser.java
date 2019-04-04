package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class KSMiamiCountyParser extends DispatchA72Parser {

  public KSMiamiCountyParser() {
    super("MIAMI COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "tpsadmin@micosheriff.org,notify@sheriffmiamicountyks.gov,notify@micoso.org";
  }
}
