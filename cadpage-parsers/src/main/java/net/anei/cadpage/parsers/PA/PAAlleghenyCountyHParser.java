package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class PAAlleghenyCountyHParser extends DispatchSPKParser {

  public PAAlleghenyCountyHParser() {
    super("ALLEGHENY COUNTY", "PA");
  }

  @Override
  public String getFilter() {
    return "InterACTCAD@uss.com";
  }

}
