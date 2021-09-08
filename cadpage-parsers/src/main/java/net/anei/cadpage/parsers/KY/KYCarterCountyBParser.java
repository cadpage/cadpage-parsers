package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYCarterCountyBParser extends DispatchA74Parser {

  public KYCarterCountyBParser() {
    super("CARTER COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }

}
