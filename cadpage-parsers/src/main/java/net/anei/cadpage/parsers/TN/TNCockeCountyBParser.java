package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class TNCockeCountyBParser extends DispatchA65Parser {

  public TNCockeCountyBParser() {
    super(CITY_LIST, "COCKE COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final String[] CITY_LIST = new String[] {

      // City
      "NEWPORT",

      // Towns
      "PARROTTSVILLE",

      // Unincorporated communities
      "ALLEN GROVE",
      "BALTIMORE",
      "BOOMER",
      "BRIAR THICKET",
      "BRIDGEPORT",
      "BYBEE",
      "COSBY",
      "DEL RIO",
      "HARTFORD",
      "MIDWAY",
      "REIDTOWN",
      "TOM TOWN",
      "WASP"
  };
}
