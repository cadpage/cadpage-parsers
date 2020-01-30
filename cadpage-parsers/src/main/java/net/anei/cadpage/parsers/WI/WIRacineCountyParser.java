package net.anei.cadpage.parsers.WI;

import net.anei.cadpage.parsers.GroupBestParser;

public class WIRacineCountyParser extends GroupBestParser {
  public WIRacineCountyParser() {
    super(new WIRacineCountyAParser(), new WIRacineCountyBParser(), 
          new WIRacineCountyCParser(), new WIRacineCountyDParser());
  }
  
  
  static final String[] CITY_LIST = new String[]{
      
      // Cities
      "BURLINGTON",
      "RACINE",

      // Villages
      "CALEDONIA",
      "ELMWOOD PARK",
      "MOUNT PLEASANT",
      "MT PLEASANT",
      "NORTH BAY",
      "ROCHESTER",
      "STURTEVANT",
      "UNION GROVE",
      "WATERFORD",
      "WIND POINT",

      // Towns
      "BURLINGTON",
      "DOVER",
      "RAYMOND",
      "NORWAY",
      "WATERFORD",
      "YORKVILLE",

      // Census-designated places
      "BOHNERS LAKE",
      "BROWNS LAKE",
      "EAGLE LAKE",
      "TICHIGAN",
      "WIND LAKE",

      // Unincorporated communities
      "BUENA PARK",
      "CALDWELL",
      "CEDAR PARK",
      "EAGLE LAKE MANOR",
      "EAGLE LAKE TERRACE",
      "FRANKSVILLE",
      "HONEY CREEK",
      "HONEY LAKE",
      "HUSHER",
      "IVES GROVE",
      "KANSASVILLE",
      "NORTH CAPE",
      "RAYMOND",
      "TICHIGAN",
      "UNION CHURCH",
      "YORKVILLE",
      
      // Milwaukee County
      "MILWAUKEE"
  };
}
