package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MOHenryCountyBParser extends DispatchSPKParser {

  public MOHenryCountyBParser() {
    super("HENRY COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "interact@henry911.com";
  }

}
