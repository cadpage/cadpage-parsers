package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CAInyoCountyParser extends DispatchA20Parser {
  
  public CAInyoCountyParser() {
    super("INYO COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "rims2text@bishoppd.org";
  }

}
