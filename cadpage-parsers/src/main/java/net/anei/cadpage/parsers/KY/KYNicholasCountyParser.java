package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class KYNicholasCountyParser extends DispatchA27Parser {
  
  public KYNicholasCountyParser() {
    super("NICHOLAS COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }

}
