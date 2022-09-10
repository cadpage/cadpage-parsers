package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA89Parser;

public class TNMorganCountyBParser extends DispatchA89Parser {

  public TNMorganCountyBParser() {
    super("MORGAN COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@MorganCountyTNE911.com";
  }
}
