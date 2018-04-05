package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class CASantaCruzCountyCParser extends DispatchH03Parser {
  
  public CASantaCruzCountyCParser() {
    super("SANTA CRUZ COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "@scr911users.org";
  }
}
