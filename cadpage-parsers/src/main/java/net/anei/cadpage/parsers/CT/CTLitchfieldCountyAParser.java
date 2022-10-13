package net.anei.cadpage.parsers.CT;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class CTLitchfieldCountyAParser extends FieldProgramParser {


  public CTLitchfieldCountyAParser() {
    super(CTLitchfieldCountyParser.CITY_LIST, "LITCHFIELD COUNTY", "CT",
          "ADDR ADDR_EXT? ( SELECT/1 ( APT3 CALL! CALL/CS+? CODE LOC ID ID/L X1 ( APT_GPS1 | APT3 GPS1L ) GPS2 TIME! " +
                                    "| PLACE ( EMPTY PLACE | CITY PLACE | PRI EMPTY | ) CALL/CS+? ( CALL_CODE | CALL/CS CODE ) TIME! ( JUNK | JUNK_ST SKIP+? JUNK_END! | APT3 | COVID_ALERT | EMPTY? ( GPS1 GPS2 ID? | ID ( ID/L | GPS1 GPS2 ID/L ) ) X1 ( APT_GPS1 GPS2 | APT3 ) ) " +
                                    ") " +
                         "| SELECT/2 CITY CALL/CS+? CALL_CODE ID! GPS1 GPS2 " +
                         "| APT3 PLACE CALL CALL/CS+? CODE ID ID/L X1 GPS1L GPS2! " +
                         ") END");
    addExtendedDirections();
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupProtectedNames(PROTECTED_STREET_LIST);
    setupSpecialStreets("EAST ST", "WEST RD");
    setupSaintNames("ONGE");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "@everbridge.net,@lcd911.com,88911,89361";
  }

  private static final Pattern HTML_MASK_PTN = Pattern.compile("\n +<p>(.*)</p>\n");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<!doctype html>\n")) {
      Matcher match = HTML_MASK_PTN.matcher(body);
      if (!match.find()) return false;
      body = match.group(1).trim();
    }
    if (!parseMsg(subject, body, data)) return false;
    if (data.strSource.equals("WINSTED AMB") && data.strCity.equalsIgnoreCase("WINCHESTER")) data.strCity = "WINSTED";
    return true;
  }

  private static final Pattern MASTER1 = Pattern.compile("(.*) RESPOND TO +(.*?)(?::|--| -)(\\d\\d:\\d\\d)\\b\\**(.*)");
  private static final Pattern MASTER2 = Pattern.compile("(.*) RESPOND TO +(.*)");

  private static final Pattern MASTER3 = Pattern.compile("(.+?)-(?!Dwelling)(.+)-(.*?) *\\*\\*\\* (\\d\\d:\\d\\d)---");
  private static final Pattern MAU_HILL = Pattern.compile("^(.*) MAUWEEHOO H(?:IL)?L (.*)$");
  private static final Pattern START_PAREN_PTN = Pattern.compile("^\\(.*?\\)");
  private static final Pattern TRAIL_TIME_PTN = Pattern.compile("(.*?) +:(\\d\\d:\\d\\d)\\**");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    body = body.replace('\n', ' ');
    Matcher match = MASTER1.matcher(body);
    if (match.matches()) {
      setSelectValue("1");
      data.strSource = match.group(1).trim();
      if (parseBody(match.group(2) + ',' + match.group(3) + ',' + match.group(4).replace(" CROSS STREET ", ", CS= "), data)) return true;;
    }

    else if ((match = MASTER2.matcher(body)).matches()) {
      setSelectValue("2");
      data.strSource = match.group(1).trim();
      if (parseBody(match.group(2), data)) return true;
    }

    else if ((match = MASTER3.matcher(body)).matches()) {
      setFieldList("CALL ADDR X ALERT PLACE APT CITY ST PLACE TIME");

      data.strCall = match.group(1).trim();
      parseAddressField(match.group(2).trim(), data);
      data.strPlace = append(data.strPlace, " - ", match.group(3).trim());
      data.strTime = match.group(4);
      return true;
    }

    else if (!subject.isEmpty() && body.startsWith(subject+',')) {

      setSelectValue("3");

      data.strSource = subject;
      body = body.substring(subject.length()+1).trim();

      int pt = body.indexOf("If you receive this alert in error");
      if (pt >= 0) body = body.substring(0,pt).trim();

      match = TRAIL_TIME_PTN.matcher(body);
      if (match.matches()) {
        body = match.group(1);
        data.strTime = match.group(2);
      }
      if (parseBody(body, data)) {

        // Intersections duplicate the address in the place field
        if (data.strAddress.contains("&") && data.strPlace.contains("&")) data.strPlace = "";
        return true;
      }
    }

    if (isPositiveId()) {
      setFieldList("CALL INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strCall = subject;
      data.strSupp = body;
      return true;
    }

    return false;
  }

  private boolean parseBody(String body, Data data) {
    String info = null;
    int pt = body.indexOf(" Hydrants:");
    if (pt >= 0) {
      info = body.substring(pt+10).trim();
      body = body.substring(0,pt).trim();
    }
    if (parseFields(body.split(","), data)) {

      if (info != null) data.strSupp = append(data.strSupp, "\n", info);

      if (!data.strCode.isEmpty()) {
        String call = CALL_CODES.getCodeDescription(data.strCode.toUpperCase().replace("-",  ""));
        if (call != null) {
          pt = data.strCall.lastIndexOf(" - ");
          if (pt >= 0) {
            data.strCall = call + data.strCall.substring(pt);
          } else {
            data.strCall = call;
          }
        }
      }
      return true;
    }
    data.initialize(this);
    return false;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram() + " INFO TIME";
  }

  private static final String CODE_PTN_STR = "(?i)\\d{1,3}-[A-Z]-\\d{1,2}(?:-?[A-Z])?|(?:\\d{1,2}-)?(?:HOT|ALPHA|COLD)|\\d+|";

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ADDR_EXT")) return new MyAddressExtField();
    if (name.equals("PLACE"))  return new MyPlaceField();
    if (name.equals("LOC")) return new PlaceField("LOC= *(.*)|", false);
    if (name.equals("CALL_CODE")) return new MyCallCodeField();
    if (name.equals("CODE")) return new CodeField(CODE_PTN_STR, true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("JUNK")) return new MyJunkField(true, true);
    if (name.equals("JUNK_ST")) return new MyJunkField(true, false);
    if (name.equals("JUNK_END")) return new MyJunkField(false, true);
    if (name.equals("ID")) return new IdField("[A-Z]?\\d{2}-\\d+|", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("GPS1L")) return new MyGPSField(1, true);
    if (name.equals("APT_GPS1")) return new MyAptGPS1Field();
    if (name.equals("X1")) return new MyCrossField();
    if (name.equals("APT3")) return new MyApt3Field();
    if (name.equals("COVID_ALERT")) return new AlertField("\\**(COVID ALERT)\\**");
    if (name.equals("PRI")) return new PriorityField("\\d");
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      parseAddressField(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT ALERT PLACE? X? CITY ST";
    }
  }

  /**
   * This field picks up the occasional framentary place/city combination
   * that got seperatd from the address field because it contained an
   * extraneous comma
   */
  private class MyAddressExtField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf("  ");
      if (pt < 0) return false;
      String place = field.substring(0,pt);
      String city = field.substring(pt+2).trim();
      if (!super.checkParse(city, data)) return false;
      data.strPlace = append(data.strPlace, ", ", place);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "PLACE CITY";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (ADDR_APT_PTN.matcher(field).matches()) {
        data.strApt = append(data.strApt, "-", field);
      } else if (!data.strPlace.contains(field)){
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*) (" + CODE_PTN_STR + ')');
  private class MyCallCodeField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCall = append(data.strCall, ", ", match.group(1).trim());
        data.strCode = match.group(2);
        return true;
      }

      // Check to see if this is a call that does  not have a accompanying code
      if (field.startsWith("W-")) {
        data.strCall = append(data.strCall, ", ", field);
        return true;
      }
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }


  private static final String GPS_PTN_STR = "[-+]?\\d{2,3}\\.\\d{4,}|0";
  private static final Pattern GPS_PTN = Pattern.compile(GPS_PTN_STR);
  private static final Pattern GPS_CALL_PTN = Pattern.compile("(" + GPS_PTN_STR + ")(?: +(.*))?");
  private class MyGPSField extends GPSField {

    private int type;
    private boolean label;

    public MyGPSField(int type) {
      this(type, false);
    }

    public MyGPSField(int type, boolean label) {
      super(type);
      this.type = type;
      this.label = label;
    }

    public boolean canFail() {
      return true;
    }

    public boolean checkParse(String field, Data data) {
      if (label) {
        if (!field.startsWith("GPS=")) return false;
        field = field.substring(4).trim();
      }
      if (type == 2) {
        Matcher match = GPS_CALL_PTN.matcher(field);
        if (!match.matches()) return false;
        field = match.group(1);
        String call = match.group(2);
        if (call != null) {
          if (call.equals("**COVID ALERT**")) {
            data.strAlert = "COVID ALERT";
          } else {
            data.strCall = append(data.strCall, " - ", call);
          }
        }
      } else {
        if (!GPS_PTN.matcher(field).matches()) return false;
      }
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return type == 2 ? "GPS ALERT? CALL?" : "GPS";
    }
  }

  private static final Pattern APT_GPS1_PTN = Pattern.compile("Apt (.*?) (" + GPS_PTN_STR + ")");
  private class MyAptGPS1Field extends GPSField {
    public MyAptGPS1Field() {
      super(1);
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = APT_GPS1_PTN.matcher(field);
      if (!match.matches()) return false;
      String apt = match.group(1).trim();
      if (!data.strApt.equals(apt)) {
        data.strApt = append(data.strApt, "-", apt);
      }
      super.parse(match.group(2), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "APT GPS";
    }
  }

  private static final Pattern STAGE_PTN = Pattern.compile("(\\*+STAGE\\*+) *(.*)");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("[A-Z]?\\d{1,4}|[A-Z]");

  private void parseAddressField(String sAddr, Data data) {
    Matcher match;
    parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, sAddr, data);
    String sPlace = stripFieldStart(getPadField(), "-");;


    // There is a street called MAUWEEHOO HILL (or HL) that just confuses
    // the heck out of the smart parser so we will make some special checks for it
    match = MAU_HILL.matcher(data.strAddress);
    if (match.find()) {
      data.strAddress = match.group(1) + " MAUWEEHOO HILL";
      sPlace = match.group(2);
    }

    // OK, there can be some strange things in the pad field
    // A leading term in parens is an alternative street name that should be appended to the
    // address.  It will be removed from the map address
    match = START_PAREN_PTN.matcher(sPlace);
    if (match.find()) {
      data.strAddress = append(data.strAddress, " ", match.group());
      sPlace = sPlace.substring(match.end()).trim();
    }

    // Looke for an alert indication
    match = STAGE_PTN.matcher(sPlace);
    if (match.matches()) {
      data.strAlert = match.group(1);
      sPlace = match.group(2);
    }

    // What's left is an optional place name followed by an optional cross
    // street, which may have an alternate street name in parens
    // So first remove the tentative alternate street name
    String chkCross = sPlace;
    String extra = null;
    if (chkCross.endsWith(")")) {
      int pt = chkCross.indexOf('(');
      if (pt >= 0) {
        extra = chkCross.substring(pt);
        chkCross = chkCross.substring(0,pt).trim();
      }
    }

    // Now use the smart addresss parser to separate the place name from the cross street
    Result res = parseAddress(StartType.START_OTHER, FLAG_ONLY_CROSS | FLAG_ANCHOR_END, chkCross);
    if (res.isValid()) {
      res.getData(data);
      sPlace = res.getStart();
    } else {
      sPlace = chkCross;
    }

    sPlace = stripFieldEnd(sPlace, "&");

    // Append the extra paren info the the cross street if we found one
    // or to the place name if we did not
    if (extra != null) {
      if (data.strCross.length() > 0) {
        data.strCross = append(data.strCross, " ", extra);
      } else {
        sPlace = append(sPlace, " ", extra);
      }
    }

    // If the original address and cross street are both single streets,
    // combine them into an intersection
    if (getStatus() == STATUS_STREET_NAME &&  res.getStatus() == STATUS_STREET_NAME) {
      data.strAddress = data.strAddress + " & " + data.strCross;
      data.strCross = "";
    }

    if (ADDR_APT_PTN.matcher(sPlace).matches()) {
      data.strApt = append(data.strApt, "-", sPlace);
    } else {
      data.strPlace = sPlace;
    }

    CTLitchfieldCountyParser.fixCity(data);
  }

  private class MyCrossField extends CrossField {
    public MyCrossField() {
      super("CS= *(.*)", true);
    }

    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "&");
      super.parse(field,  data);
    }
  }

  private class MyApt3Field extends AptField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("Apt")) return false;
      field = field.substring(3).trim();
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  /**
   * This class skips over are parenthesis enclosed block of meaningless data.  Because
   * the information block may include commas, it can be split into multiple fields.
   */
  private class MyJunkField extends SkipField {

    boolean start, end;

    public MyJunkField(boolean start, boolean end) {
      this.start = start;
      this.end = end;
    }

    @Override
    public boolean canFail() {
      return true;
    }

    public boolean checkParse(String field, Data data) {
      if (start && !field.startsWith("(")) return false;
      if (end && !field.endsWith(")")) return false;
      int parenCnt = 0;
      for (char chr : field.toCharArray()) {
        if (chr == '(') parenCnt++;
        else if (chr == ')') parenCnt--;
      }
      if (start && end) return parenCnt == 0;
      if (start) return parenCnt > 0;
      if (end) return parenCnt < 0;
      return false;
    }
  }

  private static final String[] PROTECTED_STREET_LIST = new String[]{
    "EAST CENTER",
    "EAST FARMS",
    "EAST LAKE",
    "EAST PEARL",
    "EAST VIEW",
    "NORTH MAIN",
    "SOUTH BEACH PLAIN",
    "SOUTH CHAPEL",
    "SOUTH MAIN",
    "SOUTH MAPLE",
    "WEST GRANBY",
    "WEST HILL",
    "WEST LAKE"
  };

  private static final String[] MWORD_STREET_LIST = new String[]{
    "ABOVE ALL",
    "ALAIN WHITE",
    "ALVORD PARK",
    "BALD MOUNTAIN",
    "BALDWIN HILL",
    "BEAVER BOG",
    "BEECH HILL",
    "BEL AIR",
    "BIRCH HILL",
    "BLACK BRIDGE",
    "BLUEBERRY HILL",
    "BOTTSFORD HILL",
    "BRANDY HILL",
    "BREEZY HILL",
    "BULLS BRIDGE",
    "BUNKER HILL",
    "BURR MOUNTAIN",
    "BURT HILL",
    "CAMPS FLAT",
    "CAMPVILLE HILL",
    "CANDLEWOOD MOUNTAIN",
    "CARLSON RIDGE",
    "CARMEL HILL",
    "CARMEN HILL",
    "CEDAR HILL",
    "CEDAR RIDGE",
    "CHAPEL HILL",
    "CHESTNUT HILL",
    "CHESTNUT LAND",
    "CHIMNEY HILL",
    "CHURCH HILL",
    "CLATTER VALLEY",
    "COBB CITY",
    "COLEBROOK RIVER",
    "COLLEGE FARMS",
    "COLONIAL RIDGE",
    "COTTON HILL",
    "COUNTY LINE",
    "CROOKED FURROWS",
    "DAISY HILL",
    "DANBURY QTR",
    "DISH MILL",
    "DODGE FARM",
    "DUTTON HILL",
    "EABOW BROOK",
    "EAST CENTER",
    "EAST FARMS",
    "EAST HILL",
    "EAST LAKE",
    "EAST PEARL",
    "EAST VIEW",
    "ENO HILL",
    "FARMINGTON RIVER",
    "FLAGG HILL",
    "FORGE HILL",
    "FOX HUNT",
    "FULLER MOUNTAIN",
    "GEORGE WASHINGTON",
    "GLEN AYRE",
    "GOLDEN HARVEST",
    "GOOSE GREEN",
    "GOSHEN EAST",
    "GREAT BROOK",
    "HALL MEADOW",
    "HARBOR VIEW",
    "HARD HILL",
    "HARMONY HILL",
    "HARWINTON HEIGHTS",
    "HAYDEN HILL",
    "HENRY SANFORD",
    "HIGH VIEW",
    "HILL SIDE",
    "HODGES HILL",
    "HOLCOMB HILL",
    "HOLIDAY POINT",
    "HOSPITAL HILL",
    "HOUSATONIC RIVER",
    "HUT HILL",
    "INDIAN FIELD",
    "INDIAN HILL",
    "INDIAN MEADOW",
    "INDUSTRIAL PARK",
    "JACK CORNER",
    "JUDDS BRIDGE",
    "KENT HOLLOW",
    "KNIFE SHOP",
    "LAKE HARWINTON",
    "LAKE SHORE",
    "LAURELWOOD POND",
    "LEACH HOLLOW",
    "LEAD MINE BROOK",
    "LILLINONAH LAKE",
    "LILY POND",
    "LIME ROCK",
    "LIVERY POOL",
    "LOCUST HILL",
    "LONG MOUNTAIN",
    "LOON MEADOW",
    "MACEDONIA BROOK",
    "MANGNOLIA HILL",
    "MAPLE HOLLOW",
    "MARSHALL LAKE",
    "MEADOW VIEW",
    "MIDDLE SCHOOL",
    "MILL POND",
    "MILO COE",
    "MINE HILL",
    "MIST HILL",
    "MOUNT TOM",
    "MOUNTAIN VIEW",
    "NETTLETON HOLLOW",
    "NORTH MAIN",
    "NORTH MOUTAIN",
    "OAK MEADOW",
    "ORCHARD HILL",
    "ORCHARD REST",
    "PAINTER HILL",
    "PAPER MILL",
    "PARK LANE",
    "PARKER HILL",
    "PHEASANT RUN",
    "PICKETT DISTRICT",
    "PIE HILL",
    "PINE HILL",
    "PINE RIDGE",
    "POND NORTH",
    "POPPLE SWAMP",
    "PROCK HILL",
    "PROSPECT HILL",
    "QUAKER RIDGE",
    "RAMS GATE",
    "RATLUM MOUNTAIN",
    "RIDGE VIEW",
    "ROBERT LEATHER",
    "ROBIN HILL",
    "ROCK CREEK",
    "ROCK HALL",
    "RUDD POND",
    "SAIL HARBOUR",
    "SANDY BROOK",
    "SATANS KINGDOM",
    "SAW HORESE",
    "SAW MILL HILL",
    "SAWYER HILL",
    "SCOVILLE HILL",
    "SECOND HILL",
    "SELLECK HILL",
    "SHARON STATION",
    "SHEFFIELD HILL",
    "SHERMAN HILL",
    "SHERWOOD HILL",
    "SHINGLE MILL",
    "SHINGLE MILL",
    "SILVER FOX",
    "SKIFF MOUNTAIN",
    "SKYLINE RIDGE",
    "SMITH HILL",
    "SOUTH BEACH PLAIN",
    "SOUTH CHAPEL",
    "SOUTH MAIN",
    "SOUTH MAPLE",
    "SPENCER BROOK",
    "SPENCER HILL",
    "SPRING HILL",
    "SPRING LAKE",
    "STILL RIVER",
    "STONE GATE",
    "STRAITS TURNPIKE",
    "STUB HOLLOW",
    "SUCKER BROOK",
    "SULLIVAN FARM",
    "SUNNY VALLEY",
    "SUNSET HILL",
    "SWIMING HOLE",
    "SWIMMING HOLE",
    "TANNER HILL",
    "TANNERY BROOK",
    "TECHNOLOGY PARK",
    "TODD HILL",
    "TORRINGFORD WEST",
    "TOWER HILL",
    "TOWN FARM",
    "TOWN HALL",
    "TOWN HILL",
    "TOWN LINE",
    "TWIN LAKES",
    "WAGON WHEEL",
    "WAKE ROBIN",
    "WAKEMAN HILL",
    "WALLENS HILL",
    "WELLERS BRIDGE",
    "WELLS HILL",
    "WEST GRANBY",
    "WEST HILL",
    "WEST HILL",
    "WEST LAKE",
    "WEST MEETINGHOUSE",
    "WEST PEARL",
    "WHISKER HOLLOW",
    "WHITCOMB HILL",
    "WHITE OAK",
    "WHITE PINE",
    "WILDCAT HILL",
    "WILLOW BROOK",
    "WILSON POND",
    "WISHING WILL",
    "WORDEN POINT"

  };

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "80 CHESTNUT ST",                       "+41.923967,-73.065305",
      "936 MAIN ST",                          "+41.930890,-73.080258",
      "80 S MAIN ST",                         "+41.917116,-73.056438"

  });


  private static final CodeTable CALL_CODES = new StandardCodeTable();
}
