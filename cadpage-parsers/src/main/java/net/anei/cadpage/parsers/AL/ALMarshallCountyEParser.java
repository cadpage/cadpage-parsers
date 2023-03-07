package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class ALMarshallCountyEParser extends DispatchA19Parser {
  
  public ALMarshallCountyEParser() {
    super("MARSHALL COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com,donotreply@cullmansheriff.org,cemsdispatch@cullmanregional.com";
  }

}
