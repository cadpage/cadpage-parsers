package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXCrowleyParser extends DispatchA18Parser {
  
  public TXCrowleyParser() {
    super(CITY_LIST, "CROWLEY","TX");
  }
 
  @Override
  public String getFilter() {
    return "crimespaging@ci.crowley.tx.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    String city = MISSPELLED_CITIES.getProperty(data.strCity.toUpperCase());
    if (city != null) data.strCity = city;
    if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
    else if (data.strCity.endsWith(" Co")) data.strCity += "unty";
    return true;
  }

  private static String[] CITY_LIST = new String[]{
    
    // Cities
    "ARLINGTON",
    "AZLE",
    "BEDFORD",
    "BENBROOK",
    "BLUE MOUND",
    "BURLESON",
    "BURELSON",  // Mispelled
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

    // Towns,
    "EDGECLIFF VILLAGE",
    "FLOWER MOUND",
    "LAKESIDE",
    "PANTEGO",
    "TROPHY CLUB",
    "WESTLAKE",
    "WESTOVER HILLS",

    // Census-designated places,
    "BRIAR",
    "PECAN ACRES",
    "RENDON",

    // Historical census-designated places,
    "EAGLE MOUNTAIN",

    "// Unincorporated communities",
    "ALLIANCE",
    "AVONDALE",
    "BOSS",
    "EAGLE ACRES",
    "LAKE CREST ESTATES",
    "LAKE FOREST",
    "LAKE SHORE ESTATES",

    // Historical communities,
    "BELT JUNCTION",
    "BISBEE",
    "BRANSFORD",
    "CENTER POINT",
    "EDERVILLE",
    "GARDEN ACRES",
    "HANDLEY",
    "JOHNSONS STATION",
    
    "JOHNSON CO"
  };
  
  private static final Properties MISSPELLED_CITIES = buildCodeTable(new String[]{
      "BURELSON",     "BURLESON"
  });
}
