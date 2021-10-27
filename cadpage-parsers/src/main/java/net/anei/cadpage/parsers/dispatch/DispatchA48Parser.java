package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Base parser for centers using Countryside software
 */
public class DispatchA48Parser extends FieldProgramParser {

  /**
   * Flag indicating the call description is a single word code
   */
  public static final int A48_ONE_WORD_CODE =     0x01;

  /**
   * Flag indicating the call description may be a single word code
   */
  public static final int A48_OPT_CODE = 0x02;

  /**
   * Flag indicating the call description may be a single word code
   */
  public static final int A48_OPT_ONE_WORD_CODE = A48_OPT_CODE | A48_ONE_WORD_CODE;

  /**
   * Flag indicating there is no call code.  Just a call description
   */
  public static final int A48_NO_CODE =           0x04;

  private static final Pattern GPS_PTN = Pattern.compile(" *([-+]?\\b\\d{2,3}\\.\\d{4,} +[-+]?\\d{2,3}\\.\\d{4,})\\b *");
  private static final Pattern PHONE_PTN = Pattern.compile("(?:(\\d{3}-\\d{3}-\\d{4})\\b|- -) *");

  /**
   * Enum parameter indicating what kind of information comes between the
   * address and the unit headings.
   */

  public enum FieldType {
    NONE("", "") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        if (field.length() > 0) {
          data.strSupp = append(field, "\n", data.strSupp);
        }
      }
    },

    NAME("NAME", "NAME") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        setNameField(field, data);
      }
    },

    MAP("MAP", "MAP") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        data.strMap = field;
      }
    },

    X("X/Z+?", "X") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        parser.parseCrossStreet(false, false,  false, field, data);
      }
    },

    X_NAME("X_NAME/Z+?", "X NAME") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        parser.parseCrossStreet(false, false, true, field, data);
      }
    },

    PLACE("PLACE? APT?", "PLACE APT") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {

        if (field.startsWith("/")) {
          data.strAddress = append(data.strAddress, " & ", field.substring(1).trim());
        } else {
          Parser p = new Parser(field);
          String apt = p.getLast(' ');
          if (APT_PTN.matcher(apt).matches()) {
            data.strApt = append(data.strApt, "-", apt);
            field = p.get();
          }
          data.strPlace = field;
        }
      }
    },

    PLACE_X("PLACE? X+?", "PLACE X") {   // Not currently supported for field delimited format
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        parser.parseCrossStreet(false, true, false, field, data);
      }
    },

    GPS_PLACE_X("GPS? ( X X+? | PLACE  X+? | ) INFO/Z+?", "GPS PLACE X") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        parser.parseCrossStreet(true, true, false, field, data);
      }

      @Override
      public int find(String field) {
        Matcher match = GPS_PTN.matcher(field);
        if (match.find()) return match.start();
        return -1;
      }
    },

    GPS_PHONE_NAME("GPS? PHONE? NAME/Z?", "GPS PHONE NAME") {

      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        Matcher match = GPS_PTN.matcher(field);
        if (match.lookingAt()) {
          parser.setGPSLoc(match.group(1), data);
          field = field.substring(match.end());
        }
        field = stripFieldStart(field, "1-");
        match = PHONE_PTN.matcher(field);
        if (match.lookingAt()) {
          data.strPhone = getOptGroup(match.group(1));
          field = field.substring(match.end());
        }
        data.strName = field;
      }

    },

    TRASH("SKIP", "") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {}
    },

    INFO("INFO", "INFO") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {}
    };


    private String fieldProg, fieldList;
    private FieldType(String fieldProg, String fieldList) {
      this.fieldProg = fieldProg;
      this.fieldList = fieldList;
    }

    public String getFieldProg() {
      return fieldProg;
    }

    public String getFieldList() {
      return fieldList;
    }

    public boolean isDeferredDecision() {
      return fieldProg.endsWith("?") && fieldProg.contains("/Z");
    }

    public abstract void parse(DispatchA48Parser parser, String field, Data data);

    public int find(String field) {
      return -1;
    }
  };

  private FieldType fieldType;
  private boolean oneWordCode;
  private boolean optCode;
  private boolean noCode;
  private Properties callCodes;
  private Pattern unitPtn;
  private String fieldList;

  public DispatchA48Parser(String[] cityList, String defCity, String defState, FieldType fieldType) {
    this(cityList, defCity, defState, fieldType, 0, null, null);
  }
  public DispatchA48Parser(String[] cityList, String defCity, String defState, FieldType fieldType, int flags) {
    this(cityList, defCity, defState, fieldType, flags, null, null);
  }

  public DispatchA48Parser(String[] cityList, String defCity, String defState, FieldType fieldType, int flags, Properties callCodes) {
    this(cityList, defCity, defState, fieldType, flags, null, callCodes);
  }

  public DispatchA48Parser(String[] cityList, String defCity, String defState, FieldType fieldType, Pattern unitPtn) {
    this(cityList, defCity, defState,fieldType, 0, unitPtn, null);
  }

  public DispatchA48Parser(String[] cityList, String defCity, String defState, FieldType fieldType, int flags, Pattern unitPtn) {
    this(cityList, defCity, defState,fieldType, flags, unitPtn, null);
  }

  public DispatchA48Parser(String[] cityList, String defCity, String defState, FieldType fieldType, int flags, Pattern unitPtn, Properties callCodes) {
    super(cityList, defCity, defState,
          append("DATETIME ID CALL ADDRCITY! DUPADDR? SKIPCITY?", " ", fieldType.getFieldProg()) + " ( INFO INFO/ZN+? UNIT_LABEL | UNIT_LABEL " + (fieldType.isDeferredDecision() ? "" : "| ") + ") UNIT/S+");
    this.fieldType = fieldType;
    oneWordCode = (flags & A48_ONE_WORD_CODE) != 0;
    optCode = (flags & A48_OPT_CODE) != 0;
    noCode = (flags & A48_NO_CODE) != 0;
    this.unitPtn = unitPtn;
    this.callCodes = callCodes;
    fieldList = ("DATE TIME ID CODE CALL ADDR X? APT PLACE? CITY NAME " + fieldType.getFieldList() + " UNIT INFO").replace("  ", " ");
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("As of \\d\\d?/\\d\\d?/\\d\\d \\d\\d(:\\d\\d:\\d\\d( [AP]M)?)?");
  private static final Pattern PREFIX_PTN = Pattern.compile("(?!\\d\\d:)([- A-Za-z0-9]+: *)(.*)");
  private static final Pattern TRUNC_HEADER_PTN = Pattern.compile("\\d\\d:\\d\\d \\d{4}-\\d{8} ");
  private static final Pattern TRUNC_HEADER_PTN2 = Pattern.compile(": \\d{4}-\\d{8}(\\s)");
  private static final Pattern MASTER_PTN = Pattern.compile("(?:CAD:[-_A-Za-z0-9]* |[- A-Za-z0-9]*:)? *As of (\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d?:\\d\\d:\\d\\d) (?:([AP]M) )? *(\\d{4}-\\d{5,8}) (.*)");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile("(.*?)[ ,]+([-\\w]+)");
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\b(\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d?:\\d\\d:\\d\\d)(?: ([AP]M))?\\b");
  private static final Pattern DATE_TIME_UNIT_MARK_PTN = Pattern.compile("(.*?)(?: \\d\\d?/\\d\\d?/\\d\\d)? Date/Time.*");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static final Pattern TRAIL_DATE_PTN = Pattern.compile("(.*) \\d\\d/\\d\\d/\\d\\d");
  private static final Pattern UNIT_DELIM_PTN = Pattern.compile("[ ,]+");

  private Set<String> crossSet;
  private Set<String> unitSet;

  private static class Flag {
    boolean flag;
    private Flag(boolean flag) { this.flag = flag; }
    boolean getFlag() { return flag; }
    void setFlag(boolean flag) { this.flag = flag; }
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      if (match.group(1) != null) {
        if (!body.startsWith("As of ") && !body.contains(":As of ")) {
          match = ID_PTN.matcher(body);
          if (!match.lookingAt()) return false;
          if (body.length() <= match.end()) return false;
          char delim = body.charAt(match.end());
          body = subject + delim + body;
        }
        subject = "";
      }
      else {
        match = PREFIX_PTN.matcher(body);
        if (match.matches()) {
          String tmp = match.group(2);
          if (!tmp.startsWith("As of")) {
            body = match.group(1) + subject + ':' + tmp;
          }
        } else if (!body.startsWith(":As of")) {
          body = subject + ':' + body;
        }
        subject = "";
      }
    }

    // Handle case where subject was split off from main message and then discarded
    else if (subject.length() == 0 && TRUNC_HEADER_PTN.matcher(body).lookingAt()) {
      body = "As of 99/99/99 99:" + body;
    }

    // Another variation on same theme
    else if (subject.length() == 0 && (match = TRUNC_HEADER_PTN2.matcher(body)).lookingAt()) {
      body = "As of 99/99/99 99:99:99" + match.group(1) + body.substring(2);
    }

    if (!subject.startsWith("As of") && !subject.equals("ALERT MESSAGE") && !subject.equals("Text Message")) data.strSource = subject;

    // Check for the new newline delimited format
    crossSet = new HashSet<String>();
    unitSet = new HashSet<String>();
    String flds[] = body.split("\n");
    if (flds.length < 4) flds = body.split(";");
    if (flds.length >= 4) {
      return parseFields(flds, data);
    }

    // No such luck, have to do this the old way
    match = MASTER_PTN.matcher(body);
    if (!match.matches()) return false;
    setFieldList(fieldList);
    parseDateTime(match.group(1), match.group(2), match.group(3), data);
    data.strCallId = match.group(4);
    String addr = match.group(5).trim();

    boolean first = true;
    boolean unitMark = false;
    Flag unitFound = new Flag(false);
    for (String part : DATE_TIME_PTN.split(addr)) {
      part = part.trim();

      if (unitMark) {
        int pt = part.indexOf(' ');
        if (pt >= 0) part = part.substring(0,pt);
        addUnit(part, data);
        continue;
      }

      if (fieldType == FieldType.INFO && data.strUnit.length() == 0) {
        part = parseUnitInfo(part, data, unitFound);
      }

      match = DATE_TIME_UNIT_MARK_PTN.matcher(part);
      unitMark = match.matches();
      if (unitMark) part = match.group(1).trim();

      if (first) {
        first = false;
        addr = part;
      } else {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }

    if (fieldType != FieldType.INFO) addr = parseUnitInfo(addr, data, unitFound);

    Parser p = new Parser(fixCallAddress(addr));

    StartType st = StartType.START_CALL;
    int flags = FLAG_START_FLD_REQ;

    if (!noCode) {
      if (optCode || !oneWordCode) data.strCode = p.getOptional(" - ");
      if (data.strCode.length() == 0) {
        if (!optCode && !oneWordCode) return false;
        if (oneWordCode) {
          st = StartType.START_ADDR;
          flags = 0;
          String code  = p.get(' ');
          if (callCodes != null) {
            data.strCode = code;
            data.strCall = convertCodes(code, callCodes);
          } else {
            data.strCall = code;
          }
        }
      }
    }

    addr = p.get();

    // If we didn't find a unit, try a couple backup routines
    if (unitPtn != null) {
      if (!unitFound.getFlag()) {
        while (true) {
          match = TRAIL_UNIT_PTN.matcher(addr);
          if (!match.matches()) break;
          String unit = match.group(2);
          if (!unitPtn.matcher(unit).matches()) break;
          addr = match.group(1);
          data.strUnit = append(unit, " ", data.strUnit);
        }
      }

      if (data.strUnit.isEmpty()) {
        if (data.strSupp.length() > 0 && !data.strSupp.contains("\n")) {
          boolean goodUnit = true;
          String[] parts = UNIT_DELIM_PTN.split(data.strSupp);
          for (String part : parts) {
            if (!unitPtn.matcher(part).matches()) {
              goodUnit = false;
              break;
            }
          }
          if (goodUnit) {
            for (String part : parts) addUnit(part, data);
            data.strSupp = "";
          }
        }
      }
    }
    if (addr.length() == 0) return false;

    boolean addressParsed = false;
    String extra = null;

    if (fieldType == FieldType.NONE || fieldType == FieldType.INFO) flags |= FLAG_ANCHOR_END;
    flags |= getExtraParseAddressFlags();

    addr = cleanWirelessCarrier(addr, true);


    int pt2 = addr.indexOf(',');
    while (pt2 >= 0) {
      int pt = pt2;

      // Check for duplicated address and city
      String addr1 = addr.substring(0,pt).trim();
      pt2 = addr.indexOf(',', pt+1);
      if (pt2 >= 0) {
        String addr2 = addr.substring(pt+1, pt2).trim();
        int ix = findMatch(addr1, addr2);
        if (ix == 0 || addr.substring(pt2+1).trim().startsWith(addr2.substring(0, ix))) pt = pt2;
      }
      int cityFlags = FLAG_ONLY_CITY;
      flags |= getExtraParseAddressFlags();
      parseAddress(StartType.START_ADDR, cityFlags, addr.substring(pt+1).trim(), data);
      if (data.strCity.length() > 0) {
        extra = stripFieldStart(getLeft(), data.strCity);

        addressParsed = true;
        addr = addr1.trim().replace('@', '&');
        if (st == StartType.START_ADDR){
          parseAddress(addr, data);
        } else {
          parseAddress(st, flags | FLAG_NO_CITY | FLAG_ANCHOR_END, addr, data);
        }
        break;
      }
    }

    if (!addressParsed) {
      int pt = fieldType.find(addr);
      if (pt >= 0) {
        extra = addr.substring(pt).trim();
        addr = addr.substring(0,pt).trim();
        flags |= FLAG_ANCHOR_END;
      }
      addr = addr.replace('@', '&');
      parseAddress(st, flags, addr, data);
      if (extra == null) extra = getLeft();
    }
    primeCrossStreets(data.strAddress);

    match = TRAIL_DATE_PTN.matcher(extra);
    if (match.matches()) extra = match.group(1).trim();

    fieldType.parse(this, extra, data);

    if (data.strCall.equals(data.strCode)) data.strCode = "";
    return true;
  }

  private void parseDateTime(String date, String time, String time_qual, Data data) {

    if (!date.startsWith("99/")) data.strDate = date;

    if (!time.startsWith("99:")) {
      if (time_qual != null) {
        int hour = Integer.parseInt(time.substring(0, time.indexOf(':')));
        if (hour >= 13) {
          data.strTime = time;
        } else {
          setTime(TIME_FMT, time + ' ' + time_qual, data);
        }
      } else {
        data.strTime = time;
      }
    }
  }

  private String parseUnitInfo(String field, Data data, Flag unitFound) {
    int pt = field.lastIndexOf(" Unit Org Name Area Types ");
    if (pt < 0) {
      pt = field.lastIndexOf(" Unit");
      if (pt >= 0) {
        if (" Unit Org Name Area Types ".startsWith(field.substring(pt))) {
          field = field.substring(0,pt).trim();
        }
      }
      return field;
    }

    String unitInfo = field.substring(pt+26).trim();
    field = field.substring(0,pt).trim();

    if (unitPtn == null) {
      if (!unitInfo.isEmpty()) unitFound.setFlag(true);;
      pt = unitInfo.indexOf(' ');
      if (pt >= 0) unitInfo = unitInfo.substring(0,pt);
      data.strUnit = unitInfo;
    } else if (unitInfo.length() > 0) {
      for (String unit : unitInfo.split(" +")) {
        if (unitPtn.matcher(unit).matches()) {
          unitFound.setFlag(true);
          addUnit(unit, data);
        }
      }
    }

    return field;
  }

  private int findMatch(String field1, String field2) {
    int len1 = field1.length();
    int len2 = field2.length();
    int limit = Math.min(len1, len2);
    int ix;
    for (ix = 1; ix <= limit; ix++) {
      if (field1.charAt(len1-ix) != field2.charAt(len2-ix)) break;
    }
    return len2-ix+1;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final Pattern ID_PTN = Pattern.compile("\\d{4}-?\\d{8}\\b");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("ID")) return new IdField(ID_PTN, true);
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("ADDRCITIY")) return new BaseAddressCityField();
    if (name.equals("DUPADDR")) return new BaseDupAddrField();
    if (name.equals("SKIPCITY")) return new BaseSkipCityField();
    if (name.equals("GPS")) return new BaseGPSField();
    if (name.equals("X_NAME")) return new BaseCrossNameField();
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("PHONE")) return new BasePhoneField();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("UNIT_LABEL")) return new BaseUnitLabelField();
    if (name.equals("UNIT")) return new BaseUnitField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN2 = Pattern.compile("(?:CAD:|[-_ A-Za-z0-9]*:)? *As of (\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d?:\\d\\d:\\d\\d)(?: ([AP]M))?");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN2.matcher(field);
      if (!match.matches()) abort();
      parseDateTime(match.group(1), match.group(2), match.group(3), data);
    }
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = fixCallAddress(field);
      if (noCode) {
        data.strCall = field;
        return;
      }
      if (optCode || !oneWordCode) {
        int pt = field.indexOf(" - ");
        if (pt >= 0) {
          data.strCode = field.substring(0,pt).trim();
          data.strCall = field.substring(pt+3).trim();
          return;
        }
      }

      if (!optCode && !oneWordCode) abort();
      if (callCodes != null) {
        data.strCode = field;
        data.strCall = convertCodes(field, callCodes);
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      primeCrossStreets(data.strAddress);
    }
  }

  private class BaseDupAddrField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return field.equals(getRelativeField(-1));
    }
  }

  private class BaseSkipCityField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      if (field.equalsIgnoreCase(data.strCity)) return true;
      return false;
    }
  }

  private class BaseGPSField extends GPSField {
    public BaseGPSField() {
      setPattern(GPS_PTN, true);
    }
  }

  private class BaseCrossNameField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (data.strName.length() > 0) {
        addCrossStreet(data.strName, data);
        data.strName = "";
      }
      if (isValidCrossStreet(field)) {
        addCrossStreet(field, data);
      } else {
        setNameField(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "X NAME APT";
    }
  }

  private class BasePlaceField extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals(UNIT_LABEL_STR)) return false;
      if (INFO_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("/")) return false;
      if (field.equals(UNIT_LABEL_STR)) return false;
      if (INFO_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("\\d{1,4}[A-Z]?|[A-Z]");
  private class BaseAptField extends AptField {
    public BaseAptField() {
      setPattern(APT_PTN, true);
    }
  }

  private class BasePhoneField extends PhoneField {

    public BasePhoneField() {
      super(null);
    }

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      field = stripFieldStart(field, "1-");
      Matcher match = PHONE_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strPhone = getOptGroup(match.group(1));
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern INFO_TIMES_PTN = Pattern.compile("[A-Za-z ]+: *\\d\\d:\\d\\d");
  private static final Pattern INFO_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d\\b *(.*)|\\d\\d?/\\d\\d?/\\d\\d|\\d\\d:\\d\\d:\\d\\d");
  private static final Pattern INFO_TRUNC_PTN = Pattern.compile("\\d{1,2}[/:][ 0-9:/]*");
  private class BaseInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (data.msgType != MsgType.RUN_REPORT) {
        Matcher match = INFO_TIMES_PTN.matcher(field);
        if (match.matches()) {
          data.msgType = MsgType.RUN_REPORT;
        }
        else {
          match = INFO_PTN.matcher(field);
          if (!match.matches()) {
            return INFO_TRUNC_PTN.matcher(field).matches();
          }
          field = match.group(1);
        }
      }
      if (field != null) super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      else if (INFO_TRUNC_PTN.matcher(field).matches()) return;
      if (field != null) super.parse(field, data);
    }
  }

  private static final String UNIT_LABEL_STR = "Unit Org Name Area Types";
  private class BaseUnitLabelField extends SkipField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals(UNIT_LABEL_STR)) return true;
      if (isLastField() && UNIT_LABEL_STR.startsWith(field)) return true;
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) field = field.substring(0,pt).trim();
      addUnit(field, data);
    }
  }

  private void primeCrossStreets(String addr) {
    String[] parts = addr.split("&");
    if (parts.length == 1) return;
    for (String part : parts) {
      part = part.trim();
      crossSet.add(part);
    }
  }

  private void parseCrossStreet(boolean leadGPS, boolean leadPlace, boolean trailName, String field, Data data) {

    if (leadGPS) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.lookingAt()) {
        setGPSLoc(match.group(1), data);
        field = field.substring(match.end());
      }
    }

    boolean startSlash = field.startsWith("/");
    if (startSlash) field = field.substring(1).trim();

    StartType st = leadPlace & !startSlash ? StartType.START_PLACE : StartType.START_ADDR;
    int flags = FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_CROSS_FOLLOWS;
    flags |= getExtraParseAddressFlags();

    String cross = "";
    while (field.length() > 0) {
      Result res = parseAddress(st, flags, field);
      st = StartType.START_ADDR;
      if (!res.isValid()) break;
      res.getData(data);
      cross = append(cross, " / ", data.strCross);
      data.strCross = "";
      field = res.getLeft();
    }

    if (field.length() > 0) {
      if (trailName) {
        setNameField(field, data);
      } else if (leadPlace && cross.length() == 0) {
        data.strPlace = field;
      } else {
        cross = append(cross, " / ", field);
      }
    }

    if (startSlash) cross = '/' + cross;
    addCrossStreet(cross, data);
  }

  private void addCrossStreet(String field, Data data) {
    boolean startSlash = field.startsWith("/");
    if (startSlash) {
      field = field.substring(1).trim();
      if (data.strCross.length() > 0) startSlash = false;
    }
    for (String cross : field.split("/")) {
      cross = cross.trim();
      if (startSlash) {
        startSlash = false;
        data.strAddress = append(data.strAddress, " & ", cross);
      }
      else if (crossSet.add(cross)) {
        data.strCross = append(data.strCross, " / ", cross);
      }
    }
  }

  private void addUnit(String field, Data data) {
    if (unitSet.add(field)) data.strUnit = append(data.strUnit, " ", field);
  }

  private static void setNameField(String field, Data data) {
    if (field.length() <= 4 && NUMERIC.matcher(field).matches()) {
      data.strApt = append(data.strApt, "-", field);
    } else {
      data.strName = cleanWirelessCarrier(field);
    }
  }

  /**
   * Can be overridden by parser subclasses to make corrections to the
   * call/address combination
   * @param addr
   * @return
   */
  protected String fixCallAddress(String addr) {
    return addr;
  }
}
