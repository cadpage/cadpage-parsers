package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA83Parser;

public class GABrooksCountyParser extends DispatchA83Parser {

  public GABrooksCountyParser() {
    super(CITY_LIST, "BROOKS COUNTY", "GA", A83_TRAIL_CALL | A83_REQ_SENDER);
  }

  @Override
  public String getFilter() {
    return "brooks.ga@ez911map.net";
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BARWICK",
      "MORVEN",
      "PAVO",
      "QUITMAN",

      // Unincorporated communities
      "BARNEY",
      "DIXIE",
      "GROOVERVILLE",
      "PIDCOCK",

      // Lowdnes County
      "VALDOSTA"
  };

}
