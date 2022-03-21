package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class TNRheaCountyBParser extends DispatchA65Parser {

  public TNRheaCountyBParser() {
    this("RHEA COUNTY", "TN");
  }

  public TNRheaCountyBParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState);
  }

  @Override
  public String getFilter() {
    return "rheacotn@911email.net,dispatch@911email.org";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "DAYTON",

    // Towns
    "GRAYSVILLE",
    "SPRING CITY",

    // Unincorporated communities
    "EVENSVILLE",
    "FIVE POINTS",
    "FRAZIER",
    "GRANDVIEW",
    "LIBERTY HILL",
    "OGDEN",
    "OLD WASHINGTON",

    // Former community
    "RHEA SPRINGS",

    // Bledsoe County

    // City
    "PIKEVILLE",

    // Unincorporated communities
    "COLD SPRING",
    "DILL",
    "LEES STATION",
    "LUSK",
    "MELVINE",
    "MOUNT CREST",
    "NEW HARMONY",
    "PAILO",
    "SUMMER CITY",

  };

}
