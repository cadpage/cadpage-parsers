package net.anei.cadpage.parsers.general;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GeneralParser extends SmartAddressParser {
  
  private static final Pattern DATE_PATTERN = Pattern.compile("\\b(\\d\\d?[-/]\\d\\d?[-/]\\d\\d(\\d\\d)?)\\b[,;:]?");
  private static final Pattern TIME_PATTERN = Pattern.compile("\\b(\\d\\d?(?::\\d\\d?){1,2})( [AP]M)?\\b(?:[,;:]| +hrs\\b)?", Pattern.CASE_INSENSITIVE);
  private static final Pattern TIME2_PATTERN = Pattern.compile("\\b(\\d{1,2})(\\d{2}) *hrs\\b", Pattern.CASE_INSENSITIVE);
  private static final String DELIM_PATTERN_STR = ";|,|~|\\*|\\n|\\||\\b[A-Z][A-Za-z0-9-#]*:|\\bC/S:|\\b[A-Z][A-Za-z]*#";
  private static Pattern DELIM_PATTERN; // will be filled in by constructor
  private static final Pattern CALL_ID_PATTERN = Pattern.compile("\\d{2,4}-\\d{5,}|[A-Z]{1,2}\\d{6,}|\\d{11,}");
  private static final Pattern UNIT_PATTERN = Pattern.compile("([A-Z]{1,4}[0-9]{1,4}\\s+)+");
  private static final Pattern MAP_PATTERN = Pattern.compile("\\b(?:quad|map|grid)-([A-Z0-9]+)[,:;]?",Pattern.CASE_INSENSITIVE);
  
  // Field types that we can identify 
  private enum FieldType {SKIP, CALL, PLACE, ADDRESS, CITY, APT, CROSS, BOX, UNIT, MAP, ID, PHONE, SUPP, CODE, SRC, NAME};
  
  // Map of keywords to field types
  private Map<String, FieldType> keywordMap = new HashMap<String, FieldType>();

  /**
   * Default constructor
   */
  public GeneralParser() {
    this("", "", null, CountryCode.US);
  }
  
  public GeneralParser(CountryCode countryCode) {
    this("", "", null, CountryCode.US);
  }
  
  /**
   * This constructor adds an individual delimiter to the regular delimiter pattern
   * @param delim Special delimiter
   */
  public GeneralParser(String delim) {
    this("", "", delim, CountryCode.US);
  }
  
  public GeneralParser(String delim, CountryCode countryCode) {
    this("", "", delim, countryCode);
  }
  
  /**
   * This constructor specifies default city and state
   * @param defCity default city
   * @param defState default state
   */
  public GeneralParser(String defCity, String defState) {
    this(defCity, defState, null);
  }
  
  
  public GeneralParser(String defCity, String defState, String delim) {
    this(defCity, defState, delim, CountryCode.US);
  }
  
  public GeneralParser(String defCity, String defState, 
                       String delim, CountryCode countryCode) {
    super(defCity, defState, countryCode);
    
    // Set up delimiter pattern
    String delimPattern = DELIM_PATTERN_STR;
    if (delim != null) delimPattern = delim + "|" + delimPattern;
    DELIM_PATTERN = Pattern.compile(delimPattern);
    
    // Initialize keyword map
    loadMap(FieldType.SKIP, "TIME", "TOA", "DATE", "TM", "TOC");
    loadMap(FieldType.CALL, "CALL", "TYPE", "TYP", "CT", "NAT", "NATURE", "INC");
    loadMap(FieldType.PLACE, "NAME", "COMMON", "CN", "O");
    loadMap(FieldType.ADDRESS, "ADDRESS", "LOC", "ADDR", "AD", "ADR", "ADD", "LOCATION");
    loadMap(FieldType.CITY, "CITY", "CTY", "VENUE", "VEN");
    loadMap(FieldType.APT, "APT", "BLDG", "BLD");
    loadMap(FieldType.CROSS, "CROSS", "X", "XS", "XST", "XST2", "X-ST", "C/S", "CS", "BTWN", "STS");
    loadMap(FieldType.BOX, "BOX", "BX", "ADC");
    loadMap(FieldType.UNIT, "UNIT", "UNITS", "RESPONSE", "DUE", "UNTS", "UNT");
    loadMap(FieldType.MAP, "MAP", "GRID", "GRIDS", "QUADRANT", "QUAD");
    loadMap(FieldType.ID, "ID", "CFS", "RUN", "EVT#", "INC#");
    loadMap(FieldType.PHONE, "PHONE", "PH");
    loadMap(FieldType.SUPP, "INFO", "CMT", "CMT1", "CMT2", "SCRIPT", "COMMENTS", "RMK");
    loadMap(FieldType.SRC, "STA", "ST", "DISP", "DIS", "DIST", "DISTRICT", "AGENCY");
    loadMap(FieldType.NAME, "CALLER");
  }
  
  private void loadMap(FieldType type, String ... keywords) {
    for (String keyword : keywords) {
      keywordMap.put(keyword, type);
    }
  }
  
  @Override
  public String getLocName() {
    return "Generic Location";
  }

  /**
   * Determine if message is a CAD page or not
   * Can be overridden by subclasses that have a clue
   * @param subject message subject
   * @param body message body
   * @return true if this is a CAD page
   */
  protected boolean isPageMsg(String subject, String body) {
    
    // Accept anything, but only if someone else has identified this as a CAD page
    return isPositiveId();
  }
  
  /**
   * @return set of flags to be passed to smart ParseAddress calls
   */
  protected int getParseAddressFlags() {
    return FLAG_NO_IMPLIED_APT;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!isPageMsg(subject, body)) return false;
    
    // Starting with CAD: confuses things
    body = stripFieldStart(body, "CAD:");
    
    // Strip out any date and time fields
    Matcher match = DATE_PATTERN.matcher(body);
    if (match.find()) {
      data.strDate = match.group(1).replace('-', '/');
      body = match.replaceAll("");
    }
    match = TIME_PATTERN.matcher(body);
    if (match.find()) {
      String time = match.group(1);
      String ext = match.group(2);
      body = match.replaceAll("");
      if (ext != null) {
        if (ext.charAt(1) == 'P') {
          int pt = time.indexOf(':');
          int hr = Integer.parseInt(time.substring(0,pt));
          hr += 12;
          time = Integer.toString(hr) + time.substring(pt);
        }
      }
      data.strTime = time;
    } else {
      match = TIME2_PATTERN.matcher(body);
      if (match.find()) {
        data.strTime = match.group(1) + ':' + match.group(2);
        body = match.replaceAll("");
      }
    }
    
    match = MAP_PATTERN.matcher(body);
    if (match.find()) {
      data.strMap = match.group(1);
      body = match.replaceAll("");
    }
    
    // Parse text into different fields separated by delimiters
    // that match DELIM_PATTERN
    match = DELIM_PATTERN.matcher(body);
    String nextDelim = "";
    FieldType nextType = null;
    int pt = 0;

    String last = null;

    boolean keywordDesc = false;
    boolean foundAddr = false;
    Result bestRes = null;
    String bestAddr = null;
    String secondAddr = null;

    while (nextDelim != null) {
      
      // Parse the next field
      String delim = nextDelim;
      FieldType type = nextType;
      
      String fld;
      if (match.find(pt)) {
        nextDelim = match.group();
        fld = body.substring(pt, match.start());
        pt = match.end();
        
        // Check delimiter to see if this is something we recognize?
        if (nextDelim.endsWith(":")) {
          nextType = keywordMap.get(nextDelim.substring(0, nextDelim.length()-1).toUpperCase());
        } else if (nextDelim.endsWith("#")) {
          nextType = FieldType.ID;
        } else {
          nextType = null;
        }
      } else {
        nextDelim = null;
        nextType = null;
        fld = body.substring(pt);
      }
      fld = fld.trim().replaceAll("  +", " ");
      
      // fld is the data field being processed
      // delim is the delimiter that started this field
      
      // If zero length field, nothing to worry about
      if (fld.length() == 0) continue;
      
      // If field has a value of "CROSS" and the next delimiter is a cross
      // street delimiter, ignore it
      if (fld.equalsIgnoreCase("CROSS") && nextType == FieldType.CROSS) continue; 
      
      // If we have found a recognizable keyword, assign this field to that keyword value
      if (type != null) {
        
        switch (type) {
        
        case SKIP:
          break;
          
        case CALL:
          // Call description, ignore if less than 3 characters
          if (fld.length() <= 2) break;
          keywordDesc = true;
          
          // If we already have a call desc, append this to it
          if (data.strCall.length() > 0) {
            data.strCall += " / ";
            data.strCall += fld;
          }
          
          // Otherwise run it through the smart parser just in case the
          // call desc is followed by an address
          else {
            parseAddress(StartType.START_CALL, getParseAddressFlags(), fld, data);
            if (data.strSupp.length() == 0) data.strSupp = getLeft();
          }
          break;
          
        case PLACE:
          if (data.strPlace.length() == 0) data.strPlace = fld;
          break;
          
        case ADDRESS:
          // We  might have  multiple address fields
          // Keep track of which one looks like the best address
          // Also keep the first second place address in case we want to
          // use it as a call description
          foundAddr = true;
          StartType st = (data.strCall.length() == 0 ? StartType.START_CALL :
                          data.strPlace.length() == 0 ? StartType.START_PLACE : StartType.START_OTHER);
          Result res = parseAddress(st, getParseAddressFlags(), fld);
          if (bestRes == null || res.getStatus() > bestRes.getStatus()) {
            if (secondAddr == null) secondAddr = bestAddr;
            bestAddr = fld;
            bestRes = res;
          } else {
            if (secondAddr == null) secondAddr = fld;
          }
          
          // If we have some leading call info, use it.  If not, 
          // treat the last unidentified field as the call
          if (data.strCall.length() == 0 && last != null) data.strCall = last;
          break;
          
        case CITY:
          data.strCity = fld;
          break;
          
        case APT:
          if (data.strApt.length() > 0) data.strApt += "-";
          data.strApt += fld;
          break;
          
        case CROSS:
          if (data.strCross.length() > 0) data.strCross += " & ";
          data.strCross += fld;
          break;
          
        case BOX:
          data.strBox = fld;
          break;
          
        case UNIT:
          data.strUnit = fld;
          break;
          
        case MAP:
          data.strMap = fld;
          break;
          
        case ID:
          data.strCallId = fld;
          break;
          
        case PHONE:
          data.strPhone = fld;
          break;
          
        case SUPP:
          if (data.strSupp.length() > 0) data.strSupp += " / ";
          data.strSupp += fld;
          break;
          
        case CODE:
          data.strCode = fld;
          break;
          
        case SRC:
          data.strSource = fld;
          break;
          
        case NAME:
          data.strName = fld;
          break;
          
        }
      }

      // Otherwise we have to try to figure out fields by position
      else {
        
        // If we haven't found an address yet, see if this is it
        if (!foundAddr) {
          StartType st = (data.strCall.length() == 0 ? StartType.START_CALL :
                          data.strPlace.length() == 0 ? StartType.START_PLACE : StartType.START_OTHER);
          Result res = parseAddress(st, getParseAddressFlags(), fld);
          if (res.isValid()) {
            // Bingo!  Anything past the address goes into info
            foundAddr = true;
            res.getData(data);
            
            // If we have some leading call info, use it.  If not, 
            // treat the last unidentified field as the call
            if (data.strCall.length() == 0 && last != null) data.strCall = last;
            
            // If we got any call info from anywhere, extra stuff goes into info 
            // otherwise it goes into the call description
            if (data.strCall.length() == 0) {
              data.strCall =res.getLeft();
            } else {
              data.strSupp = res.getLeft();
            }
            continue;
          }
        }
        
        // Does this look like a call ID?
        if (data.strCallId.length() == 0 && CALL_ID_PATTERN.matcher(fld).matches()) {
          data.strCallId = fld;
          continue;
        }
        
        // Nope, lets see if it looks like a unit list
        if (UNIT_PATTERN.matcher(fld + " ").matches()) {
          data.strUnit = append(data.strUnit, ",", fld);
          continue;
        }
        
        // None of the above
        // If we haven't found an address yet, save this field for use as a possible
        // call field when we find the address. 
        if (! foundAddr) {
          last = fld;
          continue;
        }
        
        // If field has an &, assume it is a set of cross streets
        if (data.strCross.length() == 0 && fld.contains("&")) {
          data.strCross = fld;
          continue;
        }
        
        // If field parse out as an complete address
        // Assume it is a cross street
        if (isValidAddress(fld)) {
          if (data.strCross.length() > 0) data.strCross += " & ";
          data.strCross += fld;
          continue;
        }
        
        // If we have found an address but have no call description, assume this is it
        if (data.strCall.length() == 0) {
          data.strCall = fld;
          continue;
        }
        
        // Otherwise bundle this into supplemental info
        StringBuilder sb = new StringBuilder(data.strSupp);
        if (sb.length() > 0 && delim.length()>0 && ",;\n".indexOf(delim.charAt(0))<0) sb.append(' ');
        if (sb.length() > 0 || 
            delim.length() > 0 && Character.isUpperCase(delim.charAt(0))) sb.append(delim);
        if (sb.length() > 0) sb.append(' ');
        sb.append(fld);
        data.strSupp = sb.toString();
      }
    }
    
    // next resolve any multiple address keyword fields
    if (bestRes != null) {
      String saveCall = data.strCall;
      bestRes.getData(data);
      if (data.strCall.length() == 0 || keywordDesc) data.strCall = saveCall;
      String supp = bestRes.getLeft();
      if (supp.length() > 0) {
        if (data.strSupp.length() > 0) {
          supp = supp + " / " + data.strSupp;
        }
        data.strSupp = supp;
      }

      // If there were multiple address keywords, the second best one
      // becomes the place.  Any existing place is demoted to a name
      if (secondAddr != null) {
        if (data.strPlace.length() > 0) data.strName = data.strPlace;
        data.strPlace =  secondAddr;
      }
    }

    // Return success if we found an address
    return data.strAddress.length() > 0;
  }
}
