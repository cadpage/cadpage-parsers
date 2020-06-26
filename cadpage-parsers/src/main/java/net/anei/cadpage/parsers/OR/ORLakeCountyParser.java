package net.anei.cadpage.parsers.OR;


import net.anei.cadpage.parsers.dispatch.DispatchA22Parser;



public class ORLakeCountyParser extends DispatchA22Parser {
  
  public ORLakeCountyParser() {
    super("LAKE COUNTY", "OR");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@LCETS.net,Dispatch@psnet.us,donotreply@townoflakeview.org";
  }
}
