package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class KYMuhlenbergCountyBParser extends DispatchA65Parser {

  public KYMuhlenbergCountyBParser() {
    super(KYMuhlenbergCountyParser.CITY_LIST, "MUHLENBERG COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean splitBreakIns() { return true; }
    };
  }

}
