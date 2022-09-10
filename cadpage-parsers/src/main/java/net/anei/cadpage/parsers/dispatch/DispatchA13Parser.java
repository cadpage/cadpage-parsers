package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;




public class DispatchA13Parser extends FieldProgramParser {
  
  // Construct flag indicating address field may contain a leading place/name field
  public static final int A13_FLG_LEAD_PLACE_NAME = 1;
  public static final int A13_FLG_LEAD_PLACE = 2;
  public static final int A13_FLG_TRAIL_PLACE = 4;
  
  private static final String PROGRAM = "SRCID+? ( UNIT TRANSPORT! ADDR END | STATUS CALL/SDS! ADDR X:GPS1 Y:GPS2 INFO/N+ )";
  
  private Properties cityCodes = null;
  private boolean checkCity;
  private boolean leadPlaceName;
  private boolean leadPlace;
  private boolean trailPlace;
  
  public DispatchA13Parser(String defCity, String defState) {
    this(defCity, defState, 0);
  }
  
  public DispatchA13Parser(Properties cityCodes, String defCity, String defState) {
    this(cityCodes, defCity, defState, 0);
  }
  
  public DispatchA13Parser(String[] cityList, String defCity, String defState) {
    this(cityList, defCity, defState, 0);
  }
  
  public DispatchA13Parser(String defCity, String defState, int flags) {
    super(defCity, defState, PROGRAM);
    checkCity = false;
    setupFlags(flags);
  }
  
  public DispatchA13Parser(Properties cityCodes, String defCity, String defState, int flags) {
    super(cityCodes, defCity, defState, PROGRAM);
    this.cityCodes = cityCodes;
    this.checkCity = (cityCodes != null);
    setupFlags(flags);
  }
  
  public DispatchA13Parser(String[] cityList, String defCity, String defState, int flags) {
    super(cityList, defCity, defState, PROGRAM);
    this.checkCity = (cityList != null);
    setupFlags(flags);
  }
  
  private void setupFlags(int flags) {
    leadPlaceName = ((flags & A13_FLG_LEAD_PLACE_NAME) != 0);
    leadPlace = ((flags & A13_FLG_LEAD_PLACE) != 0);
    trailPlace = ((flags & A13_FLG_TRAIL_PLACE) != 0);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.contains("\n")) {
      return parseFields(body.split("\n"), data);
    }
    
