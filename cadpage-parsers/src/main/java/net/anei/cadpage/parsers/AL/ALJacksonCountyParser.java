package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ALJacksonCountyParser extends DispatchA71Parser {

  public ALJacksonCountyParser() {
    super("JACKSON COUNTY", "AL");
  }

  @Override
  public String getFilter() {
    return "ALDispatch@scottsboro.org";
  }
}
