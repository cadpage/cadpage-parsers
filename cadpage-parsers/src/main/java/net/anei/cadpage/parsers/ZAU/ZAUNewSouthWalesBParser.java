package net.anei.cadpage.parsers.ZAU;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SmartAddressParser;


public class ZAUNewSouthWalesBParser extends SmartAddressParser {
  
  private static final Pattern DATE_TIME_MARKER = Pattern.compile("^(?:(\\d\\d [A-Z][a-z]+ \\d{4}) )?(\\d\\d:\\d\\d:\\d\\d) ");
  private static final Pattern INCIDENT_CALL_PTN = Pattern.compile("^[ *]*(?:\\d+-[A-Z]{3}-\\d{4} \\d\\d?:\\d\\d:\\d\\d )?(?:([ A-Z]+?)[- ]+)??INCID?ENT (CALL|STOP)(?: *# *(\\d+))?\\b[-,;* ]*", Pattern.CASE_INSENSITIVE);
  private static final Pattern STOP_MSG_PTN = Pattern.compile("STOP MESSA?GA?E\\b[- ]*");
  private static final Pattern DELIM_PTN = Pattern.compile("[ -/*]+");
  private static final Pattern RESPOND_TO_PTN = Pattern.compile("^(.*?)(?:[-=] )?(?:PLEASE |PLS )?RE?SPO?ND TO (?:REPORT OF)?", Pattern.CASE_INSENSITIVE);
  private static final Pattern TRAIL_DATE_PTN = Pattern.compile("\\. *[\\d/]*$| *(?:\\d+-[A-Z][a-z]{2}-\\d{4} )?\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?$");
  private static final DateFormat DATE_FMT = new SimpleDateFormat("dd MMMMMMMM yyyy");
  private static final Pattern COMMA_PTN = Pattern.compile(" *,[ ,]*");
  private static final Pattern BAD_ADDRESS_PTN = Pattern.compile("|.* (?:CONTACT|FOR|AT)", Pattern.CASE_INSENSITIVE);
  
  boolean strict;
  
  public ZAUNewSouthWalesBParser() {
    this(false);
  }

  protected ZAUNewSouthWalesBParser(boolean strict) {
    super(ZAUNewSouthWalesParser.CITY_LIST, "", "NSW", CountryCode.AU);
    setFieldList("DATE TIME SRC ID CALL ADDR APT X PLACE CITY INFO");
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSpecialStreets("NEW ST");
    addRoadSuffixTerms("PDE", "HIGHWAY");
    removeWords("CL");
    this.strict = strict;
  }

