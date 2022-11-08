package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MODaviessCountyParser extends DispatchA33Parser {

  public MODaviessCountyParser() {
    super("DAVIESS COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

}
