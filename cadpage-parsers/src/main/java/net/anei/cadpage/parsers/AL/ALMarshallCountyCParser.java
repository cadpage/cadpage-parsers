package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class ALMarshallCountyCParser extends DispatchA65Parser {
  
  public ALMarshallCountyCParser() {
    super(CITY_LIST, "MARSHALL COUNTY", "AL");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@911email.org,ispatch@911email.net,geoconex@nlamerica.com,dispatch@911comm2.info,dispatch@Marshall-AL-911.info";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitKeepLeadBreak() { return true; }
    };
  }



  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "ALBERTVILLE",
    "ARAB",
    "BOAZ",
    "GUNTERSVILLE",

    // Towns
    "DOUGLAS",
    "GRANT",
    "SARDIS CITY",
    "UNION GROVE",

    // Census-designated place
    "JOPPA",

    // Unincorporated communities
    "ASBURY",
    "BUCKSNORT",
    "CLAYSVILLE",
    "EDDY",
    "EGYPT",
    "GRASSY",
    "HOG JAW",
    "HORTON",
    "HUSTLEVILLE",
    "KENNAMER COVE",
    "LITTLE NEW YORK",
    "MORGAN CITY",
    "MOUNT HEBRON",
    "RAYBURN",
    "RED HILL",
    "RUTH",
    "SCANT CITY",
    "SWEARENGIN",
    "WARRENTON",
    
    // Dekalb County
    "CROSSVILLE",
    
    // Jackson County
    "SCOTTSBORO",
    "SWEARENGIN"
  };
}
