package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYWhitleyCountyParser extends DispatchA27Parser {

  public KYWhitleyCountyParser() {
    super("WHITLEY COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
