package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

public class KYLewisCountyParser extends DispatchA65Parser {

  public KYLewisCountyParser() {
    super(CITY_LIST, "LEWIS COUNTY", "KY");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public String getFilter() {
    return "dispatch@LewisKYE911.org";
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "CONCORD",
      "VANCEBURG",

      // Census-designated place
      "GARRISON",

      // Other unincorporated places
      "ALBURN",
      "AWE",
      "BLACK OAK",
      "BEECHY CREEK",
      "BUENA VISTA",
      "BURTONVILLE",
      "CABIN CREEK",
      "CAMP DIX",
      "CARRS",
      "CHARTERS",
      "CLARKSBURG",
      "COTTAGEVILLE",
      "COVEDALE",
      "CRUM",
      "EMERSON",
      "EPWORTH",
      "ESCULAPIA SPRINGS",
      "FEARIS",
      "FIREBRICK",
      "FRUIT",
      "GLENN",
      "GLENN SPRINGS",
      "GUN POWDER GAP",
      "HARRIS",
      "HEAD OF GRASSY",
      "HESELTON",
      "IRWIN",
      "JACKTOWN",
      "KINNICONICK",
      "KIRKVILLE",
      "LAUREL",
      "LIBBIE",
      "MARTIN",
      "MCDOWELL CREEK",
      "MCKENZIE",
      "MONTGOMERY CREEK",
      "NASHTOWN",
      "NOAH",
      "OAK RIDGE",
      "PENCE",
      "PETERSVILLE",
      "POPLAR FLAT",
      "QUICKS RUN",
      "RANDVILLE",
      "RECORDS",
      "REXTON",
      "RIBOLT",
      "RUGLESS",
      "SAINT PAUL",
      "SALT LICK",
      "SAND HILL",
      "STRICKLETT",
      "SULLIVAN",
      "TANNERY",
      "TEUTONIA",
      "THOR",
      "TOLLESBORO",
      "TRINITY",
      "UPPER BRUCE",
      "VALLEY",
      "WADSWORTH"
  };
}
