package net.anei.cadpage.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser.CountryCode;

/**
 * This class contains all of the useful data fields that are parsed from
 * the actual message text
 */
public class MsgInfo {

  // Flags that will be queried by the MsgParser.getMapFlags() method
  // the alter aspects of the map query string generation

  // Suppress LA -> LN translation
  public static final int MAP_FLG_SUPPR_LA = 1;

  // Remove EXT
  public static final int MAP_FLG_REMOVE_EXT = 2;

  // Add default city/county in back of parsed city/county
  public static final int MAP_FLG_ADD_DEFAULT_CNTY = 4;

  // Suppress NO EO SO WO -> & translation
  public static final int MAP_FLG_SUPPR_DIRO = 8;

  // Suppress inclusion of place name in front of naked street names
  public static final int MAP_FLG_SUPPR_AND_ADJ = 0x10;

  // Recommend use of GPS coordinates for mapping
  public static final int MAP_FLG_PREFER_GPS = 0x20;

  // Suppress SR -> ST translation
  public static final int MAP_FLG_SUPPR_SR = 0x40;

  // convert CR -> CRES instead of CIR
  public static final int MAP_FLG_CR_CRES = 0x80;

  // Suppress conversion of various forms of CR XX to COUNTY ROAD XX
  public static final int MAP_FLG_SUPPR_CR = 0x100;

  // convert CR -> CREEK instead of CIRCLE
  public static final int MAP_FLG_CR_CREEK = 0x200;

  // Suppress TE -> TER translation
  public static final int MAP_FLG_SUPPR_TE = 0x400;

  // Preserve STATE HIGHWAY construct
  public static final int MAP_FLG_KEEP_STATE_HIGHWAY = 0x800;

  public enum MsgType { PAGE, GEN_ALERT, RUN_REPORT };

  private MsgType msgType;
  private String strCall;
  private String strPlace;
  private String strAddress;
  private String strCity;
  private String strApt;
  private String strCross;
  private String strBox;
  private String strUnit;
  private String strState;
  private String strMap;
  private String strCallId;
  private String strPhone;
  private String strSupp;
  private String strCode;
  private String strSource;
  private String strName;
  private String strPriority;
  private String strChannel;
  private String strGPSLoc;
  private String strDate;
  private String strTime;
  private String strInfoURL;
  private String strAlert;
  private String defCity;
  private String defState;
  private MsgParser.CountryCode countryCode;
  private boolean preferGPSLoc;
  private MsgParser.MapPageStatus mapPageStatus;

  // Flag set when parser determines that message is incomplete
  // and another part should be expected
  private boolean expectMore;

  // Cached map address & map city & map page URL
  private String strBaseMapAddress = null;
  private String strMapCity = null;
  private String mapPageURL = null;

  // Parser code of parser used to create this information block
  private String parserCode;

  // Parser to be used for any followup processing
  // This is usually the same as the parser used to create this information block
  // The exception is the Cadpage family of parsers that are relaying information
  // created by another primary parser running on a vendor server.  In that
  // situation, parserCode refers to the Cadpage class parser that parsed the
  // information and parser points to the original information parser
  private MsgParser parser;


  /**
   * Temporary data class used to pass information to constructor
   * @author ken
   *
   */
  public static class Data {
    public MsgType msgType;
    public String strCall;
    public String strPlace;
    public String strAddress;
    public String strCity;
    public String strApt;
    public String strCross;
    public String strBox;
    public String strUnit;
    public String strState;
    public String strMap;
    public String strCallId;
    public String strPhone;
    public String strSupp;
    public String strCode;
    public String strSource;
    public String strName;
    public String strPriority;
    public String strChannel;
    public String strGPSLoc;
    public String strDate;
    public String strTime;
    public String strInfoURL;
    public String strAlert;
    public String defCity;
    public String defState;
    public MsgParser.CountryCode countryCode;
    public boolean preferGPSLoc;
    MsgParser.MapPageStatus mapPageStatus;

    public boolean expectMore;

    public String parserCode;
    public MsgParser parser;
    public String strBaseMapAddress;
    public String strMapCity;
    public String mapPageURL;

    public Data(MsgParser parser) {
      initialize(parser);
    }

    /**
     * Initialize existing Data structure to original state
     */
    public void initialize(MsgParser parser) {
      expectMore = false;

      msgType = MsgType.PAGE;
      strCall = "";
      strPlace = "";
      strAddress = "";
      strCity = "";
      strApt = "";
      strCross = "";
      strBox= "" ;
      strUnit= "" ;
      strState="";
      strMap = "";
      strCallId = "";
      strPhone="";
      strSupp="";
      strCode="";
      strSource = "";
      strName = "";
      strPriority = "";
      strChannel = "";
      strGPSLoc = "";
      strDate = "";
      strTime = "";
      strInfoURL = "";
      strAlert = "";
      defCity = "";
      defState="";
      countryCode = MsgParser.CountryCode.US;
      preferGPSLoc = false;
      mapPageStatus = null;

      this.parser = parser;
      if (parser != null) {
        this.parserCode = parser.getParserCode();
        this.defCity = parser.getDefaultCity();
        this.defState = parser.getDefaultState();
        this.countryCode = parser.getCountryCode();
        this.preferGPSLoc = (parser.getMapFlags() & MAP_FLG_PREFER_GPS) != 0;
        this.mapPageStatus = parser.getMapPageStatus();
      }

      strBaseMapAddress = null;
      strMapCity = null;
      mapPageURL = null;
    }

