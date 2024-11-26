package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WIMarinetteCountyBParser extends DispatchA19Parser {

  public WIMarinetteCountyBParser() {
    super("MARINETTE COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return "rapidnotification@marinettecounty.com,CallSummaryReport@marinettecounty.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
