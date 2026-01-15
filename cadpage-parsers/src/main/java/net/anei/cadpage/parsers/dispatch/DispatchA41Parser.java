package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA41Parser extends FieldProgramParser {

  public static final int A41_FLG_NO_CALL = 1;
  public static final int  A41_FLG_ID = 2;

  private Pattern channelPattern;
  private Properties callCodes;

  public DispatchA41Parser(Properties cityCodes, String defCity, String defState, String channelPattern) {
    this(cityCodes, defCity, defState, channelPattern, 0, null);
  }

  public DispatchA41Parser(Properties cityCodes, String defCity, String defState, String channelPattern, int flags, Properties callCodes) {
    super(cityCodes, defCity, defState, calcProgram(flags));
    this.channelPattern = Pattern.compile(channelPattern);
    this.callCodes = callCodes;
  }

  private static String calcProgram(int flags) {
    StringBuilder sb = new StringBuilder();
    sb.append("( SELECT/XX UNIT CODE IDX ( PLACE ADDRCITY/Z CITY CH/Y | ADDRCITY/Z CITY CH/Y | PLACE ADDRCITY/Z CH | ADDRCITY/Z CH? ) INFO/N+? DATETIME! END " +
              "| CODE! ( IDX PLACE ADDRCITY CITY MAP! INFO/N+ Unit:UNIT! DATETIME! END " +
                      "| CH/Z IDX PLACE ADDRCITY CITY MAP! INFO/N+ Unit:UNIT! DATETIME! END " +
                      "| ");
    if ((flags & A41_FLG_ID) != 0) sb.append("ID ");
    else if ((flags & A41_FLG_NO_CALL) == 0) sb.append("CALL ");
    sb.append(          "( PLACE1 CITY/Z AT | ADDRCITY/Z ADDR2? ) CITY? ( CH/Z MAPPAGE! | EMPTY? " +
                            "( PLACE2 PLACE_APT2 X1 | PLACE2 PLACE_APT2 INT | PLACE2 X1 | PLACE2 INT | X1 | INT | ) EMPTY? " +
                            "( CH! | PLACE3+? PLACE_APT3 CH! ) ( MAP MAPPAGE | MAPPAGE | MAP MAP2? ) ) INFO/CS+? " +
                            "( ID1 GPS1 GPS2 ID2? | GPS1 GPS2 ) INFO/CS+ Unit:UNIT UNIT+ " +
                      ") " +
              ")");
    return sb.toString();
  }

  private static final Pattern TRAIL_DATE_TIME_PTN = Pattern.compile("(.*) +- From +(?:([A-Z][A-Z0-9]+) +)?(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d)$");
  private static final Pattern MARKER = Pattern.compile("\\b(?:DISPATCH|CALLALERT):");

  @Override
  protected boolean parseMsg(String body, Data data) {

    String[] flds = body.split("\n");
    if (flds.length >= 5) {
      setSelectValue("XX");
      return parseFields(flds, data);
    }

    setSelectValue("");
    boolean good = false;
    Matcher match = TRAIL_DATE_TIME_PTN.matcher(body);
    if (match.matches()) {
      good = true;
      body = match.group(1);
      data.strSource = match.group(2);
      data.strDate = match.group(3);
      data.strTime = match.group(4);
    } else {
      int pt = body.lastIndexOf(" - From");
      if (pt >= 0) {
        good = true;
        body = body.substring(0,pt).trim();
      }
    }

    match = MARKER.matcher(body);
    if (!match.find()) {
      if (!good) return false;
      setFieldList("INFO SRC DATE TIME");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body.trim();
      return true;
    }
    String info = stripFieldEnd(body.substring(0,match.start()).trim(), ",");
    body = body.substring(match.end()).trim();

    body = body.replace(" Units:", " Unit:");
    if (body.endsWith(",")) body = body + ' ';
    if (!parseFields(body.split(",+ ", -1), data)) return false;
    data.strSupp = append(info, "\n", data.strSupp);
    return true;
  }

  @Override
  public String getProgram() {
    return "INFO? " + super.getProgram() + " SRC DATE TIME";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new BaseCodeField();
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("ADDR2")) return new BaseAddress2Field();
    if (name.equals("CITY")) return new BaseCityField();
    if (name.equals("AT")) return new AddressField("at +(.*)", true);
    if (name.equals("X1")) return new CrossField("btwn *(.*)", true);
    if (name.equals("INT")) return new SkipField("[A-Z]* *<.*>", true);
    if (name.equals("PLACE1")) return new BasePlaceField(1);
    if (name.equals("PLACE2")) return new BasePlaceField(2);
    if (name.equals("PLACE3")) return new BasePlaceField(3);
    if (name.equals("PLACE_APT2")) return new BasePlaceAptField(2);
    if (name.equals("PLACE_APT3")) return new BasePlaceAptField(3);
    if (name.equals("CH")) return new BaseChannelField();
    if (name.equals("MAPPAGE")) return new SkipField("mappage,XXXX", true);
    if (name.equals("MAP2")) return new BaseMap2Field();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("ID")) return new IdField("[A-Z]{2}\\d{9}");
    if (name.equals("ID1")) return new IdField("\\d{10}", true);
    if (name.equals("ID2")) return new BaseId2Field();
    if (name.equals("IDX")) return new BaseIdXField();
    if (name.equals("GPS1")) return new BaseGPS1Field();
    if (name.equals("GPS2")) return new BaseGPS2Field();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    return super.getField(name);
  }

  private class BaseCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      if (callCodes != null) {
        data.strCall = convertCodes(field, callCodes);
      }
    }

    @Override
    public String getFieldNames() {
      return (callCodes != null ? "CODE CALL" : "CODE");
    }
  }

  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = -1;
      if (field.endsWith(")")) {
        pt = field.indexOf('(');
      } else if (field.endsWith("]")) {
        pt = field.indexOf('[');
      }
      if (pt >= 0) {
        data.strPlace = append(data.strPlace, " - ", field.substring(pt+1, field.length()-1).trim());
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY?";
    }
  }

  private static final Pattern ADDR_GPS_PTN = Pattern.compile("[-+]?\\d{1,3}\\.\\d{6,}");
  private class BaseAddress2Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!ADDR_GPS_PTN.matcher(field).matches()) return false;
      if (!ADDR_GPS_PTN.matcher(data.strAddress).matches()) return false;
      data.strAddress = data.strAddress + ',' + field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class BaseCityField extends CityField {

    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt < 0) pt = field.indexOf('<');
      if (pt >= 0) field = field.substring(0,pt).trim();
      return super.checkParse(field, data);
    }

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt < 0) pt = field.indexOf('<');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("# *(.*)|\\d+[A-Z]?");
  private class BasePlaceAptField extends BasePlaceField {

    public BasePlaceAptField(int placeType) {
      super(placeType);
    }

    @Override
    public void parse(String field, Data data) {

    // Last place token before the source field can be an apt
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        if (apt.equals(data.strApt)) return;
        if (data.strApt.length() == 0) {
          data.strApt = apt;
          return;
        }
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }

  private int lastPlaceType = 0;
  private class BasePlaceField extends PlaceField {

    private int placeType;

    public BasePlaceField(int placeType) {
      this.placeType = placeType;
    }
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith(",")) return;

      if (data.strPlace.length() == 0) lastPlaceType = 0;
      String sep = (placeType != lastPlaceType ? " - " : ", ");
      lastPlaceType = placeType;
      data.strPlace = append(data.strPlace, sep, field);
    }
  }

  private class BaseChannelField extends ChannelField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!channelPattern.matcher(field).matches()) return false;

      // Look ahead 2 fields to see if anything looks like a channel field
      // if it does, assume this is a false positive
      for (int off = 1; off<=2; off++) {
        String fld = getRelativeField(off);
        if (fld.length() > 0 && channelPattern.matcher(fld).matches()) return false;
      }
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
    }
  }

  private class BaseMap2Field extends MapField {
    public BaseMap2Field() {
      super("\\d{1,4}[A-Z]?|\\d{1,4}[A-Z]\\d [A-Z]\\d", true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strMap = append(field, "/", data.strMap);
    }
  }

  private static final String PROQA_DISPATCH = "Medical ProQA recommends dispatch at this time";
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith(PROQA_DISPATCH)) field = field.substring(PROQA_DISPATCH.length()).trim();
      else if (PROQA_DISPATCH.startsWith(field)) return;
      super.parse(field, data);
    }
  }

  private class BaseId2Field extends IdField {
    public BaseId2Field() {
      super("[A-Z]{2}\\d{9,10}", true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strCallId = append(field, "/", data.strCallId);
    }
  }

  private class BaseIdXField extends IdField {
    public BaseIdXField() {
      super("[A-Z]{2} #\\d+|#", true);
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace("#", "").trim();
      super.parse(field, data);
    }
  }

  private static final Pattern GPS1_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{5,}");
  private class BaseGPS1Field extends GPSField {
    public BaseGPS1Field() {
      super(1);
      setPattern(GPS1_PTN, true);
    }
  }

  private static final Pattern GPS2_PTN = Pattern.compile("([-+]?\\d{2,3}\\.\\d{5,}) *(.*)");
  private class BaseGPS2Field extends GPSField {
    public BaseGPS2Field() {
      super(2);
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS2_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1), data);
      data.strSupp = append(data.strSupp, "\n", match.group(2));
    }

    @Override
    public String getFieldNames() {
      return "GPS INFO?";
    }
  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2}) (\\d\\d:\\d\\d:\\d\\d)");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime =  match.group(4);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    return stripFieldStart(addr, "1-");
  }
}
