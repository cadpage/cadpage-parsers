package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchC14Parser;

public class MSTateCountyBParser extends DispatchC14Parser {

  public MSTateCountyBParser() {
    super("TATE COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "cadpage@tate.adsisoftware.com";
  }
}
