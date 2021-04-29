package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

/**
 * Cherokee County, AL
 */
public class ALCherokeeCountyParser extends DispatchA65Parser {

  public ALCherokeeCountyParser() {
    super(CITY_LIST, "CHEROKEE COUNTY", "AL");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      public boolean splitBreakIns() { return true; }
      public boolean splitKeepLeadBreak() { return true; }
    };
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info,geoconex@nlamerica.com,dispatch@911comm1.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_SR;
  }

  private static final String[] CITY_LIST = new String[]{

      //INCORPORATED
      "CEDAR BLUFF",
      "CENTRE",
      "COLLINSVILLE",
      "GAYLESVILLE",
      "LEESBURG",
      "PIEDMONT",
      "SAND ROCK",

      //UNINCORPORATED
      "FORNEY",
      "LITTLE RIVER",
      "ROCK RUN",

      //CDPS
      "BROOMTOWN",
      "SPRING GARDEN"

  };
}
