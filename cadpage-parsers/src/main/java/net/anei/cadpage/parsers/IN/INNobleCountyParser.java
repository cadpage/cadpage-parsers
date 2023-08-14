package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

/**
 * Noble County, AL
 */
public class INNobleCountyParser extends DispatchA19Parser {


  public INNobleCountyParser() {
    super("NOBLE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com,donotreply@nobleco.us,FlexRapidNotification@dccnotify.com";
  }
}
