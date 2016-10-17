
package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCCherokeeCountyParser extends DispatchSouthernParser {

  public NCCherokeeCountyParser() {
    super(CITY_LIST, "CHEROKEE COUNTY", "NC", DSFLAG_TRAIL_PLACE);
  }
  
  @Override
  public String getFilter() {
    return "dispatch@cherokeecounty-nc.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final String[] CITY_LIST = new String[]{

  //CITIES
      "ANDREWS",
      "MURPHY",

  //CENSUS-DESIGNATED PLACE

      "MARBLE",

  //UNINCORPORATED COMMUNITIES

      "CULBERSON",
      "OWL CREEK",
      "TOPTON",
      "UNAKA",
      "WOODVILLE",
      "RANGER",
      "HANGING DOG",

  //TOWNSHIPS

      "BEAVERDAM",
      "BRASSTOWN",
      "HOTHOUSE",
      "MURPHY",
      "NOTLA",
      "SHOAL CREEK",
      "VALLEYTOWN"

  };
}
