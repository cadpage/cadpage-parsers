package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA77Parser;

public class MSNewtonCountyBParser extends DispatchA77Parser {

  public MSNewtonCountyBParser() {
    super("Station Alerting", "NEWTON COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "rapidnotification@newtoncountyms.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
