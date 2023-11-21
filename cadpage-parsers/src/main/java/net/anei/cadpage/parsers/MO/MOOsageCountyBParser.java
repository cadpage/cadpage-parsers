package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class MOOsageCountyBParser extends DispatchA71Parser {

  public MOOsageCountyBParser() {
    super("OSAGE COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

}
