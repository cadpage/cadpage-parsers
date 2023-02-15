package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INWhitleyCountyParser extends DispatchA19Parser {
  
  public INWhitleyCountyParser() {
    super("WHITLEY COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "spillman@whitleysd.com,dispatch@columbiacity.net,911@allencounty.us,@alert.active911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
