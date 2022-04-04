package net.anei.cadpage.parsers.NH;

public class NHCarrollCountyAParser extends NHGraftonCountyBParser {
  
  public NHCarrollCountyAParser() {
    super("CARROLL COUNTY", "NH");
  }
  
  @Override
  public String getFilter() {
    return "wfrpaging@gmail.com,dispatch@carrollcountynh.net";
  }
}
