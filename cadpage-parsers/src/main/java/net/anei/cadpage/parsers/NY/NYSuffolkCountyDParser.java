package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.dispatch.DispatchRedAlert2Parser;



public class NYSuffolkCountyDParser extends DispatchRedAlert2Parser {
  
  public NYSuffolkCountyDParser() {
    super("SUFFOLK COUNTY","NY");
  }

  @Override
  public String getFilter() {
    return "paging@pjvac.org";
  }
}
