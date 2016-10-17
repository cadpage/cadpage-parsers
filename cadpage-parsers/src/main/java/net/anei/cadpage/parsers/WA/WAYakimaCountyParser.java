package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;



public class WAYakimaCountyParser extends SmartAddressParser {
  
  private static final Pattern MASTER = 
    Pattern.compile("(?:(\\d\\d\\.\\d\\d\\.\\d\\d) (\\d\\d/\\d\\d/\\d\\d)|\\*\\*\\.\\*\\*\\.\\*\\* \\*\\*/\\*\\*/\\*\\*) ([^@]*?) (?:@ )?([A-Z]{2}[FP]D|AMR|ALS|SCOM|PRAM)((?: +(?:[A-Z]+\\d+[A-Z]?|AOA|[A-Z]{1,2}DC))+)(?: +(.*))?");
  private static final Pattern APT_MARK_PTN = Pattern.compile(" +(?:APT|ROOM) +", Pattern.CASE_INSENSITIVE);
  
  public WAYakimaCountyParser() {
    super("YAKIMA COUNTY", "WA");
    setup();
    setFieldList("TIME DATE CALL ADDR APT PLACE SRC CITY UNIT INFO");
  }
  
  @Override
  public String getFilter() {
    return "wwantla@ci.yakima.wa.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strTime = getOptGroup(match.group(1)).replace('.', ':');
    data.strDate = getOptGroup(match.group(2));
    String sAddr = match.group(3).trim();
    data.strSource = match.group(4);
    String city = CITY_CODES.getProperty(data.strSource);
    if (city != null) data.strCity = city;
    data.strUnit = match.group(5).trim();
    data.strSupp = getOptGroup(match.group(6));
    
    // Address section consists of a call, address, and possible semicolon separated place and/or apt
    Parser p = new Parser(sAddr);
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, p.get(';'), data);
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
      "ASSAULT WEAPON",
      "CITIZEN ASSIST",
      "EMR ALARM MED",
      "EMR AMB",
      "EMR IFT",
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
      "FIRE OTHER",
      "FIRE POWER PROB",
      "FIRE RESCUE",
      "FIRE SRVC CALL",
      "FIRE STRUC COMM",
      "FIRE STRUCTURE",
      "FIRE TRASH GARB",
      "FIRE VEHICLE",
      "PAGED",
      "ROBBERY",
      "SUSPICIOUS CIRC",
      "WELFARE CHECK"
    );
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
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
}
