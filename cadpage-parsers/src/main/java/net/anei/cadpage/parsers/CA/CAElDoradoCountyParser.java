package net.anei.cadpage.parsers.CA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * El Dorado County, CA
 */

public class CAElDoradoCountyParser extends MsgParser {

  private static final Pattern GEN_ALERT_PTN = Pattern.compile("From: \\w+\\* *(.*)");
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("#(\\d+)[\\.,]+ *(.*)");
  private static final Pattern RUN_REPORT_DELIM_PTN = Pattern.compile(", *|\\.{2,}");
  private static final Pattern RUN_REPORT_PTN2 = Pattern.compile("CLOSE: Site Inc# \\d+ Jur Inc# ([A-Z]+\\d+)\n *Call Type: (.*?)\n *Location: (.*?)(?:,(.*))?\n(.*)");
  private static final Pattern RUN_REPORT_DELIM_PTN2 = Pattern.compile(" *; *");
  private static final Pattern MARKER = Pattern.compile("^(\\d{1,2}-\\w{3}-\\d{4})/(\\d\\d:\\d\\d:\\d\\d)[:;] ");
  private static final Pattern GPS_PTN = Pattern.compile("[; ]*; (X:.*?);?$");
  private static final Pattern MAP_URL_PTN = Pattern.compile("<a href=.*?$");
  private static final Pattern UNIT_PTN = Pattern.compile("(?:;|  |(?<=\\)) ) *([ A-Z0-9\\[\\]]+)$");
  private static final Pattern CITY_UNIT_PTN = Pattern.compile(", *([_A-Za-z0-9]+) ([ A-Z0-9\\[\\]]+)$");
  private static final Pattern MASTER = Pattern.compile("(.*?)[:;] Inc# ([A-Z]*\\d*)[:;] (.*?) *(?:,([_A-Za-z0-9]*) *)?");
  private static final Pattern B_ADDR = Pattern.compile("=[BL]\\(.*\\)");
  private static final DateFormat DATE_FMT = new SimpleDateFormat("dd-MMM-yyyy");

  public CAElDoradoCountyParser() {
    this("EL DORADO COUNTY", "CA");
  }

  protected CAElDoradoCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getAliasCode() {
    return "CAElDoradoCounty";
  }

  @Override
  public String getFilter() {
    return "AEUCAD@fire.ca";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD Page")) return false;

    Matcher match = GEN_ALERT_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1);
      match = RUN_REPORT_PTN.matcher(body);
      if (match.matches()) {
        setFieldList("ID INFO");
        data.msgType = MsgType.RUN_REPORT;
        data.strCallId = match.group(1);
        data.strSupp = RUN_REPORT_DELIM_PTN.matcher(match.group(2)).replaceAll("\n");
      } else {
        data.msgType = MsgType.GEN_ALERT;
        setFieldList("INFO");
        data.strSupp = body;
      }
      return true;
    }

    match = RUN_REPORT_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("ID CALL ADDR APT CITY INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strCall = match.group(2);
      parseAddress(match.group(3).trim(), data);
      data.strCity = getOptGroup(match.group(4));
      data.strSupp = RUN_REPORT_DELIM_PTN2.matcher(match.group(5)).replaceAll("\n").trim();
      return true;
    }

    match = MARKER.matcher(body);
    if (!match.find()) return false;

    setDate(DATE_FMT, match.group(1), data);
    data.strTime = match.group(2);
    body = body.substring(match.end()).trim();

    // Now start stripping fields off of the end, starting with the GPS coordinates
    match = GPS_PTN.matcher(body);
    if(match.find()) {
      setGPSLoc(match.group(1).trim(), data);
      body = body.substring(0,match.start()).trim();
    }

    // Preceded by a redundant Google map URL
    match = MAP_URL_PTN.matcher(body);
    if (match.find()) {
      body = body.substring(0,match.start()).trim();
    }

    // Preceded by a mandatory UNIT field
    match = UNIT_PTN.matcher(body);
    if (match.find()) {
      data.strUnit = match.group(1);
      body = body.substring(0,match.start()).trim();
    } else if ((match = CITY_UNIT_PTN.matcher(body)).find()) {
      data.strCity = match.group(1).replace('_', ' ').trim();
      data.strUnit = match.group(2);
      body = body.substring(0,match.start()).trim();
    } else return false;

    // Preceded by an optional place field
    body = parsePlace(body, data);

    // We can use a  master pattern for the rest
    match = MASTER.matcher(body);
    if (!match.matches()) return false;

    setFieldList("DATE TIME CALL ID ADDR APT CITY PLACE UNIT GPS");
    data.strCall = match.group(1).trim();
    data.strCallId = match.group(2).trim();
    String addr = match.group(3).trim();
    if (!B_ADDR.matcher(addr).matches()) {
      int pt = addr.indexOf('@');
      if (pt >= 0) {
        data.strPlace = addr.substring(0,pt).trim();
        addr = addr.substring(pt+1).trim();
      }
    }
    parseAddress(addr, data);
    String city = match.group(4);
    if (city != null) data.strCity = city.replace('_', ' ').trim();
    if (data.strCity.startsWith("GEORGETOWN ")) data.strCity = "GEORGETOWN";
    else if (data.strCity.startsWith("SOUTH PLACER ")) data.strCity = "PLACER COUNTY";

    return true;
  }

  private String parsePlace(String body, Data data) {

    if (!body.endsWith(")")) return body;

    int parenCnt = 0;
    for (int pt = body.length()-1; pt>=0; pt--) {
      char chr = body.charAt(pt);
      if (chr == ')') parenCnt++;
      else if (chr == '(') {
        if (--parenCnt == 0) {
          data.strPlace = body.substring(pt+1, body.length()-1).trim();
          return body.substring(0,pt).trim();
        }
      }
    }
    return body;
  }
}