    /**
     * Clear any information left over from a failed parse attempt, and
     * set the data field to return a general alert status
     * @param message message to be set as the general alert text
     * @returns always returns true
     */
    public boolean parseGeneralAlert(MsgParser parser, String message) {
      initialize(parser);
      msgType = MsgType.GEN_ALERT;
      strSupp = message;
      return true;
    }

    /**
     * Clear any information left over from a failed parse attempt, and
     * set the data field to return a RUN REPORT status
     * @param message message to be set as the general alert text
     * @returns always returns true
     */
    public boolean parseRunReport(MsgParser parser, String message) {
      String saveCallId = strCallId;
      initialize(parser);
      msgType = MsgType.RUN_REPORT;
      strCallId = saveCallId;
      strSupp = message;
      return true;
    }
  }

  /**
   * constructor
   * @param info data object containing all message information
   */
  public MsgInfo(Data info) {
    msgType = info.msgType;
    strCall = info.strCall;
    strPlace = info.strPlace;
    strAddress = info.strAddress;
    strCity = info.strCity;
    strApt = info.strApt;
    strCross = info.strCross;
    strBox = info.strBox;
    strUnit = info.strUnit;
    strState = info.strState;
    strMap = info.strMap;
    strCallId = info.strCallId;
    strPhone = info.strPhone;
    strSupp = info.strSupp;
    strCode = info.strCode;
    strSource = info.strSource;
    strName = info.strName;
    strPriority = info.strPriority;
    strChannel = info.strChannel;
    strGPSLoc = info.strGPSLoc;
    strDate = info.strDate;
    strTime = info.strTime;
    strInfoURL = info.strInfoURL;
    strAlert = info.strAlert;
    defCity = info.defCity;
    defState = info.defState;
    countryCode = info.countryCode;
    expectMore = info.expectMore;
    preferGPSLoc = info.preferGPSLoc;
    mapPageStatus = info.mapPageStatus;

    parserCode = info.parserCode;
    parser = info.parser;
    strBaseMapAddress = info.strBaseMapAddress;
    strMapCity = info.strMapCity;
    mapPageURL = info.mapPageURL;

    // Convert old General ALert or Run report convention to new standard
    if (msgType == MsgType.PAGE) {
      if (strCall.equals("GENERAL ALERT")) msgType = MsgType.GEN_ALERT;
      else if (strCall.equals("RUN REPORT")) msgType = MsgType.RUN_REPORT;
      if (msgType != MsgType.PAGE) {
        strCall = "";
        if (strSupp.length() == 0) {
          strSupp = strPlace;
          strPlace = "";
        }
      }
    }
  }

  /**
   * @return message type
   */
  public MsgType getMsgType() {
    return msgType;
  }

  /**
   * @return call ID
   */
  public String getCallId() {
    return strCallId;
  }

  /**
   * @return the Call description
   */
  public String getCall() {
    return strCall;
  }

  /**
   * @return extended call description
   */
  public String getExtendedCall() {
    if (msgType != MsgType.GEN_ALERT && msgType != MsgType.RUN_REPORT) return strCall;
    String call = msgType == MsgType.GEN_ALERT ? "GENERAL ALERT" : "RUN REPORT";
    if (strCall.length() > 0) {
      call = call + " - " + strCall;
    }
    return call;
  }

  /**
   * @return return place name
   */
  public String getPlace() {
    return strPlace;
  }

  /**
   * @return the call address
   */
  public String getAddress() {
    return strAddress;
  }

  /*
   * @return call title
   */
  public String getTitle() {
    StringBuilder sb = new StringBuilder();
    if (strPriority.length() > 0) {
      sb.append("P:");
      sb.append(strPriority);
      sb.append(' ');
    }
    if (strCode.length() > 0) {
      sb.append(strCode);
      sb.append(" - ");
    }
    if (noCall() && strSupp.length() > 0) {
      sb.append(strSupp);
    } else {
      sb.append(getExtendedCall());
    }
    return sb.toString();
  }

  public boolean noCall() {
    if (msgType == MsgType.GEN_ALERT || msgType == MsgType.RUN_REPORT) return false;
    return strCall.length() == 0 || strCall.equals("ALERT");
  }


  /**
   * Calculate map search string to be passed to Google maps
   * @param overrideCity if non-null, this value should override parser default city
   * @param overrideState if non-null, this value should override parser default state
   * @return return mapping address or null if there is no map address
   */
  public String getMapAddress(String overrideCity, String overrideState) {
    boolean prefGPSLoc = isPreferGPSLoc();
    String result = getMapAddress(prefGPSLoc, overrideCity, overrideState);
    if (result != null) return result;
    return getMapAddress(!prefGPSLoc, overrideCity, overrideState);
  }

