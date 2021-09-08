package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Carter County,KY
 */
public class KYCarterCountyAParser extends DispatchA27Parser {

  public KYCarterCountyAParser() {
    super("CARTER COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "cis@carterco911.com";
  }
}
