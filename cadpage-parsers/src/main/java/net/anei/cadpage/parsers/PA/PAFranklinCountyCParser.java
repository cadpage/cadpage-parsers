package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class PAFranklinCountyCParser extends DispatchA19Parser {
  
  public PAFranklinCountyCParser() {
    super("FRANKLIN COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

}
