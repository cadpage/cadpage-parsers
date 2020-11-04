package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class OKOklahomaCountyBParser extends DispatchSPKParser {
  
  public OKOklahomaCountyBParser() {
    super("OKLAHOMA COUNTY", "OK");
  }
  
  @Override
  public String getFilter() {
    return "info@okletech.org";
  }

}
