package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchC02Parser;

public class NJOceanCountyHParser extends DispatchC02Parser {

  public NJOceanCountyHParser() {
    super("OCEAN COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "lakewoodactive911@gmail.com";
  }
}
