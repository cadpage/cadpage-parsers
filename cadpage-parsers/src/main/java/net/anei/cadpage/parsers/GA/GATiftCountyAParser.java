package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class GATiftCountyAParser extends DispatchB2Parser {

  public GATiftCountyAParser() {
    super("911-CENTER:", CITY_LIST, "TIFT COUNTY", "GA");
    setupCallList(CALL_LIST);
    removeWords("COURT");
    setupMultiWordStreets(
        "BILL BOWEN",
        "CHULA BROOKFIELD",
        "COLQUITT COUNTY LINE",
        "CYPRESS RIDGE",
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
      "ALARM",
      "BOMB THREAT",
      "ELECTRICAL HAZARD",
      "ELECTRICAL FIRE",
      "ESCORT",
      "FAMILY/DOMESTIC FIGHT",
      "FIGHT",
      "FIRE",
      "FIRE ALARM",
      "FIRE - GRASS",
      "FIRE - STRUCTURE",
      "FIRE -TRASH",
      "FIRE - VEHICLE",
      "FIRE - WILDLAND",
      "FOLLOW UP",
      "HANG UP CALL",
      "HAZARDOUS SPILL",
      "INTOXICATED DRIVER",
      "JUVENILE PROBLEM",
      "LAW",
      "LIFE FLIGHT",
      "LOUD NOISE/DISTRUBANCE",
      "MECHANICAL BREAKDOWN",
      "MEDICAL PROBLEM",
      "MISSING PERSON",
      "MOTOR VEHICLE ACCIDENT",
      "OPEN 911 LINE",
      "PHONE CALL",
      "PROPERTY DAMAGE",
      "RECKLESS DRIVER",
      "RECOVERED PROPERTY/VEHICLE",
      "SEX OFFENSE",
      "SHOPLIFTER",
      "SHOTS FIRED IN AREA",
      "STRUCTURE FIRE",
      "SUSPICIOUS PERSON / VEHICLE",
      "THEFT",
      "TRAFFIC HAZARD",
      "UNRULY PERSON",
      "UNSECURE PREMISE",
      "UNWANTED PERSON",
      "WELFARE CHECK"
);
}
