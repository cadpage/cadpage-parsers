package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

public class KYOldhamCountyCParser extends SmartAddressParser {

  public KYOldhamCountyCParser() {
    super(KYOldhamCountyParser.CITY_LIST, "OLDHAM COUNTY", "KY");
    setFieldList("ADDR CITY ST PLACE APT GPS CALL INFO X DATE TIME");
  }

  @Override
  public String getFilter() {
    return "cspro@oldhamcountyky.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern X_DATE_TIME_PTN = Pattern.compile("(.*) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)(?! - log) *(.*)");
  private static final Pattern LOG_DATE_TIME_PTN = Pattern.compile("[; ]+\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - log - *");
  private static final Pattern GPS_CALL_PTN = Pattern.compile("(.*) (?:None None|([-+]?\\d{2,3}\\.\\d{6} [-+]?\\d{2,3}\\.\\d{6})) *(.*)");
  private static final Pattern ADDR_CITY_ST_PLACE_PTN = Pattern.compile("([^,]*), *([ A-Z]+), ([A-Z]{2})(?: +\\d{5})?\\b *(.*)");
  private static final Pattern PLACE_APT_PTN = Pattern.compile("(.*?) *\\b(?:APT|ER|ROOM|RM|UNIT) +(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    body = stripFieldEnd(body, " No");

    Matcher match = X_DATE_TIME_PTN.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).trim();
    data.strDate = match.group(2);
    data.strTime =  match.group(3);
    data.strCross = match.group(4);

    boolean first = true;
    for (String part : LOG_DATE_TIME_PTN.split(body)) {
      if (first) {
        first = false;
        body = part;
      } else {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }

    match = GPS_CALL_PTN.matcher(body);
    if (!match.matches()) return false;
    body = match.group(1).trim();
    String gps = match.group(2);
    if (gps != null) setGPSLoc(gps, data);
    data.strCall = match.group(3);

    boolean noApt = body.endsWith(" None");
    if (noApt) body = body.substring(0, body.length()-5).trim();

    String place;
    match = ADDR_CITY_ST_PLACE_PTN.matcher(body);
    if (match.matches()) {
      parseAddress(match.group(1).trim(), data);
      data.strCity = match.group(2).trim();
      data.strState = match.group(3).trim();
      place = match.group(4);
    } else {
      int pt = body.indexOf(',');
      if (pt >= 0) {
        parseAddress(body.substring(0,pt).trim(), data);
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body.substring(pt+1).trim(), data);
        if (data.strCity.length() == 0) return false;
        place = getLeft();
      } else {
        parseAddress(StartType.START_ADDR, body, data);
        place = getLeft();
      }
    }

    if (!noApt) {
      match = PLACE_APT_PTN.matcher(place);
      if (match.matches()) {
        place = match.group(1);
        data.strApt = append(data.strApt, "-", match.group(2));
      }
    }

    return true;
  }

}
