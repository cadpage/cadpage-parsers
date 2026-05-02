package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class MNPolkCountyParser extends MsgParser {

  public MNPolkCountyParser() {
    super("POLK COUNTY", "MN");
    setFieldList("CALL PHONE GPS ADDR APT CITY ST UNIT INFO CODE");
  }

  @Override
  public String getFilter() {
    return "Zuercher-Email@co.polk.mn.us,Zuercher.Email@polkcountymn.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,} +[-+]?\\d{2}\\.\\d{6,} +");
  private static final Pattern GPS2_PTN = Pattern.compile("\\bNumber (\\(\\d{3}\\)\\d{3}-\\d{4}|); (?:Lat (\\S+); Lon (\\S+); )?.*; Uncertainty \\d*M?m\\b");
  private static final Pattern TRAIL_CODE_PTN = Pattern.compile(" ([A-Z]+|911DISP|911TEST|Arson) None$");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]+\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private static final Pattern TRAIL_UNITS_PTN = Pattern.compile("(.*?) ((?:\\b(?:[A-Za-z]{3,}|\\d{3,4}|[A-Z]+\\d{1,2})\\b(?:; )?)+)$");
  private static final Pattern BAD_ADDR2_PTN = Pattern.compile("(.*) (None None|cfs_location_address)");
  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("([-#/A-Za-z0-9 ]+)(?:, *([A-Za-z ]+)(?:, *([A-Z]{2})(?: \\d{5}))?)?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    boolean good = false;
    if (!subject.isEmpty()) {
      data.strCall = subject;
      if (!body.startsWith(subject+' ')) return false;
      good = true;
      body = body.substring(subject.length()+1).trim();
    }

    Matcher match = GPS_PTN.matcher(body);
    if (match.lookingAt()) {
      setGPSLoc(match.group(), data);
      body = body.substring(match.end());
      good = true;
    } else if ((match = GPS2_PTN.matcher(body)).lookingAt()) {
      parseGPS2(match, data);
      body = body.substring(match.end()).trim();
      good = true;
    } else if (body.startsWith("None None ")) {
      good = true;
      body = body.substring(10).trim();
    }

    match = TRAIL_CODE_PTN.matcher(body);
    if (match.find()) {
      data.strCode = match.group(1);
      body = body.substring(0,match.start()).trim();
    }

    if (body.endsWith(" None")) {
      body = body.substring(0, body.length()-5).trim();
    } else {
      String[] parts = INFO_BRK_PTN.split(body);
      if (parts.length > 1) {
        good = true;
        body = parts[0];
        for (int jj = 1; jj<parts.length; jj++) {
          data.strSupp = append(data.strSupp, "\n", parts[jj]);
        }
      }
    }

    match = TRAIL_UNITS_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strUnit = match.group(2).replace("; ", ",");
    }

    if (data.strGPSLoc.isEmpty()) {
      match = GPS2_PTN.matcher(body);
      if (match.find()) {
        if (match.end() != body.length()) return false;
        good = true;
        body = body.substring(0, match.start()).trim();
        parseGPS2(match, data);
      }
    }

    match = BAD_ADDR2_PTN.matcher(body);
    if (match.matches()) {
      good = true;
      body = match.group(1).trim();
    } else {
      int pt = body.indexOf(',');
      if (pt >= 0) {
        int pt2 = body.indexOf(body.substring(0,pt), pt);
        if (pt2 >= 0) {
          good = true;
          body = body.substring(0,pt2).trim();
        }
      } else {
        parseAddress(body, data);
        return good;
      }
    }

    match = ADDR_CITY_ST_PTN.matcher(body);
    if (!match.matches()) return false;
    parseAddress(match.group(1).trim(), data);
    data.strCity =  getOptGroup(match.group(2));
    data.strState = getOptGroup(match.group(3));
    if (!data.strState.isEmpty()) good = true;

    return good;
  }

  private void parseGPS2(Matcher match, Data data) {
    data.strPhone = match.group(1);
    if (match.group(2) != null) {
      setGPSLoc(match.group(2)+','+match.group(3), data);
    }
  }
}
