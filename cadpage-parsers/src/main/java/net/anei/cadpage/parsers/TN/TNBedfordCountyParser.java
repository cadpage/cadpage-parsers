package net.anei.cadpage.parsers.TN;

import net.anei.cadpage.parsers.SmartAddressParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class TNBedfordCountyParser extends SmartAddressParser {

  public TNBedfordCountyParser() {
    super(CITY_LIST, "BEDFORD COUNTY", "TN");
    setFieldList("ADDR APT CITY ST CALL INFO");
  }

  @Override
  public String getFilter() {
    return "bedford911reports@gmail.com";
  }

  private static final Pattern MASTER1 = Pattern.compile("([^,]+), *([A-Z ]+), ([A-Z]{2})(?: *\\d{5})? *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Notification")) return false;
    if (!body.startsWith("Notification:")) return false;
    body = body.substring(13).trim();

    Matcher match =  MASTER1.matcher(body);
    if (match.matches()) {
      parseAddress(match.group(1).trim(), data);
      data.strCity = match.group(2);
      data.strState = match.group(3);
      data.strCall = match.group(4);
      return true;
    }

    int pt = body.indexOf(',');
    if (pt >= 0) {
      String addr = body.substring(0,pt).trim();
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body.substring(pt+1).trim(), data);
      if (!data.strCity.isEmpty()) {
        parseAddress(addr, data);
        data.strCall = getLeft();
        return true;
      }
    }

    Result res = parseAddress(StartType.START_ADDR, body);
    if (res.getStatus() >= STATUS_INTERSECTION) {
      res.getData(data);
      data.strCall = getLeft();
      return true;
    }

    data.msgType = MsgType.GEN_ALERT;
    data.strSupp = body;
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // City
      "SHELBYVILLE",

      // Towns
      "BELL BUCKLE",
      "NORMANDY",
      "WARTRACE",

      // Census-designated place
      "UNIONVILLE",

      // Unincorporated communities
      "BRANCHVILLE",
      "BUGSCUFFE",
      "CENTER GROVE",
      "CORTNER'S STATION",
      "FAIRFIELD",
      "FALL CREEK",
      "FLAT CREEK",
      "HALEY'S STATION",
      "HAWTHORNE",
      "MOUNT HARMOND",
      "PALMETTO",
      "PLEASANT GROVE",
      "POPLINS CROSSROADS",
      "RAUS",
      "RICHMOND",
      "ROSEVILLE",
      "ROVER",
      "WHEEL",

      // Marshall County
      "CHAPEL HILL",
      "LEWISBURG",
      "PETERSBURG",

      // Rutherford county
      "EAGLEVILLE",
      "ROCKVALE"
  };
}
