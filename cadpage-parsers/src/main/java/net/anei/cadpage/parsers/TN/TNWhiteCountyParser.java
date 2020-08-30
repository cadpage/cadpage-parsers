package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;


public class TNWhiteCountyParser extends DispatchA65Parser {
  
  public TNWhiteCountyParser() {
    super(CITY_LIST, "WHITE COUNTY", "TN");
  }
  
  @Override
  public String getFilter() {
    return "whitecotn@911email.net,dispatch@911comm2.info,dispatch@whitecounty-tn-911.info,@whitecoe911.info";
  } 
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBreakIns() { return true; }
      @Override public boolean splitKeepLeadBreak() { return true; }
    };
  }

  private static final String[] CITY_LIST = new String[]{
      
      "BON AIR",
      "DOYLE",
      "CASSVILLE",
      "COOKEVILLE",
      "DEROSSETT",
      "QUEBECK",
      "RAVENSCROFT",
      "SPARTA",
      "SPENCER",
      "WALLING",
      "YANKEETOWN"

  };
}
