package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class SCFlorenceCountyParser extends DispatchSouthernParser {

  public SCFlorenceCountyParser() {
    super(CITY_LIST, "FLORENCE COUNTY", "SC", DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME);
    setupMultiWordStreets("I M GRAHAM");
    removeWords("COURT", "HEIGHTS", "PLACE", "STREET");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern GEN_ALERT_PTN = Pattern.compile("(?:32;)?500 +(.*)");
  private static final Pattern MARKER = Pattern.compile("(2|65536); *");
  private static final Pattern ID_PTN = Pattern.compile(";\\d{4}-\\d+;");

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = GEN_ALERT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = match.group(1);
      return true;
    }

    match = MARKER.matcher(body);
    if (match.lookingAt()) {
      body = body.substring(match.end());
      if (!match.group(1).equals("2")) {
        match = ID_PTN.matcher(body);
        if (!match.find()) return false;
        int pt = match.start();
        body = body.substring(0, pt) + ";;;;" + body.substring(pt);
      }
    }
    return super.parseMsg(body, data);
  }

  private static final String[] CITY_LIST = new String[]{
    "COWARD",
    "EFFINGHAM",
    "FLORENCE",
    "JOHNSONVILLE",
    "LAKE CITY",
    "MARS BLUFF",
    "OLANTA",
    "PAMPLICO",
    "QUINBY",
    "SCRANTON",
    "TIMMONSVILLE",

    // Charleston County
    "CHARLESTON",

    // Darlingon County
    "LAMAR",

    // Sumter County
    "SUMTER",

    // Williamsburg County
    "WILLIAMSBURG",
    "HEMINGWAY"
  };
}
