package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;



public class WAKingCountyAParser extends SmartAddressParser {

  private static final Pattern GPS_PTN = Pattern.compile("#LAT:?(?:(\\d{2})(\\d{6})|0) +#LON:?(?:(\\d{3})(\\d{6})|0)(?: +(\\d+))?$");

  public WAKingCountyAParser() {
    super(CITY_LIST, "KING COUNTY", "WA");
    setFieldList("INFO ADDR APT CITY CALL CH UNIT GPS");
  }

  @Override
  public String getFilter() {
    return "CAD@bellevuewa.gov,VisiCAD@norcom.org,NoReply@snosafe.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitBreakLength() { return 255; }
      @Override public int splitBreakPad() { return 4; }
    };
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Retrieve GPS coordinates from end of message
    boolean shortForm = false;
    String gpsInfo = null;
    Matcher match = GPS_PTN.matcher(body);
    if (match.find()) {
      if (match.group(1) != null && match.group(3)!=null) {
        gpsInfo = '+' + match.group(1) + '.' + match.group(2) + ",-" + match.group(3) + '.' + match.group(4);
      }
      body = body.substring(0,match.start()).trim();
      shortForm = match.group(5) != null;
    }

    if (! parseNormalMsg(shortForm, body, data)) {
      data.initialize(this);
      if (!parseCompressedMsg(body, data)) return false;
    }

