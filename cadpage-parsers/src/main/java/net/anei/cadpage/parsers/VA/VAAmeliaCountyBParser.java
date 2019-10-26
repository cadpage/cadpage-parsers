package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA76Parser;

public class VAAmeliaCountyBParser extends DispatchA76Parser {

  public VAAmeliaCountyBParser() {
    super("PATRICK COUNTY", "VA");
  }
  
  @Override
  public String getFilter() {
    return "cad@sheriff.co.patrick.va.us";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitBreakLength() { return 130; }
      @Override public int splitBreakPad() { return 1; }
    };
  }
}
