package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/*
Handles parsing for a vendor identified as VisionCAD
*/
public class DispatchA3Parser extends FieldProgramParser {

  private static final int BASE_FLAG_MASK = 0xF;
  private static final int BASE_INFO = 1;
  private static final int BASE_PLACE = 2;
  private static final int BASE_PLACE_OFF = 3;
  private static final int BASE_MAP = 4;
  private static final int BASE_CODE = 5;
  
  
  private static final int NBH1_OFFSET = 0;
  private static final int NBH2_OFFSET = 4;
  private static final int LANDMARK_OFFSET = 8;
  private static final int LANDMARK_COMMENT_OFFSET = 12;
  private static final int GEO_COMMENT_OFFSET = 16;
  private static final int PLACE_COMMENT_OFFSET = 20;
  
  protected static final int FA3_NBH1_INFO = BASE_INFO << NBH1_OFFSET;
  protected static final int FA3_NBH1_PLACE_OFF = BASE_PLACE_OFF << NBH1_OFFSET;
  protected static final int FA3_NBH1_MAP = BASE_MAP << NBH1_OFFSET;
  protected static final int FA3_NBH1_CODE = BASE_CODE << NBH1_OFFSET;
      
  protected static final int FA3_NBH2_INFO = BASE_INFO << NBH2_OFFSET;
  protected static final int FA3_NBH2_MAP = BASE_MAP << NBH2_OFFSET;
  protected static final int FA3_NBH2_PLACE_OFF = BASE_PLACE_OFF << NBH2_OFFSET;
  protected static final int FA3_NBH2_CODE = BASE_CODE << NBH2_OFFSET;
  
  protected static final int FA3_NBH_INFO = FA3_NBH1_INFO | FA3_NBH2_INFO;
  protected static final int FA3_NBH_MAP = FA3_NBH1_MAP | FA3_NBH2_MAP;
  protected static final int FA3_NBH_PLACE_OFF = FA3_NBH1_PLACE_OFF | FA3_NBH2_PLACE_OFF;
  protected static final int FA3_NBH_CODE = FA3_NBH1_CODE | FA3_NBH2_CODE;
  
  protected static final int FA3_LANDMARK_PLACE = BASE_PLACE << LANDMARK_OFFSET;
  protected static final int FA3_LANDMARK_PLACE_OFF = BASE_PLACE_OFF << LANDMARK_OFFSET;
  
  protected static final int FA3_LANDMARK_COMMENT_PLACE = BASE_PLACE << LANDMARK_COMMENT_OFFSET;
  
  protected static final int FA3_GEO_COMMENT_PLACE = BASE_PLACE << GEO_COMMENT_OFFSET;
  protected static final int FA3_GEO_COMMENT_PLACE_OFF = BASE_PLACE_OFF << GEO_COMMENT_OFFSET;
  
  protected static final int FA3_PLACE_COMMENT_PLACE = BASE_PLACE << PLACE_COMMENT_OFFSET;
  
  private String prefix = null;
  private Pattern prefixPtn = null;
  private Pattern delim = Pattern.compile("(?<!\\*)\\*[\n ]+");
  
  public DispatchA3Parser(int version, Pattern prefixPtn, String defCity, String defState) {
    this(version, prefixPtn, defCity, defState, 0);
  }
  
  public DispatchA3Parser(int version, Pattern prefixPtn, String defCity, String defState, int flags) {
    this(version, (Properties)null, defCity, defState, flags);
    this.prefixPtn = prefixPtn;
  }
  
  public DispatchA3Parser(int version, String prefix, String defCity, String defState) {
    this(version, prefix, defCity, defState, 0);
  }
  
  public DispatchA3Parser(int version, String prefix, String defCity, String defState, int flags) {
    this(version, (Properties)null, defCity, defState, flags);
    this.prefix = prefix;
  }
  
  public DispatchA3Parser(int version, String defCity, String defState) {
    this(version, (Properties)null, defCity, defState, 0);
  }
  
  public DispatchA3Parser(int version, Properties cityCodes, String defCity, String defState) {
    this(version, cityCodes, defCity, defState, 0);
  }
  
