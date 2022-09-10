package net.anei.cadpage.parsers.ID;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class IDBinghamCountyBParser extends DispatchA19Parser {
  
  public IDBinghamCountyBParser() {
    super("BINGHAM COUNTY", "ID");
  }
  
  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }
}