  @Override
  public String getLocName() {
    return "New South Wales, AU";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = DATE_TIME_MARKER.matcher(body);
    if (!match.find()) return false;
    String date = match.group(1);
    if (date != null) setDate(DATE_FMT, date, data);
    data.strTime = match.group(2);
    body = body.substring(match.end()).trim();
    
    body = stripFieldEnd(body, ".");
    body = stripFieldEnd(body, " FIRECOMM");
    
    // There are two different optional keywords, at least one must be present
    // INCIDENT CALL
    boolean good = false;
    String callPrefix1 = "";
    String addr = body;
    match = INCIDENT_CALL_PTN.matcher(body);
    if (match.lookingAt()) {
      String src = match.group(1);
      callPrefix1 = match.group(2);
      data.strCallId = getOptGroup(match.group(3));
      good = true;;
      addr = body.substring(match.end());
      
      if (src != null) {
        src = src.toUpperCase();
        if (src.startsWith("STOP")) {
          callPrefix1 = "STOP";
          src = null;
        }
      }
      if (src == null) {
        src = SRC_CODES.getCode(addr.toUpperCase());
        if (src != null) {
          String tmp = addr.substring(src.length());
          if (tmp.length() == 0) {
            addr = tmp;
          } else {
            match = DELIM_PTN.matcher(tmp);
            if (match.lookingAt()) {
              addr = tmp.substring(match.end());
            } else {
              src = null;
            }
          }
        }
      }
      if (src != null) data.strSource = src;
    }
    
    // Check for stop message signature
    else {
      match = STOP_MSG_PTN.matcher(addr);
      if (match.lookingAt()) {
        good = true;
        callPrefix1 = "STOP";
        addr = addr.substring(match.end());
        
        String src = SRC_CODES.getCode(addr.toUpperCase(), true);
        if (src != null) {
          data.strSource = src;
          addr = addr.substring(src.length());
          match = DELIM_PTN.matcher(addr);
          if (match.lookingAt()) addr = addr.substring(match.end());
        }
      }
    }
    
    // and - RESPOND TO
    String callPrefix2 = "";
    match = RESPOND_TO_PTN.matcher(addr);
    if (match.lookingAt()) {
      callPrefix2 = match.group(1).trim();
      good = true;
      addr = addr.substring(match.end()).trim();
    } 
    else {
      String src = SRC_CODES.getCode(addr.toUpperCase(), true);
      if (src != null) {
        data.strSource = src;
        addr = addr.substring(src.length());
      }
      match = DELIM_PTN.matcher(addr);
      if (match.lookingAt()) addr = addr.substring(match.end());
      body = addr;
    }
    
    // If we are using the strict version of this parser, and have not identified
    // the positive signature, report this as a general alert
    if (strict && !good) {
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
      return true;
    }
    
    match = TRAIL_DATE_PTN.matcher(addr);
    if (match.find()) addr = addr.substring(0,match.start()).trim();
    
    addr = addr.replace(" X ", " & ").replace(" x ", " & ").replace(" 'X' ", " & ");
    
    // Comma's certain complicate things
    String[] parts = COMMA_PTN.split(addr);
    
    // Start by steping through all parts trying to identify 
    // a recognizable address or a city name.  We have to do this
    // in one pass so we can rule out city names that are the start
    // of legitimate highway names.
    int cityNdx = -1;
    int addrNdx = -1;
    int bestStatus = Integer.MIN_VALUE;
    Result bestResult = null;
    int flags = FLAG_CHECK_STATUS;
    if (!good) flags |= FLAG_IGNORE_AT;
    for (int j = 0; j<parts.length; j++) {
      String part = parts[j];
      
      // First check if this is a city in it's own right
      // which prempts legitimate city names that look like road names (VALLEY HEIGHTS)
      if (j > 0 && isCity(parts[j])) {
        data.strCity = part;
        cityNdx = j;
        break;
      }
      
      // Next try to parse this as an address
      Result res = parseAddress(StartType.START_CALL, flags, part);
      if (res.getStatus() > bestStatus) {
        addrNdx = j;
        bestResult = res;
        bestStatus = res.getStatus();
      }
      
      // If it wasn't a valid address, see if it starts with a real city name
      if (j > 0 && !res.isValid()) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, part, data);
        if (data.strCity.length() > 0) {
          data.strSupp = getLeft();
          cityNdx = j;
          break;
        }
      }
    }
    
    // If we found a city, parse it, and parse whatever was in front of it
    // as the address
    if (cityNdx >= 0) {
      addrNdx = cityNdx-1;
      parseAddress(StartType.START_CALL, FLAG_NO_CITY | FLAG_NEAR_TO_END | FLAG_ANCHOR_END, parts[addrNdx], data);
      if (data.strAddress.length() == 0) {
        data.strCall = "";
        parseAddress(parts[addrNdx], data);
      }
    }
    
    // Otherwise go with the best address part
    else {
      
      cityNdx = addrNdx;
      
      // If we have identified this as a dispatch alert
      // or if we found a decent address, parse the result
      if (good || bestResult.isValid()) {
        bestResult.getData(data);
        data.strSupp = bestResult.getLeft();
        match = DELIM_PTN.matcher(data.strSupp);
        if (match.lookingAt()) data.strSupp = data.strSupp.substring(match.end());
      } else {
        // Otherwise set everything up to fail and fall through
        // to a general alert
        addrNdx = 0;
        cityNdx = parts.length;
      }
    }
    
    // Next fill in the call description from any leading parts
    String call = "";
    for (int j = 0; j<addrNdx; j++) {
      call = append(call, ", ", parts[j]);
    }
    data.strCall = append(call, ", ", data.strCall);
    
    // And fill in the info field with any trailing parts
    for (int j = cityNdx+1; j<parts.length; j++) {
      data.strSupp = append(data.strSupp, ", ", parts[j]);
    }

    // Make any last minute adjustments to the call field
    data.strCall = append(callPrefix2, " - ", data.strCall);
    if (!callPrefix1.equalsIgnoreCase("CALL")) {
      data.strCall = append(callPrefix1, " - ", data.strCall);
    }
    if (data.strCall.length() == 0) {
      data.strCall = data.strSupp;
      data.strSupp = "";
    }
    
    // And correct misspelled city names
    if (data.strCity.equalsIgnoreCase("WINMALLEE")) data.strCity = "WINMALEE";
    
    // The moment  truth.  If we found one of the expected signatures, or
    // if we found any kind of address, return that result
    if (good || !BAD_ADDRESS_PTN.matcher(data.strAddress).matches()) return true;

    // Otherwise turn this into a general alert
    data.msgType = MsgType.GEN_ALERT;
    data.strCall = data.strSupp = data.strAddress = data.strApt = data.strCity = "" ;
    data.strSupp = body;
    return true;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BARNRYS REEF",
    "BUENA VISTA",
    "GREEN GULLY",
    "ILFORD SOFALA",
    "MUD HUT CREEK",
    "PUTTA BUCCA",
    "QUEENS PINCH",
    "ROCKY WATERHOLE",
    "SINGLES RIDGE",
    "SPRING RIDGE",
    "TARA LOOP",
    "YELLOW ROCK"
  };
  
  private static final CodeSet SRC_CODES = new CodeSet(
      "BERKSHIRE PK",
      "ERSKINE PK",
      "COOKS GAP",
      "ERSKINE PARK",
      "GULGONG",
      "HORSLEY PK",
      "LLANDILO",
      "LONDONDERRY",
      "MARSDEN PK",
      "MUDGEE",
      "MUDGEE MULLAMUDDY",
      "MUDGEE & MULLAMUDDY",
      "MULLAMUDDY",
      "MULLAMUDDY MUDGEE",
      "MULLAMUDDY & MUDGEE",
      "ORCHARD HILLS",
      "PLUMPTON",
      "REGENTVILLE",
      "SCHOFIELDS",
      "SHANES PK",
      "WALLACIA",
      "WIN",
      "WINMALEE",
      "WINMALEE"
  );
  
}
