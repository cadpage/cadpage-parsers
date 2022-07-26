package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;


public class TNCumberlandCountyParser extends DispatchA65Parser {

  public TNCumberlandCountyParser() {
    super(CITY_LIST, "CUMBERLAND COUNTY", "TN");
    setupMultiWordStreets("MILLSTONE MNTN");
  }

  @Override
  public String getFilter() {
    return "@911email.net,@911email.org,e911@cumberlandtn911.org,dispatch@cumberlandtn911.info,Dispatch@Etowah911.info";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBreakIns() { return true; }
    };
  }


  private static final String[] CITY_LIST = new String[]{

      // Cities
      "CRAB ORCHARD",
      "CROSSVILLE",

      // Town
      "PLEASANT HILL",

      // Census-designated places
      "BOWMAN",
      "FAIRFIELD GLADE",
      "LAKE TANSI",

      // Unincorporated communities
      "ALLOWAY",
      "BIG LICK",
      "BOWLING",
      "DAYSVILLE",
      "GRASSY COVE",
      "MIDWAY",
      "OZONE",
      "RENEGADE MOUNTAIN",
      "WESTEL",

      // Putnam County
      "MONTEREY",

      // Roane County
      "ROCKWOOD",

      // White County
      "SPARTA"
  };
}
