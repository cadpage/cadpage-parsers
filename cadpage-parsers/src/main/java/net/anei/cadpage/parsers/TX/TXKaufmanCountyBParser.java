
package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;


public class TXKaufmanCountyBParser extends DispatchSouthernParser {

  public TXKaufmanCountyBParser() {
    super(CITY_LIST, "KAUFMAN COUNTY", "TX",
          DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_NAME | DSFLG_PHONE | DSFLG_CODE | DSFLG_ID | DSFLG_TIME | DSFLG_PROC_EMPTY_FLDS);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern TIME_MARKER_PTN = Pattern.compile("; *\\d\\d:\\d\\d:\\d\\d;");

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Reject any TXKaufmanCountyA alerts

    // They recently dropped the prefix, which makes things a bit more difficult.  They are both
    // based on Southern dispatch format, but TXKaufmanCountyA uses comma delimiters and
    // TXKaufmanCountyB used semicolon delimiters, so we will check for a properly delimited time field

    if (!TIME_MARKER_PTN.matcher(body).find()) return false;

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
