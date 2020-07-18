package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOWayneCountyParser extends DispatchA33Parser {


  public MOWayneCountyParser() {
    super("WAYNE COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "dispatch@wccd911.org,WAYNECODISPATCH@itiusa.com,WAYNECODISPATCH@OMNIGO.COM";
  }
}