  /**
   * Calculate map search string to be passed to Google maps
   * @param useGPS true to request GPS map coordinates,
   *  false to request regular street map address
   * @param overrideCity if non-null, this value should override parser default city
   * @param overrideState if non-null, this value should override parser default state
   * @return return mapping address or null if there is no map address
   */
  public String getMapAddress(boolean useGPS, String overrideCity, String overrideState) {
    if (useGPS) return (strGPSLoc.length() == 0 ? null : strGPSLoc);

    // if there is no base address, return null
    String mapAddr = getBaseMapAddress();
    if (mapAddr.length() == 0) return null;

    // If there is a GPS pattern in this, don't append anything
    if (MsgParser.GPS_PATTERN.matcher(mapAddr).find()) return mapAddr;


    StringBuilder sb = new StringBuilder(mapAddr);

    // Add city if specified, default city otherwise
    String city = getMapCity();
    if (city.equalsIgnoreCase("OUT OF COUNTY") ||
        city.equalsIgnoreCase("OUTSIDE COUNTY") ||
        city.equalsIgnoreCase("OUT OF AREA")) city = "";
    else if (city.length() == 0) {
      city = (overrideCity != null ? overrideCity : defCity);
    } else if (overrideCity == null && (parser.getMapFlags() & MAP_FLG_ADD_DEFAULT_CNTY) != 0 &&
                !city.contains(",")) {
      city = city.length() == 0 ? defCity : city + ',' + defCity;
    }

    if (city.length() > 0 && !city.equalsIgnoreCase("NONE")) {
      sb.append(",");
      sb.append(city);
    }

    // Add state if specified, default state otherwise
    String state = strState;
    if (state.length() == 0) {
      state = (overrideState != null ? overrideState : defState);
    }
    if (state.length() > 0 && !state.equalsIgnoreCase("NONE")) {
      sb.append(",");
      sb.append(state);
    }

    // Only add country for non-US addresses
    if (countryCode != MsgParser.CountryCode.US) {
      sb.append(",");
      sb.append(countryCode.toString());
    }

    return sb.toString();
  }

  /**
   * @return base mapping address, which is the adjusted address without
   * the additional city and state information
   */
  private static final Pattern UK_POST_CODE_PTN = Pattern.compile("^[A-Z]{1,2}\\d{1,2}[A-Z]? \\d[A-Z]{2} +");
  private static final Pattern DIR_OF_PTN = Pattern.compile(" [NSEW]O |(?: JUST)? (?:[NSEW]|NORTH|SOUTH|EAST|WEST) OF ", Pattern.CASE_INSENSITIVE);
  private static final Pattern NO_DIGIT_PTN = Pattern.compile("\\bNO *(\\d+)");
  private static final Pattern CROSS_DELIM = Pattern.compile("(?<=..)[&/,@]| - | AND ", Pattern.CASE_INSENSITIVE);
  private static final Pattern DEAD_END_PTN = Pattern.compile("END OF .*|DEAD END", Pattern.CASE_INSENSITIVE);
  public String getBaseMapAddress() {

    if (strBaseMapAddress != null) return strBaseMapAddress;

    if (strAddress.length() == 0) return strAddress;

    // See if we can find any GPS coordinates, if we do, return those
    String mapAddr = null;
    if (mapAddr == null) mapAddr = countryCode.parseGPSCoords(strAddress);
    if (mapAddr != null) {
      strBaseMapAddress = mapAddr;
      return mapAddr;
    }

    // If we have a parser, see if we have a GPS location match
    if (parser != null) {
      String addr = parser.lookupGpsCoordinates(strAddress, strApt, strPlace, strCity);
      if (addr != null) {
        strBaseMapAddress = addr;
        return addr;
      }
    }

    // Perform any parser specific customizations
    String sAddr = strAddress;
    if (parser != null) sAddr = parser.adjustMapAddress(sAddr, strCity, false);

    // UK addresses have a postal code prefix that we don't want to mess with
    String prefix = "";
    if (countryCode == MsgParser.CountryCode.UK){
      Matcher match = UK_POST_CODE_PTN.matcher(sAddr);
      if (match.find()) {
        prefix = sAddr.substring(0,match.end());
        sAddr = sAddr.substring(match.end());
      }
    }

    if ((parser.getMapFlags() & MAP_FLG_SUPPR_DIRO) == 0) {
      sAddr = DIR_OF_PTN.matcher(sAddr).replaceAll(" & ");
    } else {
      sAddr = NO_DIGIT_PTN.matcher(sAddr).replaceAll("$1");
    }
    sAddr = cleanParens(sAddr);
    sAddr = cleanStreetSuffix(sAddr);
    sAddr = cleanBlock(sAddr);
    sAddr = cleanBounds(sAddr);
    sAddr = cleanRoutes(sAddr);
    sAddr = cleanDoubleRoutes(sAddr);
    sAddr = cleanHouseNumbers(sAddr);
    sAddr = cleanInterstate(sAddr);
    sAddr = cleanOffRamp(sAddr);
    sAddr = cleanSTRoutes(sAddr);

    // Make sure & are surrounded by blanks
    // Except in Swedish where & can be part of a legitimate street name
    if (countryCode != CountryCode.SE) {
      sAddr = sAddr.replaceAll(" *&[& ]*", " & ");
    }

    // If there wasn't an address number or intersection marker in address
    // try appending cross street info as as intersection
    if (validAddress(sAddr)) {
      sAddr = prefix + sAddr;
    } else {
      if (strCross.length() > 0) {
        String sCross = strCross;
        if (parser != null) sCross = parser.adjustMapAddress(sCross, strCity, true);
        Matcher match = CROSS_DELIM.matcher(sCross);
        if (match.find()) {
          String sTemp = sCross.substring(0,match.start()).trim();
          if (sTemp.equals(sAddr)) {
            int pt1 = match.end();
            int pt2 = match.find() ? match.start() : sCross.length();
            sTemp = strCross.substring(pt1, pt2).trim();
          }
          sCross = sTemp;
        }

        // Don't use DEAD END or like phrases
        if (!DEAD_END_PTN.matcher(sCross).matches()) {
          sCross = cleanParens(sCross);
          sCross = cleanStreetSuffix(sCross);
          sCross = cleanBounds(sCross);
          sCross = cleanRoutes(sCross);
          sCross = cleanDoubleRoutes(sCross);
          sCross = cleanInterstate(sCross);
          sCross = cleanOffRamp(sCross);
          sCross = cleanSTRoutes(sCross);
          sCross = sCross.trim();
          if (sCross.length() > 0) sAddr = prefix + sAddr + " & " + sCross;
        }
      }

      // else just append prefix
      else {
        sAddr = prefix + sAddr;
      }
    }

    // ANy last minute parser adjustments go here
    if (parser != null) sAddr = parser.postAdjustMapAddress(sAddr);

    strBaseMapAddress = sAddr.trim();
    return strBaseMapAddress;
	}

