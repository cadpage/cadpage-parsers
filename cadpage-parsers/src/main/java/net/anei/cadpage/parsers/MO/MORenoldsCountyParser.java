package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;

public class MORenoldsCountyParser extends DispatchA33Parser {
  
  public MORenoldsCountyParser() {
    super("RENOLDS COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "@omnigo.com";
  }

}
