package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * LYON County, MN
 */

public class MNLyonCountyParser extends DispatchA27Parser {
  
  public MNLyonCountyParser() {
    super("LYON COUNTY", "MN", "[A-Z]+FD|\\d+[A-Z]{2}");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
