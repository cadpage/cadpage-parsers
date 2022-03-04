package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class KYMonroeCountyParser extends DispatchA65Parser {

  public KYMonroeCountyParser() {
    super(CITY_LIST, "MONROE COUNTY", "KY");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitKeepLeadBreak() { return true; }
      @Override public boolean splitBreakIns() { return true; }
    };
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm1.info,geoconex@nlamerica.com,dispatch@MonroeCoE911.com";
  }

  private static final String[] CITY_LIST = new String[]{

      "FOUNTAIN RUN",
      "GAMALIEL",
      "TOMPKINSVILLE",

      "BUGTUSSLE",
      "MOUNT HERMON",

      "HESTAND"

  };
}
