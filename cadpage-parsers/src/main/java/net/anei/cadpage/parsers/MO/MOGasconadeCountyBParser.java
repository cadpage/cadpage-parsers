package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MOGasconadeCountyBParser extends DispatchA33Parser {

  public MOGasconadeCountyBParser() {
    super("GASCONADE COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "HPDISPATCH@CENTURYTEL.NET";
  }

}
