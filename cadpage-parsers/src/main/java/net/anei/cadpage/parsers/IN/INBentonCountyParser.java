package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INBentonCountyParser extends DispatchA19Parser {
  
  public INBentonCountyParser() {
    super("BENTON COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

}
