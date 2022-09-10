

package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class NCPolkCountyParser extends DispatchSouthernParser {

  private static final Pattern ID_TIME_PTN = Pattern.compile("\\b(\\d{10})(\\d\\d:\\d\\d:\\d\\d)\\b");
  private static final Pattern TRAIL_TIME_PTN = Pattern.compile("\\. +[A-Z][a-z]+ \\d+, \\d+:\\d+ [AP]M\\.?$");

  public NCPolkCountyParser() {
    super(CITY_LIST, "POLK COUNTY", "NC",
          DSFLG_OPT_DISP_ID|DSFLG_ADDR|DSFLG_OPT_NAME|DSFLG_OPT_PHONE|DSFLG_OPT_CODE|DSFLG_TIME);
  }

  @Override
  public String getFilter() {
    return "polkcounty911@polknc.org";
  }

  @Override
  public boolean parseMsg(String body, Data data) {

    // Strip optional prefix
    body = stripFieldStart(body, "polkcounty911:");

    // Sometimes an ID and time run together, in which case we need to split them apart
    Matcher match = ID_TIME_PTN.matcher(body);
    if (match.find()) {
      body = body.substring(0, match.end(1)) + " " + body.substring(match.start(2));
    }
    match = TRAIL_TIME_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();

    body = body.replace('@', '&');
    if (!super.parseMsg(body, data)) return false;

    // Fix some after market additions
    if (data.strName.length() == 0) {
      data.strName = data.strPlace;
      data.strPlace = "";
    }

    return true;
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("NR ")) return true;
    return super.isNotExtraApt(apt);
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = BEFORE_PTN.matcher(addr).replaceAll(" & ");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern BEFORE_PTN = Pattern.compile(" (?:JUST )?BEFORE ");

  public static final String[] CITY_LIST = new String[]{
    "COLUMBUS",
    "MILL SPRING",
    "RUTHERFORD",
    "RUTHERFORDTON",
    "SALUDA",
    "TRYON"
  };
}
