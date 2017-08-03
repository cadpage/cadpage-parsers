package net.anei.cadpage.parsers.MI;

import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class MIVanBurenCountyParser extends DispatchA1Parser {
  
  public MIVanBurenCountyParser() {
    super("VAN BUREN COUNTY", "MI");
  }
  
  @Override
  public String getFilter() {
    return "Dispatchers@vbco.org";
  }
}
