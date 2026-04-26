package net.anei.cadpage.parsers.TX;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;
/**
 * Travis County, TX
 */
public class TXTravisCountyAParser extends MsgParser {

  public TXTravisCountyAParser() {
    super("TRAVIS COUNTY", "TX");
  }

  public String getFilter() {
    return "PublicSafety@austintexas.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern COMMENT_PTN = Pattern.compile("Comment: (.*?), ?(From -.*?)(?: From -.*)?");

  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = COMMENT_PTN.matcher(body);
    if (match.matches()) {
      data.strSupp = match.group(1).trim();
      body = match.group(2).trim();
    }
    if (parseFmt1(body, data)) return true;
    if (parseFmt2(body, data)) return true;
    if (parseFmt3(body, data)) return true;
    if (parseFmt4(body, data)) return true;
    if (parseFmt5(body, data)) return true;
    if (parseFmt6(body, data)) return true;
    return false;
  }

  private static final Pattern MASTER1 =
      Pattern.compile("From -([A-Z0-9]+) Dispatch - ?\\d?ALARM -(.*?) - BOX -([-A-Z0-9]*) ?On -([ A-Z0-9]*) - AT -(.+?) - INC# =>(\\d+) Case Num:([- A-Z0-9]*) For -([A-Z0-9,]+)");

  private boolean parseFmt1(String body, Data data) {
    Matcher match;
    match = MASTER1.matcher(body);
    if (!match.matches()) return false;
    setFieldList("INFO SRC CALL MAP CH ADDR CITY APT ID UNIT");

    data.strSource = match.group(1);
    data.strCall = match.group(2).trim();
    data.strMap = match.group(3).trim();
    data.strChannel = match.group(4).trim();
    parseAddress(match.group(5).trim(), data);
    data.strCallId = append(match.group(7), "/", match.group(6));
    data.strUnit = match.group(8);
    return true;
  }

  private static final Pattern MASTER2 =
      Pattern.compile("From - ?(NON - ADVI|[A-Z0-9]+) *- ?\\d? ?Alarm / ?(.*?) (?:Pri (\\d+) +)?(?:Box|BOX|\\| RAP) - ?([-A-Z0-9]*) ?@ ?(.*?) (?:\\| )?XStreets: *(.*?)[ \\|]+?On - ?([ A-Z0-9]*)\\|? Time:[ \\|]*(?:(\\d\\d:\\d\\d:\\d\\d)|(\\d\\d:\\d\\d [AP]M))[ \\|]+Inc# ?(\\d+)(?: Case Num:([-A-Z0-9]*))?[ \\|]+For - ?([A-Z0-9,]*)(?: +Lat: ?(\\d+) +Lon: ?(\\d+))?");
  private static final Pattern CROSS_PTN = Pattern.compile("(?:(?:N?o )?CrossStreet Found)?/?(.*?)/?(?:No CrossStreet Found)?");

  private boolean parseFmt2(String body, Data data) {
    Matcher match = MASTER2.matcher(body);
    if (!match.matches()) return false;
    setFieldList("INFO SRC CALL PRI MAP ADDR CITY APT X CH TIME ID UNIT GPS");

    data.strSource = match.group(1);
    data.strCall = match.group(2).trim();
    data.strPriority = getOptGroup(match.group(3));
    data.strMap = match.group(4).trim();
    parseAddress(match.group(5).trim(), data);
    String cross  = match.group(6).trim();
    data.strChannel = match.group(7);
    String time = match.group(8);
    if (time != null) data.strTime = time;
    else setTime(TIME_FMT, match.group(9), data);
    data.strCallId = append(match.group(10), "/", getOptGroup(match.group(11)));
    data.strUnit = match.group(12);
    String gps1 = match.group(13);
    String gps2 = match.group(14);

    match = CROSS_PTN.matcher(cross);
    if (match.matches()) cross = match.group(1);
    data.strCross = cross;

    if (gps1 != null) {
      setGPSLoc(fixGPS(gps1)+','+fixGPS(gps2), data);
    }
    return true;
  }

