package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;


public class MOWebsterCountyAParser extends DispatchA27Parser {
  
  public MOWebsterCountyAParser() {
    super("WEBSTER COUNTY", "MO", "([A-Z]{1,4}\\d*)(?:\\t.*)?");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
}
