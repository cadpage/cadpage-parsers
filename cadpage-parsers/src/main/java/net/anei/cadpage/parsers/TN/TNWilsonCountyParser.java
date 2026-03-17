package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TNWilsonCountyParser extends DispatchA19Parser {
  
  public TNWilsonCountyParser() {
    super("WILSON COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "dispatch1@wilson911.org";
  }

}
