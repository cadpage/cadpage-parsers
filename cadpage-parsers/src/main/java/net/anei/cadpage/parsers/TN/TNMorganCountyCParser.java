package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNMorganCountyCParser extends DispatchA74Parser {

  public TNMorganCountyCParser() {
    super("MORGAN COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@MorganCountyTNE911.com";
  }
}
