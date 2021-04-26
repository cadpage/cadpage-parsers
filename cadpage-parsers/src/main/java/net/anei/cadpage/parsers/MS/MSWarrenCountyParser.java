package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

public class MSWarrenCountyParser extends DispatchA49Parser {

  public MSWarrenCountyParser() {
    super("WARREN COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "vicksburg.warren911@co.warren.ms.us";
  }
}
