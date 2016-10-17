package net.anei.cadpage.parsers.WI;

import java.util.Properties;
import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class WIRacineCountyAParser extends DispatchProphoenixParser {
  
  public WIRacineCountyAParser() {
    super(CITY_CODES,"RACINE COUNTY", "WI");
    setupProtectedNames("SIX AND ONE HALF");
  }
  
  @Override
  public String getFilter() {
    return "PhoenixAlert@goracine.org";
  }
  
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
