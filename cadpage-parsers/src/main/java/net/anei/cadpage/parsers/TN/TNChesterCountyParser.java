package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class TNChesterCountyParser extends DispatchA65Parser {

  public TNChesterCountyParser() {
    super(CITY_LIST, "CHESTER COUNTY", "TN");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
    };
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "HENDERSON",

      // Town
      "ENVILLE",
      "MILLEDGEVILLE",
      "SILERTON",

      // Unincorporated communities
      "DEANBURG",
      "HICKORY CORNERS",
      "JACKS CREEK",
      "MASSEYVILLE",
      "MIFFLIN",
      "MONTEZUMA",
      "SWEET LIPS",
      "WOODVILLE"
  };

}
