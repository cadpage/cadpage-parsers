package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Christian County, KY
 */
public class KYChristianCountyParser extends DispatchA27Parser {
  
  public KYChristianCountyParser() {
    super("CHRISTIAN COUNTY", "KY","[A-Z]{1,3}FD|[A-Z]+\\d+|\\d{8}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
