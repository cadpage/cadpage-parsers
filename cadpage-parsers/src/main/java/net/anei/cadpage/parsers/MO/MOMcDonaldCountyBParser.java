package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MOMcDonaldCountyBParser extends DispatchSPKParser {
  public MOMcDonaldCountyBParser() {
    super("MCDONALD COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "lisa@mc-911.org,911@NC-CDC.ORG";
  }
}
