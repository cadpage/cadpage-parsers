package net.anei.cadpage.parsers.KS;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSKingmanCountyParser extends DispatchA25Parser {

  public KSKingmanCountyParser() {
    super(CITY_LIST, "KINGMAN COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "alert@kingmanlec.kscoxmail.com";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CUNNINGHAM",
      "KINGMAN",
      "NASHVILLE",
      "NORWICH",
      "PENALOSA",
      "SPIVEY",
      "ZENDA",

      // Unincorporated communities
      "ADAMS",
      "BELMONT",
      "CALISTA",
      "CLEVELAND",
      "MIDWAY",
      "MOUNT VERNON",
      "MURDOCK",
      "RAGO",
      "SKELLYVILLE",
      "ST LEO",
      "VARNER",
      "WATERLOO",
      "WILLOWDALE",

      // Sedgwick County
      "CHENEY"

  };
}
