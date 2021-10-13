package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class KYLawrenceCountyParser extends DispatchA86Parser {

  public KYLawrenceCountyParser() {
    super("LAWRENCE COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm3.info";
  }
}
