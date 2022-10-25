package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;

public class MOWarrenCountyBParser extends DispatchA24Parser {

  public MOWarrenCountyBParser() {
    super("WARREN COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "8800@centralregion-fire.org";
  }
}
