package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GATiftCountyParser extends DispatchB2Parser {

  public GATiftCountyParser() {
    super("911-CENTER:", CITY_LIST, "TIFT COUNTY", "GA");
    setupCallList(CALL_LIST);
    removeWords("COURT");
    setupMultiWordStreets(
        "BILL BOWEN",
        "CHULA BROOKFIELD",
        "COLQUITT COUNTY LINE",
        "FERRY LAKE",
        "FOREST LAKE",
        "JACOB HALL",
        "JOHN ORR",
        "LONG PINE",
        "MAGNOLIA INDUSTRIAL",
        "MARTIN L KING JR",
        "MITCHELL STORE",
        "MT OLIVE CHURCH",
        "OMEGA ELDORADO",
        "PINE LAKE",
        "POST OAK",
        "RIVER CHURCH",
        "TC GORDON",
        "TIFTON ELDORADO",
        "TY TY OMEGA",
        "TY TY SPARKS",
        "TY TY WHIDDON MILL",
        "VANCEVILLE COUNTY LINE",
        "WESLEY CHAPEL",
        "WESLEY RIGDON",
        "WHIDDON MILL",
        "ZION HOPE"
    );
  }
  
  @Override
  public String getFilter() {
    return "911-CENTER@otelco.net,test@otelco.net";
  }  
  
  private static final String[] CITY_LIST = new String[]{

    "BROOKFIELD",
    "CHULA",
    "OMEGA",
    "PHILLIPSBURG",
    "TIFTON",
    "TY TY",
    "UNIONVILLE"

  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT",
      "ACCIDENT WITH INJURIES",
      "BOMB THREAT",
      "ELECTRICAL HAZARD",
      "ELECTRICAL FIRE",
      "FIRE",
      "FIRE ALARM",
      "FIRE - GRASS",
      "FIRE - STRUCTURE",
      "FIRE -TRASH",
      "FIRE - VEHICLE",
      "FIRE - WILDLAND",
      "HAZARDOUS SPILL",
      "LIFE FLIGHT",
      "MEDICAL PROBLEM"
);
}
