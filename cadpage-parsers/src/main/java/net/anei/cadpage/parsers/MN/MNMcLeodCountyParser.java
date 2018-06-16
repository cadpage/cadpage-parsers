package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;

public class MNMcLeodCountyParser extends DispatchA43Parser {
  
  public MNMcLeodCountyParser() {
    super(CITY_LIST, "MCLEOD COUNTY", "MN");
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities  
    "BISCAY",
    "BROWNTON",
    "GLENCOE",
    "HUTCHINSON",
    "LESTER PRAIRIE",
    "PLATO",
    "SILVER LAKE",
    "STEWART",
    "WINSTED",

    //Townships
    "ACOMA",
    "BERGEN",
    "COLLINS",
    "GLENCOE",
    "HALE",
    "HASSAN VALLEY",
    "HELEN",
    "HUTCHINSON",
    "LYNN",
    "PENN",
    "RICH VALLEY",
    "ROUND GROVE",
    "SUMTER",
    "WINSTED",

    // Unincorporated communities
    "FERNANDO",
    "HEATWOLE",
    "KOMENSKY",
    "SHERMAN",
    "SOUTH SILVER LAKE",
    "SUMTER",
    
    // Carver County
    "HOLLYWOOD",
    
    // Wright County
    "VICTOR"
  };
}
