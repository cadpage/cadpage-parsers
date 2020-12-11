package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Carter County,KY
 */
public class KYCarterCountyParser extends DispatchA27Parser {

  public KYCarterCountyParser() {
    super("CARTER COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "cis@carterco911.com";
  }
}
