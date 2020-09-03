package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOScottCountyAParser extends DispatchA33Parser {


  public MOScottCountyAParser() {
    super("SCOTT COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "SCOTTCITY@PUBLICSAFETYSOFTWARE.NET,SCOTTCITY@itiusa.com,@OMNIGO.COM";
  }
}