package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Christian County, IL
 */
public class ILShelbyCountyParser extends DispatchEmergitechParser {
  
  public ILShelbyCountyParser() {
    super("ChristianCounty911:", CITY_LIST, "SHELBY COUNTY", "IL", TrailAddrType.PLACE);
  }
  
  private static final String[] CITY_LIST = new String[]{
      
      //Cities
      
      "SHELBYVILLE",

      //Towns

      "EDINBURGH",
      "FAIRLAND",
      "MORRISTOWN",
      "ST. PAUL",

      //Census-designated place

      "WALDRON",

      //Other unincorporated places

      "BEECH BROOK",
      "BENGAL",
      "BLUE RIDGE",
      "BOGGSTOWN",
      "BRENT WOODS",
      "BROOKFIELD",
      "CAMP FLAT ROCK",
      "CANDLEGLO VILLAGE",
      "CLOVER VILLAGE",
      "FENNS",
      "FLAT ROCK",
      "FOUNTAINTOWN",
      "FREEPORT",
      "GENEVA",
      "GREEN MEADOWS",
      "GWYNNEVILLE",
      "HILDEBRAND VILLAGE",
      "KNIGHTHOOD GROVE",
      "KNIGHTHOOD VILLAGE",
      "LEWIS CREEK",
      "LONDON",
      "LONDON HEIGHTS",
      "MARIETTA",
      "MARION",
      "MEIKS",
      "MELTZER",
      "MIDDLETOWN",
      "MORVEN",
      "MOUNT AUBURN",
      "NORRISTOWN",
      "PLEASANT VIEW",
      "PLEASURE VALLEY",
      "PRESCOTT",
      "RAYS CROSSING",
      "ROLLING RIDGE",
      "SLEEPY HOLLOW",
      "SMITHLAND",
      "SOUTHEAST MANOR",
      "SUGAR CREEK",
      "WALKERVILLE",
      "WILSON CORNER",

      //Townships

      "ADDISON",
      "BRANDYWINE",
      "HANOVER",
      "HENDRICKS",
      "JACKSON",
      "LIBERTY",
      "MARION",
      "MORAL",
      "NOBLE",
      "SHELBY",
      "SUGAR CREEK",
      "UNION",
      "VAN BUREN",
      "WASHINGTON"


  };
}
