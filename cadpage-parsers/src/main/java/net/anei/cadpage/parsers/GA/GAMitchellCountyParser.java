package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class GAMitchellCountyParser extends DispatchSPKParser {

  public GAMitchellCountyParser() {
    super("MITCHELL COUNTY", "GA");
  }

  @Override
  public String getFilter() {
    return "MitchellCADS@mitchellcountyga.net";
  }

}
