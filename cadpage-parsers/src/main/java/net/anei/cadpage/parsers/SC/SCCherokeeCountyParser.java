package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;


public class SCCherokeeCountyParser extends DispatchA13Parser {
  
  public SCCherokeeCountyParser() {
    super(CITY_LIST, "CHEROKEE COUNTY", "SC", A13_FLG_LEAD_PLACE | A13_FLG_TRAIL_PLACE);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    "BEAR DEN",
    "BOILING SPRINGS",
    "BONER LAKE",
    "BRICK HOUSE",
    "CHEROKEE CREEK",
    "CHEROKEE FALLS",
    "CHEROKEE FORD",
    "CHEROKEE NATIONAL",
    "COWPENS PACOLET",
    "DRY FORK",
    "ELLIS FERRY",
    "GOLD MINE",
    "GOUCHER SCHOOL",
    "GREEN RIVER",
    "LIMESTONE SPRINGS",
    "LITTLE EGYPT",
    "LOOKOUT TOWER",
    "MCKOWN'S MOUNTAIN",
    "OAK DALE",
    "QUARTER ROUND",
    "RACE TRACK",
    "ROCK CUT",
    "SARRATT SCHOOL",
    "SUNNY SLOPE",
    "WHITE PLAINS",
  };
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "CHESNEE",
    "GAFFNEY",

    //Towns
    "BLACKSBURG",
    "SMYRNA",

    // Unincorporated communities
    "CORINTH",
    "DRAYTONVILLE",
    "EAST GAFFNEY",
    "GOUCHER",
    "GRASSY POND",
    "THICKETTY",
    
    // Spartanburg County
    "COWPENS",
    "PACOLET",
    "SPARTANBURG",
    
    // York County
    "YORK"
  };
}
