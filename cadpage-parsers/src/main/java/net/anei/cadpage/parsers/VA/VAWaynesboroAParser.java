package net.anei.cadpage.parsers.VA;


public class VAWaynesboroAParser extends VAAugustaCountyParser {
  
  public VAWaynesboroAParser() {
    super("WAYNESBORO", "VA");
  }
  
  @Override
  public String getFilter() {
    return "@ci.waynesboro.va.us";
  }
}


