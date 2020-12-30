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
          "ADDR ADDR_EXT? ( SELECT/1 PLACE ( EMPTY PLACE | CITY PLACE | ) CALL/CS+? ( CALL_CODE | CALL/CS CODE ) TIME! ( JUNK | JUNK1 SKIP+? JUNK2! | ( EMPTY ID ID/L | ID? GPS1 GPS2 ID/L ) X1 APT1 ) " +
                         "| CITY CALL/CS+? CALL_CODE ID! GPS1 GPS2 " +
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

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
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
    int pt = data.strApt.indexOf("**COVID ALERT**");
    if (pt >= 0) {
      data.strAlert = "COVID ALERT";
      data.strApt = append(data.strApt.substring(0,pt).trim(), "-", data.strApt.substring(pt+15).trim());
    }
    return true;
  }

  private static final Pattern MASTER1 = Pattern.compile("(.*) RESPOND TO +(.*?)(?::|--| -)(\\d\\d:\\d\\d)\\b\\**(.*)");
  private static final Pattern MASTER2 = Pattern.compile("(.*) RESPOND TO +(.*)");

  private static final Pattern MASTER3 = Pattern.compile("(.+?)-(?!Dwelling)(.+)-(.*?) *\\*\\*\\* (\\d\\d:\\d\\d)---");
  private static final Pattern MAU_HILL = Pattern.compile("^(.*) MAUWEEHOO H(?:IL)?L (.*)$");
  private static final Pattern START_PAREN_PTN = Pattern.compile("^\\(.*?\\)");

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
      setFieldList("CALL ADDR X PLACE APT CITY ST PLACE TIME");

      data.strCall = match.group(1).trim();
      parseAddressField(match.group(2).trim(), data);
      data.strPlace = append(data.strPlace, " - ", match.group(3).trim());
      data.strTime = match.group(4);
      return true;
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
        String call = CALL_CODES.getCodeDescription(data.strCode.replace("-",  ""));
        if (call != null) data.strCall = call;
      }
      return true;
    }
    data.initialize(this);
    return false;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram().replace("APT", "APT ALERT") + " INFO";
  }

  private static final String CODE_PTN_STR = "\\d{1,3}-[A-Z]-\\d{1,2}(?:-?[A-Z])?|(?:\\d{1,2}-)?(?:HOT|ALPHA|COLD)|\\d+|";

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ADDR_EXT")) return new MyAddressExtField();
    if (name.equals("PLACE"))  return new MyPlaceField();
    if (name.equals("CALL_CODE")) return new MyCallCodeField();
    if (name.equals("CODE")) return new CodeField(CODE_PTN_STR, true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("JUNK")) return new SkipField("\\(.*\\)", true);
    if (name.equals("JUNK1")) return new SkipField("\\(.*", true);
    if (name.equals("JUNK2")) return new SkipField(".*\\)", true);
    if (name.equals("ID")) return new IdField("[A-Z]?\\d{2}-\\d+|", true);
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("X1")) return new CrossField("CS= *(.*)", true);
    if (name.equals("APT1")) return new MyApt1Field();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      parseAddressField(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE? X? CITY ST";
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

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{4,}|0");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type, GPS_PTN, true);
    }
  }

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

  private static final Pattern APT_CALL_PTN = Pattern.compile("(.*?) *((?:1st |2nd |3rd )?Call in Town)", Pattern.CASE_INSENSITIVE);
  private class MyApt1Field extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("Apt")) abort();
      field = field.substring(3).trim();
      Matcher match =  APT_CALL_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCall = append(data.strCall, " - ", match.group(2));
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "APT CALL";
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
    "BURR MOUNTAIN",
    "BURT HILL",
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
    "CHURCH HILL",
    "CLATTER VALLEY",
    "COLEBROOK RIVER",
    "COLLEGE FARMS",
    "COLONIAL RIDGE",
    "COTTON HILL",
    "COUNTY LINE",
    "CROOKED FURROWS",
    "DAISY HILL",
    "DISH MILL",
    "DUTTON HILL",
    "EABOW BROOK",
    "EAST CENTER",
    "EAST FARMS",
    "EAST LAKE",
    "EAST PEARL",
    "EAST VIEW",
    "ENO HILL",
    "FARMINGTON RIVER",
    "FLAGG HILL",
    "FORGE HILL",
    "GLEN AYRE",
    "GOOSE GREEN",
    "GOSHEN EAST",
    "GREAT BROOK",
    "HALL MEADOW",
    "HARD HILL",
    "HARMONY HILL",
    "HARWINTON HEIGHTS",
    "HAYDEN HILL",
    "HENRY SANFORD",
    "HIGH VIEW",
    "HILL SIDE",
    "HODGES HILL",
    "HOLCOMB HILL",
    "HOSPITAL HILL",
    "HUT HILL",
    "INDIAN FIELD",
    "INDIAN MEADOW",
    "INDUSTRIAL PARK",
    "JACK CORNER",
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
    "MANGNOLIA HILL",
    "MAPLE HOLLOW",
    "MARSHALL LAKE",
    "MEADOW VIEW",
    "MIDDLE SCHOOL",
    "MILO COE",
    "MINE HILL",
    "MIST HILL",
    "MOUNT TOM",
    "MOUNTAIN VIEW",
    "NETTLETON HOLLOW",
    "NORTH MAIN",
    "OAK MEADOW",
    "ORCHARD REST",
    "PAPER MILL",
    "PARK LANE",
    "PARKER HILL",
    "PICKETT DISTRICT",
    "PINE RIDGE",
    "POPPLE SWAMP",
    "PROSPECT HILL",
    "QUAKER RIDGE",
    "RAMS GATE",
    "RATLUM MOUNTAIN",
    "RIDGE VIEW",
    "ROBERT LEATHER",
    "ROBIN HILL",
    "ROCK HALL",
    "RUDD POND",
    "SANDY BROOK",
    "SATANS KINGDOM",
    "SAW MILL HILL",
    "SAWYER HILL",
    "SCOVILLE HILL",
    "SELLECK HILL",
    "SHARON STATION",
    "SHEFFIELD HILL",
    "SHERMAN HILL",
    "SHERWOOD HILL",
    "SHINGLE MILL",
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
    "SULLIVAN FARM",
    "SUNNY VALLEY",
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
    "WAGON WHEEL",
    "WAKE ROBIN",
    "WAKEMAN HILL",
    "WALLENS HILL",
    "WELLERS BRIDGE",
    "WELLS HILL",
    "WEST GRANBY",
    "WEST HILL",
    "WEST LAKE",
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
