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
  public static final int A48_OPT_ONE_WORD_CODE = 0x02;
  
  /**
   * Flag indicating there is no call code.  Just a call desciption
   */
  public static final int A48_NO_CODE =           0x04;
  
  /**
   * Enum parameter indicating what kind of information comes between the
   * address and the unit headings.
   */
  
  public enum FieldType { 
    NONE("", "") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {}
    }, 
    
    NAME("NAME", "NAME") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        setNameField(field, data);
      }
      
      @Override
      public boolean check(DispatchA48Parser parser, String field) {
        return field.contains(",");
      }
      
    }, 
    
    X("X/Z+?", "X") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        boolean startSlash = field.startsWith("/");
        if (startSlash) field = field.substring(1).trim();
        int flags = FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_CROSS_FOLLOWS | FLAG_ANCHOR_END;
        flags |= parser.getExtraParseAddressFlags();
        parser.parseAddress(StartType.START_ADDR, flags, field, data);
        String cross = data.strCross;
        data.strCross = "";
        if (startSlash) cross = '/' + cross;
        parser.addCrossStreet(cross, data);
      }
      
      @Override
      public boolean check(DispatchA48Parser parser, String field) {
        return parser.isValidAddress(field);
      }
    },
    
    X_NAME("X_NAME/Z+?", "X NAME") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        
        boolean startSlash = field.startsWith("/");
        if (startSlash) field = field.substring(1).trim();
        
        String cross = "";
        while (field.length() > 0) {
          int flags = FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_CROSS_FOLLOWS;
          flags |= parser.getExtraParseAddressFlags();
          Result res = parser.parseAddress(StartType.START_ADDR, flags, field);
          if (!res.isValid()) break;
          res.getData(data);
          cross = append(cross, " / ", data.strCross);
          data.strCross = "";
          field = res.getLeft();
        } 
        if (startSlash) cross = '/' + cross;
        parser.addCrossStreet(cross, data);
        setNameField(field, data);
      }
      
    },
    
    PLACE("PLACE? APT?", "PLACE APT") {
      @Override
      public void parse(DispatchA48Parser parser, String field, Data data) {
        Parser p = new Parser(field);
        String apt = p.getLast(' ');
        if (APT_PTN.matcher(apt).matches()) {
          data.strApt = append(data.strApt, "-", apt);
          field = p.get();
        }
        data.strPlace = field;
      }
    },
    
    TRASH("SKIP", "") {
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
    
    public abstract void parse(DispatchA48Parser parser, String field, Data data);
    
    public boolean check(DispatchA48Parser parser, String field) {
      return false;
    }
  };
    
  private FieldType fieldType;
  private boolean oneWordCode;
  private boolean optOneWordCode;
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
          append("DATETIME ID CALL ADDRCITY! DUPADDR? SKIPCITY?", " ", fieldType.getFieldProg()) + " ( INFO INFO/ZN+? UNIT_LABEL | UNIT_LABEL ) UNIT/S+");
    this.fieldType = fieldType;
    oneWordCode = (flags & A48_ONE_WORD_CODE) != 0;
    optOneWordCode = (flags & A48_OPT_ONE_WORD_CODE) != 0;
    noCode = (flags & A48_NO_CODE) != 0;
    this.unitPtn = unitPtn;
    this.callCodes = callCodes;
    fieldList = ("DATE TIME ID CODE CALL ADDR APT CITY NAME " + fieldType.getFieldList() + " UNIT INFO").replace("  ", " ");
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("As of \\d\\d?/\\d\\d?/\\d\\d \\d\\d(:\\d\\d:\\d\\d)?");
  private static final Pattern PREFIX_PTN = Pattern.compile("(?!\\d\\d:)([- A-Za-z0-9]+: *)(.*)");
  private static final Pattern TRUNC_HEADER_PTN = Pattern.compile("\\d\\d:\\d\\d \\d{4}-\\d{8} ");
  private static final Pattern TRUNC_HEADER_PTN2 = Pattern.compile(": \\d{4}-\\d{8} ");
  private static final Pattern MASTER_PTN = Pattern.compile("(?:CAD:[-_A-Za-z0-9]* |[- A-Za-z0-9]*:)? *As of (\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d?:\\d\\d:\\d\\d) (?:([AP]M) )?(\\d{4}-\\d{5,8}) (.*)");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile("(.*?)[ ,]+(\\w+)");
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\b(\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d?:\\d\\d:\\d\\d)(?: ([AP]M))?\\b");
  private static final Pattern DATE_TIME_UNIT_MARK_PTN = Pattern.compile("(.*?)(?: \\d\\d?/\\d\\d?/\\d\\d)? Date/Time Unit Status Unit Location/Remarks");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:dd aa");
  private static final Pattern TRAIL_DATE_PTN = Pattern.compile("(.*) \\d\\d/\\d\\d/\\d\\d");
  private static final Pattern UNIT_DELIM_PTN = Pattern.compile("[ ,]+");

  private Set<String> crossSet;
  private Set<String> unitSet;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      if (match.group(1) != null) {
        if (!body.startsWith("As of")) body = subject + '\n' + body;
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
    
    // Another varation on same theme
    else if (subject.length() == 0 && TRUNC_HEADER_PTN2.matcher(body).lookingAt()) {
      body = "As of 99/99/99 99:99:99 " + body.substring(2); 
    }
    
    if (!subject.startsWith("As of") && !subject.equals("ALERT MESSAGE")) data.strSource = subject;
    
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
    data.strDate = match.group(1);
    if (data.strDate.startsWith("99/")) data.strDate = "";
    String time = match.group(2);
    if (!time.startsWith("99:")) {
      String time_qual = match.group(3);
      if (time_qual != null) {
        setTime(TIME_FMT, time + ' ' + time_qual, data);
      } else {
        data.strTime = time;
      }
    }
    data.strCallId = match.group(4);
    String addr = match.group(5).trim();
    
    boolean first = true;
    boolean unitMark = false;
    for (String part : DATE_TIME_PTN.split(addr)) {
      part = part.trim();
      if (first) {
        first = false;
        addr = part;
        match = DATE_TIME_UNIT_MARK_PTN.matcher(addr);
        unitMark = match.matches();
        if (unitMark) addr = match.group(1).trim();
      } else {
        if (unitMark) {
          int pt = part.indexOf(' ');
          if (pt >= 0) part = part.substring(0,pt);
          addUnit(part, data);
        } else {
          data.strSupp = append(data.strSupp, "\n", part);
        }
      }
    }
    
    Parser p = new Parser(fixCallAddress(addr));
    
    String unitInfo = p.getLastOptional(" Unit Org Name Area Types ");

    StartType st = StartType.START_CALL;
    int flags = FLAG_START_FLD_REQ;
    
    if (!noCode) {
      if (optOneWordCode || !oneWordCode) data.strCode = p.getOptional(" - ");
      if (data.strCode.length() == 0) {
        if (!optOneWordCode && !oneWordCode) return false;
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

    addr = p.get();

    if (unitPtn == null) {
      int pt = unitInfo.indexOf(' ');
      if (pt >= 0) unitInfo = unitInfo.substring(0,pt);
      data.strUnit = unitInfo;
    } else if (unitInfo.length() > 0) {
      for (String unit : unitInfo.split(" +")) {
        if (unitPtn.matcher(unit).matches()) addUnit(unit, data);
      }
    } else { 
      while (true) {
        match = TRAIL_UNIT_PTN.matcher(addr);
        if (!match.matches()) break;
        String unit = match.group(2);
        if (!unitPtn.matcher(unit).matches()) break;
        addr = match.group(1);
        data.strUnit = append(unit, " ", data.strUnit);
      }
    }
    
    if (data.strUnit.length() == 0) {
      int pt = addr.lastIndexOf(" Unit");
      if (pt >= 0) {
        if (" Unit Org Name Area Types ".startsWith(addr.substring(pt))) {
          addr = addr.substring(0,pt).trim();
        }
      }
      
      else if (data.strSupp.length() > 0 && !data.strSupp.contains("\n")) {
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
    if (addr.length() == 0) return false;

    boolean addressParsed = false;
    String extra = null;
    
    if (fieldType == FieldType.NONE) flags |= FLAG_ANCHOR_END;
    flags |= getExtraParseAddressFlags();
    
    addr = cleanWirelessCarrier(addr, true);
    
    int pt = addr.lastIndexOf(',');
    if (pt >= 0) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, addr.substring(pt+1).trim(), data);
      if (data.strCity.length() > 0) {
        extra = stripFieldStart(getLeft(), data.strCity);
        
        addressParsed = true;
        addr = addr.substring(0,pt).trim().replace('@', '&');
        if (st == StartType.START_ADDR){
          parseAddress(addr, data);
        } else {
          parseAddress(st, flags | FLAG_NO_CITY | FLAG_ANCHOR_END, addr, data);
        }
        
        // Check for duplicated city/address
        pt = data.strAddress.indexOf(',');
        if (pt >= 0) {
          String tmp1 = data.strAddress.substring(0,pt).trim();
          String tmp2 = data.strAddress.substring(pt+1).trim();
          if (tmp1.equals(tmp2) ||
              tmp2.startsWith(data.strCity) && tmp2.endsWith(tmp1)) {
            data.strAddress = tmp1;
          }
        }
      }
    }
    
    if (!addressParsed) {
      addr = addr.replace('@', '&');
      parseAddress(st, flags, addr, data);
      extra = getLeft();
    }
    
    match = TRAIL_DATE_PTN.matcher(extra);
    if (match.matches()) extra = match.group(1).trim();
    
    fieldType.parse(this, extra, data);
    
    if (data.strCall.equals(data.strCode)) data.strCode = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}", true);
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("DUPADDR")) return new BaseDupAddrField();
    if (name.equals("SKIPCITY")) return new BaseSkipCityField();
    if (name.equals("X_NAME")) return new BaseCrossNameField();
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("UNIT_LABEL")) return new BaseUnitLabelField();
    if (name.equals("UNIT")) return new BaseUnitField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN2 = Pattern.compile("(?:CAD:|[-A-Za-z0-9]*:)? *As of (\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN2.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
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
      if (optOneWordCode || !oneWordCode) {
        int pt = field.indexOf(" - ");
        if (pt >= 0) {
          data.strCode = field.substring(0,pt).trim();
          data.strCall = field.substring(pt+3).trim();
          return;
        }
      }
      
      if (!optOneWordCode && !oneWordCode) abort();
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
  
  private static final Pattern INFO_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d\\b *(.*)|\\d\\d?/\\d\\d?/\\d\\d|\\d\\d:\\d\\d:\\d\\d");
  private static final Pattern INFO_TRUNC_PTN = Pattern.compile("\\d{1,2}[/:][ 0-9:/]*");
  private class BaseInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = INFO_PTN.matcher(field);
      if (!match.matches()) {
        return INFO_TRUNC_PTN.matcher(field).matches();
      }
      field = match.group(1);
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
