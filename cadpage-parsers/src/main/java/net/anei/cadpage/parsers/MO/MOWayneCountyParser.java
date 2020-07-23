package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.dispatch.DispatchBCParser;


public class MOWayneCountyParser extends DispatchBCParser {


  public MOWayneCountyParser() {
    super("WAYNE COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "dispatch@wccd911.org,WAYNECODISPATCH@itiusa.com,WAYNECODISPATCH@OMNIGO.COM";
  }
}