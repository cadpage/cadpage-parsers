package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXHowardCountyParser extends DispatchA72Parser {
  
  public TXHowardCountyParser() {
    super("HOWARD COUNTY", "TX");
  }
  
  @Override
  public String getFilter() {
    return "brazo@bigspringpd.net,bigspringtxnotifications@gmail.com";
  }
}
