package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class NJPassaicCountyBParser extends DispatchProphoenixParser {

  public NJPassaicCountyBParser() {
    super("PASSAIC COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }
}
