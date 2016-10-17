
package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class TXKaufmanCountyBParser extends DispatchSouthernParser {

  public TXKaufmanCountyBParser() {
    super(CITY_LIST, "KAUFMAN COUNTY", "TX", DSFLAG_TRAIL_PLACE);
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Reject any TXKaufmanCountyA alerts
    if (body.startsWith("kaufmancotx911:") || body.startsWith("Dispatch:")) return false;

    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{

      //Cities
      
      "COMBINE",
      "COTTONWOOD",
      "CRANDALL",
      "DALLAS",
      "FORNEY",
      "HEATH",
      "KAUFMAN",
      "MESQUITE",
      "SEAGOVILLE",
      "SEVEN POINTS",
      "TALTY",
      "TERRELL",

      //Towns

      "KEMP",
      "MABANK",
      "OAK GROVE",
      "OAK RIDGE",
      "POST OAK",
      "BEND CITY",
      "SCURRY",

      //Villages

      "GRAYS PRAIRIE",
      "ROSSER",

      //Census-designated places

      "ELMO",
      "TRAVIS RANCH",

      //Unincorporated communities

      "ABLES SPRINGS",
      "ABNER",
      "BECKER",
      "CARTWRIGHT",
      "CEDAR GROVE",
      "CEDARVALE",
      "COBB",
      "COLLEGE MOUND",
      "COLQUITT",
      "EGYPT",
      "FROG",
      "GASTONIA",
      "HEARTLAND",
      "HIRAM",
      "JIBA",
      "LAWRENCE",
      "LIVELY",
      "LONE STAR",
      "MARKOUT",
      "MCCOY",
      "OLA",
      "PEELTOWN",
      "POETRY",
      "PRAIRIEVILLE",
      "RAND",
      "STUBBS",
      "STYX",
      "TOLOSA",
      "UNION VALLEY",
      "WARSAW",
      "WILSON"


  };
}
