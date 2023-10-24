
package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class TXKaufmanCountyBParser extends DispatchA57Parser {

  public TXKaufmanCountyBParser() {
    super("KAUFMAN COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "tylernwauth@terrelltx.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
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