  public DispatchA3Parser(int version, Properties cityCodes, String defCity, String defState, int flags) {
    super(cityCodes, defCity, defState, null);
    if (version < 0) {
      version = -version;
      delim = Pattern.compile("\\*");
    }
    setProgram(version == 0 ?
                    "ID? ADDR/SXP APT CH CITY! X X MAP INFO1 CALL CALL ( UNIT! | NAME UNIT! | NAME PHONE UNIT ) INFO+"
                : version == 1 ?
                    "ID? ADDR/SXP APT CH CITY! EMPTY+? Place_Comment:INFO? CALL CALL ( UNIT! | NAME UNIT! | NAME PHONE UNIT ) INFO+"
                : version == 2 ?
                    "ID? ADDR APT CH CITY X X MAP INFO1 SKIP CALL! PLACENAME PHONE UNIT INFO+"
                : null, 0);
    setupInfoFields(flags);
  }
  
  public DispatchA3Parser(String defCity, String defState, String program) {
    this(defCity, defState, program, 0);
  }
  
  public DispatchA3Parser(Pattern prefixPtn, String defCity, String defState, String program) {
    this(prefixPtn, defCity, defState, program, 0);
  }
  
  public DispatchA3Parser(Pattern prefixPtn, String defCity, String defState, String program, int flags) {
    super(defCity, defState, program);
    setupInfoFields(flags);
    this.prefixPtn = prefixPtn;
  }
  
  public DispatchA3Parser(String prefix, String defCity, String defState, String program) {
    this(prefix, defCity, defState, program, 0);
  }
  
  public DispatchA3Parser(String defCity, String defState, String program, int flags) {
    super(defCity, defState, program);
    setupInfoFields(flags);
  }
  
  public DispatchA3Parser(String prefix, String defCity, String defState, String program, int flags) {
    super(defCity, defState, program);
    setupInfoFields(flags);
    this.prefix = prefix;
  }
  
  public DispatchA3Parser(String[] cityList, String defCity, String defState, String program) {
    this(cityList, defCity, defState, program, 0);
  }
  
  public DispatchA3Parser(String prefix, String[] cityList, String defCity, String defState, String program) {
    this(prefix, cityList, defCity, defState, program, 0);
  }
  
  public DispatchA3Parser(String[] cityList, String defCity, String defState, String program, int flags) {
    super(cityList, defCity, defState, program);
    setupInfoFields(flags);
  }
  
