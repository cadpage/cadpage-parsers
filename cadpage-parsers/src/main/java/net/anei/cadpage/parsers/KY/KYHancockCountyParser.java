package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

/**
 * Hancock County, KY
 */
public class KYHancockCountyParser extends DispatchA65Parser {
  
  public KYHancockCountyParser() {
    super(CITY_LIST, "HANCOCK COUNTY", "KY");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@hancockky.us";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBreakIns() { return true; }
    };
  }

  private static final String[] CITY_LIST = new String[]{
      
      // Incorporated
      "HAWESVILLE",
      "LEWISPORT",

      // Unincorporated
      "ADAIR",
      "CABOT",
      "CHAMBERS",
      "DUKES",
      "EASTON",
      "FLORAL",
      "GOERING",
      "PATESVILLE",
      "PELLVILLE",
      "PETRI",
      "ROSEVILLE",
      "SANDERS",
      "SKILLMAN",
      "UTILITY",
      "WAITMAN",
      "WEBERSTOWN",
      "BOLING CHAPEL"
  };
}
