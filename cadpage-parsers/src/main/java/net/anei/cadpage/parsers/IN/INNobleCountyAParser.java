package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

/**
 * Noble County, AL
 */
public class INNobleCountyAParser extends DispatchA19Parser {


  public INNobleCountyAParser() {
    super("NOBLE COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com,donotreply@nobleco.us,FlexRapidNotification@dccnotify.com,donotreply@nobleco.gov";
  }
}
