package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CAButteCountyCParser extends DispatchA20Parser {

  public CAButteCountyCParser() {
    super("BUTTE COUNTY", "CA");
  }

  @Override
  public String getFilter() {
    return "RIMS2text@chicoca.gov";
  }
}
