package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYLaurelCountyParser extends DispatchA27Parser {

  public KYLaurelCountyParser() {
    super("LAUREL COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org,londonlaurelcounty911@cissystem.com,CAD@knoxco911.org,ciscads@laurelco911.com";
  }

}
