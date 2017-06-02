package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOScottCountyParser extends DispatchA33Parser {
  
  
  public MOScottCountyParser() {
    super("SCOTT COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "SCOTTCITY@PUBLICSAFETYSOFTWARE.NET";
  }
}