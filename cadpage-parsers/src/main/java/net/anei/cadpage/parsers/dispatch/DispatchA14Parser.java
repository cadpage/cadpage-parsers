package net.anei.cadpage.parsers.dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;


public class DispatchA14Parser extends FieldProgramParser {

  private static final Pattern MARKER = Pattern.compile("(.*?)\\*{3,}([^\\*]+)\\*{3,}(.*)", Pattern.DOTALL);
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("([^ \\*])(CS:|ADTML:|CODE:|TOA:|TYPE:|LOC:|CROSS:|CODE:|TIME:)");
  private static final Pattern TIME_MARK_PTN = Pattern.compile(" TIME:\\d\\d:\\d\\d:\\d\\d$");
  private static final String[] PART2_KEYWORDS = new String[]{" TIME:", " CODE:", " CROSS:"};
  private static final Pattern ID_PTN = Pattern.compile("^(\\d{4}-\\d{6}) ");
  
  private ReverseCodeSet sourceSet;
  private Properties cityCodes = null;
  private ReverseCodeSet citySet = null;
  
  private boolean unitInfo;
  
  public DispatchA14Parser(String defCity, String defState, boolean unitInfo) {
    this(null, null, defCity, defState, unitInfo);
  }
  
  public DispatchA14Parser(Properties cityCodes, ReverseCodeSet sourceSet, String defCity, String defState, boolean unitInfo) {
    super(cityCodes, defCity, defState,
          "ADDR1! CS:X? ADTML:CODE? TOA:TIMEDATE TYPE:INFO LOC:ADDR2/SXP CROSS:X2 CODE:CODE TIME:TIME");
    this.sourceSet = sourceSet;
    this.unitInfo = unitInfo;
    
    // Build reverse code set with all of the full length city name values
    this.cityCodes = cityCodes;
    if (cityCodes != null) {
      List<String> cityList = new ArrayList<String>();
      for (Object code : cityCodes.keySet()) {
        cityList.add((String)code);
        cityList.add(cityCodes.getProperty((String)code));
      }
      citySet = new ReverseCodeSet(cityList.toArray(new String[cityList.size()]));
    }
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    // Rule out the similar NYNassauCountyA format
    if (body.contains(" c/s:")) return false;
    
    Matcher match = ID_PTN.matcher(body);
    if (match.find()) {
      data.strCallId = match.group(1);
      body = body.substring(match.end()).trim();
    }
    
    // Call description is in front of text bracketed by three asterisks
    match = MARKER.matcher(body);
    if (!match.matches()) return false;
    data.strCall = append(match.group(1).trim(), " - ", match.group(2).trim());
    body = match.group(3).trim();

    // Fill in missing blanks in front of known keywords
    body = MISSING_BLANK_PTN.matcher(body).replaceAll("$1 $2");
    
    // Some, but not all, calls contain two basically duplicated sets of 
    // information, The first part of the message contains a compressed
    // version taht has almost everything.  The second following part contains
    // a more detailed description of what was included in the first part.
    // The second part is more reliable and should be used when present.  But
    // some or all of it's data may be truncated because of SMS message limits.
    
    // So, the first thing we do is confirm that the second part is present
    // and remove any field that looks like it may be truncated
    int pt2 = body.indexOf(" LOC:");
    if (pt2 >= 0) {
      match = TIME_MARK_PTN.matcher(body);
      if (!match.find()) {
        for (String keyword : PART2_KEYWORDS) {
          int tmp = body.lastIndexOf(keyword);
          if (tmp >= 0) {
            pt2 = tmp;
            break;
          }
        }
        body = body.substring(0,pt2).trim();
      }
    }

    if (! super.parseMsg(body, data)) return false;
    return (data.strCross.length() > 0 || data.strTime.length() > 0);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR1")) return new MyAddressField1();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ADDR2")) return new MyAddressField2();
    if (name.equals("X2")) return new MyCrossField2();
    return super.getField(name);
  }
    
  private static final Pattern ADDR_PLACE_PTN = Pattern.compile("(.*[;\\*@] *)(.*)");
  private static final Pattern PLACE_MARK_PTN = Pattern.compile("(.*)[;\\*@]");
  private class MyAddressField1 extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      // If we have a city set, use it to strip a full city name from the
      // end of the field
      if (citySet != null) {
        String city = citySet.getCode(field);
        if (city != null) {
          data.strCity = convertCodes(city, cityCodes);
          field = field.substring(0,field.length()-city.length());
        }
      }
      
      String placePrefix = "";
      Matcher match = ADDR_PLACE_PTN.matcher(field);
      if  (match.matches()) {
        placePrefix = match.group(1);
        field = match.group(2);
      }
      
      // And sometimes a place name is in parens
      StartType st = StartType.START_PLACE;
      if (field.startsWith("(")) {
        st = StartType.START_ADDR;
        int pt = field.lastIndexOf(')');
        if (pt > 0) {
          data.strPlace = field.substring(0,pt+1);
          field = field.substring(pt+1).trim();
        }
      }
      int flags = FLAG_START_FLD_NO_DELIM | FLAG_RECHECK_APT | FLAG_ANCHOR_END | FLAG_NO_CITY;
      flags |= getExtraParseAddressFlags();
      parseAddress(st, flags, field, data);
      
      // If we found a place name ending with &, treat it like an unrecognzied street name intersection
      if (st == StartType.START_PLACE && data.strPlace.endsWith("&")) {
        data.strAddress = append(stripFieldEnd(data.strPlace, "&"), " & ", data.strAddress);
        data.strPlace = "";
      }
      
      // Strip off a place marking delimiter.  We can't trust these to parse because they frequently are followed by
      // a longer place name.  But if they do end up at the end of the place name, strip them off
      data.strPlace = (placePrefix + data.strPlace).trim();
      match = PLACE_MARK_PTN.matcher(data.strPlace);
      if (match.matches()) data.strPlace = match.group(1).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" ZONE ");
      if (pt >= 0) {
        data.strMap = field.substring(pt+6).trim();
        field = field.substring(0,pt).trim();
      }
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "X MAP";
    }
  }

  private static final Pattern TIME_DATE = Pattern.compile("^(\\d\\d:\\d\\d)(?: (\\d\\d[-/]\\d\\d[-/]\\d\\d))?");
  private static final Pattern ANGLE_BKT_PTN = Pattern.compile("<[^<>]*>");
  private static final Pattern ID_PTN2 = Pattern.compile("20\\d{2}-\\d{6}");
  private static final Pattern ID_PTN3 = Pattern.compile("(?<=^|[^\\d])(?:\\d{4}-\\d*|20\\d{0,2})$");
  private static final Pattern CODE_PTN = Pattern.compile("(\\d{1,2}-[A-Z]-\\d{1,2}[A-Z]?)\\b *(.*)", Pattern.DOTALL);
  private static final Pattern UNIT_PTN = Pattern.compile("([-A-Za-z0-9]*)(?<!-)-[- ]*(.*)", Pattern.DOTALL);
  
  private class MyTimeDateField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE.matcher(field);
      if (!match.find()) return;
      data.strTime = match.group(1);
      data.strDate = getOptGroup(match.group(2)).replaceAll("-", "/");
      field = field.substring(match.end()).trim();
      field = ANGLE_BKT_PTN.matcher(field).replaceAll("").trim();
      String extra = "";
      match = ID_PTN2.matcher(field);
      if (match.find()) {
        data.strCallId = match.group();
        extra = field.substring(match.end()).trim();
        field = field.substring(0,match.start()).trim();
      } else {
        match = ID_PTN3.matcher(field);
        if (match.find()) field = field.substring(0,match.start()).trim();
      }
      if (sourceSet != null) {
        String src = sourceSet.getCode(field);
        if (src != null) {
          data.strSource = src;
          field = field.substring(0,field.length()-src.length()).trim();
        }
      }
      match = CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      if (unitInfo) {
        match = UNIT_PTN.matcher(field);
        if (match.matches()) {
          data.strUnit = match.group(1).replace('-',',');
          field = match.group(2);
        }
      }
      extra = stripFieldStart(extra, "-");
      field = append(field, " / ", extra);
      field = field.trim().replaceAll("  +", " ");
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "TIME DATE CODE SRC UNIT ID INFO";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() <= 2) {
        data.strCall = append(data.strCall, " - ", field);
      } else {
        data.strSupp = field;
      }
    }
  }
  
  @Override
  public String getProgram() {
    return "ID CALL " + super.getProgram();
  }

  private static final Pattern ADDR_MARK_PTN = Pattern.compile(" (?:(C/?S:?)|(APT|SUITE|ROOM|RM|UNIT)) ", Pattern.CASE_INSENSITIVE);
  private class MyAddressField2 extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String saveCity = data.strCity;
      data.strAddress = data.strApt = data.strCity = "";
      String place = "";
      int pt = field.indexOf('@');
      if (pt >= 0) {
        place = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        field = stripFieldEnd(field, ":");
      }
      
      Matcher match = ADDR_MARK_PTN.matcher(field);
      if (match.find()) {
        super.parse(field.substring(0,match.start()).trim(), data);
        int last = -1;
        boolean cross = false;
        do {
          if (last >= 0) {
            appendField(cross, field.substring(last, match.start()), data);
          }
          last = match.end();
          cross = match.group(1) != null;
        } while (match.find());
        appendField(cross, field.substring(last), data);
      }
      
      else {
        super.parse(field, data);
      }
      
      data.strPlace = append(data.strPlace, " ", place);
      
      if (data.strCity.length() == 0) data.strCity = saveCity;
    }
    
    private void appendField(boolean cross, String fld, Data data) {
      fld = fld.trim();
      if (cross) {
        fld = stripFieldStart(fld, "/");
        fld = stripFieldEnd(fld, "/");
        data.strCross = append(data.strCross, " / ", fld);
      }  else {
        data.strApt = append(data.strApt, "-", fld);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY APT X PLACE";
    }
  }
  
  private class MyCrossField2 extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      if (field.length() == 0) return;
      
      data.strCross = "";
      super.parse(field, data);
    }
  }
}


