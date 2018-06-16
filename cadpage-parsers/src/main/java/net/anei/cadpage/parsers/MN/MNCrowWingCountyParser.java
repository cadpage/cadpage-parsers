package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;


public class MNCrowWingCountyParser extends DispatchA43Parser {
  
  public MNCrowWingCountyParser() {
    super(CITY_LIST, "CROW WING COUNTY", "MN");
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "BAXTER",
    "BRAINERD",
    "BREEZY POINT",
    "CROSBY",
    "CROSSLAKE",
    "CUYUNA",
    "DEERWOOD",
    "EMILY",
    "FIFTY LAKES",
    "FORT RIPLEY",
    "GARRISON",
    "IRONTON",
    "JENKINS",
    "MANHATTAN BEACH",
    "NISSWA",
    "PEQUOT LAKES",
    "RIVERTON",
    "TROMMALD",
 
    // Townships
    "BAY LAKE",
    "CENTER",
    "CROW WING",
    "DAGGETT BROOK",
    "DEERWOOD",
    "FAIRFIELD",
    "FORT RIPLEY",
    "GAIL LAKE",
    "GARRISON",
    "IDEAL",
    "IRONDALE",
    "JENKINS",
    "LAKE EDWARD",
    "LITTLE PINE",
    "LONG LAKE",
    "MAPLE GROVE",
    "MISSION",
    "NOKAY LAKE",
    "OAK LAWN",
    "PELICAN",
    "PERRY LAKE",
    "PLATTE LAKE",
    "RABBIT LAKE",
    "ROOSEVELT",
    "ROSS LAKE",
    "SIBLEY",
    "ST MATHIAS",
    "TIMOTHY",
    "WOLFORD",

    // Other
    "LAKE HUBERT",
    "MERRIFIELD",
    "MISSON",
    "DEAN LAKE",
    "WEST CROW WING",
    "OLD CROW WING"
    
  };
}
