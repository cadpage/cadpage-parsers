package net.anei.cadpage.parsers.OK;

import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;


public class OKMayesCountyParser extends DispatchEmergitechParser {
  
  public OKMayesCountyParser() {
    super("MayesCounty911:", CITY_LIST, "MAYES COUNTY", "OK", TrailAddrType.INFO);
  }

  @Override
  public String getFilter() {
    return "MayesCounty911@active911.com";
  }
  
  private static final String[] CITY_LIST = new String[]{

    "ADAIR",
    "CHOUTEAU",
    "DISNEY",
    "GRAND LAKE TOWNE",
    "HOOT OWL",
    "LANGLEY",
    "LOCUST GROVE",
    "PENSACOLA",
    "PRYOR",
    "SALINA",
    "SPAVINAW",
    "SPORTSMEN ACRES",
    "STRANG",

    // Unincorporated community
    "ROSE",

    // Census Designated Places (CDPs)
    "BALLOU",
    "CEDAR CREST",
    "IRON POST",
    
    // Counties
    "MAYES COUNTY"
  };
}