    if (gpsInfo != null) data.strGPSLoc = gpsInfo;
    return true;
  }

  private boolean parseNormalMsg(boolean shortForm, String body, Data data) {

    FParser p = new FParser(body);

    if (p.check("ADDRESS CHANGE")) {
      p.check(":");
      parseAddress(p.get(42), data);
      if (!p.check("#")) return false;
      data.strApt = append(data.strApt, "-", p.get(10));
      if (p.checkBlanks(1)) return false;
      data.strCall = p.get(30);
      if (!p.check("#")) return false;
      data.strChannel = p.get(7);
      if (p.check(" ")) return false;
      data.strUnit = p.get();
      return true;
    }

    parseAddress(p.get(50), data);
    if (!p.check("#")) return false;

    if (p.checkAheadBlanks(26, 2) && !p.checkAheadBlanks(28, 1)) {
      data.strApt = append(data.strApt, "-", p.get(6));
      data.strCity = p.get(22);
      data.strCall = p.get(29);
      if (!p.check(" ")) return false;
      data.strChannel = p.get(6);
      if (p.check(" ")) return false;
      data.strUnit = p.get();
      return true;
    }

    String apt = p.get(shortForm ? 7 : 10);
    data.strApt = append(data.strApt, "-", apt);
    if (p.check(" ")) return false;

    if (p.lookahead(34,1).length() == 0 && p.lookahead(35,1).length() == 1) data.strCity = p.get(35);
    data.strCall = p.get(29);
    if (!p.check(" ")) return false;
    String channel = p.get(6);
    if (!shortForm) p.check("   ");
    if (channel.startsWith("FT")) data.strChannel = channel;
    if (!p.check(" ") || p.check(" ")) return false;
    data.strUnit = p.get();
    return true;
  }

  private static final Pattern COMP_MASTER = Pattern.compile("([^#]+) #(\\S*) (.*)");
  private static final Pattern TRAIL_MASTER = Pattern.compile("(.*?) (?:#?((?:FT|SP)[A-Z0-9]+)|#) ([A-Z0-9, ]+)");

  private boolean parseCompressedMsg(String body, Data data) {
    if (body.startsWith("CAD||")) return false;

    body = body.replace("CALLBK=", " ");

    Matcher match = COMP_MASTER.matcher(body);
    if (!match.matches()) return false;
    String addr = match.group(1).trim();
    if (addr.startsWith("Comment:")) {
      int pt = addr.lastIndexOf(',');
      if (pt < 0) return false;
      data.strSupp = addr.substring(8, pt).trim();
      addr = addr.substring(pt+1).trim();
    } else {
      addr = stripFieldStart(addr, "ADDRESS CHANGE");
    }
    parseAddress(addr, data);
    data.strApt = append(data.strApt, "-", match.group(2));
    String tail = match.group(3);
    parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, tail, data);
    tail = getLeft();

    match = TRAIL_MASTER.matcher(tail);
    if (match.matches()) {
      data.strCall = match.group(1).trim();
      data.strChannel = getOptGroup(match.group(2));
      data.strUnit = match.group(3).trim().replace(' ', '_');
      return true;
    }

    String call = CALL_LIST.getCode(tail);
    if (call != null) {
      data.strCall = call;
      data.strUnit = tail.substring(call.length()).trim();
      return true;
    }

    return false;
  }

  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "AFA - Commercial",
      "AFA - Multi-Family",
      "AFA - Reset",
      "AFA - Residential",
      "AFA - Trouble",
      "AFA Water Flow - Commercial",
      "AFA Water Flow - Multi-Family",
      "Aid - Emergency",
      "Aid - Emergency CVA Protocol",
      "Aid - Emergency - DOA",
      "Aid - Emergency Weapons",
      "Aid - Non-Emergency",
      "Appliance - Fire",
      "Cardiac Arrest",
      "Electrical - Odor",
      "Flooding - Minor",
      "Haz",
      "Medic",
      "Medic - CVA Protocol",
      "Medic - Weapons",
      "Mutual Aid Request",
      "MVA - Aid Emergency",
      "MVA - Medic",
      "MVA - Rescue",
      "MVC - Aid Emergency",
      "Natural Gas - Major",
      "Natural Gas - Odor",
      "Rescue - Surface Water",
      "Service Call - Fire",
      "Smoke - Haze In The Area",
      "Smoke - Residence",
      "Smoke - Smell",
      "Still Alarm",
      "Structure Fire - Commercial",
      "Structure Fire - Outbuilding",
      "Structure Fire - Residential",
      "Structure Fire - Unconfirmed",
      "Test Call",
      "UPGRADE",
      "Vehicle - Fire",
      "Wires Air - Flames Seen",
      "Wires Ground - Fire/Arc/Spark",
      "Working Fire - Multi/Fam",
      "Working Fire - Residential"
  );

  private static final String[] CITY_LIST = new String[] {

      // Counties
      "KING COUNTY",
      "SNOHOMISH COUNTY",

      // Cities
      "ALGONA",
      "AUBURN",
      "BELLEVUE",
      "BLACK DIAMOND",
      "BOTHELL",
      "BURIEN",
      "CARNATION",
      "CLYDE HILL",
      "COVINGTON",
      "DES MOINES",
      "DUVALL",
      "ENUMCLAW",
      "FEDERAL WAY",
      "ISSAQUAH",
      "KENMORE",
      "KENT",
      "KIRKLAND",
      "LAKE FOREST PARK",
      "MAPLE VALLEY",
      "MEDINA",
      "MERCER ISLAND",
      "MILTON",
      "NEWCASTLE",
      "NORMANDY PARK",
      "NORTH BEND",
      "PACIFIC",
      "REDMOND",
      "RENTON",
      "SAMMAMISH",
      "SEATAC",
      "SEATTLE",
      "SHORELINE",
      "SNOQUALMIE",
      "TUKWILA",
      "WOODINVILLE",
      "TOWNS",
      "BEAUX ARTS VILLAGE",
      "HUNTS POINT",
      "SKYKOMISH",
      "YARROW POINT",

      // Census-designated places
      "AMES LAKE",
      "BARING",
      "BOULEVARD PARK",
      "BRYN MAWR-SKYWAY",
      "COTTAGE LAKE",
      "EAST RENTON HIGHLANDS",
      "FAIRWOOD",
      "FALL CITY",
      "HOBART",
      "INGLEWOOD-FINN HILL",
      "KLAHANIE",
      "LAKE HOLM",
      "LAKE MARCEL-STILLWATER",
      "LAKE MORTON-BERRYDALE",
      "LAKELAND NORTH",
      "LAKELAND SOUTH",
      "MAPLE HEIGHTS-LAKE DESIRE",
      "MIRRORMONT",
      "RAVENSDALE",
      "RIVERBEND",
      "RIVERTON",
      "SHADOW LAKE",
      "TANNER",
      "UNION HILL-NOVELTY HILL",
      "VASHON",
      "WESTWOOD",
      "WHITE CENTER",
      "WILDERNESS RIM",

      // Other unincorporated communities
      "CEDAR FALLS",
      "CUMBERLAND",
      "DENNY CREEK",
      "ERNIES GROVE",
      "GROTTO",
      "KANASKAT",
      "KANGLEY",
      "LAKE JOY",
      "NACO",
      "NOVELTY",
      "PALMER",
      "PRESTON",
      "SELLECK",
      "SPRING GLEN",
      "WABASH",

      // Former cities and towns
      "EAST REDMOND",
      "FOSTER",
      "HOUGHTON",
      "GHOST TOWNS",
      "BAYNE",
      "CEDAR FALLS",
      "EDGEWICK",
      "FRANKLIN",
      "HOT SPRINGS",
      "KRAIN",
      "LESTER",
      "MONOHON",
      "NAGROM",
      "OSCEOLA",
      "TAYLOR",
      "WELLINGTON",
      "WESTON"
  };
}
