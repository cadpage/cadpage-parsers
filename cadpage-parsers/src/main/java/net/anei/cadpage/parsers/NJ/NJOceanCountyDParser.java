package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.dispatch.DispatchC06Parser;

public class NJOceanCountyDParser extends DispatchC06Parser {

  public NJOceanCountyDParser() {
    super("OCEAN COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "OceanPDPaging@Enfwebmail.onmicrosoft.com";
  }
}
