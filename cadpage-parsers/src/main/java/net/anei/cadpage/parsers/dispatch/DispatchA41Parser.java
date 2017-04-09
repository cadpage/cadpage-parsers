package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA41Parser extends FieldProgramParser {
  
  public static final int A41_FLG_NO_CALL = 1;
  public static final int  A41_FLG_ID = 2;
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(.*) +- From +(?:([A-Z][A-Z0-9]+) +)?(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d)$");
  
  private String prefix;
  private Pattern channelPattern;
  private Properties callCodes;
  
  public DispatchA41Parser(Properties cityCodes, String defCity, String defState, String channelPattern) {
    this("DISPATCH:", cityCodes, defCity, defState, channelPattern, 0, null);
  }
  
  public DispatchA41Parser(String prefix, Properties cityCodes, String defCity, String defState, String channelPattern) {
    this(prefix, cityCodes, defCity, defState, channelPattern, 0, null);
  }
  
  public DispatchA41Parser(Properties cityCodes, String defCity, String defState, String channelPattern, int flags) {
    this("DISPATCH:", cityCodes, defCity, defState, channelPattern, flags, null);
  }
  
  public DispatchA41Parser(String prefix, Properties cityCodes, String defCity, String defState, String channelPattern, int flags) {
    this(prefix, cityCodes, defCity, defState, channelPattern, flags, null);
  }
  
  public DispatchA41Parser(Properties cityCodes, String defCity, String defState, String channelPattern, int flags, Properties callCodes) {
    this("DISPATCH:", cityCodes, defCity, defState, channelPattern, flags, callCodes);
  }

  public DispatchA41Parser(String prefix, Properties cityCodes, String defCity, String defState, String channelPattern, int flags, Properties callCodes) {
    super(cityCodes, defCity, defState, calcProgram(flags));
    this.prefix = prefix;
    this.channelPattern = Pattern.compile(channelPattern);
    this.callCodes = callCodes;
  }
  
  private static String calcProgram(int flags) {
    StringBuilder sb = new StringBuilder();
    sb.append("CODE! ");
    if ((flags & A41_FLG_ID) != 0) sb.append("ID ");
    else if ((flags & A41_FLG_NO_CALL) == 0) sb.append("CALL ");
    sb.append("( PLACE1 CITY/Z AT | ADDRCITY/Z ADDR2? ) CITY? ( CH/Z MAPPAGE! | EMPTY? ( PLACE2 PLACE_APT2 X1 | PLACE2 PLACE_APT2 INT | PLACE2 X1 | PLACE2 INT | X1 | INT | ) EMPTY? ( CH! | PLACE3+? PLACE_APT3 CH! ) ( MAP MAPPAGE | MAPPAGE | MAP MAP2? ) ) INFO/CS+? ID1 GPS1 GPS2 ID2? Unit:UNIT UNIT+");
    return sb.toString();
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    if (!body.startsWith(prefix)) return false;
    body = body.substring(prefix.length()).trim();
    
    Matcher match = DATE_TIME_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1);
      data.strSource = match.group(2);
      data.strDate = match.group(3);
      data.strTime = match.group(4);
    } else {
      int pt = body.lastIndexOf(" - From");
      if (pt >= 0) body = body.substring(0,pt).trim();
    }
    body = body.replace(" Units:", " Unit:");
    if (body.endsWith(",")) body = body + ' ';
    return parseFields(body.split(",+ "), data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " SRC DATE TIME"; 
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
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("TIMESTAMP")) return new SkipField("\\d{4}-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d", true);
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
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt >= 0) field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
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
      if (!checkParse(field, data)) abort();
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
  
  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
}
