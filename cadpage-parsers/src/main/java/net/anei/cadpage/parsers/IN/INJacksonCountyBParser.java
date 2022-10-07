package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INJacksonCountyBParser extends DispatchA19Parser {

  public INJacksonCountyBParser() {
    super("JACKSON COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "jacksoncountydispatch@jacksoncountyin.org,FlexRapidNotification@dccnotify.com";
  }
}
