package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class FLCitrusCountyCParser extends DispatchH03Parser {

  public FLCitrusCountyCParser() {
    super("CITRUS COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "NoReply@sheriffcitrus.org";
  }

}
