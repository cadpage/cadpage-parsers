package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;


public class TNCampbellCountyAParser extends DispatchA65Parser {

  public TNCampbellCountyAParser() {
    super(CITY_LIST, "CAMPBELL COUNTY", "TN");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBreakIns() { return true; }
      @Override public boolean splitKeepLeadBreak() { return true; }
    };
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info,geoconex@nlamerica.com,campbellcotn@911email.net,dispatch@campbelltne911.net,@nlamerica.com";
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "JELLICO",
      "LAFOLLETTE",
      "ROCKY TOP",

      // Towns
      "CARYVILLE",
      "JACKSBORO",

      // Census-designated place
      "FINCASTLE",

      // Unincorporated communities
      "ALDER SPRINGS",
      "ANTHRAS",
      "BLOCK",
      "CLINCHMORE",
      "COAL CREEK",
      "COOLIDGE",
      "COTULA",
      "DUFF",
      "ELK VALLEY",
      "HABERSHAM",
      "LAKE CITY",
      "MORLEY",
      "NEWCOMB",
      "PINECREST",
      "PIONEER",
      "STINKING CREEK",
      "STONY FORK",
      "VASPER",
      "WESTBOURNE",
      "WHITE OAK",
      "WOOLDRIDGE",
      "WYNN"
  };
}
