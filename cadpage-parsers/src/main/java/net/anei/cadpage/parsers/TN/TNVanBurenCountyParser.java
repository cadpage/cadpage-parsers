package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class TNVanBurenCountyParser extends DispatchA65Parser {

  public TNVanBurenCountyParser() {
    super(CITY_LIST, "VAN BUREN COUNTY", "TN");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm3.info";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final String[] CITY_LIST = new String[] {

      // Town
      "SPENCER",

      // Unincorporated communities
      "BONE CAVE",
      "CEDAR GROVE",
      "LONEWOOD",
      "MOONEYHAM",
      "NEW MARTIN",
      "PINEY",
      "WELCHLAND",

      // Bledsoe County
      "PIKEVILLE",

      // Warren County
      "ROCK ISLAND"
  };
}
