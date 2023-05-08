package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INAdamsCountyBParser extends DispatchA19Parser {
  
  public INAdamsCountyBParser() {
    super("ADAMS COUNTY", "IN");
  }
  
  @Override
  public String getFilter() {
    return "notification@co.adams.in.us";
  }
}
