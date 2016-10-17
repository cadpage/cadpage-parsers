package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.dispatch.DispatchA12Parser;


public class ORLincolnCityParser extends DispatchA12Parser {
  
  public ORLincolnCityParser() {
    super("LINCOLN CITY", "OR");
  }
  
  @Override
  public String getFilter() {
    return "Group_Page_Notification@usamobility.net";
  }
}
