package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class GAChattoogaCountyParser extends DispatchA65Parser {
  
  public GAChattoogaCountyParser() {
    super(CITY_LIST, "CHATTOOGA COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@ChattoogaCo-GA-911.info";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBreakIns() { return true; }
      @Override public boolean splitKeepLeadBreak() { return true; }
    };
  }



  private static final String[] CITY_LIST = new String[] {
      
      // Fulton County
      // Cities
      "ALPHARETTA",
      "ATLANTA",
      "CHATTAHOOCHEE HILLS",
      "COLLEGE PARK",
      "EAST POINT",
      "FAIRBURN",
      "HAPEVILLE",
      "JOHNS CREEK",
      "MILTON",
      "MOUNTAIN PARK",
      "PALMETTO",
      "ROSWELL",
      "SANDY SPRINGS",
      "SOUTH FULTON",
      "UNION CITY",
      
      // Chatooga County
      // Incorporated cities
      "SUMMERVILLE",
      "TRION",
      "LYERLY",
      "MENLO",
      
      // Unincorporated communities
      "ALPINE",
      "ARMUCHEE",
      "BERRYTON",
      "CHATTOOGAVILLE",
      "CLOUDLAND",
      "FARMERSVILLE",
      "SUBLIGNA",
      "GORE",
      "HOLLAND",
      "PENNVILLE",
      "SILVER HILL",
      "TELOGA",
      
      // Floyd County
      "ROME"
  };
}
