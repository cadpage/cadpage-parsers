package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchA88Parser;

public class SCDillonCountyParser extends DispatchA88Parser {

  public SCDillonCountyParser() {
    super(CITY_LIST, "DILLON COUNTY", "SC");
  }

  @Override
  public String getFilter() {
    return "dilloncounty911@gmail.com";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "DILLON",

      // Towns
      "LAKE VIEW",
      "LATTA",
      "CENSUS-DESIGNATED PLACES",
      "FLOYDALE",
      "HAMER",
      "LITTLE ROCK",
      "NEWTOWN",

      // Other unincorporated communities
      "BASS CROSSROADS",
      "BERRYS CROSSROADS",
      "BINGHAM",
      "BRONSON CROSSROADS",
      "BUNKER HILL",
      "CARMICHAEL CROSSROADS",
      "CAROLINA",
      "CARTER LANDING",
      "CENTERVILLE",
      "COTTON VALLEY",
      "DALCHO",
      "DOTHAN",
      "DUNBARTON",
      "FIVE FORKS",
      "FORK",
      "FORREST HILLS",
      "GADDYS CROSSROADS",
      "GADDYS MILL",
      "GALAVON",
      "HAYESTOWN",
      "HIGH HILL CROSSROADS",
      "JACKSONVILLE",
      "JUDSON",
      "KEMPER",
      "KENTYRE",
      "LINKSIDE",
      "MALLORY",
      "MALLORY BEACH",
      "MANNING CROSSROADS",
      "MAY HILLTOP",
      "MCCORMICK CROSSROADS",
      "MINTURN",
      "MOUNT CALVARY",
      "NEWTOWN",
      "OAK GROVE",
      "OAKLAND CROSSROADS",
      "OLIVER CROSSROADS",
      "PITTMAN CORNER",
      "RIVERDALE",
      "SELMA",
      "SINCLAIR CROSSROADS",
      "SOUTH OF THE BORDER",
      "SQUIRES",
      "SQUIRES CURVE",
      "TEMPERANCE HILL",

      // Marion County
      "SELLERS"
  };
}
