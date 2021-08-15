package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class KSMiamiCountyAParser extends DispatchA72Parser {

  public KSMiamiCountyAParser() {
    super("MIAMI COUNTY", "KS");
  }
  
  @Override
  public String getFilter() {
    return "tpsadmin@micosheriff.org,notify@sheriffmiamicountyks.gov,notify@micoso.org";
  }
}
