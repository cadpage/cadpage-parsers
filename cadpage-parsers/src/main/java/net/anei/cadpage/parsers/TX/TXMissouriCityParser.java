package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Missouri City, TX
 */
public class TXMissouriCityParser extends DispatchOSSIParser {
  public TXMissouriCityParser() {
    super("MISSOURI CITY", "TX",
        "FYI? CALL ADDR INFO! INFO+");
  }
  
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }


  public String getFilter() {
    return "CAD@missouricitytx.gov";
  }
}