  private static final Pattern MASTER3 =
      Pattern.compile("INCIDENT ASSIGNED - ?\\d?Alarm /(.*?) \\| RAP - ?([-A-Z0-9]*) ?@ ?(.*?) \\| On - ?(.*?) \\| Inc#(\\d+) \\| For - ?(.*)");

  private boolean parseFmt3(String body, Data data) {
    Matcher match = MASTER3.matcher(body);
    if (!match.matches()) return false;
    setFieldList("CALL MAP CH ADDR CITY APT CH ID UNIT");
    data.strCall = match.group(1);
    data.strMap = match.group(2);
    parseAddress(match.group(3), data);
    data.strChannel = match.group(4).trim();
    data.strCallId = match.group(5);
    data.strUnit =  match.group(6).trim();
    return true;
  }

  private static final Pattern MASTER4 =
      Pattern.compile("DISPATCH ALERT!! Inc #:(\\S*) Map:(\\S*) Location:(.*?) Prem:(.*?) Bldg:(.*?) Apt:(.*?) City:(.*?) Zip:\\S* Problem:(.*?) (?:Pri (\\S*) )?Unit:(\\S*)");

  private boolean parseFmt4(String body, Data data) {
    setFieldList("INFO ID MAP ADDR PLACE APT CITY CALL PRI UNIT");
    Matcher match = MASTER4.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strMap = match.group(2);
    parseAddress(match.group(3).trim(), data);
    data.strPlace = getOptGroup(match.group(4));
    data.strApt = append(data.strApt, "-", match.group(5).trim());
    data.strApt = append(data.strApt, "-", match.group(6).trim());
    data.strCity = convertCodes(match.group(7).trim(), CITY_CODES);
    data.strCall =  match.group(8).trim();
    data.strPriority = getOptGroup(match.group(9));
    data.strUnit =  match.group(10);
    return true;
  }

  private static final Pattern MASTER5 =
      Pattern.compile("(Hospital Times): Inc# (\\S*) \\| Add:(.*?) \\| Zip:\\S* \\| (.*?) \\| Unit: (\\S*)");

  private boolean parseFmt5(String body, Data data) {
    setFieldList("CALL ID ADDR APT INFO");
    Matcher match = MASTER5.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1);
    data.strCallId = match.group(2);
    parseAddress(match.group(3).trim(), data);
    data.strSupp = match.group(4).trim().replace(" | ", "\n");
    return true;
  }

  private static final Pattern MASTER6 =
      Pattern.compile("Closest \\*+([A-Z ]+)\\*+ to (.*?): +(.*)");

  private boolean parseFmt6(String body, Data data) {
    setFieldList("CALL ADDR CITY APT INFO");
    Matcher match = MASTER6.matcher(body);
    if (!match.matches()) return false;
    data.strCall = "Closest " + match.group(1).trim();
    parseAddress(match.group(2).trim(), data);
    data.strSupp = stripFieldEnd(match.group(3).trim(), "[Shared]").replace(" | ", "\n");
    return true;
  }

  @Override
  public void parseAddress(String addr, Data data) {
    int pt = addr.lastIndexOf(',');
    if (pt >= 0) {
      data.strCity = convertCodes(addr.substring(pt+1).trim(), CITY_CODES);
      addr = addr.substring(0,pt).trim();
    }

    pt = addr.indexOf('[');
    if (pt >= 0) addr = addr.substring(0,pt).trim();

    String apt = "";
    pt = addr.indexOf(',');
    if (pt >= 0) {
      apt = addr.substring(pt+1).trim();
      addr = addr.substring(0,pt).trim();
    }

    super.parseAddress(addr, data);

    data.strApt = append(data.strApt, "-", apt);
  }

  private static String fixGPS(String gps) {
    int pt = gps.length()-6;
    if (pt >= 0) gps = gps.substring(0,pt)+'.' + gps.substring(pt);
    return gps;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BAS", "BASTROP COUNTY",
      "BLC", "BLANCO COUNTY",
      "BUC", "BURNET COUNTY",
      "CAC", "CALDWELL COUNTY",
      "HAC", "HAYS COUNTY",
      "TC",  "TRAVIS COUNTY",
      "WSC", "WILLIAMSON COUNTY"
  });
}
