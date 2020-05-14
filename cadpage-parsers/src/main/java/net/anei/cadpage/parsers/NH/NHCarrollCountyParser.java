package net.anei.cadpage.parsers.NH;

public class NHCarrollCountyParser extends NHGraftonCountyBParser {
  
  public NHCarrollCountyParser() {
    super("CARROLL COUNTY", "NH");
  }
  
  @Override
  public String getFilter() {
    return "wfrpaging@gmail.com,dispatch@carrollcountynh.net";
  }
}
