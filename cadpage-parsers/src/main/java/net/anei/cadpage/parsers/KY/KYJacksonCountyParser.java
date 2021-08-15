package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYJacksonCountyParser extends DispatchA27Parser {

  public KYJacksonCountyParser() {
    super("JACKSON COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@jacksoncountyky911.com";
  }

}
