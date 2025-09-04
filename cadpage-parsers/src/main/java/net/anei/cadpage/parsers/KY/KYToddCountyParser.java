package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;


public class KYToddCountyParser extends DispatchA27Parser {

  public KYToddCountyParser() {
    super("TODD COUNTY", "KY","[A-Z]{1,3}FD|[A-Z]+\\d+|\\d{8}");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,noreply@toddcogov.com,dispactive911@toddcogov.com";
  }
}