  public DispatchA3Parser(String prefix, String[] cityList, String defCity, String defState, String program, int flags) {
    super(cityList, defCity, defState, program);
    setupInfoFields(flags);
    this.prefix = prefix;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseMsg(body, data, true)) return false;
    return true;
  }
  
  /**
   * New variant of standard parseMsg() call
   * @param body text body
   * @param data parsed data object to be filled with information
   * @param splitField true if textline should be broken up by the standard A3 delimiter seqeuence.
   * false if these delimiters are  not going to be found in the text body
   * @return true if parse was successful
   */
  protected boolean parseMsg(String body, Data data, boolean splitField) {
    if (prefix != null) {
      if (!body.startsWith(prefix)) return false;
      body = body.substring(prefix.length()).trim();
    } else if (prefixPtn != null) {
      Matcher match = prefixPtn.matcher(body);
      if (!match.lookingAt()) return false;
      body  = body.substring(match.end()).trim();
    }
    if (splitField) {
      if (body.endsWith("*")) body = body + " ";
      return parseFields(delim.split(body), data);
    } else {
      return super.parseMsg(body, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2,6}-\\d{4,}|\\d{11,12}|", true);
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("CH")) return new BaseChannelField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("INFO1")) return new BaseInfo1Field();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("NAME")) return new BaseNameField();
    if (name.equals("PHONE")) return new BasePhoneField();
    if (name.equals("UNIT")) return new BaseUnitField();
    return super.getField(name);
  }
  
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      super.parse(field, data);
    }
  }
  
  private class BaseChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      
      // If nothing entered, see if the apt looks like a channel
      if (field.length() == 0 && data.strApt.startsWith("TAC")) {
        data.strChannel = data.strApt;
        data.strApt = "";
      } else {
        super.parse(field, data);
      }
    }
  }
  
  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("_")) field = field.substring(1).trim();
      super.parse(field, data);
    }
  }
  
  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      
      // Override previous call field
      // Unless this is a generic call description
      if (field.length() == 0) return;
      if (data.strCall.length() > 0 && GENERIC_CALL_SET.contains(field)) return;
      data.strCall = field;
    }
  }
  
  private static final Pattern COMMENT_LABEL = Pattern.compile("NBH:|(?:Landmark|Geo|Place) Comment:");
  protected class BaseInfo1Field extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!COMMENT_LABEL.matcher(field).lookingAt()) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      String key = "I";
      int lastPt = 0;
      Matcher match = COMMENT_LABEL.matcher(field);
      while (match.find()) {
        processSubField(key, field.substring(lastPt, match.start()).trim(), data);
        key = match.group();
        lastPt = match.end();
      }
      processSubField(key, field.substring(lastPt).trim(), data);
    }
    
    private void processSubField(String key, String field, Data data) {
      if (field.length() == 0) return;
      Field procField;
      switch (key.charAt(0)) {
      case 'I': procField = infoInfoField; break;
      case 'N': procField = infoNbh1Field; break;
      case 'L': procField = infoLandmarkCommentField; break;
      case 'G': procField = infoGeoCommentField; break;
      case 'P': procField = infoPlaceCommentField; break;
      default: throw new RuntimeException("Bad Switch Key");
      }
      procField.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "INFO MAP PLACE";
    }
  }
  
  /**
   * Find first INFO related keyword in line
   * @param line line to be searched
   * @return position of first INFO related keyword.  If none found
   * returns line length.
   */
  protected int findInfoBreak(String line) {
    Matcher match = DATE_TIME_PTN.matcher(line);
    if (match.find()) return match.start();
    match = TRUNC_DATE_TIME_PTN.matcher(line);
    if (match.find()) return match.start();
    return line.length();
  }
  
  private static final Pattern LINE_PTN = Pattern.compile("Line\\d+=");
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\[?\\b(\\d?\\d/\\d?\\d/\\d{4}) (\\d?\\d:\\d?\\d:\\d\\d(?: [AP]M)?)(?: : pos\\d+ : \\w+\\b\\]?|$| :$| : po?s?\\d*$| : pos\\d+ :$)");
  private static final Pattern TRUNC_DATE_TIME_PTN = Pattern.compile(" (?:0?[1-9]|1[0-2])/[\\d /:]*(?: [AP])?$|  (?:0?[1-9]|1[0-2])$");
  private static final DateFormat DATE_TIME_FMT1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
  private static final DateFormat DATE_TIME_FMT2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  private static final Pattern EXTRA_DELIM = Pattern.compile("(?:(?:\\*\\* )?EMD (?:Case Entry Finished|Case Complete|Recommended Dispatch|Key Questions Finished)(?: \\*\\*)?)|\\bResponse Text:|\\bKey Questions:|Narrative ?:|\\bALI\\b|\\b(?=Cross Streets:|Landmark:|Geo Comment:|Landmark Comment:|NBH:|[XY] Coordinate:|Uncertainty Factor:|Confidence Factor:|\\**Nearest Address:)|Place Comment:|  +|\n| \\.\\. |\bALI\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SKIP_PTN = Pattern.compile("UPDATED? +\\d\\d?(?:[-/]\\d\\d?){1,2}\\b.*|Uncertainty Factor:.*|Confidence Factor:.*");
  protected class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      field = field.replace('\n', ' ');
      
      if (data.strCall.length() > 0 && field.toUpperCase().startsWith(data.strCall.toUpperCase())) {
        data.strCall = field;
        return;
      }
      
      Matcher match = LINE_PTN.matcher(field);
      if (match.find()) field = field.substring(match.end()).trim();
      
      // If we have a time set going into this, do not mess with it
      boolean lockTime = data.strDate.length() == 0 && data.strTime.length() > 0;
      Date time = null;
      if (data.strDate.length() > 0 && data.strTime.length() > 0) {
        try {
          time = DATE_TIME_FMT1.parse(data.strDate + ' ' + data.strTime);
        } catch (ParseException ex) {
          ex.printStackTrace();
        } 
      }
     
      int lastPos = 0;
      List<String> parts = new ArrayList<String>();
      match = DATE_TIME_PTN.matcher(field);
      while (match.find()) {
        if (!lockTime) {
          String timeStr = match.group(1) + ' ' + match.group(2);
          DateFormat dfmt = timeStr.endsWith("M") ? DATE_TIME_FMT2 : DATE_TIME_FMT1;
          Date ttime;
          try {
            ttime = dfmt.parse(timeStr);
          } catch (ParseException ex) {
            throw new RuntimeException(ex);
          }
          if (time == null || ttime.before(time)) {
            time = ttime;
            timeStr = DATE_TIME_FMT1.format(time);
            data.strDate = timeStr.substring(0,10);
            data.strTime = timeStr.substring(11);
          }
        }
        parts.add(field.substring(lastPos, match.start()));
        lastPos = match.end();
      }
      String last = field.substring(lastPos);
      match = TRUNC_DATE_TIME_PTN.matcher(last);
      if (match.find()) last = last.substring(0,match.start()).trim();
      if (last.length() > 0) parts.add(last);
      
      for (String fld1 : parts) {
        String gps = null;
        String[] parts2 = EXTRA_DELIM.split(fld1);
        if (parts.size() > 1 || parts2.length > 1) infoInfoField.setBreak();
        for (String fld2 : parts2) { 
          fld2 = fld2.trim();
          if (fld2.length() == 0) continue;
          
          if (SKIP_PTN.matcher(fld2).matches()) continue;

          String upshift = fld2.toUpperCase();
          if (upshift.startsWith("X COORDINATE:")) {
            gps = fld2.substring(13).trim();
            continue;
          }
          
          if (upshift.startsWith("Y COORDINATE:")) {
            if (gps != null) {
              setGPSLoc(gps+','+fld2.substring(13).trim(), data);
              gps = null;
            }
            continue;
          }

          if (upshift.startsWith("LANDMARK:")) {
            infoLandmarkField.parse(fld2.substring(9).trim(), data);
            continue;
          }
          
          if (upshift.startsWith("NBH:")) {
            infoNbh2Field.parse(fld2.substring(4).trim(), data);
            continue;
          }
          
          if (upshift.startsWith("LANDMARK COMMENT:")) {
            infoLandmarkCommentField.parse(fld2.substring(17).trim(), data);
            continue;
          }
          
          if (upshift.startsWith("GEO COMMENT:")) {
            infoGeoCommentField.parse(fld2.substring(12).trim(), data);
            continue;
          }
          
          if (upshift.startsWith("PLACE COMMENT:")) {
            infoPlaceCommentField.parse(fld2.substring(14).trim(), data);
            continue;
          }

          if (upshift.startsWith("CROSS STREETS:")) {
            fld2 = fld2.substring(14).trim();
            String saveCross = data.strCross;
            int pt = fld2.indexOf("//");
            if (pt < 0) {
              data.strCross = fld2;
              fld2 = "";
            } else {
              String prefix = fld2.substring(0,pt).trim();
              fld2 = fld2.substring(pt+2);
              if (fld2.startsWith(" ")) {
                fld2 = fld2.trim();
              } else {
                Result res = parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, fld2);
                if (res.isValid()) {
                  res.getData(data);
                  fld2 = res.getLeft();
                } else {
                  data.strCross = fld2;
                  fld2 = "";
                }
              }
              data.strCross = append(prefix, " / ", data.strCross);
            }
            if (saveCross.length() > 0) data.strCross = saveCross;
            upshift = fld2.toUpperCase();
          }
          
          if (upshift.startsWith("NARR:")) fld2 = fld2.substring(5).trim();
          infoInfoField.parse(fld2, data);
        }
      }
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME X MAP PLACE INFO GPS";
    }
  }
  
  private class BaseNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("*");
      if (pt >= 0) {
        data.strPhone = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
  }
  
  private static final Pattern BAD_PHONE_PTN = Pattern.compile("(?:\\d{3})?-   -");
  private class BasePhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (BAD_PHONE_PTN.matcher(field).matches()) return;
      super.parse(field, data);
      
    }
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("(?:[A-Z0-9]{1,4}[0-9]|\\d{3}|RRS|CSRS)\\b(?:[,/][,/A-Z0-9]+)?");
  private class BaseUnitField extends UnitField {
    @Override
    public boolean canFail(){
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      return checkParse(field, data, false);
    }
    
    @Override
    public void parse(String field, Data data) {
      checkParse(field, data, true);
    }
    
    private boolean checkParse(String field, Data data, boolean force) {
      if (field.startsWith("Line14=")) {
        data.strUnit = field.substring(7).trim();
        return true;
      }
      if (field.length() > 0 && "Line14=".startsWith(field)) return true;
      if (!force && !UNIT_PTN.matcher(field).matches()) return false;
      data.strUnit = field;
      return true;
    }
  }

  /**
   * Set of field used to process the different subfields found in the INFO1 master field
   */
  private InfoInfoField infoInfoField;
  private Field infoNbh1Field;
  private Field infoNbh2Field;
  private Field infoLandmarkField;
  private Field infoLandmarkCommentField;
  private Field infoGeoCommentField;
  private Field infoPlaceCommentField;
  
  private void setupInfoFields(int flags) {
    Field defPlaceField = new InfoPlaceField();
    infoInfoField = new InfoInfoField();
    
    infoNbh1Field = infoNbh2Field = defPlaceField;
    infoLandmarkField = defPlaceField;
    infoLandmarkCommentField = infoPlaceCommentField = infoGeoCommentField = infoInfoField;
    
    infoNbh1Field = getInfoField(flags >> NBH1_OFFSET, infoInfoField, defPlaceField, defPlaceField);
    infoNbh2Field = getInfoField(flags >> NBH2_OFFSET, infoInfoField, defPlaceField, defPlaceField);
    infoLandmarkField = getInfoField(flags >> LANDMARK_OFFSET, infoInfoField, defPlaceField, defPlaceField);
    infoLandmarkCommentField = getInfoField(flags >> LANDMARK_COMMENT_OFFSET, infoInfoField, defPlaceField, infoInfoField);
    infoGeoCommentField = getInfoField(flags >> GEO_COMMENT_OFFSET, infoInfoField, defPlaceField, infoInfoField);
    infoPlaceCommentField = getInfoField(flags >> PLACE_COMMENT_OFFSET, infoInfoField, defPlaceField, infoInfoField);
  }
  
  private Field getInfoField(int code, Field infoField, Field placeField, Field defaultField) {
    code &= BASE_FLAG_MASK;
    switch (code) {
    case BASE_INFO:
      return infoField;
    case BASE_PLACE:
      return placeField;
    case BASE_PLACE_OFF:
      return new InfoPlaceOffField();
    case BASE_MAP:
      return new InfoMapField();
    case BASE_CODE:
      return new InfoCodeField();
    default:
      return defaultField;
    }
  }
  
  private class InfoMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (data.strMap.length() > 0) return;
      data.strMap = field;
    }
  }
  
  private String infoConnect = "/";
  private class InfoInfoField extends InfoField {
    
    public void setBreak() {
      infoConnect = "\n";
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.equals(":")) return;
      if (SKIP_PTN.matcher(field).matches()) return;
      if (data.strSupp.contains(field)) return;
      if (field.contains(data.strSupp)) {
        data.strSupp = field;
      } else {
        data.strSupp = append(data.strSupp, infoConnect, field);
      }
      infoConnect = " / ";
    }
  }
  
  private class InfoPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strPlace = field;
    }
  }
  
  private class InfoPlaceOffField extends InfoInfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      
      if (data.strPlace.length() == 0) {
        data.strPlace = field;
        return;
      }
      
      if (field.startsWith("OFF ")) {
        boolean placeOff = false;
        int pt;
        if (data.strPlace.startsWith("OFF ")) pt = 0;
        else {
          pt = data.strPlace.indexOf(" OFF ");
          if (pt > 0) pt++;
        }
        if (pt >= 0) {
          String offPlace = data.strPlace.substring(pt);
          if (field.startsWith(offPlace)) {
            field = field.substring(offPlace.length()).trim();
            super.parse(field, data);
            return;
          }
          if (offPlace.startsWith(field)) return;
          placeOff = true;
        }
        int tpt = field.lastIndexOf(" OFF ");
        tpt =  tpt >= 0 ? tpt+5 : 4;
        String tmp = stripFieldStart(field.substring(tpt).trim(), "TO");
        Result res = parseAddress(StartType.START_ADDR, tmp);
        res.getData(new Data(DispatchA3Parser.this));
        String left = res.getLeft();
        if (left.length() > 0) {
          super.parse(left, data);
          field = stripFieldEnd(field, left);
        }
        if (!placeOff) data.strPlace = data.strPlace + ' ' + field;
        return;
      }
      
      if (data.strPlace.startsWith("OFF ")) {
        data.strPlace = field + ' ' + data.strPlace;
        return;
      }
      
      if (field.startsWith(data.strPlace)) {
        field = field.substring(data.strPlace.length()).trim();
        super.parse(field, data);
      }
    }
  }
  
  private class InfoCodeField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCode = p.get(' ');
      data.strSupp = append(data.strSupp, " / ", p.get());
    }
  }
  
  private static final Set<String> GENERIC_CALL_SET = new HashSet<String>(Arrays.asList(
      "FIRE",
      "HAZ-MAT INCIDENT",
      "MEDICAL EMERGENCY",
      "TRAUMA EMERGENCY"
  ));
}
