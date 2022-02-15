package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class INRipleyCountyParser extends DispatchA19Parser {

  public INRipleyCountyParser() {
    super("RIPLEY COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

}
