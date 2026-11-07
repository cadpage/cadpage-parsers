package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class DispatchA88Parser extends SmartAddressParser {

  public DispatchA88Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState);
    setFieldList("ID PLACE ADDR CITY APT CALL URL");
  }

  private static final Pattern URL_PTN = Pattern.compile("\n(https?://.*?)\n");
  private static final Pattern MASTER = Pattern.compile("(?:\\d+-)?CFS Report (\\d{2}(?:\\d{2})?-\\d{5,8}) (?:([^|,]*?)\\|)?([^,]*?)(?:,(.*))?");
  private static final Pattern CITY_APT_PTN = Pattern.compile("([A-Za-z ]+)-(\\d+)\\b *(.*)");
  private static final Pattern MM_PTN = Pattern.compile("(MM \\d+(?:/\\d*)?)\\b *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("Disclaimer:")) {
      body = "";
    } else if (body.startsWith("New Zix secure email message")) {
      Matcher match = URL_PTN.matcher(body);
      if (match.find()) {
        data.strInfoURL = match.group(0).trim();
      }
      body = "";
    }
    body = append(subject, " | ", body);
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    String place = getOptGroup(match.group(2));
    String addr = match.group(3).trim();
    String city = match.group(4);

    if (addr.endsWith(" INT")) {
      parseAddress(place, data);
      return true;
    }
    data.strPlace = place;
    if (city != null) {
      parseAddress(addr, data);
      if (city.isEmpty()) return true;
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city.trim(), data);
      if (!data.strCity.isEmpty()) {
        data.strCall = getLeft();
        return true;
      } else {
        match = CITY_APT_PTN.matcher(city);
        if (match.matches()) {
          city = match.group(1);
          if (isCity(city)) {
            data.strCity = city;
            data.strApt = append(data.strApt, "-", match.group(2));
            data.strCall = match.group(3);
            return true;
          }
        }
        return false;
      }
    } else {
      parseAddress(StartType.START_ADDR, addr, data);
      data.strCall = getLeft();
      if (data.strCall.isEmpty()) return false;
      match = MM_PTN.matcher(data.strCall);
      if (match.matches()) {
        data.strAddress = append(data.strAddress,  " ", match.group(1));
        data.strCall = match.group(2);
      }
      return true;
    }
  }
}