  // Remove any nested parenthesis from potential address
  private String cleanParens(String sAddr) {
    if (!sAddr.contains("(") && !sAddr.contains("[")) return sAddr;

    StringBuilder sb = new StringBuilder();
    int level = 0;
    boolean brk = false;
    boolean lastBlank = false;
    char startBrk = 0, endBrk = 0;
    for (char chr : sAddr.toCharArray()){
      if (!brk) {
        if (chr == '(' || chr == '[') {
          brk = true;
          startBrk = chr;
          endBrk = chr == '(' ? ')' : ']';
        }
      }
      if (chr == startBrk) level++;
      else if (chr == endBrk) {
        if (level > 0) level--;
      } else if (level == 0) {
        if (brk) {
          brk = false;
          if (!lastBlank && chr != ' ') sb.append(' ');
          else if (lastBlank && chr == ' ') continue;
        }
        sb.append(chr);
        lastBlank = (chr == ' ');
      }
    }
    return (level == 0 ? sb.toString().trim() : sAddr);
  }

  // Clean up any street suffix abbreviations that Google isn't happy with
  private static final Pattern CR_PTN = Pattern.compile("\\bCR\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern CRNN_PTN = Pattern.compile("\\b(?:CR|COUNTY|(?:CO|CTY|CNTY|COUNTY)(?: *(?:RD|RT|RTE|ROAD|HWY|ROUTE))?)[- ]*(\\d+[A-Z]?|(?<= )[A-Z]|(?<= )([A-Z])\\2)(?: HWY)?\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern AV_PTN = Pattern.compile("\\bAV\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern HW_PTN = Pattern.compile("\\bH[WY]\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern PK_PTN = Pattern.compile("\\b(?:PI|PI?K|PKE)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern PW_PTN = Pattern.compile("\\bPK?[WY]\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern CI_PTN = Pattern.compile("\\bC[IR]\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern BLV_PTN = Pattern.compile("\\bBLV\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern TL_PTN = Pattern.compile("\\bTL\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern TRC_PTN = Pattern.compile("\\bTRC\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern CRSN_PTN = Pattern.compile("\\bCRSN\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern CG_PTN = Pattern.compile("\\bCG\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern BYP_PTN = Pattern.compile("\\bBY?P\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern LA_PTN = Pattern.compile("\\bLA\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern EXT_PTN = Pattern.compile(" EXT?\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SR_PTN = Pattern.compile("\\bSR\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern TP_PTN = Pattern.compile("\\b(?:TP|TRPK)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern PA_PTN = Pattern.compile("\\bPA\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern PLAZ_PTN = Pattern.compile("\\bPLAZ\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern NEAR_PTN = Pattern.compile("\\b(?:NEAR|OFF(?: OF)?)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern TE_PTN = Pattern.compile("\\bTE\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern WK_PTN = Pattern.compile("\\bWK\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern RCH_PTN = Pattern.compile("\\bRCH\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern HT_PTN = Pattern.compile("\\bHT\\b", Pattern.CASE_INSENSITIVE);
  private String cleanStreetSuffix(String sAddr) {

    // CR is a very versatile abbreviation.  In New Zealand, it is an abbreviation for Crescent
    if (countryCode == CountryCode.NZ || (parser.getMapFlags() & MAP_FLG_CR_CRES) != 0) {
      sAddr = CR_PTN.matcher(sAddr).replaceAll("CRES");
    }

    // Or sometimes creek
    if ((parser.getMapFlags() & MAP_FLG_CR_CREEK) != 0) {
      sAddr = CR_PTN.matcher(sAddr).replaceAll("CREEK");
    }


    // convert CR nn to COUNTY ROAD nn
    // we need to do this before we do the abbreviations conversions that will
    // change CR to CIR.
    if ((parser.getMapFlags() & MAP_FLG_SUPPR_CR) == 0) {
      sAddr = CRNN_PTN.matcher(sAddr).replaceAll("COUNTY ROAD $1");
    }

    sAddr = replace(sAddr, AV_PTN, "AVE");
    sAddr = replace(sAddr, HW_PTN, "HWY");
    sAddr = replace(sAddr, PK_PTN, "PIKE");
    sAddr = replace(sAddr, PW_PTN, "PKWY");
    sAddr = replace(sAddr, CI_PTN, "CIR");
    sAddr = replace(sAddr, BLV_PTN, "BLVD");
    sAddr = replace(sAddr, TL_PTN, "TRL");
    sAddr = replace(sAddr, TRC_PTN, "TRCE");
    sAddr = replace(sAddr, CRSN_PTN, "CRESCENT");
    sAddr = replace(sAddr, CG_PTN, "CROSSING");
    sAddr = replace(sAddr, BYP_PTN, "BYPASS");
    if ((parser.getMapFlags() & MAP_FLG_SUPPR_SR) == 0) {
      sAddr = replace(sAddr, SR_PTN, "ST");
    }
    sAddr = replace(sAddr, TP_PTN, "TPK");
    if (!defState.equals("PA")) sAddr = replace(sAddr, PA_PTN, "PATH");
    sAddr = replace(sAddr, PLAZ_PTN, "PLAZA");
    sAddr = replace(sAddr, WK_PTN, "WALK");
    sAddr = replace(sAddr, RCH_PTN, "REACH");
    sAddr = replace(sAddr, HT_PTN, "HTS");

    // Some alterations are suppressed by different parsers to meet local
    // requirements
    int mapFlags = parser.getMapFlags();
    if ((mapFlags & MAP_FLG_SUPPR_LA) == 0) sAddr = replace(sAddr, LA_PTN, "LN");
    if ((mapFlags & MAP_FLG_REMOVE_EXT) != 0) sAddr = EXT_PTN.matcher(sAddr).replaceAll("");
    if ((mapFlags & MAP_FLG_SUPPR_TE) == 0 && countryCode != CountryCode.NZ) sAddr = replace(sAddr, TE_PTN, "TER");

    if (!sAddr.contains("CUT OFF")) {
      sAddr = NEAR_PTN.matcher(sAddr).replaceAll("&");
    }
    return sAddr;
  }

  /**
   * Replace all occurrences of a given pattern in an address string with
   * a replacement string.  The case of characters in the replacement string
   * will be adjusted to best match the case of the characters being replaced
   * @param address address line in which pattern sequence should be replaced
   * @param pat pattern to be searched for
   * @param replace string that should replace all occurrences of pattern
   * @return the adjusted address string
   */
  private String replace(String address, Pattern pat, String replace) {
    StringBuffer sb = null;
    Matcher match = pat.matcher(address);
    while (match.find()) {
      if (sb == null) sb = new StringBuffer();
      String token = match.group();
      match.appendReplacement(sb, "");
      for (int ndx = 0; ndx < replace.length(); ndx++) {
        Character chr = replace.charAt(ndx);
        if (isLower(token, ndx)) chr = Character.toLowerCase(chr);
        sb.append(chr);
      }
    }
    if (sb == null) return address;
    match.appendTail(sb);
    return sb.toString();
  }

  /**
   * Determine if character at string position index is a lower case character.
   * if index is beyond end of string, check status of last character in string
   * @param token String token being checked
   * @param index index being checked
   * @return true if character at specified index is lower case
   */
  private boolean isLower(String token, int index) {
    if (index >= token.length()) index = token.length()-1;
    if (index < 0) return false;
    return Character.isLowerCase(token.charAt(index));
  }

  // Clean up any BLK indicators
  // Remove occurrence of BLK bracketed by non-alpha characters
  private static final Pattern BLK_PAT = Pattern.compile("(?:(?:-|(?<![A-Z]))-?BLK|[- ]BLOCK(?! (?:DR|RD)))(?![A-Z])(?: OF)?", Pattern.CASE_INSENSITIVE);
  private String cleanBlock(String sAddr) {
    sAddr = sAddr.replaceAll("[\\{\\}]", "");
    Matcher match = BLK_PAT.matcher(sAddr);
    sAddr = match.replaceFirst("");
    return sAddr;
  }

  // Clean up some Interstate conventions
  private static final Pattern INA_PATTERN = Pattern.compile("\\bIH?[- ]?(\\d+)[NSEW]?\\b");
  private static final Pattern FRONTAGE_PTN = Pattern.compile("\\b(?:[NSEW]B)?FR\\b");
  private static final Pattern IH_PTN = Pattern.compile("\\bIH\\b");
  private String cleanInterstate(String sAddr) {
    sAddr = INA_PATTERN.matcher(sAddr).replaceAll("I $1");
    sAddr = FRONTAGE_PTN.matcher(sAddr).replaceAll("FRONTAGE RD");
    sAddr = IH_PTN.matcher(sAddr).replaceAll("");
    return sAddr;
  }

  // Clean up and NB, SB, EB, or WB words

  private static final Pattern DIRBOUND_PAT = Pattern.compile("[\\s]*(?:(?<![A-Z])(?:NB|SB|EB|WB)|NORTHBOUND|EASTBOUND|SOUTHBOUND|WESTBOUND)(?: ON)?\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern END_JUNK_PAT = Pattern.compile("[-,\\s]+(?=$| &)");
  private String cleanBounds(String sAddr) {
    sAddr = DIRBOUND_PAT.matcher(sAddr).replaceAll("").trim();
    sAddr = END_JUNK_PAT.matcher(sAddr).replaceAll("");
    return sAddr;
  }

  // Google doesn't always handle single word route names like US30 or HWY10.
  // This method breaks those up into two separate tokens, also dropping any
  // direction qualifiers
  private static final Pattern ROUTE_PTN =
    Pattern.compile("\\b(?:(RT|RTE|HW|HWY|HIGH|US|STH?Y?|SHY?|FM|I|CO|CR|CORD|SRT?|TWP)|([A-Z]{2}|M))-?(\\d{1,4}[A-Z]?)(?:[NSEW]B?)?\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SRT_PTN = Pattern.compile("\\bS(?:RT?| ?H|TH)\\b", Pattern.CASE_INSENSITIVE);

  private String cleanRoutes(String sAddress) {

    String state = getStateCode();
    String repState = getRepState(state);

    Matcher match = ROUTE_PTN.matcher(sAddress);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        String g1 = match.group(1);
        if (g1 == null) {
          g1 = match.group(2);
          if (state.equals("MI") && g1.equalsIgnoreCase("M")) g1 = "MI";
          if (!g1.equals(state)) continue;
        }
        String g1u = g1.toUpperCase();
        if (g1u.startsWith("ST") || g1u.startsWith("SH") || g1u.equals(state)) g1 = repState;
        String replace = g1 + ' ' + match.group(3);
        match.appendReplacement(sb, replace);
      } while (match.find());
      match.appendTail(sb);
      sAddress = sb.toString();
    }

    boolean suppr_sr = (parser.getMapFlags() & MAP_FLG_SUPPR_SR) != 0;;
    match = SRT_PTN.matcher(sAddress);
    if (match.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        if (!suppr_sr || !match.group().equalsIgnoreCase("SR")) {
          match.appendReplacement(sb, repState);
        }
      } while (match.find());
      match.appendTail(sb);
      sAddress = sb.toString();
    }

    // If the state code should be replaced with something else, we have to look for
    // a route number starting with the state code
    sAddress = sAddress.replaceAll("\\b" + state + " *(\\d+)\\b", repState + " $1");
    return sAddress;
  }


  // Google can handle things like ST 666 or US 666 or RTE 666.
  // But it doesn't like US RTE 666 or US 666 HWY
  // If we find a construct like that, remove the middle section
  // When we are done with that, check for addresses ending with 666 HWY and reverse the terms
  private static final Pattern[] DBL_ROUTE_PTNS = new Pattern[]{
    Pattern.compile("\\b([A-Z]{2}|STE|STATE|COUNTY|TWP) *(ROAD|RD|RT|RTE|ROUTE|HW|HWY|HY|HIGH|HIGHWAY) +(\\d+[ABMNSEW]?|[A-Z]{1,2})\\b", Pattern.CASE_INSENSITIVE),
    Pattern.compile("\\b([A-Z]{2}|STATE|COUNTY|ROUTE|RTE|FARM-TO-MARKET|TWP) +(\\d+[A-Z]?|[A-Z]{1,2})\\b *(?:ROAD|RD|RT|RTE|ROUTE|HW|HWY|HY|HIGH)\\b", Pattern.CASE_INSENSITIVE)
  };
  private static final Pattern I_FWY_PTN = Pattern.compile("\\b(I[- ]\\d+) +[FH]WY\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern AND_PTN = Pattern.compile(" and ", Pattern.CASE_INSENSITIVE);
  private static final Pattern REV_HWY_PTN = Pattern.compile("(?<!-)\\b(\\d+[A-Z]?|([A-Z&&[^NSEW]])\\2?\\b) *(HWY|RT|RTE|ROUTE)(?=$| *&)", Pattern.CASE_INSENSITIVE);
  private static final Pattern FM_PTN = Pattern.compile("\\bFM\\b", Pattern.CASE_INSENSITIVE);
  private String cleanDoubleRoutes(String sAddress) {

    String state = getStateCode();
    if (state.length() == 0) state = defState;
    String repState = getRepState(state);
    boolean keepStateHighway = (parser.getMapFlags() & MAP_FLG_KEEP_STATE_HIGHWAY) != 0;
    for (Pattern ptn : DBL_ROUTE_PTNS) {
      Matcher match = ptn.matcher(sAddress);
      if (!match.find()) continue;

      StringBuffer sb = new StringBuffer();
      do {
        String prefix = match.group(1).toUpperCase();
        String middle, hwy;
        if (match.groupCount() == 3) {
          middle = match.group(2).toUpperCase();
          hwy = match.group(3).toUpperCase();
        } else {
          middle = "";
          hwy = match.group(2).toUpperCase();
        }
        if (keepStateHighway && prefix.equalsIgnoreCase("STATE") && middle.equalsIgnoreCase("HIGHWAY")) continue;
        if (!hwy.equals("TO") && !hwy.equals("OF") && !hwy.equals("OR")) {
          if (prefix.length() != 2 ||
              (prefix.equals(state) ||
               prefix.equals("CO") ||
               prefix.equals("FM") ||
               prefix.equals("RT") ||
               prefix.equals("ST") ||
               prefix.equals("US"))) {
            if (!prefix.equals("COUNTY") || !middle.equals("ROAD") && !middle.equals("RD")) {
              if (prefix.equals("ST") || prefix.equals("STE") || prefix.equals(state)) prefix = repState;
              match.appendReplacement(sb, prefix + " " + hwy);
            }
          }
        }
      } while (match.find());
      match.appendTail(sb);
      sAddress = sb.toString();
    }

    // Google also doesn't like I-20 fwy contructs
    sAddress = I_FWY_PTN.matcher(sAddress).replaceAll("$1");

    if (parser == null || (parser.getMapFlags() & MAP_FLG_SUPPR_AND_ADJ) == 0) {
      sAddress = AND_PTN.matcher(sAddress).replaceAll(" & ");
    }
    sAddress = REV_HWY_PTN.matcher(sAddress).replaceAll("$3 $1");

    // Aexpand FM route names
    sAddress = replace(sAddress, FM_PTN, "FARM-TO-MARKET");
    return sAddress;
  }

  // Google map isn't found of house numbers mixed with intersections
  // If we find an intersection marker, remove any house numbers
  private static final Pattern HOUSE_RANGE = Pattern.compile("^(\\d+) *- *[A-Z0-9/\\.]+\\b");
  private static final Pattern HOUSE_NUMBER = Pattern.compile("(?!911 CENTER)\\d+ +(?![NSEW]{0,2} *(?:[&/]|\\bM[MP]|$)|(?:1/2|ABOUT|ALT|ALTERNATE|AT|AVE?|AVENUE|BETWEEN|BYPASS|BUS|DR|EXIT|FROM|FY|JUNCTION|MILES?|LANES?|LN|MI|ND|ON|ONTO|OVERPASS|RAMP|RD|SERV|SPUR|TH)\\b|(?:HWY|ST)\\b(?! *\\d+))", Pattern.CASE_INSENSITIVE);
  private String cleanHouseNumbers(String sAddress) {

    // Start by eliminating any house ranges
    sAddress = HOUSE_RANGE.matcher(sAddress).replaceAll("$1");

    // If this has an house number and an intersecting street.  Drop the intersecting street
    // Ditto if the house number is in the second part
    int ipt = sAddress.indexOf('&');
    if (ipt >= 0) {
      if (HOUSE_NUMBER.matcher(sAddress).lookingAt()) {
        sAddress = sAddress.substring(0,ipt).trim();
      } else {
        String tmp = sAddress.substring(ipt+1).trim();
        if (HOUSE_NUMBER.matcher(tmp).lookingAt()) {
          sAddress = tmp;
        }
      }
    }

    // Check for a trailing apt number
    // Generally google ignores appt numbers, but it chokes on one that
    // starts with a #
    ipt = sAddress.lastIndexOf(' ');
    if (ipt >= 0 && ipt+1 < sAddress.length() && sAddress.charAt(ipt+1) == '#') {
      sAddress = sAddress.substring(0, ipt).trim();
    }
    return sAddress;
  }

  private static final Pattern OFFRAMP_PTN = Pattern.compile(" OFRP ", Pattern.CASE_INSENSITIVE);
  private static String cleanOffRamp(String sAddress) {
    Matcher match = OFFRAMP_PTN.matcher(sAddress);
    if (match.find()) sAddress = sAddress.substring(0,match.start()).trim();
    return sAddress;
  }

  private static final Pattern ST_PTN = Pattern.compile("(?<=^|& ?|\\d ?(?:[NSEW] ?)?)S[TH](?= +\\d+)\\b", Pattern.CASE_INSENSITIVE);
  private String cleanSTRoutes(String sAddress) {

    // Google gets confused by the ST abbreviation for State hwy.  We have a couple choices but lets use
    // the default state if there is one and STATE if there isn't.
    String state = getRepState(getStateCode());
    return ST_PTN.matcher(sAddress).replaceAll(state);
  }


  // Google gets confused by the ST abbreviation for State hwy.  We have a couple choices but lets use
  // the default state if there is one and STATE if there isn't.
  // That turned out to be an unwise choice.  STATE seems be more reliable.  But changing it
  // accross the board will break so many existing test case that we are going to phase this in
  // on a state by state basis, starting with states where it is known to be a problem.

  /**
   * Returns the current state code in effect for this message
   * @return current state code
   */
  private String getStateCode() {
    return strState.length() > 0 && VALID_STATE_PTN.matcher(strState).matches() ? strState :
            defState.length() > 0 ? defState : "STATE";
  }
  private static final Pattern VALID_STATE_PTN = Pattern.compile("[A-Z]{2}");

  /**
   * returns the value that should replace the state code when we encounter it in abbreviations for
   * state highways.  We used to always set this to the current state code, but found some cases and
   * some states where this does not work properly and "STATE" does.  Changing this across the board is
   * going to break huge numbers of test cases, so we will phase it in on a state by state basis
   * @param state current state code
   * @return state code replacement value
   */
  private String getRepState(String state) {
    return state.equals("NC") || state.equals("MI") ? "STATE" : state;
  }

  /**
   * @return true if address is valid standalone address
   * (ie either starts with a house number or contains an intersection marker)
   */
  private static final Pattern HOUSE_NO_PTN = Pattern.compile("^(?:(?:[NSEW] +)?\\d+[A-Z]?|\\d+[NSEW]\\d+) ", Pattern.CASE_INSENSITIVE);
  private boolean validAddress(String sAddr) {
    if (HOUSE_NO_PTN.matcher(sAddr).find()) return true;;
    if (sAddr.contains("&") || sAddr.contains(" AND ") || sAddr.contains(" and ")) return true;
    return false;
  }

  /**
   * @return the call city
   */
  public String getCity() {
    return strCity;
  }

  /**
   * @return map adjusted city
   */
  public String getMapCity() {
    if (strMapCity == null) {
      strMapCity = (parser != null ? parser.adjustMapCity(strCity) : strCity);
    }
    return strMapCity;
  }

  /**
   * @return the call State
   */
  public String getState() {
    return strState;
  }

  /**
   * @return the call appt number
   */
  public String getApt() {
    return strApt;
  }

  /**
   * @return the call Cross street description
   */
  public String getCross() {
    return strCross;
  }

  /**
   * @return the call box
   */
  public String getBox() {
    return strBox;
  }

  /**
   * @return the call unit
   */
  public String getUnit() {
    return strUnit;
  }

  /**
   * @return map code
   */
  public String getMap() {
    return strMap;
  }

  /**
   * @return the callback phone number
   */
  public String getPhone() {
    return strPhone;
  }
  /**
   * @return the Supp Info
   */
  public String getSupp() {
    return strSupp;
  }

  /***
   * @return call type code
   */
  public String getCode() {
    return strCode;
  }

  /**
   * @return the page source
   */
  public String getSource() {
    return strSource;
  }

  /**
   * @return name
   */
  public String getName() {
    return strName;
  }

  /**
   * @return call priority
   */
  public String getPriority() {
    return strPriority;
  }

  /**
   * @return radio channel
   */
  public String getChannel() {
    return strChannel;
  }

  /**
   * @return GPS Location
   */
  public String getGPSLoc() {
    return strGPSLoc;
  }

  /**
   * @return incident date
   */
  public String getDate() {
    return strDate;
  }

  /**
   * @return incident time
   */
  public String getTime() {
    return strTime;
  }

  /**
   * @return information URL field
   */
  public String getInfoURL() {
    return strInfoURL;
  }

  /**
   * @return alert field
   */
  public String getAlert() {
    return strAlert;
  }

  /**
   * @return the default city
   */
  public String getDefCity() {
    return defCity;
  }

  /**
   * @return the default state
   */
  public String getDefState() {
    return defState;
  }

  public CountryCode getCountryCode() {
    return countryCode;
  }

  public boolean isPreferGPSLoc() {
    return preferGPSLoc;
  }

  public MsgParser.MapPageStatus getMapPageStatus() {
    return mapPageStatus;
  }

  public String getMapPageURL() {
    if (mapPageURL == null) {
      if (parser != null && mapPageStatus != null) {
        mapPageURL = parser.getMapPageURL(this);
      }
      if (mapPageURL == null) mapPageURL = "";
    }
    return mapPageURL.length() == 0 ? null : mapPageURL;
  }

  /**
   * @return true if additional messages are expected
   */
  public boolean isExpectMore() {
    return expectMore;
  }

  public String getParserCode() {
    return parserCode;
  }

  public MsgParser getParser() {
    return parser;
  }

  /**
   * @return relative score that can be used to pick out the better result
   * from multiple possible parsings
   */
  public int score() {
    return score(false);
  }

  /**
   * @param incomplete true if this is an incomplete message.
   * @return relative score that can be used to pick out the better result
   * from multiple possible parsings
   */
  public int score(boolean incomplete) {
    int result = 0;
    if (msgType == MsgType.GEN_ALERT || strCall.equals("GENERAL ALERT")) result += 0;
    else if (msgType == MsgType.RUN_REPORT || strCall.equals("RUN REPORT")) result += 20000;
    else if (strCall.length() == 0 || strCall.equals("ALERT")) result += 40000;
    else result += 42000;
    if (strAddress.length() > 0) result += 10000;
    if (strCall.length() > 0) result += 500;
    if (strCity.length() > 0) result += 500;
    if (strCross.length() > 0) result += 100;
    if (strApt.length() > 0) result += 100;
    if (strBox.length() > 0) result += 10;
    if (strUnit.length() > 0) result += 10;
    if (strState.length() > 0) result += 10;
    if (strMap.length() > 0) result += 10;
    if (strPlace.length() > 0) result += 10;
    if (strCallId.length() > 0) result += 10;
    if (strName.length() > 0) result += 10;
    if (strPhone.length() > 0) result += 10;
    if (strSupp.length() > 0) result += 2;
    if (strCode.length() > 0) result += 10;
    if (strSource.length() > 0) result += 10;
    if (strState.length() > 0) result += 10;
    if (strPriority.length() > 0) result += 10;
    if (strChannel.length() > 0) result += 10;
    if (strGPSLoc.length() > 0) result += 10;
    if (strDate.length() > 0) result += 10;
    if (strTime.length() > 0) result += 10;
    if (strInfoURL.length() > 0) result += 5;
    if (strAlert.length() > 0) result += 20;
    if (incomplete ^ expectMore) result--;
    return result;
  }

  /**
   * Append message information to support message under construction
   * @param sb String builder holding message being constructed
   */
  public void addMessageInfo(StringBuilder sb) {
    sb.append("\n\nParser Info");

    addInfo(sb, "Type", msgType.toString());
    addInfo(sb, "Call", strCall);
    addInfo(sb, "Place", strPlace);
    addInfo(sb, "Alert", strAlert);
    addInfo(sb, "Addr", strAddress);
    addInfo(sb, "MapAddr", strBaseMapAddress);
    addInfo(sb, "City", strCity);
    addInfo(sb, "Apt", strApt);
    addInfo(sb, "X", strCross);
    addInfo(sb, "Box", strBox);
    addInfo(sb, "Unit", strUnit);
    addInfo(sb, "St", strState);
    addInfo(sb, "Map", strMap);
    addInfo(sb, "ID", strCallId);
    addInfo(sb, "Ph", strPhone);
    addInfo(sb, "Info", strSupp);
    addInfo(sb, "Code", strCode);
    addInfo(sb, "Src", strSource);
    addInfo(sb, "Name", "strName");
    addInfo(sb, "Pri",  strPriority);
    addInfo(sb, "Ch", strChannel);
    addInfo(sb, "GPS", strGPSLoc);
    addInfo(sb, "Date", strDate);
    addInfo(sb, "Time", strTime);
  }

  private void addInfo(StringBuilder sb, String title, String value) {
    if (value != null && value.length() > 0) {
      sb.append('\n');
      sb.append(title);
      sb.append(':');
      sb.append(value);
    }
  }
}
