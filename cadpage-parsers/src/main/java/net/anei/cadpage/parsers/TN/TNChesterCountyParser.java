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
      @Override public boolean splitBreakIns() { return true; }
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
      "WOODVILLE",

      // Hardeman County
      "BOLIVAR",
      "FORTY FIVE",
      "GRAND JUNCTION",
      "HICKORY VALLEY",
      "HORNSBY",
      "MIDDLETON",
      "POCAHONTAS",
      "SAULSBURY",
      "SILERTON",
      "TOONE",
      "VAN BUREN",
      "WHITEVILLE",

      // Hardin County
      "ADAMSVILLE",
      "CRUMP",
      "MILLEDGEVILLE",
      "OLIVET",
      "SALTILLO",
      "SAVANNAH",
      "SHILOH",
      "WALNUT GROVE",

      // Henderson County
      "CEDAR GROVE",
      "CHESTERFIELD",
      "CRUCIFER",
      "DARDEN",
      "HURON",
      "LEXINGTON",
      "LURAY",
      "MIDDLE FORK",
      "MIDDLEBURG",
      "PARKERS CROSSROADS",
      "REAGAN",
      "SARDIS",
      "SCOTTS HILL",
      "WILDERSVILLE",

      // Madison County
      "ADAIR",
      "BEECH BLUFF",
      "DENMARK",
      "FIVE POINTS",
      "HUMBOLDT",
      "JACKSON",
      "MEDON",
      "MERCER",
      "OAKFIELD",
      "PINSON",
      "SPRING CREEK",
      "THREE WAY",

      // Mcnary County
      "ACTON",
      "ADAMSVILLE",
      "AMISHVILLE",
      "BETHEL SPRINGS",
      "CHEWALLA",
      "EASTVIEW",
      "ENVILLE",
      "FALCON",
      "FINGER",
      "GILCHRIST",
      "GOOD HOPE",
      "GUYS",
      "LAWTON",
      "LEAPWOOD",
      "MCNAIRY",
      "MICHIE",
      "MILLEDGEVILLE",
      "PURDY",
      "RAMER",
      "ROSE CREEK",
      "SELMER",
      "STANTONVILLE",
      "WEST SHILOH"
  };

}
