package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.dispatch.DispatchA63Parser;

public class MNDodgeCountyBParser extends DispatchA63Parser {

  public MNDodgeCountyBParser() {
    super(CITY_LIST, "DODGE COUNTY", "MN");
    setupGpsLookupTable(MNDodgeCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "phoenix.notifications@dodgecountymn.gov";
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BLOOMING PRAIRIE",
      "CLAREMONT",
      "DODGE CENTER",
      "HAYFIELD",
      "KASSON",
      "MANTORVILLE",
      "WEST CONCORD",

      // Unincorporated communities
      "BERNE",
      "CONCORD",
      "DANESVILLE",
      "EDEN",
      "OSLO",
      "RICE LAKE",
      "WASIOJA",

      // Townships
      "ASHLAND TWP",
      "CANISTEO TWP",
      "CLAREMONT TWP",
      "CONCORD TWP",
      "ELLINGTON TWP",
      "HAYFIELD TWP",
      "MANTORVILLE TWP",
      "MILTON TWP",
      "RIPLEY TWP",
      "VERNON TWP",
      "WASIOJA TWP",
      "WESTFIELD TWP",
  };
}
