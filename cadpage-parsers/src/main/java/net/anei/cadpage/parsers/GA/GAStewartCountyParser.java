package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.CodeSet;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA83Parser;

public class GAStewartCountyParser extends DispatchA83Parser {

  public GAStewartCountyParser() {
    super(CITY_LIST, PLACE_SET, "STEWART COUNTY", "GA", 1);
    setupMultiWordStreets("CHARLES OVERBY");
  }

  @Override
  public String getFilter() {
    return "earlyga@ez911mail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final CodeSet PLACE_SET = new CodeSet(
      "FOUR COUNTY HEALTH & REHAB",
      "HOUSE",
      "PROVIDENCE FAMILY MEDICAL"
  );

  private static final String[] CITY_LIST = new String[]{
      "GREEN GROVE",
      "LOUVALE",
      "LUMPKIN",
      "OMAHA",
      "RICHLAND",
      "BROOKLYN",
      "FLORENCE"
  };
}
