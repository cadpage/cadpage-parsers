package net.anei.cadpage.parsers.WI;

import java.util.Properties;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class WIRacineCountyAParser extends DispatchProphoenixParser {
  
  public WIRacineCountyAParser() {
    super(CITY_CODES,CITY_LIST, "RACINE COUNTY", "WI");
    setupProtectedNames("SIX AND ONE HALF");
  }
  
  @Override
  public String getFilter() {
    return "PhoenixAlert@goracine.org";
  }
  
  private static final String[] CITY_LIST = new String[]{
      
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
      "YORKVILLE"
  };
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BU", "BURLINGTON",
      "CA", "CALEDONIA",
      "DO", "DOVER",
      "MP", "MOUNT PLEASENT",
      "NW", "NORWAY",
      "RA", "RACINE",
      "RY", "RAYMOND",
      "SO", "BURLINGTON",
      "SV", "STURTEVANT",
      "TB", "BURLINGTON",
      "TW", "WATERFORD",
      "UG", "UNION GROVE",
      "VR", "BURLINGTON",
      "VW", "WATERFORD",
      "YO", "YORKVILLE"
  });
}
