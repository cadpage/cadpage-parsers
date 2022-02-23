package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class TNGibsonCountyParser extends DispatchA74Parser {

  public TNGibsonCountyParser() {
    super("GIBSON COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@GibsonTN911.info";
  }
}
