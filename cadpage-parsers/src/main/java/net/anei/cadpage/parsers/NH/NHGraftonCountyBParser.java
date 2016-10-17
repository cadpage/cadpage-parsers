package net.anei.cadpage.parsers.NH;

import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class NHGraftonCountyBParser extends DispatchA32Parser {
  
  public NHGraftonCountyBParser() {
    super(CITY_LIST, "GRAFTON COUNTY", "NH");
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "LEBANON",

    // Towns
    "ALEXANDRIA",
    "ASHLAND",
    "BATH",
    "BENTON",
    "BETHLEHEM",
    "BRIDGEWATER",
    "BRISTOL",
    "CAMPTON",
    "CANAAN",
    "DORCHESTER",
    "EASTON",
    "ELLSWORTH",
    "ENFIELD",
    "FRANCONIA",
    "GRAFTON",
    "GROTON",
    "HANOVER",
    "HAVERHILL",
    "HEBRON",
    "HOLDERNESS",
    "LANDAFF",
    "LINCOLN",
    "LISBON",
    "LITTLETON",
    "LYMAN",
    "LYME",
    "MONROE",
    "ORANGE",
    "ORFORD",
    "PIERMONT",
    "PLYMOUTH",
    "RUMNEY",
    "SUGAR HILL",
    "THORNTON",
    "WARREN",
    "WATERVILLE VALLEY",
    "WENTWORTH",
    "WOODSTOCK",

    // Township
    "LIVERMORE TWP",

    // Census-designated places
    "ASHLAND",
    "BETHLEHEM",
    "BRISTOL",
    "CANAAN",
    "ENFIELD",
    "HANOVER",
    "LINCOLN",
    "LISBON",
    "LITTLETON",
    "MOUNTAIN LAKES",
    "NORTH HAVERHILL",
    "NORTH WOODSTOCK",
    "PLYMOUTH",
    "WOODSVILLE",

    // Villages
    "EAST HEBRON",
    "ENFIELD CENTER",
    "ETNA",
    "GLENCLIFF",
    "LYME CENTER",
    "MONTCALM",
    "PIKE",
    "STINSON LAKE",
    "WEST LEBANON"
  };

}