    return parseStrippedMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("SRCID")) return  new SourceIdField();
    if (name.equals("TRANSPORT")) return new CallField("Transport", true);
    if (name.equals("STATUS")) return new BaseStatusField();
    if (name.equals("ADDR")) return new BaseAddressField(false);
    if (name.equals("GPS1")) return new BaseGPSField(1);
    if (name.equals("GPS2")) return new BaseGPSField(2);
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }
  
  // SRCID field contains source and call ID
  private static final Pattern SRCID_PTN = Pattern.compile("([A-Z0-9][- A-Za-z0-9]+)?:(\\d+[:0-9]*)|(([A-Z]{2,4})\\d{9})");
  private class SourceIdField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = SRCID_PTN.matcher(field);
      if (!match.matches()) return false;
      String src = match.group(1);
      if (src != null) data.strSource = src;
      String callId = match.group(2);
      if (callId != null) {
        data.strCallId = callId;
      } else {
        data.strCallId = match.group(3);
        data.strSource = match.group(4);
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "SRC ID";
    }
  }
  
  private static final Pattern STATUS_PTN = Pattern.compile("\\b(?:Dispatched|Req[ _]?Dispatch|(Acknowledge|Enroute|En Route Hosp|Responding|On *Scene|Standby|Standing ?By|Stack|In Command|Staged|Staging|ArrivedAtDestination|Transport)|(Disposed|Terminated))\\b", Pattern.CASE_INSENSITIVE);
  private class BaseStatusField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = STATUS_PTN.matcher(field);
      if (!match.matches()) abort();
      String call = match.group(1);
      if (call == null) {
        call = match.group(2);
        if (call != null) data.msgType = MsgType.RUN_REPORT;
      }
      if (call != null) data.strCall = call;
    }

    @Override
    public String getFieldNames() {
      return "CALL";
    }
  }
  
  // Address field contains address, city, and possibly cross streets
  // and now an optional leading place name :(
  private static final Pattern NUMBER_COMMA_PTN = Pattern.compile("([-\\d]+), *(.*)");
  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");
  private static final Pattern APT_PREFIX_PTN = Pattern.compile("^(?:APT|RM|ROOM|LOT) *");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) *(.*)|BLDG?.*|.* FLR|.* FLOOR");
  private static final Pattern BREAK_PTN = Pattern.compile(" *(?:[,;]) *");
  private static final Pattern INCOMPLETE_NAME_PTN = Pattern.compile("(?:Dr\\.? )?[A-Z][A-Za-z]*");
  private static final Pattern TRAIL_NAME_PTN = Pattern.compile("(.*) # ([A-Z][-a-z]+)");
  protected class BaseAddressField extends AddressField {
    
    private boolean includeCall;
    
    public BaseAddressField() {
      this(false);
    }
    
    public BaseAddressField(boolean includeCall) {
      this.includeCall = includeCall;
    }
    
    @Override
    public void parse(String field, Data data) {
      
      field = field.replace("\\\\", "&").replace('\\', '&');
      
      // And some agencies have a comma following the initial street number
      Matcher match = NUMBER_COMMA_PTN.matcher(field);
      if (match.matches()) field = match.group(1) + ' ' + match.group(2);
      
      // Break address field into stuff before, inside, and after two sets of parenthesis
      Parser p = new Parser(field);
      String sPart1 = p.get('(');
      String sPart2 = p.getSmart(')');

      String sPart3 = p.get('(');
      String sPart4 = p.getSmart(')');
      String sPart5 = p.get();
      
      if (includeCall) {
        int pt = sPart1.indexOf('@');
        if (pt >= 0) {
          data.strCall = append(data.strCall, " - ", sPart1.substring(0,pt).trim());
          sPart1 = sPart1.substring(pt);
        }
      }
     
      // If first part starts with @, it contains a place name
      // and the second part is the address
      // Following parts contain cross streets, city names, or plain info
      if (sPart1.startsWith("@")) {
        sPart1 = stripApt(sPart1.substring(1).trim(), data);
        int pt = sPart1.lastIndexOf(',');
        if (pt >= 0) {
          String place = sPart1.substring(0,pt).trim();
          String addr = sPart1.substring(pt+1).trim();
          if (isValidAddress(addr)) {
            data.strPlace = place;
            parseAddress(addr, data);
          } else {
            pt = -1;
          }
        }
        if (pt < 0) {
          data.strPlace = sPart1;
          if (sPart3.length() == 0 && sPart4.length() > 0 && checkAddress(sPart4) > checkAddress(sPart2)) {
            data.strPlace = data.strPlace + '(' + sPart2 + ')';
            sPart2 = stripApt(sPart4, data);
            sPart4 = "";
          }
          parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, sPart2, data);
          sPart2 = "";
        }
      }
      
      // Otherwise, first part is the address and city and occasionally
      // a cross street
      else {
        
        // A leading place name that contains parenthesis really messes
        // things up.  
        if ((leadPlaceName || leadPlace) && sPart3.length() > 0) {
          String tmp3 = sPart3;
          int pt = tmp3.indexOf('#');
          if (pt >= 0) tmp3 = tmp3.substring(0,pt).trim();
          Result res3 = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, tmp3);
          if (res3.getStatus() >= STATUS_INTERSECTION) {
            sPart1 = sPart1 + " (" + sPart2 + ") " + sPart3;
            sPart2 = sPart4;
            sPart3 = sPart5;
            sPart4 = sPart5 = "";
          }
        }
        if (sPart1.length() == 0) {
          if (sPart2.length() > 0) {
            sPart1 = sPart2;
            sPart2 = "";
          } else if (sPart3.length() > 0) {
            sPart1 = sPart3;
            sPart3 = "";
          } else if (sPart4.length() > 0) {
            sPart1 = sPart4;
            sPart4 = "";
          }
          else return;
        }
        
        String addr = null;
        boolean incomplete = false;
        boolean startName = false;
        for (String fld : BREAK_PTN.split(sPart1)) {
          
          if (startName) {
            incomplete = true;
            startName = false;
          }
          
          if (addr != null && leadPlaceName && data.strName.length() == 0) {
            match = TRAIL_NAME_PTN.matcher(fld);
            if (match.matches()) {
              fld = match.group(1).trim();
              data.strName = match.group(2).trim();
              startName = true;
            }
          }
          fld = stripApt(fld, data);
          if (fld.length() == 0) continue;
          
          // First part is going to be parsed as an address
          // If we are looking for leading name, it might contain a comma
          if (addr == null) {
            addr = fld;
            if ((leadPlaceName || leadPlace) && INCOMPLETE_NAME_PTN.matcher(fld).matches()) incomplete = true;
            continue;
          }
          if (incomplete) {
            if (data.strName.length() > 0) {
              data.strName = append(data.strName, ", ", fld);
            } else {
              addr = addr + ", " + fld;
            }
            incomplete = false;
            continue;
          }
          
          match = APT_PTN.matcher(fld);
          if (match.matches()) {
            String apt = match.group(1);
            if (apt == null) apt = fld;
            data.strApt = append(data.strApt, "-", apt);
            continue;
          }
          
          if (fld.equals("NE") || fld.equals("NW") || fld.equals("SE") || fld.equals("SW")) {
            addr = addr + " - " + fld;
            continue;
          }
          
          if (data.strCity.length() == 0) {
            boolean isCity;
            if (checkCity) {
              isCity = isCity(fld);
            } else {
              isCity = !fld.contains("/") && !fld.contains(";") && !fld.contains("&");
            }
            if (isCity) {
              if (cityCodes != null) fld = convertCodes(fld, cityCodes);
              data.strCity = fld;
              continue;
            }
          }
          
          if (data.strState.length() == 0 && 
              STATE_PTN.matcher(fld).matches()) {
            data.strState = fld;
            continue;
          }
          if (fld.startsWith("X ")) fld = fld.substring(2).trim();
          data.strCross = append(data.strCross, " / ", fld);
        }
        
        if (addr != null) {
          StartType st = (includeCall && (leadPlaceName | leadPlace) ? StartType.START_CALL_PLACE :
                          includeCall ? StartType.START_CALL :
                          leadPlaceName || leadPlace ? StartType.START_OTHER 
                                                     : StartType.START_ADDR);
          int flags = FLAG_IGNORE_AT;
          if (includeCall) flags |= FLAG_START_FLD_REQ;
          flags |= (trailPlace ? FLAG_PAD_FIELD : FLAG_ANCHOR_END);
          String saveCall = null;
          if (includeCall) {
            saveCall = data.strCall;
            data.strCall = "";
          }
          if (data.strCity.length() > 0) flags |= FLAG_NO_CITY;
          String place;
          if (addr != null) parseAddress(st, flags, addr, data);
          if (includeCall) {
            data.strCall = append(saveCall, " - ", data.strCall);
            place = data.strPlace;
            data.strPlace = "";
          } else {
            place = getStart();
          }
          if (place.length() > 0) {
            if (data.strAddress.length() == 0) {
              parseAddress(place, data);
            }
            else if (leadPlace || checkPlace(place)) {
              addPlace(place, data);
            }
            else {
              data.strName = place;
            }
          }
        }
        
        // another oddity, sometimes the a city (and not necessarily the same city)
        // occurs at the end of both streets in an intersection, so will try to 
        // eliminate the extra ones
        if (data.strCity.length() > 0) {
          int pt2 = data.strAddress.indexOf("&");
          if (pt2 >= 0) {
            String part1 = data.strAddress.substring(0,pt2).trim();
            String part2 = data.strAddress.substring(pt2+1).trim();
            data.strAddress = "";
            String saveCity = data.strCity;
            parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, part1, data);
            data.strAddress = append(data.strAddress, " & ", part2);
            if (data.strCity.length() == 0) {
              data.strCity = saveCity;
            } else if (saveCity.length() > 0 && !saveCity.equalsIgnoreCase(data.strCity)) {
              data.strCity = "";
            }
          }
          data.strAddress = data.strAddress.replace(' ' + data.strCity + " &", " &");
        }
        
        // If there might be a trailing place, pick it up and process it
        // If it starts with a slash, this is probably the intersection with
        // cities after both streets again :(
        if (trailPlace) {
          String place = getPadField();
          if (isCity(place)) {
            data.strCity = place;
          } else {
            addPlace(getPadField(), data);
          }
          place = getLeft();
          if (place.startsWith("/") ||   place.startsWith("&")) {
            place = place.substring(1).trim();
            String saveAddr = data.strAddress;
            String saveCity = data.strCity;
            data.strAddress = "";
            parseAddress(StartType.START_ADDR, FLAG_IGNORE_AT, place, data);
            data.strAddress = append(saveAddr, " & ", data.strAddress);
            if (data.strCity.length() == 0) {
              data.strCity = saveCity;
            } else if (saveCity.length() > 0 && !saveCity.equalsIgnoreCase(data.strCity)) {
              data.strCity = "";
            }
            place = getLeft();
          }
          if (isCity(place)) {
            data.strCity = place;
          } else {
            addPlace(place, data);
          }
        }
        
        // Second part is generally the cross street
        // But if it does not contain a slash or semicolon, and the
        // address isn't a recognizable address, swap this for the address
        sPart2 = stripNearPlace(sPart2, data);
        if (sPart2.length() > 0) {
          if (data.strCity.length() == 0) {
            int pt = sPart2.lastIndexOf(',');
            if (pt >= 0) {
              String city = sPart2.substring(pt+1).trim();
              city = stripFieldStart(city, "/");
              city = stripFieldEnd(city, "/");
              if (!checkCity || isCity(city)) {
                data.strCity = city;
                sPart2 = sPart2.substring(0,pt).trim();
              }
            }
          }
          if (!sPart2.contains("/") && !sPart2.contains(";") &&
              data.strPlace.length() == 0 && !isValidAddress(data.strAddress) &&
              checkAddress(sPart2, 1) >= STATUS_FULL_ADDRESS) {
            data.strPlace = data.strAddress;
            data.strAddress = "";
            parseAddress(sPart2, data);
          } else {
            sPart2 = stripFieldStart(sPart2, "/");
            sPart2 = stripFieldEnd(sPart2, ";");
            sPart2 = stripFieldEnd(sPart2, "/");
            data.strCross = append(data.strCross, " & ", sPart2);
          }
        }
        sPart2 = "";
      }

      // The rest contains city names, cross streets and/or supp info
      for (String part : new String[]{sPart2, sPart3, sPart4, sPart5}) {
        part = stripNearPlace(part, data);
        if (part.length() == 0) continue;
        if (data.strCity.length() == 0) {
          int pt = part.lastIndexOf(',');
          if (pt >= 0) {
            String city = part.substring(pt+1).trim();
            if (!checkCity || isCity(city)) {
              data.strCity = city;
              part = part.substring(0,pt).trim();
            }
          }
        }
        part = stripApt(part, data);
        int pt = part.lastIndexOf("- ");
        if (pt >= 0) {
          addPlace(part.substring(pt+2).trim(), data);
          part = part.substring(0,pt).trim();
        }
        if (part.length() == 0) continue;
        if (part.contains("/")) {
          data.strCross = append(data.strCross, "/", part);
        } else if (part.startsWith("X ")) {
          data.strCross = append(data.strCross, "/", part.substring(2).trim());
        }
        else if (data.strCity.length() == 0  &&
                 (!checkCity || isCity(part))) {
          data.strCity = part;
        } else if ((match = APT_PTN.matcher(part)).matches()) {
          String apt = match.group(1);
          if (apt == null) apt = part;
          data.strApt = append(data.strApt, "-", apt);
        }
        else {
          addPlace(part, data);
        }
      }
      
      // Very occasionally, the city name will end up in the APT field
      if (data.strCity.length() == 0 && data.strApt.length() > 0) {
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, data.strApt, data);
        data.strApt = getStart();
      }
      
      // Occasionally a city name still makes it into the cross streets :(
      StringBuilder sb = new StringBuilder();
      for (String part : data.strCross.split("[,/]")) {
        part = part.trim();
        if (part.length() == 0) continue;
        if (part.equalsIgnoreCase(data.strCity)) continue;
        if (data.strCity.length() == 0 && checkCity && isCity(part.toUpperCase())) {
          data.strCity = part;
        } else {
          if (sb.length() > 0) sb.append('/');
          sb.append(part);
        }
        data.strCross = sb.toString();
      }
      
      // If the address is a naked ampersand, try to parse the address from the place field
      if (data.strAddress.equals("&") && data.strPlace.length() > 0) {
        data.strAddress = "";
        parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, data.strPlace, data);
        data.strPlace = "";
      }
    }
    
    private String stripApt(String part, Data data) {
      int pt = part.indexOf('#');
      if (pt >= 0) {
        String apt = part.substring(pt+1).trim();
        if (apt.startsWith("PVT")) {
          addPlace(apt, data);
        } else { 
          if (checkCity && data.strCity.length() == 0) {
            parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, apt, data);
            apt = getStart();
          }
          apt = APT_PREFIX_PTN.matcher(apt).replaceAll("");
          data.strApt = append(data.strApt, " - ", apt);
        }
        part = part.substring(0,pt).trim();
      }
      return part;
    }

    private String stripNearPlace(String part, Data data) {
      int pt = part.indexOf("; Near:");
      if (pt >= 0) {
        addPlace(part.substring(pt+2), data);
        part = part.substring(0,pt).trim();
      }
      part = stripFieldStart(part, ";");
      return part;
    }
    
    private void addPlace(String place, Data data) {
      if (place.length() == 0) return;
      if (place.equals(data.strPlace)) return;
      data.strPlace = append(data.strPlace, " - ", place);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE NAME ADDR CITY ST APT X INFO";
    }
  }
  
  private class BaseGPSField extends GPSField {
    
    private int type;
    
    public BaseGPSField(int type) {
      this.type = type;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (type == 1) {
        data.strGPSLoc = field;
      }
      else if (type == 2) {
        field = data.strGPSLoc +',' + field;
        data.strGPSLoc = "";
        setGPSLoc(field, data);
      }
    }
  }
  
  private static final Pattern INFO_PREFIX_PTN = Pattern.compile("^\\[\\d{3,}\\] *");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Response Notes:")) return;
      if (field.equals("Incident Notes:")) return;
      field = INFO_PREFIX_PTN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
  }
  
  private boolean fieldsInitialized = false;
  private SourceIdField sourceIdField;
  private BaseStatusField statusField;
  private BaseAddressField addressField;
  private String fieldList;
  
  /**
   * Parse message that has been stripped of all field breaks :(
   */
  private boolean parseStrippedMsg(String body, Data data) {
    
    
    if (!fieldsInitialized) {
      fieldsInitialized = true;
      sourceIdField = new SourceIdField();
      statusField = new BaseStatusField();
      addressField = new BaseAddressField(true);
      
      fieldList = sourceIdField.getFieldNames()+' '+statusField.getFieldNames()+' '+addressField.getFieldNames();
    }
    
    setFieldList(fieldList);
    
    try {
      body = stripFieldStart(body, "?");
      Matcher match = STATUS_PTN.matcher(body);
      if (!match.find()) return false;
      String callId = body.substring(0, match.start()).trim();
      for (String id : callId.split(" ")) {
        sourceIdField.parse(id, data);
      }
      
      statusField.parse(match.group(), data);
      
      addressField.parse(body.substring(match.end()).trim(), data);
      
      return true;
    }
    
    catch (FieldProgramException ex) {
      return false;
    }
  }
}
	