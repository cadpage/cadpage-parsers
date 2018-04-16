package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class WASnohomishCountyDParser extends SmartAddressParser {
  
  public WASnohomishCountyDParser() {
    super(CITY_LIST, "SNOHOMISH COUNTY", "WA");
    setFieldList("CALL UNIT ADDR APT CITY PLACE MAP CH INFO");
  }
  
  @Override
  public String getFilter() {
    return "noreply@snocom.org";
  }
  
  private static final Pattern MASTER = Pattern.compile(">>>([A-Z]+)<<< ([,A-Z0-9]+) (.+?) ([A-Z][A-Z0-9]\\d{3}|[A-Z]{3}\\d[A-Z]{2}\\d|NORCOMSHO)(?: ([A-Z]+ TAC \\d+))?(.*)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISP")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = match.group(1);
    data.strUnit = match.group(2);
    String addr = match.group(3).trim();
    data.strMap = match.group(4);
    data.strChannel = getOptGroup(match.group(5));
    data.strSupp = match.group(6).trim();

    if (!addr.startsWith("LAT:")) {
      int pt = addr.indexOf(',');
      if (pt >= 0) {
        String city = addr.substring(pt+1).trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
        if (data.strCity.length() == 0) {
          data.strCity = city;
          return false;
        } else {
          data.strPlace = getLeft();
        }
        addr = addr.substring(0,pt).trim();
      }
    }
    parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_NO_CITY | FLAG_ANCHOR_END, addr.replace('@', '/'), data);
    
    return true;
  }

  private static final Pattern NOT_APT_PTN = Pattern.compile("[NSEW]O .*");

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (NOT_APT_PTN.matcher(apt).matches()) return true;
    return super.isNotExtraApt(apt);
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "ARLINGTON",
    "BOTHELL",
    "BRIER",
    "EDMONDS",
    "EVERETT",
    "GOLD BAR",
    "GRANITE FALLS",
    "LAKE STEVENS",
    "LYNNWOOD",
    "MARYSVILLE",
    "MILL CREEK",
    "MONROE",
    "MOUNTLAKE TERRACE",
    "MUKILTEO",
    "SNOHOMISH",
    "STANWOOD",
    "SULTAN",
    "WOODWAY",

    // Towns
    "DARRINGTON",
    "INDEX",

    // Census-designated places
    "ALDERWOOD MANOR",
    "ARLINGTON HEIGHTS",
    "BOTHELL EAST",
    "BOTHELL WEST",
    "BRYANT",
    "BUNK FOSS",
    "CANYON CREEK",
    "CATHCART",
    "CAVALERO",
    "CHAIN LAKE",
    "CLEARVIEW",
    "EASTMONT",
    "ESPERANCE",
    "FOBES HILL",
    "HAT ISLAND",
    "HIGH BRIDGE",
    "LAKE BOSWORTH",
    "LAKE KETCHUM",
    "LAKE ROESIGER",
    "LAKE STICKNEY",
    "LOCHSLOY",
    "MACHIAS",
    "MALTBY",
    "MARTHA LAKE",
    "MAY CREEK",
    "NORTH MARYSVILLE",
    "NORTH SULTAN",
    "NORTHWEST STANWOOD",
    "OSO",
    "SILVANA",
    "SILVER FIRS",
    "STARTUP",
    "SUNDAY LAKE",
    "SWEDE HEAVEN",
    "THREE LAKES",
    "VERLOT",
    "WARM BEACH",
    "WOODS CREEK",
    
    "KING COUNTY"
  };
}
