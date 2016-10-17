package net.anei.cadpage.parsers.UT;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class UTTooeleCountyParser extends DispatchA19Parser {
  
  public UTTooeleCountyParser() {
    super("TOOELE COUNTY", "UT");
  }
  
  @Override
  public String getFilter() {
    return "ripspillman@co.tooele.ut.us,donotreply@tooeleco.org";
  }
}
