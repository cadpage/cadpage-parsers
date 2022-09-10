package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INDeKalbCountyBParser extends DispatchA19Parser {
  
  public INDeKalbCountyBParser() {
    super("DEKALB COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "jober@co.dekalb.in.us,FlexRapidNotification@dccnotify.com";
  }
}
