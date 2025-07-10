package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class MIGratiotCountyBParser extends DispatchSPKParser {

  public MIGratiotCountyBParser() {
    super("GRATIOT COUNTY", "MI");
  }

  @Override
  public String getFilter() {
    return "GratiotEMD@GratiotMI.com,mitchell_mattingly@yahoo.com,gratiot911cad@gratiotmi.com,gratiot911cad@gratiot911.org";
  }

}
