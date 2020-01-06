package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.GroupBestParser;

/**
 * Tarrant County, TX
 */

public class TXTarrantCountyParser extends GroupBestParser {
  
  public TXTarrantCountyParser() {
    super(new TXTarrantCountyAParser(), new TXTarrantCountyBParser(), 
          new TXTarrantCountyCParser(), new TXTarrantCountyDParser());
  }

  static String[] CITY_LIST = new String[]{
      
      "TARRANT COUNTY",
    
      //Cities
      "ARLINGTON",
      "AZLE",
      "BEDFORD",
      "BENBROOK",
      "BLUE MOUND",
      "BURLESON",
      "COLLEYVILLE",
      "CROWLEY",
      "DALWORTHINGTON GARDENS",
      "EULESS",
      "EVERMAN",
      "FOREST HILL",
      "FORT WORTH",
      "GRAPEVINE",
      "GRAND PRAIRIE",
      "HALTOM CITY",
      "HASLET",
      "HURST",
      "KELLER",
      "KENNEDALE",
      "LAKE WORTH",
      "MANSFIELD",
      "NEWARK",
      "NORTH RICHLAND HILLS",
      "PELICAN BAY",
      "RICHLAND HILLS",
      "RIVER OAKS",
      "SAGINAW",
      "SANSOM PARK",
      "SOUTHLAKE",
      "WATAUGA",
      "WESTWORTH VILLAGE",
      "WHITE SETTLEMENT",

      //Towns
      "EDGECLIFF VILLAGE",
      "FLOWER MOUND",
      "LAKESIDE",
      "PANTEGO",
      "TROPHY CLUB",
      "WESTLAKE",
      "WESTOVER HILLS",

      //Census-designated places
      "BRIAR",
      "EAGLE MOUNTAIN",
      "PECAN ACRES",
      "RENDON",
      

      //Unincorporated communities
      "ALLIANCE",
      "AVONDALE",
      "BOSS",
      "EAGLE ACRES",
      "LAKE CREST ESTATES",
      "LAKE FOREST",
      "LAKE SHORE ESTATES",

      //Historical communities
      "BELT JUNCTION",
      "BIRDS",
      "BISBEE",
      "BRANSFORD",
      "CENTER POINT",
      "DIDO",
      "EDERVILLE",
      "GARDEN ACRES",
      "HANDLEY",
      "JOHNSONS STATION",
      
      // Dallas County
      "CEDAR HILL",
      "DESOTO",
      "DUNCANVILLE",
      "FERRIS",
      "GLENN HEIGHTS",
      "LANCASTER",
      "OVILLA",
      
      // Ellis County
      "MIDLOTHIAN",
      "OAK LEAF",
      "PALMER",
      "RED OAK",
      "WAXAHACHIE",
      
      // Johnson COunty
      "VENUS",
      
      // Guadalupe County
      "MCQUEENEY",
      "SEGUIN",
      
      // No city
      "NONE"
  };

}
