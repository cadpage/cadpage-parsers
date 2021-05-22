package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;


public class MNCrowWingCountyParser extends DispatchA43Parser {

  public MNCrowWingCountyParser() {
    this("CROW WING COUNTY", "MN");
  }

  public MNCrowWingCountyParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "MNCrowWingCounty";
  }

  private static final String[] CITY_LIST = new String[]{

    // Crow Wing County
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
    "OLD CROW WING",

    // Morison County
    // Cities
    "BOWLUS",
    "BUCKMAN",
    "ELMDALE",
    "FLENSBURG",
    "GENOLA",
    "HARDING",
    "HILLMAN",
    "LASTRUP",
    "LITTLE FALLS",
    "MOTLEY",
    "PIERZ",
    "RANDALL",
    "ROYALTON",
    "SOBIESKI",
    "SWANVILLE",
    "UPSALA",

    // Unincorporated communities
    "BELLE PRAIRIE",
    "CENTER VALLEY",
    "CUSHING",
    "DARLING",
    "FREEDHEM",
    "GREGORY",
    "LINCOLN",
    "LITTLE ROCK",
    "MORRILL",
    "NORTH PRAIRIE",
    "PLATTE",
    "RAMEY",
    "SHAMINEAU PARK",
    "SULLIVAN",
    "VAWTER",

    // Townships
    "AGRAM",
    "BELLE PRAIRIE",
    "BELLEVUE",
    "BUCKMAN",
    "BUH",
    "CULDRUM",
    "CUSHING",
    "DARLING",
    "ELMDALE",
    "GRANITE",
    "GREEN PRAIRIE",
    "HILLMAN",
    "LAKIN",
    "LEIGH",
    "LITTLE FALLS",
    "MORRILL",
    "MOTLEY",
    "MOUNT MORRIS",
    "PARKER",
    "PIERZ",
    "PIKE CREEK",
    "PLATTE",
    "PULASKI",
    "RAIL PRAIRIE",
    "RICHARDSON",
    "RIPLEY",
    "ROSING",
    "SCANDIA VALLEY",
    "SWAN RIVER",
    "SWANVILLE",
    "TWO RIVERS"
  };
}
