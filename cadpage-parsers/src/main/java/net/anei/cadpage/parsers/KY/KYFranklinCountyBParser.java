package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYFranklinCountyBParser extends DispatchA74Parser {

  public KYFranklinCountyBParser() {
    super(CITY_LIST, "FRANKLIN COUNTY", "KY", FLG_LEAD_PLACE);
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }

  private static final String[] CITY_LIST = new String[] {
      "BRIDGEPORT",
      "FORKS OF ELKHORN",
      "FRANKFORT",
      "JETT",
      "SWITZER",
      "PEAKS MILL",
      "BALD KNOB"
  };
}
