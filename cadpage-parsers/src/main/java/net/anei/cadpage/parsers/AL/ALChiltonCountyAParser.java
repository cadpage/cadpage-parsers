
package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernPlusParser;

/**
 * Chilton County, AL
 */

public class ALChiltonCountyAParser extends DispatchSouthernPlusParser {

  private static final Pattern GENERAL_ALERT_PTN = Pattern.compile("CFS: *(\\d+);Unit: *(.*?);Status: *(.*?);Note: *(.*)");
  private static final Pattern ADDR_EXIT_PTN = Pattern.compile("(\\d+ +EXIT) +(.*)");

  public ALChiltonCountyAParser() {
    super(CITY_LIST, "CHILTON COUNTY", "AL",
        DSFLG_ADDR_LEAD_PLACE | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_BAD_PLACE | DSFLG_OPT_X | DSFLG_OPT_UNIT1 | DSFLG_OPT_ID | DSFLG_TIME,
        ".*Fire|[A-Z ]+ FIRE|[a-z][a-z0-9_]+|.*\\b(?:BATT|BRUSH TK|CAR|CORONER|EMA|ENG|GRASS|PCO|PT|RESCUE|SERVICE|TANKER) [-0-9]+[A-Z]?|CARE|EMA|RPS");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "dispatch@chiltoncounty.org,dispatch@dispatch.ccso911.net,4702193605";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    body = stripFieldStart(body, "dispatch:");

    Matcher match = GENERAL_ALERT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      data.strSupp = append(match.group(3), " - ", match.group(4));
      return true;
    }

    body = body.replace('\\', '/');
    body = body.replaceAll("\\bCOUNTY RD\\b", "CO");
    if (body.startsWith("/")) body = body.substring(1).trim();
    if (! super.parseMsg(subject, body, data)) return false;

    data.strAddress = data.strAddress.replaceAll("\\bCO\\b", "COUNTY RD");

    match = ADDR_EXIT_PTN.matcher(data.strAddress);
    if (match.matches()) {
      data.strPlace = append(data.strPlace, " ", match.group(1));
      data.strAddress = match.group(2).trim();
    }
    return true;
  }

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_IGNORE_AT | FLAG_CROSS_FOLLOWS;
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    Matcher match = I_65_GPS_PTN.matcher(address);
    if (match.matches()) return match.group(1);
    return null;
  }
  private static final Pattern I_65_GPS_PTN = Pattern.compile("(\\d+ I 65)\\b.*");

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "198 I 65", "32.707198,-86.524518",
      "199 I 65", "32.720211,-86.532265",
      "200 I 65", "32.733067,-86.540234",
      "201 I 65", "32.745671,-86.548738",
      "202 I 65", "32.758163,-86.557601",
      "203 I 65", "32.770106,-86.565760",
      "204 I 65", "32.783200,-86.572944",
      "205 I 65", "32.797830,-86.578741",
      "206 I 65", "32.810977,-86.583488",
      "207 I 65", "32.824956,-86.588515",
      "208 I 65", "32.388490,-86.593545",
      "209 I 65", "32.851408,-86.598183",
      "211 I 65", "32.877567,-86.617036",
      "212 I 65", "32.890050,-86.625328",
      "213 I 65", "32.902922,-86.633940",
      "214 I 65", "32.914952,-86.643072",
      "215 I 65", "32.926489,-86.653570",
      "216 I 65", "32.938620,-86.663011",
      "217 I 65", "32.952191,-86.669573",
      "218 I 65", "32.964786,-86.677963",
      "219 I 65", "32.977439,-86.686524",
      "220 I 65", "32.989659,-86.694650",
      "221 I 65", "33.002401,-86.703860",
      "222 I 65", "33.014940,-86.712753",
      "223 I 65", "33.028082,-86.719807",
      "224 I 65", "33.041748,-86.726009",
      "225 I 65", "33.055557,-86.727639",
      "226 I 65", "33.069199,-86.730036"
  });

  private static final String[] CITY_LIST = new String[]{
    "CLANTON",
    "ISABELLA",
    "JEMISON",
    "MAPLESVILLE",
    "MOUNTAIN CREEK",
    "STANTON",
    "THORSBY",
    "VERBENA",
    "BILLINGSLEY",

    // Autauga County
    "MARBURY",

    // Bibb County
    "LAWLEY",
    "RANDOLPH",

    // Dallas County
    "PLANTERSVILLE",

    // Shelby County
    "CALERA",
    "MONTEVALLO"
  };
}
