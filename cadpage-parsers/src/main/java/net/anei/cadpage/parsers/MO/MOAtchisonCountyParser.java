package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class MOAtchisonCountyParser extends DispatchA27Parser {

  public MOAtchisonCountyParser() {
    super("ATCHISON COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }

}
