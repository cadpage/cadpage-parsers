package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYOhioCountyBParser extends DispatchSPKParser {

  public KYOhioCountyBParser() {
    super("OHIO COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@ohiocountyky.gov";
  }

}
