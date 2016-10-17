package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXTarrantCountyCParser extends DispatchA18Parser {
  
  public TXTarrantCountyCParser() {
    super(CITY_LIST, "TARRANT COUNTY","TX");
  }
 
  @Override
  public String getFilter() {
    return "crimespage@lakeworthtx.org";
  }

  private static String[] CITY_LIST = new String[]{
    
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
      "JOHNSONS STATION"
  };
  
  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "BURELSON",     "BURLESON"
  });
}
