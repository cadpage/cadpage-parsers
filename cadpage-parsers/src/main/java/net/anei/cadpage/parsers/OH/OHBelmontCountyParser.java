package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHBelmontCountyParser extends DispatchEmergitechParser {

  public OHBelmontCountyParser() {
    super(CITY_LIST, "BELMONT COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "BelmontCounty911@comcast.net";
  }
  
  private static final String[] CITY_LIST = new String[] { 
    // Cities
    "MARTINS FERRY",
    "ST CLAIRSVILLE",

    // Villages
    "BARNESVILLE",
    "BELLAIRE",
    "BELMONT",
    "BETHESDA",
    "BRIDGEPORT",
    "BROOKSIDE",
    "FAIRVIEW",
    "FLUSHING",
    "HOLLOWAY",
    "MORRISTOWN",
    "POWHATAN POINT",
    "SHADYSIDE",
    "WILSON",
    "YORKVILLE",

    // Townships
    "COLERAIN TWP",
    "FLUSHING TWP",
    "GOSHEN TWP",
    "KIRKWOOD TWP",
    "MEAD TWP",
    "PEASE TWP",
    "PULTNEY TWP",
    "RICHLAND TWP",
    "SMITH TWP",
    "SOMERSET TWP",
    "UNION TWP",
    "WARREN TWP",
    "WASHINGTON TWP",
    "WAYNE TWP",
    "WHEELING TWP",
    "YORK TWP"
  };
}