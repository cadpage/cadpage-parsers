package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA11Parser;

/**
 * Whitman County, WA
 */
public class WAWhitmanCountyAParser extends DispatchA11Parser {

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("([A-Z]+)\n([A-Z0-9]+)\n(\\d{2}-\\d{6})\n(.*\nCMPLT- \\d\\d\\.\\d\\d\\.\\d\\d)",Pattern.DOTALL);

  public WAWhitmanCountyAParser() {
    this("WHITMAN COUNTY", "WA");
  }

  public WAWhitmanCountyAParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState);
  }

  @Override
  public String getFilter() {
    return "hiplink@whitcom.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    int pt = body.indexOf("\nSent by Whitcom");
    if (pt >= 0) body = body.substring(0,pt).trim();

    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = "RUN REPORT";
      data.strSource = match.group(1);
      data.strUnit = match.group(2);
      data.strCallId = match.group(3);
      data.strPlace = match.group(4).trim();
      return true;
    }
    if (!super.parseMsg(body, data)) return false;
    String state = CITY_ST_TABLE.getProperty(data.strCity.toUpperCase());
    if (state != null) data.strState = state;
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
     "ANA", "ANATONE",
     "ASO", "ASOTIN",
     "CLA", "CLARKSTON",
     "CLT", "COLTON",
     "GEN", "GENESSEE",
     "LEW", "LEWISTON",
     "PUL", "PULMAN",
     "UNI", "UNIONTOWN"
  });

  private static final Properties CITY_ST_TABLE = buildCodeTable(new String[]{
    "GENESEE",    "ID",
    "LEWISTON",   "ID",
    "MOSCOW",     "ID"
  });
}
