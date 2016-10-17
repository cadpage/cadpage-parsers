package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class DispatchA40Parser extends SmartAddressParser {

  public DispatchA40Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState);
    setFieldList("SRC CALL PLACE ADDR APT CITY X GPS INFO");
  }

  private static Pattern NAT_LOC_COM = Pattern.compile("(?:(?:([A-Za-z]+):)?(?:\\[[A-Z0-9]+\\])?[- ]*- Nature:? *)?(.*) - Location:? *(.*?)(?: [-.] (?:Comments:?)? *(.*))?");
  private static Pattern DECIMAL_ADDR = Pattern.compile("(.+)\\b(\\d+\\.\\d{2} .*?)");
  private static Pattern INFO_GPS_PTN = Pattern.compile("([-+]\\d{3}\\.\\d{6} [-+]\\d{3}\\.\\d{6}) CF= *\\d*%? *");

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher mat = NAT_LOC_COM.matcher(body);
    if (!mat.matches()) return false;
    data.strSource = getOptGroup(mat.group(1));
    data.strCall = mat.group(2).trim(); // nature
    String addr = mat.group(3).trim();
    data.strSupp = getOptGroup(mat.group(4)); // comments
    
    // clean up address, and extract possible cross street
    addr = stripFieldEnd(addr, "-");
    int pt = addr.indexOf(" X-STREET ");
    if (pt >= 0) {
      data.strCross = addr.substring(pt+10).trim();
      addr = addr.substring(0,pt).trim();
    }
    
    // Look for place indicator
    pt = addr.indexOf('@');
    if (pt >= 0) {
      data.strPlace = addr.substring(0,pt).trim();
      addr = addr.substring(pt+1).trim();
    }

    // locate address by finding \\d+\\.00, then parse and make sure it is an address
    // this is to support rare pages with \
    // - Location: REDISPATCH!! DRIVER IS NEEDED FOR 11-8 OR ANOTHER ENGINE TO RESPOND 802.00 FAIRFIELD CT EAST GREENBUSH
    // but wont work if address has no number
    Matcher daMat = DECIMAL_ADDR.matcher(addr);
    Result res = null;
    if (daMat.matches()) {
      res = parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD_EXCL_CITY | FLAG_ANCHOR_END, daMat.group(2).replace(".00", ""));
      if (res.isValid()) {
        res.getData(data);
        data.strCall = append(data.strCall, " / ", daMat.group(1).trim());
      }
      else res = null;
    }

    // otherwise try just parsing location to addr like before
    if (res == null) {
      res = parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD_EXCL_CITY | FLAG_ANCHOR_END, addr.replace(".00", ""));
      res.getData(data);
    }
    
    // general cleanup
    if (data.strApt.equals("APT") || data.strApt.equals("RM")) data.strApt = "";
    data.strApt = append(data.strApt, "-", res.getPadField());
    
    // Check for GPS coordinates
    mat = INFO_GPS_PTN.matcher(data.strSupp);
    if (mat.lookingAt()) {
      setGPSLoc(mat.group(1), data);
      data.strSupp = data.strSupp.substring(mat.end());
    }
    return true;

  }

}
