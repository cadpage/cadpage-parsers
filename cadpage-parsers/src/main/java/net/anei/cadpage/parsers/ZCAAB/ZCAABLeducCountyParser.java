package net.anei.cadpage.parsers.ZCAAB;

/*
Leduc County, AB, Canadaa
Alias for Red Deer County

*/
public class ZCAABLeducCountyParser extends ZCAABRedDeerCountyAParser {
  
  public ZCAABLeducCountyParser() {
    super("LEDUC COUNTY");
  }
  
  @Override
  public String getFilter() {
    return "Incident@ParklandCounty.com";
  }
}
