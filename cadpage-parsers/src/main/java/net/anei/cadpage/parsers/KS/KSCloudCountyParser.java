package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class KSCloudCountyParser extends DispatchA25Parser {

  public KSCloudCountyParser() {
    super("CLOUD COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "EnterpolAlerts@concordiaks.org";
  }
}
