package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Leavenworth County, KS
 */
public class KSLeavenworthCountyParser extends SmartAddressParser {

  public KSLeavenworthCountyParser() {
    super(CITY_LIST, "LEAVENWORTH COUNTY", "KS");
    setFieldList("CALL ID ADDR APT CITY ST PLACE UNIT INFO");
  }

  @Override
  public String getFilter() {
    return "sysalert@leavenworthcounty.gov";
  }

  private static final Pattern TIMES_PTN =
      Pattern.compile("(?:;? \\S+ - (?:ASSIGNED|ENROUTE|On Scene|EXCHANGE|TRAFFIC STOP|OFFICER INITIATED|LEAVING SCENE) \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d)+$");
  private static final Pattern INFO_BRK_PTN = Pattern.compile(";? \\d\\d/\\d\\d.\\d\\d \\d\\d:\\d\\d:\\d\\d - ");
  private static final Pattern MASTER =
      Pattern.compile("(\\d+) (?:None|[A-Z][A-Z0-9]\\d{9}(?:; [A-Z][A-Z0-9]\\d{9})*) ([^,]*?)(?:, ([A-Z ]+)(?:, ([A-Z]{2}) (?:\\d{5} )?)?)?([^,;]*?)\\b((?:[A-Z0-9]+; )*[A-Z0-9]+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    body = stripFieldEnd(body, "{location_notes}");
    Matcher match = TIMES_PTN.matcher(body);
    String times = "";
    if (match.find()) {
      body = body.substring(0, match.start()).trim();
      times = match.group().trim().replace("; ", "\n");
    }
    if (body.endsWith(" None")) {
      body = body.substring(0,body.length()-5).trim();
    } else {
      String[] parts = INFO_BRK_PTN.split(body);
      body = parts[0];
      for (int ii = 1; ii < parts.length; ii++) {
        data.strSupp = append(data.strSupp, "\n", parts[ii].trim());
      }
    }
    data.strSupp = append(data.strSupp, "\n", times);

    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    parseAddress(match.group(2).trim(), data);
    data.strCity = getOptGroup(match.group(3));
    data.strState = getOptGroup(match.group(4));
    data.strPlace = match.group(5).trim();
    data.strUnit = match.group(6).trim().replace("; ", ",");

    if (data.strAddress.isEmpty() && !data.strPlace.isEmpty()) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    if (data.strState.isEmpty() && !data.strCity.isEmpty()) {
      String city = data.strCity;
      data.strCity = "";
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
      if (data.strCity.isEmpty()) {
        data.strCity = city;
      } else {
        data.strPlace = getLeft();
      }
    }
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BASEHOR",
      "BONNER SPRINGS",
      "DE SOTO",
      "EASTON",
      "LANSING",
      "LEAVENWORTH",
      "LINWOOD",
      "TONGANOXIE",

      // Unincorporated communities
      "FAIRMOUNT",
      "FALL LEAF",
      "FORT LEAVENWORTH",
      "HOGE",
      "JARBALO",
      "KICKAPOO",
      "LENAPE",
      "LOWEMONT",
      "MILLWOOD",
      "RENO",
      "SPRINGDALE",
      "WADSWORTH",

      // Ghost towns
      "DELAWARE CITY",

      // Atchison County
      "ATCHISON",

      // Jefferson County
      "MC LOUTH",
      "MCLOUTH",

      // Wyandotte County
      "KANSAS CITY"

  };

}
