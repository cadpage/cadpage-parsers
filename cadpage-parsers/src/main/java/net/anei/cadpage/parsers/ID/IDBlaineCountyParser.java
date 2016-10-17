package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA4Parser;


public class IDBlaineCountyParser extends DispatchA4Parser {
  
  public IDBlaineCountyParser() {
    super("BLAINE COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "ldispatch@co.blaine.id.us,LogiSYS CAD";
  }
 
}
