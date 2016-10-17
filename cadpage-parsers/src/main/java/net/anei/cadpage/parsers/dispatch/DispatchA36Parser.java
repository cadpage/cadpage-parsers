package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA36Parser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN1 = Pattern.compile("Disp ([-A-Z0-9]+) Case # [-0-9]+ Call # ([0-9]+)");
  private static final Pattern SUBJECT_PTN3 = Pattern.compile("Disp: ([-A-Z0-9]+) ([0-9]+) [-0-9]+");
  private static final Pattern GPS_PTN = Pattern.compile(" +http://maps\\.google\\.com/maps\\?q=([-+\\d\\.,]+)$");
  private static final String PART_GPS_STR = "http://maps.google.com/maps?q=";
  private static final Pattern MASTER3_PTN = Pattern.compile("(?:([A-Z0-9]+) )?Date Recv (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)(?: ([^ ].*?))?  (.*)");
  private static final DateFormat TIME_FMT3 = new SimpleDateFormat("hh:mm:ss aa");
  
  private int iVersion;
  private String version;
  
  private String[] cityList = null;
  private int citySpaceCnt = 0;

  public DispatchA36Parser(String defCity, String defState, int iVersion) {
    this(null, defCity, defState, iVersion);
  }

  public DispatchA36Parser(String[] cityList, String defCity, String defState, int iVersion) {
    super(cityList, defCity, defState,
          "DATETIME CALL ( SELECT/2 ( ADDR/Z! ( END | INFO ) | PLACE ( STNO | X/Z STNO | ) APT? ADDR! ) | STNO? ADDR! ) INFO+");
    this.iVersion = iVersion;
    this.version = Integer.toString(iVersion);
    this.cityList = cityList;
    
    // For version 3, the truncated city detection logic needs to know 
    // the maximum number of words in the city names
    if (iVersion == 3 && cityList != null) {
      for (String city : cityList) {
        int cnt = 0;
        for (char chr : city.toCharArray()) {
          if (chr == ' ') cnt++;
        }
        if (cnt > citySpaceCnt) citySpaceCnt = cnt;
      }
    }
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN1.matcher(subject);
    if (!match.matches()) {
      match = SUBJECT_PTN3.matcher(subject);
      if (!match.matches()) return false;
    }
    data.strSource = match.group(1);
    data.strCallId = match.group(2);
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    // Check for GPS coordinates or partial GPS coordinates
    // If either is found, we will not have to worry about checking
    // for truncated address later on.
    boolean partAddr = true;
    match = GPS_PTN.matcher(body);
    if (match.find()) {
      partAddr = false;
      body = body.substring(0,match.start());
      setGPSLoc(match.group(1), data);
    }
    else {
      pt = body.lastIndexOf(" ht");
      if (pt >= 0) {
        String part = body.substring(pt+1);
        if (part.startsWith(PART_GPS_STR) || PART_GPS_STR.startsWith(part)) {
          partAddr = false;
          body = body.substring(0,pt).trim();
        }
      }
    }
    
    // Version 3 has no delimiters and  must use a pattern match to resolve things
    match = MASTER3_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT DATE TIME PLACE CALL ADDR CITY GPS");
      data.strUnit =  getOptGroup(match.group(1));
      data.strDate = match.group(2);
      setTime(TIME_FMT3, match.group(3), data);
      data.strPlace = getOptGroup(match.group(4));
      String addr = match.group(5).trim();
      
      // Life would be so much simpler if we did not have to worry about possibly truncated
      // city names at the end of an address, especially multi word cities :(
      int flags = FLAG_START_FLD_REQ | FLAG_PAD_FIELD | FLAG_ANCHOR_END;
      if (partAddr) {
        int saveLen = addr.length();
        addr = fixAddressCity(addr, data);
        if (saveLen != addr.length()) flags |= FLAG_NO_CITY;
      }
      
      parseAddress(StartType.START_CALL, flags, addr, data);
      data.strApt = append(data.strApt, "-", getPadField());
      return true;
    }
    if (iVersion == 3) return false;
    
    if (!parseFields(body.split(" /"), data)) return false;
    
    // For the version 2 format, the place field can be, and usually is
    // a restatement of the address
    if (iVersion == 2) {
      if (data.strAddress.contains(data.strPlace)) data.strPlace = "";
      if (data.strPlace.length() > 0 && 
          !data.strPlace.endsWith(" PLACE") &&
          !data.strPlace.endsWith(" MALL")) {
        int sp = checkAddress(data.strPlace);
        if (sp > STATUS_MARGINAL) {
          String place = data.strPlace;
          data.strPlace = "";
          int sa = checkAddress(data.strAddress);
          if (sp > sa) {
            String tmp = place;
            place = data.strAddress;
            data.strAddress = "";
            parseAddress(tmp, data);
            int ii = sp;
            sp = sa;
            sa = ii;
          }
          if (sa < 3) {
            data.strAddress = append(data.strAddress, " & ", place);
          } else {
            data.strCross = append(data.strCross, " & ", place);
          }
        }
      }
    }
    return true;
  }
  
  /**
   * Fix address lines that end with (possibly partial) city names
   * @param addr address line to be fixed
   * @param data parsed data object
   * @return original address, possibly truncated to remove city name
   */
  private String fixAddressCity(String addr, Data data) {
    
    // We already know how many blanks might be in a city name
    // First thing we do is identify the start of the number of
    // words at the end of the address lines corresponding to
    // the maximum number of city words.  Build the list in
    // back to front order so we will process these word breaks
    // from front to back.
    int pt = addr.length();
    int[] blankCols = new int[citySpaceCnt+1];
    for (int ii = 0; ii <= citySpaceCnt; ii++) {
      if (pt >= 0) pt = addr.lastIndexOf(' ', pt-1);
      blankCols[blankCols.length-ii-1] = pt;
    }
    
    // Next step through the possible city word breaks
    for (int brk : blankCols) {
      
      // Skip if not a valid break
      if (brk < 0) continue;
      
      // Pick out the remaining possible partial city
      // And scan through the city list checking to see if this
      // is a possible match for any of them.  If it matches 
      // more than one, set the prospective city to empty string
      String partCity = addr.substring(brk+1).toUpperCase();
      String city = null;
      for (String tCity : cityList) {
        if (tCity.startsWith(partCity)) {
          if (city == null) city = tCity;
          else city = "";
        }
      }
      
      // If we found one, save it as the parsed city name
      // and return the truncated address
      if (city != null) {
        data.strCity = city;
        return addr.substring(0,brk).trim();
      }
    }
    
    // If we never find one, just return the original address
    return addr;
  }
    
  
  @Override
  public String getProgram() {
    return "SRC ID " + super.getProgram().replace("PLACE", "PLACE X") + " GPS";
  }
  
  @Override
  protected String getSelectValue() {
    return version;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("STNO")) return new MyStreetNoField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d)( +[AP]M)?");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:dd aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String qual = match.group(3);
      if (qual == null) {
        data.strTime = match.group(2);
      } else {
        setTime(TIME_FMT, match.group(2) + match.group(3), data);
      }
    }
  }
  
  private static final Pattern STREET_NO_PTN = Pattern.compile("(\\d+)(?: NORTH| SOUTH| EAST |WEST)?", Pattern.CASE_INSENSITIVE);
  private class MyStreetNoField extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = STREET_NO_PTN.matcher(field);
      if (! match.matches()) return false;
      data.strAddress = match.group(1);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = STREET_NO_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      data.strAddress = field;
    }
  }
  
  private class MyAptField extends AptField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() > 4) return false;
      super.parse(field, data);
      return true;
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String fld, Data data) {
      data.strAddress = append(data.strAddress, " ", fld);
    }
  }
  
  private static final Pattern NOT_ADDRESS_PTN = Pattern.compile("[^ ]/ |\\bFIRE\\b");
  private class MyInfoField extends InfoField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      
      // This is only called to handle one situation where the place name is
      // missing and the address is followed by an information field that we
      // have to identify as not looking even remotely like an address
      if (field.length() < 35 && !NOT_ADDRESS_PTN.matcher(field).find()) return false;
      parse(field, data);
      return true;
      
    }
    
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) {
        data.strCall = field;
      } else {
        data.strSupp = append(data.strSupp, " / ", field);
      }
    }
  }
}
