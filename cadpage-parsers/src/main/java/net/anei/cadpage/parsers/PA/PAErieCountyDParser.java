package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAErieCountyDParser extends FieldProgramParser {

  public PAErieCountyDParser() {
    super(PAErieCountyParser.CITY_LIST, "ERIE COUNTY", "PA",
          "Date/Time:DATETIME! Priority:PRI! Call_Type:CALL! Address:ADDRCITY! Common_Name:PLACE% Cross_Streets:X% Lat:GPS1 Lon:GPS2 Tactical:CH " +
          "Incident_#:ID Narrative:INFO CC_Text:INFO/N Caller_Statement:INFO/N");
    setupMultiWordStreets(MWORD_STREET_LIST);
    removeWords("RIDGE");
  }

  @Override
  public String getFilter() {
    return "snpp@eriecountypa.gov,messaging@iamresponding.com,777";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_SRC_PTN = Pattern.compile("[A-Za-z ]+");
  private static final Pattern BRK_PTN = Pattern.compile("(?:=20)? *\n");
  private static final Pattern MASTER = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) (?:(High|Medium|Low) )?(.*)");
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*?) -(\\d{1,3})\\b *(.*)");
  private static final Pattern CALL_ADDR_PTN = Pattern.compile("(.*? (?:ALPHA|BRAVO|CHARLIE|DELTA|ECHO)(?: (?:ENTRAPMENT|HIGH MECHANISM|PINNED|STRUCT|UNK|\\d COM / INDUST|\\d SINGLE RES|\\d MULTI RES|\\d+ OVER WATER|\\d+ UNKNOWN))?) (.*)");
  private static final Pattern APT_PTN = Pattern.compile("(?:LOT|APT|RM|ROOM) (\\S+) *(.*)", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (SUBJECT_SRC_PTN.matcher(subject).matches() && !subject.equals("Text Message")) data.strSource = subject;

    body = BRK_PTN.matcher(body).replaceAll(" ");

    if (!body.startsWith("snpp:")) return false;
    body = body.substring(5).trim();

    // Is this the new labeled format?
    if (body.startsWith("Date/Time:")) {
      return super.parseMsg(body, data);
    }

    // Otherwise parse the old style messages
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    setFieldList("DATE TIME PRI CALL CODE ADDR CITY APT PLACE X INFO GPS");
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    data.strPriority = getOptGroup(match.group(3));

    Parser p = new Parser(match.group(4));
    String addr = p.get(" Lat:");
    String gps1 = p.get(" Lon:");
    String gps2 = p.get();
    setGPSLoc(gps1+','+gps2, data);

    match = CALL_CODE_PTN.matcher(addr);
    if (match.matches()) {
      data.strCall = match.group(1).trim();
      data.strCode = match.group(2);
      addr = match.group(3).trim();
      match = CALL_CODE_PTN.matcher(addr);
      if (match.matches()) {
        String call = match.group(1).trim();
        String code =  match.group(2).trim();
        addr = match.group(3).trim();
        if (!call.equals(data.strCall)) data.strCall = append(data.strCall, " - ", call);
        if (!code.equals(data.strCode)) data.strCode = append(data.strCode, "/", code);
      }
      addr = stripFieldStart(addr, "UNKNOWN ");
    }
    else if ((match = CALL_ADDR_PTN.matcher(addr)).matches()) {
      data.strCall = match.group(1);
      addr = stripFieldStart(match.group(2), data.strCall);
      addr = stripFieldStart(addr, "UNKNOWN ");
    }
    else {
      int pt = addr.indexOf(" UNKNOWN ");
      if (pt >= 0) {
        data.strCall = addr.substring(0,pt).trim();
        addr = addr.substring(pt+9).trim();
      }
    }

    StartType st = data.strCall.length() > 0 ? StartType.START_ADDR : StartType.START_CALL;

    addr = addr.replace('@', '/');
    int pt = addr.indexOf(',');
    if (pt >= 0) {
      parseAddress(st, FLAG_ANCHOR_END, addr.substring(0,pt).trim(), data);
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, addr.substring(pt+1).trim(), data);
    } else {
      parseAddress(st, FLAG_CROSS_FOLLOWS, addr, data);
    }
    data.strAddress = stripFieldStart(data.strAddress, "*EFD ");
    addr = getLeft();
    addr = stripFieldStart(addr, "BORO");
    addr = stripFieldStart(addr, "CITY");
    if (!data.strAddress.contains("&")) {
      if (addr.startsWith("No Cross Streets Found")) {
        addr =  addr.substring(22).trim();
      } else {
        Result res = parseAddress(StartType.START_PLACE, FLAG_ONLY_CROSS | FLAG_IGNORE_AT, addr);
        if (res.isValid()) {
          res.getData(data);
          addr = res.getLeft();

          match = APT_PTN.matcher(data.strPlace);
          if (match.matches()) {
            data.strApt = append(data.strApt, "-", match.group(1));
            data.strPlace = match.group(2);
          }
        }
      }
    }
    data.strSupp = addr;

    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN2 = Pattern.compile("(.*) -(\\d{2,3})");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      Matcher match =  CALL_CODE_PTN2.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCode = match.group(2);
      }
      super.parse(field,  data);
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
        data.strPlace = getLeft();
      }
      parseAddress(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE?";
    }
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
      "BEAR CREEK",
      "BEAR RUN",
      "BLUE SPRUCE",
      "BRIER HILL",
      "CAMBRIDGE SPRINGS",
      "CARRIAGE HILL",
      "CHERRY HILL",
      "CIDER MILL",
      "CLAIR WRIGHT",
      "COVINGTON VALLEY",
      "CROSS STATION",
      "EDGE PARK",
      "ELK CREEK",
      "ELK PARK",
      "FAIR OAKS",
      "FIELD VALLEY",
      "FOREST GLEN",
      "FOREST HOME",
      "FOX HOLLOW",
      "FRANKLIN CENTER",
      "GENEVA MARIE",
      "GIBSON HILL",
      "GLENWOOD PARK",
      "GOLF CLUB",
      "GREEN OAKS",
      "HALF MOON",
      "HANNA HALL",
      "HARLEY DAVIDSON",
      "HASKELL HILL",
      "HOPSON HILL",
      "IMPERIAL POINT",
      "JOHN WILLIAMS",
      "JUVA VALLEY",
      "KAHKWA CLUB",
      "KIMBALL HILL",
      "KINTER HILL",
      "LAKE FRONT",
      "LAKE PLEASANT",
      "LAKE SHORE",
      "LIFELINE BRIDGE",
      "LONE PINE",
      "MANCHESTER BEACH",
      "MANCHESTER FARMS",
      "MAPLE LAWN",
      "MCGAHEN HILL",
      "MILLER HILL",
      "MILLER STATION",
      "MOUNT PLEASANT",
      "NASH HILL",
      "NICKLE PLATE",
      "OAK TREE",
      "OLD RIDGE",
      "OLD STATE",
      "OLD WATTSBURG",
      "OLD ZUCK",
      "PARK S CREEK",
      "PEBBLE CREEK",
      "PENELEC PARK",
      "PIN OAK",
      "PINE LEAF",
      "PINE TREE",
      "PINE VALLEY",
      "RILEY SIDING",
      "SHERROD HILL",
      "SHORT HARE",
      "SHREVE RIDGE",
      "SPIRIT HILL",
      "SPRING LAKE",
      "SPRING VALLEY",
      "SPRUCE TREE",
      "STONE QUARRY",
      "SUNRISE LAKES",
      "TAYLOR RIDGE",
      "UNION AMITY",
      "UNION LEBOEUF",
      "VALLEY VIEW",
      "VAN CAMP",
      "VILLAGE COMMON",
      "WALNUT CREEK",
      "WASHINGTON TOWNE",
      "WOLF RUN VILLAGE",
      "WOLF RUN",
      "WOODLAND HILL",
      "YORKTOWN CENTER"
  };
}
