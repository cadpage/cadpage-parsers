package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WVNicholasCountyParser extends DispatchA19Parser {

  public WVNicholasCountyParser() {
    super("NICHOLAS COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "smtp.flex@nicholas911.com";
  }
}
