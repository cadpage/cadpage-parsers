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
        "FYI? CALL ADDR UNIT? INFO! INFO/N+");
  }
  
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+|\\S+,\\S+", true);
    return super.getField(name);
  }


  public String getFilter() {
    return "CAD@missouricitytx.gov";
  }
}
