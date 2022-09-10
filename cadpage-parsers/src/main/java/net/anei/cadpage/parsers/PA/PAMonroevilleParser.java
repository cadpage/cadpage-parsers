package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class PAMonroevilleParser extends DispatchA19Parser {
  
  public PAMonroevilleParser() {
    super("MONROEVILLE", "PA");
  }
  
  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
 
}
