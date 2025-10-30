package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class TXHutchinsonCountyParser extends DispatchA19Parser {
  
  public TXHutchinsonCountyParser() {
    super("HUTCHINSON COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com,FRN-borgertx@email.getrave.com";
  }

}
