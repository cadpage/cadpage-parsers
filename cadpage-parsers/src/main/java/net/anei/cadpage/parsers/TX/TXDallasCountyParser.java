package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Dallas County, TX
 */
public class TXDallasCountyParser extends GroupBestParser {

  public TXDallasCountyParser() {
    super(new TXDallasCountyAParser(), new TXDallasCountyBParser(),
          new TXDallasCountyCParser(), new TXDallasCountyDParser(),
          new TXDallasCountyEParser(), new TXDallasCountyFParser(),
          new TXDallasCountyHParser());
  }

  static String[] CITY_LIST = new String[]{

      "DALLAS CO",

      //cities

      "BALCH SPRINGS",
      "CEDAR HILL",
      "CARROLLTON",
      "COCKRELL HILL",
      "COMBINE",
      "COPPELL",
      "DALLAS",
      "DESOTO",
      "DUNCANVILLE",
      "FARMERS BRANCH",
      "FERRIS",
      "GARLAND",
      "GLENN HEIGHTS",
      "GRAND PRAIRIE",
      "GRAPEVINE",
      "HUTCHINS",
      "IRVING",
      "LANCASTER",
      "LEWISVILLE",
      "MESQUITE",
      "OVILLA",
      "RICHARDSON",
      "ROWLETT",
      "SACHSE",
      "SEAGOVILLE",
      "UNIVERSITY PARK",
      "WILMER",
      "WYLIE",

      //towns

      "ADDISON",
      "HIGHLAND PARK",
      "SUNNYVALE",

      //Unincorporated community

      "SAND BRANCH",


      // Collin County
      "MURPHY"



  };
}
