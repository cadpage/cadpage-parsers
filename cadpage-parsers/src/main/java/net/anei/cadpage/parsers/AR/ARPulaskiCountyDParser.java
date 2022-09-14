package net.anei.cadpage.parsers.AR;

import net.anei.cadpage.parsers.dispatch.DispatchA32Parser;

public class ARPulaskiCountyDParser extends DispatchA32Parser {

  public ARPulaskiCountyDParser() {
    super(CITY_LIST, "PULASKI COUNTY", "AR");
  }

  @Override
  public String getFilter() {
    return "IMCPaging@maumelle.org";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CAMMACK VILLAGE",
      "JACKSONVILLE",
      "LITTLE ROCK",
      "MAUMELLE",
      "NORTH LITTLE ROCK",
      "SHERWOOD",
      "WRIGHTSVILLE",
      "TOWN",
      "ALEXANDER",

      // Census-designated places
      "COLLEGE STATION",
      "GIBSON",
      "HENSLEY",
      "LANDMARK",
      "MCALMONT",
      "NATURAL STEPS",
      "ROLAND",
      "SCOTT",
      "SWEET HOME",
      "WOODSON",

      // Other communities
      "CRYSTAL HILL",
      "GRAVEL RIDGE",
      "IRONTON",
      "LITTLE ITALY",
      "MABELVALE",
      "MARCHE",
      "PANKEY",
      "WOODYARDVILLE"
  };
}
