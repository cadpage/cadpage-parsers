package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA43Parser;


public class MNCrowWingCountyAParser extends DispatchA43Parser {

  public MNCrowWingCountyAParser() {
    this("CROW WING COUNTY", "MN");
  }

  public MNCrowWingCountyAParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "MNCrowWingCountyA";
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

    // Cass County
    // Cities
    "BACKUS",
    "BENA",
    "BOY RIVER",
    "CASS LAKE",
    "CHICKAMAW BEACH",
    "EAST GULL LAKE",
    "FEDERAL DAM",
    "HACKENSACK",
    "LAKE SHORE",
    "LAKESHORE",
    "LONGVILLE",
    "MOTLEY",
    "PILLAGER",
    "PINE RIVER",
    "REMER",
    "WALKER",

    // Census-designated place
    "WHIPHOLT",

    // Unincorporated communities
    "AH-GWAH-CHING",
    "BREVIK",
    "BRIDGEMAN",
    "CASINO",
    "ELLIS",
    "ESTERDY",
    "GRAFF",
    "INGUADONA",
    "LEADER",
    "LEECH LAKE",
    "MAE",
    "MILDRED",
    "ONIGUM",
    "OSHAWA",
    "OUTING",
    "PONTORIA",
    "RABOIN",
    "RYAN VILLAGE",
    "SCHLEY",
    "SYLVAN",
    "TOBIQUE",
    "WABEDO",
    "WILKINSON",

    // Townships
    "ANSEL",
    "BARCLAY",
    "BECKER",
    "BEULAH",
    "BIRCH LAKE",
    "BLIND LAKE",
    "BOY LAKE",
    "BOY RIVER",
    "BULL MOOSE",
    "BUNGO",
    "BYRON",
    "CROOKED LAKE",
    "DEERFIELD",
    "FAIRVIEW",
    "GOULD",
    "HIRAM",
    "HOME BROOK",
    "INGUADONA",
    "KEGO",
    "LEECH LAKE",
    "LIMA",
    "LOON LAKE",
    "MAPLE",
    "MAY",
    "MCKINLEY",
    "MEADOW BROOK",
    "MOOSE LAKE",
    "OTTER TAIL PENINSULA",
    "PIKE BAY",
    "PINE LAKE",
    "PINE RIVER",
    "PONTO LAKE",
    "POPLAR",
    "POWERS",
    "REMER",
    "ROGERS",
    "SALEM",
    "SHINGOBEE",
    "SLATER",
    "SMOKY HOLLOW",
    "SYLVAN",
    "THUNDER LAKE",
    "TORREY",
    "TRELIPE",
    "TURTLE LAKE",
    "WABEDO",
    "WALDEN",
    "WILKINSON",
    "WILSON",
    "WOODROW",

    // Unorganized territories
    "WAHNENA",
    "NORTH CASS",
    "NORTH CENTRAL CASS",
    "EAST CASS",

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
    "TWO RIVERS",

    // Aitkin County
    "AITKIN",

    // Mile Lacs County
    "ONAMIA",

    // Wadena County
    "STAPLES",
    "VERNDALE"
  };
}
