package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;



public class WAYakimaCountyParser extends SmartAddressParser {

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("([A-Z0-9]+)\n([A-Z0-9]+)\n([A-Z]\\d+|\\d{2}[A-Z]{1,3}\\d+)\n(.*)", Pattern.DOTALL);
  private static final Pattern MASTER =
    Pattern.compile("(?:(\\d\\d\\.\\d\\d\\.\\d\\d) (\\d\\d/\\d\\d/\\d\\d)|\\*\\*\\.\\*\\*\\.\\*\\* \\*\\*/\\*\\*/\\*\\*) ([^@]*?) (?:@ )?([A-Z]{2}[FP]D|AMR|ALS|BIA|SCOM|PRAM|YCSO)((?: +(?:[A-Z]+\\d+[A-Z]?|AOA|[A-Z]{1,2}DC))+)(?: +(.*))?");
  private static final Pattern APT_MARK_PTN = Pattern.compile(" +(?:APT|ROOM) +", Pattern.CASE_INSENSITIVE);

  public WAYakimaCountyParser() {
    super("YAKIMA COUNTY", "WA");
    setup();
    setFieldList("TIME DATE CALL ADDR APT PLACE SRC CITY UNIT ID INFO");
  }

  @Override
  public String getFilter() {
    return "wwantla@ci.yakima.wa.us,Brad.Coughenour@yakimawa.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.msgType = MsgType.RUN_REPORT;
      data.strSource = match.group(1);
      data.strUnit = match.group(2);
      data.strCallId = match.group(3);
      data.strSupp = match.group(4).trim();
      return true;
    }

    match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strTime = getOptGroup(match.group(1)).replace('.', ':');
    data.strDate = getOptGroup(match.group(2));
    String sAddr = match.group(3).trim();
    data.strSource = match.group(4);
    String city = SRC_CITY_CODES.getProperty(data.strSource);
    if (city != null) data.strCity = city;
    data.strUnit = match.group(5).trim();
    data.strSupp = getOptGroup(match.group(6));

    // Address section consists of a call, address, and possible semicolon separated place and/or apt
    Parser p = new Parser(sAddr);
    String addr = p.get(';');
    int pt = addr.lastIndexOf(',');
    if (pt >= 0) {
      data.strCity = convertCodes(addr.substring(pt+1).trim(), CITY_CODES);
      addr = addr.substring(0,pt).trim();
    }
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, addr, data);
    String place = p.get(';');
    match = APT_MARK_PTN.matcher(place);
    if (match.find()) {
      data.strApt = append(data.strApt, "-", place.substring(match.end()));
      data.strPlace = place.substring(0,match.start());
    }
    else if (data.strApt.length() == 0 && place.length() <= 4) {
      data.strApt = append(data.strApt, "-", place);
    } else {
      data.strPlace = place;
    }
    data.strApt = append(data.strApt, "-", p.get());

    if (data.strAddress.length() == 0) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    return true;
  }

  @Override
  public String adjustMapAddress(String addr) {
    return SUMMITVIEW_EX.matcher(addr).replaceAll("SUMMITVIEW EXN");
  }
  private static final Pattern SUMMITVIEW_EX = Pattern.compile("\\bSUMMITVIEW +EXT?\\b", Pattern.CASE_INSENSITIVE);

  private void setup() {
    setupCallList(
      "911 HANG UP",
      "ACCIDENT HITRUN",
      "ACCIDENT INJURY",
      "ACCIDENT NO INJ",
      "ACCIDENT UNKNOW",
      "AGENCY ASSIST",
      "ALARM BUSINESS",
      "ALARM RESIDENT",
      "ASSAULT WEAPON",
      "CITIZEN ASSIST",
      "EMR ALARM MED",
      "EMR AMB",
      "EMR IFT",
      "EMR LIFT ASSIST",
      "EMR MEDIC",
      "EMR NURSE",
      "EMR RED",
      "EMR YELLOW",
      "FIRE O2QLTY",
      "FIRE AIR HEAVY",
      "FIRE AIR LIGHT",
      "FIRE AIR STANDB",
      "FIRE ALARM RES",
      "FIRE ALARM 1",
      "FIRE ALARM 2",
      "FIRE APPLIANCE",
      "FIRE AUTO ALARM",
      "FIRE AUTO ALM 1",
      "FIRE AUTO ALM 2",
      "FIRE BRUSH GRAS",
      "FIRE CHIMNEY",
      "FIRE ELECTRICAL",
      "FIRE EWR",
      "FIRE FW",
      "FIRE HAYSTACK",
      "FIRE HAZMAT",
      "FIRE INVEST",
      "FIRE N-GAS LEAK",
      "FIRE OTHER",
      "FIRE POWER PROB",
      "FIRE RESCUE",
      "FIRE SMOKE INV",
      "FIRE SRVC CALL",
      "FIRE STRUC COMM",
      "FIRE STRUCTURE",
      "FIRE TRASH GARB",
      "FIRE VEHICLE",
      "FIRE WILD/URBAN",
      "FIRE WILDFIRE",
      "OVERDOSE",
      "PAGED",
      "ROBBERY",
      "SUSPICIOUS CIRC",
      "WEAPON OFFENSE",
      "WELFARE CHECK"
    );
  }

  private static final Properties SRC_CITY_CODES = buildCodeTable(new String[]{
      "GRFD", "GRANGER",
      "GVFD", "GRANDVIEW",
      "MBFD", "MABTON",
      "NAFD", "NACHES",
      "SEFD", "SELAH",
      "SSFD", "SUNNYSIDE",
      "TPFD", "TOPPENISH",
      "YKFD", "YAKIMA",
      "YKPD", "YAKIMA",
      "WPFD", "WAPATO",
      "ZIFD", "ZILLAH"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BUE", "BUENA",
      "COW", "COWICHE",
      "GRA", "GRANGER",
      "GRV", "GRANDVIEW",
      "HAR", "HARRAH",
      "MAB", "MABTON",
      "MOX", "MOXEE",
      "NAC", "NACHES",
      "PAR", "PARKER",
      "PRO", "PROSSER",
      "SEL", "SELAH",
      "SUN", "SUNNYSIDE",
      "TIE", "TIETON",
      "TOP", "TOPPENISH",
      "WAP", "WAPATO",
      "WHI", "WHITE SWAN",
      "U1",  "UNION GAP",
      "U2",  "UNION GAP",
      "U3",  "UNION GAP",
      "Y1",  "YAKIMA",
      "Y2",  "YAKIMA",
      "Y3",  "YAKIMA",
      "Y4",  "YAKIMA",
      "Y5",  "YAKIMA",
      "Y6",  "YAKIMA",
      "Y7",  "YAKIMA",
      "Y8",  "YAKIMA",
      "ZIL", "ZILAH"
  });
}
