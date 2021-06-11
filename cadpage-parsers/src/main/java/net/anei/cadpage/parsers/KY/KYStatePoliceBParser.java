package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYStatePoliceBParser extends DispatchSPKParser {

  public KYStatePoliceBParser() {
    this("");
  }

  KYStatePoliceBParser(String defCity) {
    super(defCity, "KY");
  }

  @Override
  public String getAliasCode() {
    return "KYStatePoliceB";
  }

  @Override
  public String getFilter() {
    return "Ksp.NGCAD@ky.gov";
  }

}
