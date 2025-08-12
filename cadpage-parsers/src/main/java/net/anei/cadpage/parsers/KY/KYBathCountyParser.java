package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYBathCountyParser extends DispatchA74Parser {

  public KYBathCountyParser() {
    super("BATH COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }

}
