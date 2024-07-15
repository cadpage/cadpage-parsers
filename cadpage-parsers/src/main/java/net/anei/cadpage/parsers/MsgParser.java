package net.anei.cadpage.parsers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * This class is responsible for parsing useful information from an SMS page message
 */
public abstract class MsgParser {

  private static final int GPS_POS_LAT =  0x01;  // Lattitude should be positive
  private static final int GPS_NEG_LAT =   0x02;  // Lattitude should be negative
  private static final int GPS_POS_LONG = 0x04;  // Longitidue should be positive
  private static final int GPS_NEG_LONG =  0x08;  // Longitude should be negative
  private static final int GPS_LARGE_LAT = 0x10;  // Abs value of latitude should exceed abs value of longitude
  private static final int GPS_LARGE_LONG = 0x20;  // Abs value of longitude should exceed abs value of latitidue

  /**
   * Country code passed to constructor to set up tables for specific countries
   * The country code is also responsible for converting GPS coordinates to a normal
   * Google standard form.  Because there is no standard as to which coordinate comes
   * first and whether or not they are signed, this conversion has to make various
   * assumptions about what legitimate GPS coordinates look like that can vary from
   * country to country
   */
  public static enum CountryCode {

    US(GPS_POS_LAT | GPS_NEG_LONG | GPS_LARGE_LONG),  // United States
    UK(GPS_POS_LAT | GPS_LARGE_LAT),                  // United Kingdom
    AU(GPS_NEG_LAT | GPS_POS_LONG | GPS_LARGE_LONG),  // Australia
    NZ(GPS_NEG_LAT | GPS_POS_LONG | GPS_LARGE_LONG),  // New Zealand
    SE(GPS_POS_LAT | GPS_POS_LONG | GPS_LARGE_LAT),   // Sweden
    EE(GPS_POS_LAT | GPS_POS_LONG | GPS_LARGE_LAT);   // Estonia

    private int gpsFlags;

    CountryCode(int gpsFlags) {
      this.gpsFlags = gpsFlags;
    }

    /**
     * Look for GPS coordinates in address line.  If found, parse them into a
     * set of coordinates that Google Maps will recognize
     */
    public String parseGPSCoords(String address) {
      Matcher match = GPS_PATTERN.matcher(address);
      if (!match.find()) return null;

      return parseGPSCoords(match);
    }

    /**
     * Extract GPS coordiantes from pattern matcher.  The Matcher
     * object must have been created by calling GPS_PATTERN.matcher()
     * @param match Pattern match
     * @return parsed GPS coordinates
     */
    public String parseGPSCoords(Matcher match) {

      // Calculate the coordinate values
      double c1 = cvtGpsCoord(match.group(2));
      double c2 = cvtGpsCoord(match.group(4));

      // If they used the X: ... Y: ... labels, swap the parameters
      // because we and google expect the latitude to come first
      if (match.group(1) != null && match.group(3) != null && match.group(3).startsWith("Y:")) {
        double tmp = c1;
        c1 = c2;
        c2 = tmp;
      }

      // There isn't a consistent standard as to which is latitude and
      // which is longitude, so we will have to make some guesses.
      double latitude, longitude;

      // First step is to identify which is latitude and which is longitude
      // If one must be larger hat the other, we can use their relative sizes
      // to determine that
      double ac1 = Math.abs(c1);
      double ac2 = Math.abs(c2);
      if (ac1 == 1.0 && ac2 == 1.0) return null;
      if (ac1 == 0.0 && ac2 == 0.0) return null;
      if ((gpsFlags & GPS_LARGE_LAT) != 0) {
        if (ac1 > ac2) {
          latitude = c1;
          longitude = c2;
        } else {
          latitude = c2;
          longitude = c1;
        }
      } else if ((gpsFlags & GPS_LARGE_LONG) != 0) {
        if (ac2 > ac1) {
          latitude = c1;
          longitude = c2;
        } else {
          latitude = c2;
          longitude = c1;
        }
      }

      // No luck there  there are some other tricks we can try if one
      // coordinate is negative but the other is not.  Then check to
      // see which coordinate is supposed to be negative
      else if (c1 < 0 ^ c2 < 0) {
        if ((gpsFlags & GPS_NEG_LAT|GPS_NEG_LONG) == GPS_NEG_LAT) {
          if (c1 < 0) {
            latitude = c1;
            longitude = c2;
          } else {
            latitude = c2;
            longitude = c1;
          }
        } else if ((gpsFlags & (GPS_NEG_LAT|GPS_NEG_LONG)) == GPS_NEG_LONG) {
          if (c2 < 0) {
            latitude = c1;
            longitude = c2;
          } else {
            latitude = c2;
            longitude = c1;
          }
        } else {
          latitude = c1;
          longitude = c2;
        }
      }

      // Lacking any other information, assume first is lattitude and second is longitude
      else {
        latitude = c1;
        longitude = c2;
      }

      // Rest is easy, make sure anything that should be negative is negative
      if ((gpsFlags & GPS_NEG_LAT) != 0) {
        if (latitude > 0) latitude = - latitude;
      }
      if ((gpsFlags & GPS_NEG_LONG) != 0) {
        if (longitude > 0) longitude = - longitude;
      }

      // And anything that should be positive is positive
      if ((gpsFlags & GPS_POS_LAT) != 0) {
        if (latitude < 0) latitude = - latitude;
      }
      if ((gpsFlags & GPS_POS_LONG) != 0) {
        if (longitude < 0) longitude = - longitude;
      }
      // And convert the result to a GPS string
      return String.format(Locale.US, "%+8.6f,%+8.6f", latitude, longitude);
    }

    /**
     * Convert GPS coordinate in degree:min:sec form  or degree min form to strait degrees
     */
    private static double cvtGpsCoord(String coord) {
      Matcher match = GPS_COORD_PTN.matcher(coord);
      if (!match.matches()) throw new RuntimeException("This cannot possibly fail");
      String tmp = match.group(1);
      if (tmp != null) return Double.parseDouble(tmp);

      String sign;
      double degrees;
      double minutes;
      sign = match.group(2);
      if (sign != null) {
        degrees = Double.parseDouble(match.group(3));
        minutes = Double.parseDouble(match.group(4));
      } else {
        sign = match.group(5);
        degrees = Double.parseDouble(match.group(6));
        minutes = Double.parseDouble(match.group(7));
        double seconds = Double.parseDouble(match.group(8));
        minutes += seconds/60.0;
      }

      degrees += minutes/60;
      if (sign.equals("-")) degrees = - degrees;
      return degrees;
    }
  };

  /**
   * Pattern that identifies GPS coordinates in an arbitrary field
   */
  private static final String GPS_COORD_PTN_STR = "([-+]?[0-9]+\\.[0-9]{2,})\\??|([-+]?)([0-9]+)[°% \\?] *([0-9]+\\.[0-9]{1,})['`°]?|([-+]?)([0-9]{1,3})[: %°\\?] *([0-9]{1,2})[:'` ] *([0-9]{1,2}(?:\\.[0-9]{1,})?)\"?";
  private static final String GPS_COORD_PTN_STR2 = "[-+]?[0-9]+\\.[0-9]{2,}\\??|[-+]?[0-9]+[°% \\?] *[0-9]+\\.[0-9]{1,}['`]?|[-+]?[0-9]{1,3}[: °%\\?] *[0-9]{1,2}[:'` ] *[0-9]{1,2}(?:\\.[0-9]{1,})?\"?";
  private static final Pattern GPS_COORD_PTN = Pattern.compile(GPS_COORD_PTN_STR);
  public static final Pattern GPS_PATTERN =
      Pattern.compile("(?:\\b|^|[ ,;\\.]+)(X: *|LAT: *|LL\\( *)?[NS]?<?(" + GPS_COORD_PTN_STR2 + ")[ >]?[NnSs]?[,\\W] ?\\W*?(Y: *|LONG?: *|x )?[EW]?<?(" + GPS_COORD_PTN_STR2 + ") ?(?:[EW](?!/))?>?(?:\\)|\\b)", Pattern.CASE_INSENSITIVE);

  /**
   * Parse flag indicates that sender address filtering should not be checked
   */
  public static final int PARSE_FLG_SKIP_FILTER = 0x01;

  /**
   * Parse flag indicates that message has been positively ID's as coming from
   * this dispatch center
   */
  public static final int PARSE_FLG_POSITIVE_ID = 0x02;

  /**
   * Parse flag indicating parser should be invoked even if we already
   * have the results from a previous parser.
   */
  public static final int PARSE_FLG_REPARSE = 0x04;

  /**
   * Parse flag indicating message is the result of merging two or more alert messages
   */
  public static final int PARSE_FLG_MULTI = 0x08;

  /**
   * Force flag forces processing of message
   */
  public static final int PARSE_FLG_FORCE = PARSE_FLG_SKIP_FILTER | PARSE_FLG_POSITIVE_ID;

  // Pattern matching a terminated string of digits
  public static final Pattern NUMERIC = Pattern.compile("\\b\\d+\\b");

  // States in which the defautl LA -> LN map conversion should always be suppressed
  // (ie states where Spanish or French street names can be expected)
  private static final Set<String> SUPPR_LA_STATES = new HashSet<String>(Arrays.asList(new String[]{"LA","TX","NM","AZ","CA","QC"}));

  // parser code
  private String parserCode;

  // Default map flags
  private int mapFlags = 0;

  // Default city and state passed in constructor
  private String defCity;
  private String defState;

  // Parser country code
  private CountryCode countryCode;

  // Parser specific table of GPS coordinates associated with
  // specific addresses (typically mile markers)
  private Properties gpsLookupTable = null;

  // Parser specific table of GPS coordinates associated with
  // specific place names
  private Properties placeGpsLookupTable =  null;

  // Save parse flags so we can check message status from methods that
  // were not passed the parse flags
  private int parseFlags;

  // List of field terms to be used when generating tests for this parser
  private String fieldList = null;

  private boolean testMode = false;

  public MsgParser(String defCity, String defState) {
    this(defCity, defState, CountryCode.US);
  }

  public MsgParser(String defCity, String defState, CountryCode countryCode) {
    this.defCity = defCity;
    this.defState = defState;
    this.countryCode = countryCode;

    String clsName = this.getClass().getName();
    int ipt = clsName.lastIndexOf('.');
    parserCode = clsName.substring(ipt+1, clsName.length()-6);

    if (countryCode == CountryCode.US && SUPPR_LA_STATES.contains(defState)) mapFlags = MAP_FLG_SUPPR_LA;
  }

  public void setTestMode(boolean testMode) {
    this.testMode = testMode;
  }

  public boolean isTestMode() {
    return testMode;
  }

  /**
   * Set up GPS address lookup table
   * @param gpsLookupTable table of GPS coordinates keyed by address string
   */
  protected void setupGpsLookupTable(Properties gpsLookupTable) {
    this.gpsLookupTable = gpsLookupTable;
  }

  /**
   * Set up GPS place lookup table
   * @param gpsLookupTable table of GPS coordinates keyed by address string
   */
  protected void setupPlaceGpsLookupTable(Properties placeGpsLookupTable) {
    this.placeGpsLookupTable = placeGpsLookupTable;
  }

  /**
   * @return parsers default city
   */
  public String getDefaultCity() {
    return defCity;
  }

  /**
   * @return parsers default state
   */
  public String getDefaultState() {
    return defState;
  }

  /**
   * @return parser country code
   */
  public CountryCode getCountryCode() {
    return countryCode;
  }

  public void setFieldList(String fieldList) {
    this.fieldList = fieldList;
  }

  /**
   * Misnamed for unfortunate historical reason.  This is not to be confused
   * with the real program field set by FieldProgramParser.setProgram
   * @return list of field terms to be used to generate tests for this parser
   */
  public String getProgram() {
    return fieldList;
  }

  /**
   * Return call description CodeSet object.  We do not use this in any way
   * shape or form, but delaring it here gives any subclasses a way to
   * report their call description list to the test suite which can use
   * it to validate that all call descriptions are included
   * @return call description CodeSet object
   */
  public CodeSet getCallList() {
    return null;
  }

  /**
   * Validate that call code is valid.  Also not used in any way, but can be
   * overridden by subclasses if they some call validation logic that
   * does not involve a simple CodeSet object
   * @param call call to be validated
   * @return true if call is valid, false if regular logic should be used
   */
  public boolean checkCall(String call) {
    return false;
  }

  private static final Object MsgParserLock = new Object();

  /**
   * Determine if message is a valid CAD message for this parser, and parse
   * all information from the message if it is
   * @param msg message to be parsed
   * @param parseFlags parser flags
   * @return true if this message contain the text phrases that indicate it is
   * a valid page message
   */
  public boolean isPageMsg(Message msg, int parseFlags) {

    // Has to be synchronized because this is called from the MMS service thread
    // And we only have one global copy of parsing flags for each parser

    // And has to be synchronized at the class level because GroupBestParser may
    // invoke parsing of several different sub-parsers after synchronizing on
    // it's own parsing object.  Unbelievably, this odd problem resulted in
    // two user reported crashes in the space of as many weeks.  And yes, it
    // took a while to track down :(

    synchronized (MsgParserLock) {

      // Save parse flags for future reference
      this.parseFlags = parseFlags;

      // If we have been called before and returned a parsed result
      // we do not have to do all of that work again.
      MsgInfo info = msg.getInfo();
      if (info == null || (parseFlags & PARSE_FLG_REPARSE) != 0) {

        // See what the parseMsg method thinks of this
        // If it does not like the results, return failure
        Data data = parseMsg(msg, parseFlags);
        if (data == null) return false;

        // The people running the show at Prince William County in VA are absolutely
        // totally adamant that unstructured information never ever ever be visible
        // to the end users.  To the point where they will refuse to send dispatch
        // information to services who will not guarantee this.  So we will do
        // our part to go with the flow....
        if (msg.getFromAddress().contains("pwcgov.org")) {
          data.strSupp = "";
          if (data.strCall.equals("GENERAL ALERT") || data.strCall.equals("RUN REPORT")) {
            data.strPlace = "";
          }
        }

        // Save parser code and information object in message so we won't have to
        // go through all of this again
        info = new MsgInfo(data);
        msg.setInfo(info);
      }
    }

    // Life is good!!
    return true;
  }

  /**
   * build information object from information parsed from message
   * @param msg message to be parsed
   * @param parseFlags
   * @return new message information object if successful, false otherwise
   */
  protected Data parseMsg(Message msg, int parseFlags) {
    return parseMsg(msg, parseFlags, getFilter());
  }

  /**
   * build information object from information parsed from message
   * @param msg message to be parsed
   * @param parseFlags
   * @return new message information object if successful, false otherwise
   */
  protected Data parseMsg(Message msg, int parseFlags, String filter) {

    // If parser filter is not being overridden, and the message address does not
    // match the parser filter, message should be rejected
    boolean overrideFilter = (parseFlags & PARSE_FLG_SKIP_FILTER) != 0;
    if (! overrideFilter && ! matchFilter(msg.getFromAddress(), filter)) return null;

    // Save parse flags for future reference (again)
    // We have to do this again because the GroupBestParser will call
    // this method is sub parsers without calling the initial inPageMsg() method
    this.parseFlags = parseFlags;

    // Decode the call page and place the data in the database
    String strSubject = msg.getSubject();
    String strMessage = msg.getMessageBody(keepLeadBreak());
    strMessage = htmlFilter(strMessage);
    Data data = new Data(this);
    if (strMessage == null) return data;
    if (parseHtmlMsg(strSubject, strMessage, data)) return data;

    // If this isn't a valid CAD page, but has been positively identified as a dispatch
    // message, parse as a general alert.  If General alerts are not desired, they will
    // be filtered out one level up.  Unless we are in test mode in which case we skip this step
    // If not then return failure
    if (!isTestMode() && isPositiveId()) {
      return ManageParsers.getInstance().getAlertParser().parseMsg(msg, parseFlags);
    }
    return null;
  }

  /**
   * Determine if leading line breaks should be preserved
   * @return true if they should
   */
  protected boolean keepLeadBreak() {
    return false;
  }

  /**
   * Determine if this message has been identified as coming from the dispatch
   * center we are parsing messages for
   * @return true if it is, false otherwise
   */
  public boolean isPositiveId() {

    // If the caller flagged this as positively ID's, so be it
    if ((parseFlags & PARSE_FLG_POSITIVE_ID) != 0) return true;

    // Otherwise consider this a positive ID if the message had to pass
    // a non-empty filter
    if ((parseFlags & PARSE_FLG_SKIP_FILTER) != 0) return false;
    if (getFilter().length() <= 1) return false;

    return true;
  }

  public boolean isMultiMsg() {
    return ((parseFlags & PARSE_FLG_MULTI) != 0);
  }

  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    boolean force = body.startsWith("<!DOCTYPE");
    if (force) body = cleanDocHeaders(body);
    body = decodeHtmlSequence(body);
    if (parseUntrimmedMsg(subject, body, data)) return true;
    if (force) {
      setFieldList("INFO");
      data.parseGeneralAlert(this, body.trim());
      return true;
    }
    return false;
  }

  private static final Pattern CLEAN_HTML_PTN = Pattern.compile("</?(?:div|span|p)\\b[^>]*?>", Pattern.CASE_INSENSITIVE);

  private String cleanDocHeaders(String body) {
    StringBuffer sb = null;
    int ifCnt = 0;
    for (String line : body.split("\n")) {
      if (sb == null) {
        if (line.trim().length() == 0) continue;
        if (line.contains("<!--[if ")) ifCnt++;
        else if (line.contains("<![endif]")) ifCnt--;
        else if (ifCnt == 0) {
          sb = new StringBuffer(CLEAN_HTML_PTN.matcher(line).replaceAll("").trim());
        }
      }
      else {
        Matcher match = CLEAN_HTML_PTN.matcher(line.trim());
        if (match.lookingAt()) break;
        sb.append("\n");
        sb.append(match.replaceAll("").trim());
      }
    }
    if (sb == null) return body;
    return sb.toString();
  }

  /**
   * Parse information object from message
   * @param subject message subject to be parsed
   * @param body message text to be parsed
   * @param data data object to be constructed
   * @return true if successful, false otherwise
   */
  protected boolean parseUntrimmedMsg(String subject, String body, Data data) {
    return parseMsg(subject, body.trim(), data);
  }

  /**
   * Parse information object from message
   * @param subject message subject to be parsed
   * @param body message text to be parsed
   * @param data data object to be constructed
   * @return true if successful, false otherwise
   */
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseMsg(body, data);
  }

  /**
   * Parse information object from message
   * @param body message text to be parsed
   * @param data data object to be constructed
   * @return true if successful, false otherwise
   */
  protected boolean parseMsg(String body, Data data) {
    throw new RuntimeException("parseMsg method was not overridden");
  }

  /**
   * If parser subclass provides an HTML filter pattern, and if messages starts with an HTML tag
   * use the HTML filter to extract the useful message data from the HTML message
   * @param body message body
   * @return adjusted message body
   */
  private String htmlFilter(String body) {
    if (body == null) return null;
    Pattern ptn = getHtmlFilter();
    if (ptn == null) return body;
    if (!body.startsWith("<html>") && !body.startsWith("<HTML>")) return body;
    String result = "";
    for (String line : body.split("\n")) {
      Matcher match = ptn.matcher(line);
      if (match.matches()) {
        if (match.groupCount() > 0) {
          for (int ndx = 0; ndx < match.groupCount(); ndx++) {
            String tmp = match.group(ndx);
            if (tmp != null) {
              line = tmp;
              break;
            }
          }
        }
        result = append(result, "\n", line.trim());
      }
    }
    return result;
  }

  /**
   * @return Pattern used to extract data message from an HTML message
   */
  protected Pattern getHtmlFilter() {
    return null;
  }

  /**
   * @return sponsor of this user.  A non-null value means someone else is
   * paying and we aren't going to bug users for donations.
   */
  public String getSponsor() {

    // UK locations are free for now
    if (countryCode == CountryCode.UK) return "UK";
    if (countryCode == CountryCode.AU) return "AU";
    if (countryCode == CountryCode.NZ) return "NZ";
    if (countryCode == CountryCode.SE) return "SE";
    return null;
  }

  /**
   * @return returns sponsor purchase date if it should be used to compute
   * a sponsor expiration date or null if the sponsorship should not expire
   * there is no sponsor expiration date.
   */
  public Date getSponsorDate () {
    String dateStr = getSponsorDateString();
    if (dateStr == null) return null;
    try {
      return DATE_FORMAT.parse(dateStr);
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
  }
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMddyyyy");

  /**
   * @return sponsor purchase date in MMDDYYYY form, or null if not defined
   */
  protected String getSponsorDateString() {
    return null;
  }

  /**
   * @return Filter associated with this parser
   */
  public String getFilter() {
    return "";
  }

  /**
   * @return map address adjustment flags<br>
   * MAP_FLG_SUPPR_LA suppresses LA -> LN adjustment<br>
   * MAP_FLG_SUPPR_EXT suppresses EXT removal
   * MAP_FLG_ADD_DEFAULT_CNTY always add default county
   * MAP_FLG_SUPPR_DIRO suppresses [NEWS]O -> & adjustment
   * MAP_FLG_SUPPR_ADD_PLACE suppress logic to add place name to naked streets
   * MAP_FLG_PREFER_GPS recomend GPS coordinates of map address for mapping purposes
   * MAP_FLG_CR_CRES convert CR to CRES instead of CIR
   * MAP_FLG_SUPPR_CR supress CR -> CIR adjustment
   * MAP_FLG_CR_CREEK convert CR to CREEK instead of CIR
   * MAP_FLG_SUPPR_TE suppress TE -> TER adjustment
   * MAP_FLG_MAP_PAGES parser may return map page information
   * MAP_FLG_ADOBE_MAP_PAGE Map pages must be displayed with Adobe Reader
   * MAP_FLG_PK_PARKWAY convert PK -> PARKWAY instead of PIKE
   */
  public int getMapFlags() {
    return mapFlags;
  }
  public static final int MAP_FLG_SUPPR_LA = MsgInfo.MAP_FLG_SUPPR_LA;
  public static final int MAP_FLG_REMOVE_EXT = MsgInfo.MAP_FLG_REMOVE_EXT;
  public static final int MAP_FLG_ADD_DEFAULT_CNTY = MsgInfo.MAP_FLG_ADD_DEFAULT_CNTY;
  public static final int MAP_FLG_SUPPR_DIRO = MsgInfo.MAP_FLG_SUPPR_DIRO;
  public static final int MAP_FLG_PREFER_GPS = MsgInfo.MAP_FLG_PREFER_GPS;
  public static final int MAP_FLG_SUPPR_SR = MsgInfo.MAP_FLG_SUPPR_SR;
  public static final int MAP_FLG_CR_CRES = MsgInfo.MAP_FLG_CR_CRES;
  public static final int MAP_FLG_SUPPR_CR = MsgInfo.MAP_FLG_SUPPR_CR;
  public static final int MAP_FLG_CR_CREEK = MsgInfo.MAP_FLG_CR_CREEK;
  public static final int MAP_FLG_SUPPR_TE = MsgInfo.MAP_FLG_SUPPR_TE;
  public static final int MAP_FLG_KEEP_STATE_HIGHWAY = MsgInfo.MAP_FLG_KEEP_STATE_HIGHWAY;
  public static final int MAP_FLG_PK_PARKWAY = MsgInfo.MAP_FLG_PK_PARKWAY;

  public enum MapPageStatus { ANY, ADOBE };

  /**
   * @return Enum that determines which view must be used to view map pages<br>
   * null if no map pages will ever be returned.
   * MAP_PAGE_STATUS.ANY if any viewer may be used to read map pages
   * MAP_PAGE_STATUS.ADOBE if Adobe ready must be used to view map pages
   */
  public MapPageStatus getMapPageStatus() {
    return null;
  }

  /**
   * @return map page URL associated with this call
   */
  public String getMapPageURL(MsgInfo info) {
    return null;
  }

  /**
   * Convenience method to identify a page message by checking to see if
   * it contains a sequence of key phrases in the proper order
   * @param body body of message to be checked
   * @param keywords array of key phrases used to identify paging message
   * @return true if all key phrases were found.
   */
  protected boolean isPageMsg(String body, String[] keywords) {
    int ipt = 0;
    for (String keyword : keywords) {
      keyword = keyword + ":";
      ipt = body.indexOf(keyword, ipt);
      if (ipt < 0) return false;
      ipt += keyword.length();
    }
    return true;
  }

  /**
   * @return Alias code.  This code will be non-null for parsers that are different aliases of another parser
   * and all of the parsers that are aliases of one another will return the same code.
   */
  public String getAliasCode() {
    return null;
  }

  /**
   * @return parser code identifying the parser that actually was used
   */
  public String getParserCode() {
    return parserCode;
  }

  /**
   * @return Human readable name of location parser
   */
  public String getLocName() {

    // Overridden in special cased, but general default is to build a name
    // from the default city and state
    String defCity = getDefaultCity();
    String defState = getDefaultState();
    if (defCity.length() == 0) return "";

    char[] carry = defCity.toCharArray();
    boolean upshift = true;
    for (int j = 0; j<carry.length; j++) {
      char chr = carry[j];
      if (chr == ' ' || chr == '-') {
        upshift = true;
      } else if (upshift) {
        chr = Character.toUpperCase(chr);
        upshift = false;
      } else {
        chr = Character.toLowerCase(chr);
      }
      carry[j] = chr;
      if (chr == 'c' && j > 0 && carry[j-1] == 'M') upshift = true;
    }

    String locName = new String(carry);

    if (defState.length() > 0) {
      locName = locName + ", " + defState;
    }

    return locName;
  }

  public SplitMsgOptions getActive911SplitMsgOptions() {
    return null;
  }

  /**
   * General purpose parser for formats where there is not a clear delimiter
   * between key: value item pairs.
   * @param body message body to be parsed
   * @param keyWords list of expected keywords
   * @return Properties object containing the parsed key: value pairs
   */
  protected Properties parseMessage(String body, String[] keyWords) {

    Properties props = new Properties();
    String[] flds = parseMessageFields(body, keyWords);
    for (String fld : flds) {
      int pt = fld.indexOf(':');
      if (pt > 0) {
        String key = fld.substring(0,pt).trim();
        String value = fld.substring(pt+1).trim();
        props.setProperty(key, value);
      }
    }
    return props;
  }


  /**
   * General purpose parser for formats where there is not a clear delimiter
   * between key: value item pairs.
   * @param body message body to be parsed
   * @param keyWords list of expected keywords
   * @return Array of data fields broken up by defined keywords
   */
  protected String[] parseMessageFields(String body, String[] keyWords) {
    return parseMessageFields(body, keyWords, ':', false, false, false);
  }

  /**
   * General purpose parser for formats where there is not a clear delimiter
   * between key: value item pairs.
   * @param body message body to be parsed
   * @param keyWords list of expected keywords
   * @param breakChar character that marks the end of all keywords
   * @param anyOrder true if if keywords can occur in any order
   * @param ignoreCase true if case should be ignored when comparing keywords
   * @param newLineBrk treat newline characters as field breaks
   * @return Array of data fields broken up by defined keywords
   */
  protected String[] parseMessageFields(String body, String[] keyWords,
                                        char breakChar, boolean anyOrder, boolean ignoreCase,
                                        boolean newLineBrk) {

    List<String> fields = new ArrayList<String>();
    int iKey = -1;  // Current key table pointer
    int iStartPt = 0;   // current data field start index
    int iColonPt = -1;
    int iNxtKey;

    // Loop processing each keyword found
    do {

      // Start searching for the next keyword starting at the current data field
      int iEndPt = -1;
      iNxtKey = -1;

      // This loop checks each break characters looking for one that
      // matches an available keyword
      while (true) {

        // Find the next break character, if there isn't one, bail out
        int iDataPt = iColonPt+1;
        iColonPt = body.indexOf(breakChar, iDataPt);
        if (iColonPt < 0) break;

        int ipt = iColonPt;
        while (ipt>iDataPt && body.charAt(ipt-1)==' ') ipt--;

        // Next search the available keywords to see if this colon
        // defines one of them
        for (int ndx = iKey+1; ndx < keyWords.length; ndx++) {
          String key = keyWords[ndx];
          int len = key.length();
          int iTempPt = ipt - len;
          int iTemp2Pt = iTempPt;
          if (iTempPt < iDataPt) continue;
          if (iTempPt > iDataPt) {
            char chr = body.charAt(iTempPt-1);
            if (breakChar == ')') {
              if (chr != '(') continue;
              iTemp2Pt--;
            } else {
              if (!Character.isWhitespace(chr)) continue;
            }
          } else if (breakChar == ')') continue;
          String keyword = body.substring(iTempPt, ipt);
          if (ignoreCase) keyword = keyword.toUpperCase();
          if (!keyword.equals(key)) continue;
          if (rejectBreakKeyword(keyword)) continue;
          iNxtKey = ndx;
          iEndPt = iTemp2Pt;
          break;
        }


        // If we found a keyword for this colon, bail out of loop
        // Otherwise try looking for another colon
        if (iEndPt >= 0) break;
      }

      // Coming up of the preceding loop, we either found a valid keyword
      // with index iNextPt, starting at iEndPT and ending at iColonPt.
      // Or we didn't.  If we didn't we need to go through some more
      // cleverness to find the end of the current field.
      if (iNxtKey < 0) {

        // Normally this would be the end of the message body
        // But we are going to be clever and see if the last token in
        // the message body looks like a truncated available keyword.
        // if it is, we will trim that part off.
        iEndPt = body.length();
        String[] trailers;
        if (breakChar == ')') {
          trailers = new String[]{null};
          int iTempPt = body.lastIndexOf('(');
          if (iTempPt >= 0) trailers[0] = body.substring(iTempPt+1);
        } else {
          int iTempPt = iEndPt;
          trailers = new String[]{null, null, null};
          for (int cnt = 0; cnt < trailers.length; cnt++) {
            iTempPt = body.lastIndexOf(' ', iTempPt-1);
            if (iTempPt < 0) break;
            String key = body.substring(iTempPt+1);
            if (ignoreCase) key = key.toUpperCase();
            trailers[cnt] = key;
          }
        }
        boolean found = false;
        for (int ndx = iKey+1; ndx < keyWords.length; ndx++) {
          for (String tail : trailers) {
            if (tail != null && keyWords[ndx].startsWith(tail)) {
              iEndPt = body.length()-tail.length()-1;
              found = true;
              break;
            }
          }
          if (found) break;
        }
      }

      // By this point we have identified
      //   iKey     current keyword index (or -1 if none)
      //   iNxtKey  Next keyword index (or -1 if none)
      //   iStartPt Start of data field associated with iKey
      //   iEndPt   End of data field associated with iKey
      //   iColonPn break character terminating next keyword

      // Save current field and get ready to start looking for the
      // end of the next keyword
      if (iEndPt > 0) {
        String fld = body.substring(iStartPt, iEndPt).trim();
        if (newLineBrk) {
          for (String part : fld.split("\n")) {
            fields.add(part.trim());
          }
        } else {
          fields.add(fld);
        }
      }
      if (!anyOrder) iKey = iNxtKey;
      iStartPt = iEndPt;
    } while (iNxtKey >= 0);

    return fields.toArray(new String[0]);
  }

  /**
   * Allows parse subclasses to reject keywords that should only
   * be recognized in certain circumstances
   * @param key keyword being checked
   * @return true keyword should be rejected
   */
  protected boolean rejectBreakKeyword(String key) {
    return false;
  }

  /**
   * General purpose message parser for formats where there is a clear delimiter
   * between key: value data pairs
   * @param body
   * @param delim line delimiter
   * @return
   */
  protected static Properties parseMessage(String body, String delim) {
    return parseMessage(body, delim, null);
  }

  /**
   * General purpose parser
   * @param body of message to be parsed
   * @param delim expression to be used to separate lines in message
   * @param missedKeys if not null, an array of keywords to be assigned to
   * lines that are missing a keyword
   * @return Properties object containing the parsed line tokens
   */
  protected static Properties parseMessage(String body, String delim, String[] missedKeys) {

    Properties props = new Properties();
    String[] lines = body.split(delim);
    int missed = 0;
    for (String line : lines) {
      line = line.trim();
      int ndx = line.indexOf(':');
      if (ndx < 0) {
        if (missedKeys != null && missed < missedKeys.length) {
          props.put(missedKeys[missed++], line);
        }
        continue;
      }
      String key = line.substring(0, ndx).trim();
      String value = line.substring(ndx+1).trim();
      props.put(key, value);
    }
    return props;
  }

  /**
   * Parse address line into address and city fields treating anything behind a
   * dash as a city
   * @param addressLine address line to be parsed
   * @param data message info object to be filled
   */
  protected static void parseAddressCity(String addressLine, MsgInfo.Data data) {
    parseAddress(addressLine, data, true);
  }

  /**
   * Parse address line into address fields
   * @param addressLine address line to be parsed
   * @param data message info object to be filled
   */
  protected void parseAddress(String addressLine, MsgInfo.Data data) {
    parseAddress(addressLine, data, false);
  }

  /**
   * Internal class used to perform case insensitive pattern replacesments
   * where the replacement string must be terminated by a break sequence on both ends
   */
  public static class PatternReplace {
    private Pattern pattern;
    private String replace;

    public PatternReplace(String pattern, String replace) {
      this.pattern = Pattern.compile("\\b" + pattern + "\\b", Pattern.CASE_INSENSITIVE);
      this.replace = replace;
    }

    public String replace(String field) {
      return pattern.matcher(field).replaceAll(replace);
    }
  }

  /**
   * Internal class used to support street names that have to be protected
   * from being treated as individual name values, usually because they
   * contain the word "AND".
   */
  public static class ProtectPatternReplace {
    private Pattern pattern;
    boolean reverse;


    /**
     * Constructor
     * @param name  String containing pattern to match.  Any expression matching this
     * pattern will have blanks changed to underscores
     */
    public ProtectPatternReplace(String name) {
      this(name, false);
    }

    /**
     * Constructor
     * @param name  String containing pattern to match.  Any expression matching this
     * pattern will have blanks changed to underscores or vice versa
     * @param reverse false to change blanks to underscores, true to change underscores to blanks
     */
    public ProtectPatternReplace(String name, boolean reverse) {
      if (reverse) name = name.replace(' ', '_');
      pattern = Pattern.compile("\\b" + name + "\\b", Pattern.CASE_INSENSITIVE);
      this.reverse = reverse;
    }

    public String replace(String address) {
      Matcher match = pattern.matcher(address);
      if (match.find()) {
        StringBuffer sb = new StringBuffer();
        do {
          String replace = match.group();
          if (!reverse) {
            replace = replace.replace(' ', '_');
          } else {
            replace = replace.replace('_', ' ');
          }
          match.appendReplacement(sb, replace);
        } while (match.find());
        match.appendTail(sb);
        address = sb.toString();
      }
      return address;
    }

    /**
     * Construct array of PatternReplace objects
     * @param patterns array of patterns to match
     * @return array of constructed PatternReplace objects
     */
    public static ProtectPatternReplace[] buildArray(String ... patterns) {
      return buildArray(patterns, false);
    }

    /**
     * Construct array of PatternReplace objects
     * @param patterns array of patterns to match
     * @param reverse false to convert blank to underscore, true to convert underscore to blank
     * @return array of constructed PatternReplace objects
     */
    public static ProtectPatternReplace[] buildArray(String[] patterns, boolean reverse) {
      ProtectPatternReplace[] result = new ProtectPatternReplace[patterns.length];
      for (int jj = 0; jj < patterns.length; jj++) {
        result[jj] = new ProtectPatternReplace(patterns[jj], reverse);
      }
      return result;
    }

    /**
     * Apply array of PatternReplace objects to line
     * @param line line to be converted
     * @param patterns array of PatternReplace objects to apply to line
     * @return final result
     */
    public static String replaceArray(String line, ProtectPatternReplace[] patterns) {
      if (patterns != null) {
        for (ProtectPatternReplace pr : patterns) {
          line = pr.replace(line);
        }
      }
      return line;
    }
  }

  private ProtectPatternReplace[] protectList = null;
  private ProtectPatternReplace[] reverseProtectList = null;

  /**
   * Setup protected name list.  These are street names that have to be
   * protected from being treated as individual words, usually because
   * they contain the word "AND"
   * @param nameList list of street names that need to be protected
   */
  protected void setupProtectedNames(String ... nameList) {

    protectList = ProtectPatternReplace.buildArray(nameList, false);
    reverseProtectList = ProtectPatternReplace.buildArray(nameList, true);
  }

  private  PatternReplace[] adjustMapAddressList = null;

  protected void setupMapAdjustReplacements(String ... mapAdjustments) {
    adjustMapAddressList = new PatternReplace[mapAdjustments.length/2];
    for (int j = 0; j<mapAdjustments.length; j+=2) {
      adjustMapAddressList[j/2] = new PatternReplace(mapAdjustments[j], mapAdjustments[j+1]);
    }
  }

  /**
   * Perform any parser specific customizations involved in calculating a
   * map address
   * @param sAddress original map address
   * @param city parsed city name
   * @param cross true if we are adjusting a cross street instead of the main address
   * @return customized map address
   */
  public String adjustMapAddress(String sAddress, String city, boolean cross) {
    return adjustMapAddress(sAddress, cross);
  }

  /**
   * Perform any parser specific customizations involved in calculating a
   * map address
   * @param sAddress original map address
   * @param cross true if we are adjusting a cross street instead of the main address
   * @return customized map address
   */
  public String adjustMapAddress(String sAddress, boolean cross) {
    return adjustMapAddress(sAddress);
  }

  /**
   * Perform any parser specific customizations involved in calculating a
   * map address
   * @param sAddress original map address
   * @return customized map address
   */
  public String adjustMapAddress(String sAddress) {
    sAddress = protectNames(sAddress);

    if (adjustMapAddressList != null) {
      for (PatternReplace pr : adjustMapAddressList ) {
        sAddress = pr.replace(sAddress);
      }
    }
    return sAddress;
  }

  /**
   * Perform an final parser specific custom adjustments to the calculated
   * map search address
   * @param sAddress calculated map search address
   * @return adjusted map search address
   */
  public String postAdjustMapAddress(String sAddress) {
    return unprotectNames(sAddress);
  }

  /**
   * Protect any declared protected names in string field
   * @param field
   * @return protected string field
   */
  protected String protectNames(String field) {
    return ProtectPatternReplace.replaceArray(field,  protectList);
  }

  /**
   * Restore any previously protected names in string field
   * @param field string field
   * @return original unprotected string field
   */
  protected String unprotectNames(String field) {
    return ProtectPatternReplace.replaceArray(field,  reverseProtectList);
  }

  /**
   * Perform parser specific conversions to city field before it is used to
   * generate the map address
   * @param city city field
   * @return adjusted city field
   */
  public String adjustMapCity(String city) {
    return city;
  }

  public String lookupGpsCoordinates(String address, String apt, String place, String city) {
    if (placeGpsLookupTable != null && !place.isEmpty()) {
      String gps = placeGpsLookupTable.getProperty(place);
      if (gps != null) return gps;
    }
    if (gpsLookupTable == null) return null;
    address = adjustGpsLookupAddress(address, apt, place, city);
    if (address == null) return null;

    String gps = gpsLookupTable.getProperty(address);
    if (gps == null) {
      int pt = address.indexOf(" APT:");
      if (pt >= 0) {
        gps = gpsLookupTable.getProperty(address.substring(0, pt).trim());
      }
    }
    return gps;
  }

  /**
   * Call to perform any adjustments on the raw address field before
   * trying to match it to an GPS location table entry
   * @param address raw address field
   * @return adjusted address field
   */
  protected String adjustGpsLookupAddress(String address) {
    return address;
  }

  /**
   * Call to perform any adjustments on the raw address field before
   * trying to match it to an GPS location table entry
   * @param address raw address field
   * @param apt apt/lot number
   * @return adjusted address field
   */
  protected String adjustGpsLookupAddress(String address, String apt) {
    return adjustGpsLookupAddress(address);
  }

  /**
   * Call to perform any adjustments on the raw address field before
   * trying to match it to an GPS location table entry
   * @param address raw address field
   * @param apt apt/lot number
   * @param place place name
   * @return adjusted address field
   */
  protected String adjustGpsLookupAddress(String address, String apt, String place) {
    return adjustGpsLookupAddress(address, apt);
  }

  /**
   * Call to perform any adjustments on the raw address field before
   * trying to match it to an GPS location table entry
   * @param address raw address field
   * @param apt apt/lot number
   * @param place place name
   * @param city city name
   * @return adjusted address field
   */
  protected String adjustGpsLookupAddress(String address, String apt, String place, String city) {
    return adjustGpsLookupAddress(address, apt, place);
  }

  /**
   * Parse address line into address and city fields
   * @param addressLine address line to be parsed
   * @param data message info object to be filled
   * @param parseCity true if cities should be parsed with dashes
   */
  private static final Pattern MSPACE = Pattern.compile(" {2,}");
  private static final Pattern INTERSECT = Pattern.compile("/|&|@");
  private static final Pattern APT = Pattern.compile("(?!^)(?!RMP|SUITES)((?:APTS|\\bAPT(?!S)|\\bUNIT|\\bSUITE|\\bROOM|\\bSTE|\\bRM|\\bFLOOR|\\bFLRS?|\\bLOT)(?![A-Z].)|#APT|#)[ #\\.:]*(.+)$",Pattern.CASE_INSENSITIVE);
  private static final Pattern DOT = Pattern.compile("\\.(?!\\d)");
  private static final Pattern DOUBLE_SLASH = Pattern.compile("//+");
  private static void parseAddress(String addressLine, MsgInfo.Data data, boolean parseCity) {
    addressLine = addressLine.trim();

    // Periods used with abbreviations also cause trouble.  Just get rid of all periods
    // except those followed by a digit which are presumed to be decimal points
    addressLine = DOT.matcher(addressLine).replaceAll("").trim();

    // Remove multple blanks
    addressLine = MSPACE.matcher(addressLine).replaceAll(" ");

    addressLine = stripLeadingZero(addressLine);
    addressLine = DOUBLE_SLASH.matcher(addressLine).replaceAll("/");
    addressLine = stripFieldEnd(addressLine, "#");

    // Pick off trailing apartment
    Matcher match = APT.matcher(addressLine);
    if (match.find()) {
      String sPrefix = match.group(1);
      if (!sPrefix.toUpperCase().startsWith("FL")) sPrefix = "";
      data.strApt = append(sPrefix, " ", match.group(2));
      addressLine = addressLine.substring(0,match.start()).trim();
    }

    addressLine = addressLine.replace("1/2", "1%2");
    for (String addr : INTERSECT.split(addressLine)) {
      addr = addr.trim();
      if (parseCity) {
        int pt = addr.lastIndexOf('-');
        if (pt > 0) {
          if (data.strCity.length() == 0) data.strCity = addr.substring(pt+1).trim();
          addr = addr.substring(0, pt).trim();
        }
      }
      data.strAddress = append(data.strAddress, " & ", addr);
    }
    data.strAddress = data.strAddress.replace("1%2", "1/2");
  }

  protected static String stripLeadingZero(String addressLine) {
    return LEAD_ZERO_PTN.matcher(addressLine).replaceFirst("");
  }
  private static final Pattern LEAD_ZERO_PTN = Pattern.compile("^[-0 ]+( |$)|^0+(?=[1-9])");

  /**
   * Set formated date/time field
   * @param dateFmt Date format to be used to parse date/time string
   * @param field date/time string to be parsed
   * @param data Data object where information should be saved
   * @return true if field was parsed successfully
   */
  public static boolean setDateTime(DateFormat dateFmt, String field, Data data) {
    try {
       Date date = dateFmt.parse(field);
       DateFormat fmt = dateFmt.getCalendar().get(Calendar.YEAR) == 1970 ? DATE_FMT2 : DATE_FMT;
       data.strDate = fmt.format(date);
       data.strTime = TIME_FMT.format(date);
       return true;
    } catch (ParseException ex) {
      return false;
    }
  }

  /**
   * Set formated date field
   * @param dateFmt Date format to be used to parse date string
   * @param field date string to be parsed
   * @param data Data object where information should be saved
   * @return true if field was parsed successfully
   */
  public static boolean setDate(DateFormat dateFmt, String field, Data data) {
    try {
       Date date = dateFmt.parse(field);
       DateFormat fmt = dateFmt.getCalendar().get(Calendar.YEAR) == 1970 ? DATE_FMT2 : DATE_FMT;
       data.strDate = fmt.format(date);
       return true;
    } catch (ParseException ex) {
      return false;
    }
  }

  /**
   * Set formated time field
   * @param dateFmt Date format to be used to parse date/time string
   * @param field time string to be parsed
   * @param data Data object where information should be saved
   * @return true if field was parsed successfully
   */
  public static boolean setTime(DateFormat dateFmt, String field, Data data) {
    try {
       Date date = dateFmt.parse(field);
       data.strTime = TIME_FMT.format(date);
       return true;
    } catch (ParseException ex) {
      return false;
    }
  }

  private static final DateFormat DATE_FMT = new SimpleDateFormat("MM/dd/yyyy");
  private static final DateFormat DATE_FMT2 = new SimpleDateFormat("MM/dd");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("HH:mm:ss");


  /**
   * Set GPS Coordinates in standard Google search format
   * handling special case where trailing zeros have been dropped
   * from the coordinates
   * @param location GPS coordinates to be saved
   * @param data data information object
   */
  public void setTrimmedGPSLoc(String location, Data data) {
    location = END_NUMBER.matcher(location).replaceAll("000");
    setGPSLoc(location, data);
  }
  private static final Pattern END_NUMBER = Pattern.compile("(?<=\\d)(?= |$)");


  /**
   * Set GPS Coordinates in standard Google search format
   * @param location GPS coordinates to be saved
   * @param data data information object
   * @return location field after removing the GPS coordinates
   */
  public String setGPSLoc(String location, Data data) {
    Matcher match = GPS_PATTERN.matcher(location);
    if (!match.find()) return location;
    data.strGPSLoc = data.countryCode.parseGPSCoords(match);
    if (data.strGPSLoc == null) data.strGPSLoc = "";
    location = append(location.substring(0,match.start()).trim(), " - ", location.substring(match.end()).trim());
    return location.trim();
  }

  /**
   * Determine if this is a personal name or a place name
   * @param field field to be checked
   * @return true if should be place name, false if a personal name
   */
  protected boolean checkPlace(String field) {
    if (field.contains(",")) return false;
    String upshift = field.toUpperCase();
    if (upshift.startsWith("MR")) return false;
    if (upshift.startsWith("DR ")) return false;
    if (upshift.startsWith("MS ")) return false;
    int cnt = 0;
    char last = 'X';
    for (char chr : field.toCharArray()) {
      if (chr == ' ' && last != ' ') cnt++;
      last = chr;
    }
    return cnt >= 2;
  }

 /**
  * Build a code table for use by convertCodeTable
  * @param table array containing pairs of strings, each pair will be considered
  * as a key/value pair to be inserted into the code table
  * @return resulting code table
  */
 public static Properties buildCodeTable(String[] table) {
   Properties props = new Properties();
   addCodeTable(props, table);
   return props;
 }

public static void addCodeTable(Properties props, String[] table) {
  for (int ndx = 0; ndx < table.length-1; ndx+=2) {
     props.put(table[ndx], table[ndx+1]);
   }
}

 /**
  * Look for an abbreviated form of a full street name.  If found, expand it to the full
  * street name
  * @param fullName Full street name
  * @param field address field containing possible abbreviated street name
  * @return address field with street name expanded to full name
  */
 protected static String expandStreet(String fullName, String field) {
   int trigLen  = fullName.lastIndexOf(' ');
   if (trigLen < 0) return field;
   trigLen += 2;
   if (trigLen > fullName.length()) return field;
   String trigger = fullName.substring(0,trigLen);
   int pt1 = 0;
   while (true) {
     int pt2 = field.indexOf(trigger, pt1);
     if (pt2 < 0) break;
     int pt3 = field.indexOf(' ', pt2+trigLen);
     if (pt3 < 0) pt3 = field.length();
     if (fullName.startsWith(field.substring(pt2,pt3))) {
       field = field.substring(0,pt2) + fullName + field.substring(pt3);
       pt3 += fullName.length()-(pt3-pt2);
     }
     pt1 = pt3;
   }
   return field;
 }

 /**
  * Convert any special codes in an item
  * @param item String item to be converted
  * @param codeTable table of codes with corresponding values
  * @return if item is a key to a codeTable entry, returns the associated value,
  * otherwise returns item
  */
 protected static String convertCodes(String item, Properties codeTable) {
   String value = codeTable.getProperty(item);
   return (value != null ? value : item);
 }

 /**
  * Utility method to cut a selected subfield out of a string field and
  * return everything that is left
  * @param field field are working on
  * @param start start index of subfield to be removed
  * @param end end index of subfield to be removed
  * @return Remainder of field once subfield has been removed.
  */
 protected static String cutOut(String field, int start, int end) {
   String pfx = field.substring(0,start).trim();
   String sfx = field.substring(end).trim();
   if (pfx.length() == 0) return sfx;
   if (sfx.length() == 0) return pfx;
   return pfx + " " + sfx;
 }

 /**
  * Class containing a list of strings that tokens will need to be checked against
  */
 protected static class MatchList {

   private Set<String> set;

   /**
    * Constructor
    * @param list list of strings that tokens will be checked against
    */
   public MatchList(String[] list) {
     set = new HashSet<String>(list.length);
     for (String entry : list) set.add(entry.toUpperCase());
   }

   /**
    * Determine if token is in list
    * @param token token to be checked
    * @return true if token is in list
    */
   public boolean contains(String token) {
     return set.contains(token.toUpperCase());
   }
 }

 /**
  * Convenience method to append two strings with a connector
  * @param str1 first string
  * @param connector connector string
  * @param str2 second string
  * @return appended string
  */
 public static String append(String str1, String connector, String str2) {
   if (str1.length() == 0) return str2;
   if (str2.length() == 0) return str1;
   return str1 + connector + str2;
 }

 /**
  * Covenience method to get results of a (possibly null) match group
  * @param input resuts of the Matcher.group() call
  * @return unnullified and trimmed result.
  */
 protected static String getOptGroup(String input) {
   return (input == null ? "" : input.trim());
 }

 /**
  * Convenience method to remove any extended charset characters from input data
  * @param line input data string
  * @return input data string purged of any extended charset characters
  */
 protected static String cleanExtendedChars(String line) {
   StringBuilder sb = null;
   for (int ndx = 0; ndx < line.length(); ndx++) {
     char ch = line.charAt(ndx);
     if (ch > 127) {
       if (sb == null) sb = new StringBuilder(line.substring(0,ndx));
     } else {
       if (sb != null) sb.append(ch);
     }
   }
   return (sb == null ? line : sb.toString());
 }

 /**
  * Convience method to extract substring from string which might not
  * be long enough to contain the full substring
  * @param body original string
  * @param st start position
  * @return substring
  */
 public static String substring(String body, int st) {
   return substring(body, st, Integer.MAX_VALUE);
 }

 /**
  * Convience method to extract substring from string which might not
  * be long enough to contain the full substring
  * @param body original string
  * @param st start position
  * @param end end position
  * @return substring
  */
 public static String substring(String body, int st, int end) {
   return untrimmedSubstring(body,st,end).trim();
 }

 /**
  * Convience method to extract substring from string which might not
  * be long enough to contain the full substring
  * @param body original string
  * @param st start position
  * @param end end position
  * @return substring
  */
 public static String untrimmedSubstring(String body, int st, int end) {
   int len = body.length();
   if (st >= len) return "";
   if (end > len) end = len;
   return body.substring(st, end);
 }

 /**
  * Clean name or place field of any references to wireless carrier names
  * @param name name to be cleaned
  * @return return cleaned result
  */
 protected static String cleanWirelessCarrier(String name) {
   return cleanWirelessCarrier(name, false);
 }

 /**
  * Clean name or place field of any references to wireless carrier names
  * @param name name to be cleaned
  * @param partial true if partial match should be removed, false if a complete match is required
  * @return return cleaned result
  */
 protected static String cleanWirelessCarrier(String name, boolean partial) {
   Matcher match = WIRELESS_CARRIER_PTN.matcher(name);
   boolean found = (partial ? match.find() : match.matches());
   if (found) name = name.substring(0,match.start()).trim();
   return name;
 }
 private static final Pattern WIRELESS_CARRIER_PTN = Pattern.compile("\\b(?:WIRELESS[-, ]+)?(?:VERIZON(?: WIRELESS)?(?: INTRADO \\(PSAP\\))?(?:\\(XYP\\))?|VZW(?:M?OBILE)?(?: USA)?|(?:MOBILITY,? )?(?:ATT? ?& ?T|AT ?T)(?: MOBILITY)?(?: \\(TCS\\))?|ATTMO|CONNEXON|T-MOBILE|SPRINT(?:PCS)?(?: NEXTEL- CDMA)?|US[- ]CELLULAR|METRO ?PCS|CORR WIRELESS|BLUEGRASS CELLULAR|APPALACHIAN WIRELESS|STARR WIRELESS|CELLULAR, US)\\b.*", Pattern.CASE_INSENSITIVE);

 /**
  * Strip optional constant string value from beginning of field
  * @param field field to be adjusted
  * @param val constant string value
  * @return adjusted field value
  */
 public static String stripFieldStart(String field, String val) {
   if (field.startsWith(val)) field = field.substring(val.length()).trim();
   return field;
 }

 /**
  * Strip optional constant string value from end of field
  * @param field field to be adjusted
  * @param val constant string value
  * @return adjusted field value
  */
 public static String stripFieldEnd(String field, String val) {
   if (field.endsWith(val)) field = field.substring(0,field.length()-val.length()).trim();
   return field;
 }

 /**
  * Remove common HTML sequences
  * @param body
  * @return
  */
 public static  String decodeHtmlSequence(String body) {
   body = COMMENT_PTN.matcher(body).replaceAll("");
   body = HTML_PTN.matcher(body).replaceAll("");
   body = HEAD_PTN.matcher(body).replaceFirst("");
   body = BR_PTN.matcher(body).replaceAll("\n");
   body = END_BR_PTN.matcher(body).replaceAll("");
   return decodeHtmlField(body);
 }
 private static final Pattern COMMENT_PTN = Pattern.compile("<!--.*?-->", Pattern.DOTALL);
 private static final Pattern HTML_PTN = Pattern.compile("^.*<HTML\\b[^>]*>|</?(?:B|BODY|DIV|FONT|I|META|O|P|PRE|SPAN|TABLE|TD|TR)\\b[^>]*>|</HTML>.*$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
 private static final Pattern HEAD_PTN = Pattern.compile("<HEAD>.*</HEAD>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
 private static final Pattern BR_PTN = Pattern.compile("< *(?:br|p) */?>", Pattern.CASE_INSENSITIVE);
 private static final Pattern END_BR_PTN = Pattern.compile("< */(?:br|p) *>", Pattern.CASE_INSENSITIVE);

 public static String decodeHtmlField(String body) {
   body = body.replace("&nbsp;",  " ").replace("&amp;",  "&").replace("&gt;", ">").replace("&lt;", "<");

   Matcher match = CODE_PTN.matcher(body);
   if (match.find()) {
     StringBuffer sb = new StringBuffer();
     do {
       match.appendReplacement(sb, String.valueOf((char)Integer.parseInt(match.group(1))));
     } while (match.find());
     match.appendTail(sb);
     body = sb.toString();
   }

   return body;
 }
 private static final Pattern CODE_PTN = Pattern.compile("&#(\\d+);");

 /**
  * Utility class used to parse fixed length fields from text line
  */
 public static class FParser {
   private String line;
   private int spt = 0;
   private boolean optional = false;

   public FParser(String line) {
     this.line = line;
   }

   /**
    * Declare all following fields to be optional.
    * Basically this means checks for fixed labels will not fail if
    * the expected position of the label is beyond the length of the
    * text string
    */
   public void setOptional() {
     optional = true;
   }

   /**
    * @return remainder of line
    */
   public String get() {
     String value =  substring(line, spt);
     spt = line.length();
     return value;
   }

   /**
    * Return next fixed length field
    * @param len length of requested field
    * @return value if next fixed length field
    */
   public String get(int len) {
     int ept = spt + len;
     String value = substring(line, spt, ept);
     spt = ept;
     return value;
   }

   /**
    * Check for fixed label
    * @param marker expected label value
    * @return true if label found in next field
    */
   public boolean check(String marker) {
     marker = trimOptionalMarker(0, marker);
     int mlen = marker.length();
     if (!lookahead(0, mlen).equals(marker.trim())) return false;
     skip(mlen);
     return true;
   }

   /**
    * Check for matching pattern
    * This does not work properly if called after setOptional()
    * @param ptn Pattern to be matched
    * @return true if pattern matched current data position
    */
   public boolean check(Pattern ptn) {
     if (spt >= line.length()) return false;
     Matcher match = ptn.matcher(line.substring(spt));
     if (!match.lookingAt()) return false;
     skip(match.end());
     return true;
   }

   /**
    * Check ahead for for fixed number of blanks
    * @param len number of required blanks
    * @return true if successful
    */
   public boolean checkAheadBlanks(int offset, int len) {
     return lookahead(offset,len).length() == 0;
   }

   /**
    * Check for fixed number of blanks
    * @param len number of required blanks
    * @return true if successful
    */
   public boolean checkBlanks(int len) {
     if (lookahead(0,len).length() > 0) return false;
     skip(len);
     return true;
   }

   /**
    * Return fixed length value terminated by fixed marker
    * @param marker fixed marker value
    * @param lengths possible lengths of field value
    * @return next field value terminated by fixed marker
    * or null if marker not found
    */
   public String getOptional(String marker, int ... lengths) {
     if (spt >= line.length()) return null;
     if (lengths.length == 0) {
       int ept = line.indexOf(marker, spt);
       if (ept < 0) return null;
       String value = line.substring(spt, ept).trim();
       spt = ept + marker.length();
       return value;
     }

     for (int len : lengths) {
       String value = getOptional(len, marker);
       if (value != null) return value;
     }
     return null;
   }


   /**
    * Return fixed length value terminated by fixed marker
    * @param len fixed field length
    * @param marker fixed marker value
    * @return next field value terminated by fixed marker
    * or null if marker not found
    */
   public String getOptional(int len, String marker) {
     if (!checkAhead(len, marker)) return null;
     String value = get(len);
     skip(marker.length());
     return value;
   }

   /**
    * Check for fixed marker value somewhere ahead of text
    * @param marker fixed marker value
    * @param offsets possible offsets to check value
    * @return offset where marker was found, or -1 if nothing matched
    */
   public int checkAhead(String marker, int ... offsets) {
     for (int offset : offsets) {
       if (checkAhead(offset, marker)) return offset;
     }
     return -1;
   }

   /**
    * Check for fixed label value
    * @param offset of expected label value
    * @param marker expected label value
    * @return true if found
    */
   public boolean checkAhead(int offset, String marker) {
     marker = trimOptionalMarker(offset, marker);
     return lookahead(offset, marker.length()).equals(marker.trim());
   }

   /**
    * retrieve look ahead field value
    * @param offset offset of requested field value
    * @param length length of requested field value
    * @return requested look ahead field value
    */
   public String lookahead(int offset, int length) {
     return substring(line, spt+offset, spt+offset+length);
   }

   /**
    * Skip field
    * @param len length of field to be skipped
    */
   public void skip(int len) {
     spt += len;
   }

   public boolean atEnd() {
     return spt >= line.length();
   }

   /**
    * Trim fixed label to match end of text string if optional flag is set
    * @param offset offset from current position
    * @param marker value to be checked
    * @return possibly trimmed value
    */
   private String trimOptionalMarker(int offset, String marker) {
     if (!optional) return marker;
     int left = line.length() - (spt+offset);
     if (left < 0) left = 0;
     if (left >= marker.length()) return marker;
     return marker.substring(0,left);
   }
 }

 /**
  * Worker class that will parse a into consecutive substrings up to the
  * next occurrence of a particular pattern or string or character
  */
 public static class Parser {

   private String line;
   private int spt;
   private int ept;

   /**
    * Constructor
    * @param line string to be parsed out
    */
   public Parser(String line) {
     init(line);
   }

   /**
    * Reset parser with new string
    * @param line new string to be parsed
    */
   public void init(String line) {
     this.line = line;
     this.spt = 0;
     this.ept = line.length();
   }

   /**
    * @return true if parser contains no more information
    */
   public boolean isEmpty() {
     return spt == ept;
   }

   /**
    * Retrieve the next complete line from the parser buffer
    * @return null if buffer is empty, otherwise returns next line
    */
   public String getLine() {
     if (isEmpty()) return null;
     return get('\n');
   }

   public String getUntrimmedLine() {
     if (isEmpty()) return null;
     Matcher match = getMatcher(NEXT_LINE_PTN);
     if (match == null) return null;
     return match.group(1);
   }
   private static final Pattern NEXT_LINE_PTN = Pattern.compile("(.*?)(?:\n|$)");

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter
    */
   public String get(char delim) {
     return get(delim, false, false, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter if found, empty string otherwise
    */
   public String getOptional(char delim) {
     return get(delim, true, false, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter
    */
   public String getRequired(char delim) {
     return get(delim, false, true, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter without breaking
    * up matched parens
    */
   public String getSmart(char delim) {
     return get(delim, false, false, true);
   }

   /**
    * @return next item enclosed in parenthesis.  If not found return empty string
    */
   public String getParenItem() {
     skipBlanks();
     if (spt >= ept || line.charAt(spt) != '(') return "";
     spt++;
     return getSmart(')');
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter
    */
   public String getLast(char delim) {
     return getLast(delim, false, false, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter if found, empty string otherwise
    */
   public String getLastOptional(char delim) {
     return getLast(delim, true, false, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter if found, empty string otherwise
    */
   public String getLastRequired(char delim) {
     return getLast(delim, false, true, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter without breaking
    * up matched parens
    */
   public String getLastSmart(char delim) {
     return getLast(delim, false, false, true);
   }

   /**
    * @return next item enclosed in parenthesis.  If not found return empty string
    */
   public String getLastParenItem() {
     skipLastBlanks();
     if (spt >= ept || line.charAt(ept-1) != ')') return "";
     ept--;
     return getLastSmart('(');
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter
    */
   public String get(String delim) {
     return get(delim, false, false, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter if found, empty string otherwise
    */
   public String getOptional(String delim) {
     return get(delim, true, false, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter if found, empty string otherwise
    */
   public String getRequired(String delim) {
     return get(delim, false, true, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter if found
    */
   public String getLast(String delim) {
     return getLast(delim, false, false, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter if found, empty string otherwise
    */
   public String getLastOptional(String delim) {
     return getLast(delim, true, false, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter if found, empty string otherwise
    */
   public String getLastRequired(String delim) {
     return getLast(delim, false, true, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter
    */
   public String get(Pattern delim) {
     return get(delim, false, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter if found, empty string otherwise
    */
   public String getOptional(Pattern delim) {
     return get(delim, true, false);
   }

   /**
    * @param delim delimiter
    * @return everything up to next occurrence of delimiter if found, empty string otherwise
    */
   public String getRequired(Pattern delim) {
     return get(delim, false, true);
   }

   /**
    * @return Everything left
    */
   public String get() {
     return get(-1, 0, false, false);
   }

   /**
    * @param delim delimiter
    * @param optional true if empty string should be returned if deliminter not found
    * @param required true if null should be returned if delimiter not found
    * @param smart true to not break up matched parens
    * @return everything up to next occurrence of delimiter
    */
   private String get(char delim, boolean optional, boolean required, boolean smart) {
     if (delim == ' ') skipBlanks();
     return get(indexOf(delim, smart), 1, optional, required);
   }

   /**
    * @param delim delimiter
    * @param optional true if empty string should be returned if deliminter not found
    * @param required true if null should be returned if delimiter not found
    * @param smart true to not break up matched parens
    * @return everything up to next occurrence of delimiter
    */
   private String get(String delim, boolean optional, boolean required, boolean smart) {
     if (isAllBlanks(delim)) skipBlanks();
     return get(indexOf(delim, smart), delim.length(), optional, required);
   }

   /**
    * @param delim delimiter
    * @param optional true if empty string should be returned if deliminter not found
    * @param required true if null should be returned if delimiter not found
    * @param smart true to not break up matched parens
    * @return everything up to next occurrence of delimiter
    */
   private String getLast(char delim, boolean optional, boolean required, boolean smart) {
     if (delim == ' ') skipLastBlanks();
     return getLast(lastIndexOf(delim, smart), 1, optional, required);
   }

   /**
    * @param delim delimiter
    * @param optional true if empty string should be returned if deliminter not found
    * @param required true if null should be returned if delimiter not found
    * @return everything up to next occurrence of delimiter
    */
   private String getLast(String delim, boolean optional, boolean required, boolean smart) {
     if (isAllBlanks(delim)) skipLastBlanks();
     int len = delim.length();
     return getLast(lastIndexOf(delim, smart), len, optional, required);
   }

   /**
    * @param ptn delimiter pattern
    * @param optional true if empty string should be returned if deliminter not found
    * @param required true if null should be returned if delimiter not found
    * @return everything up to next occurrence of delimiter
    */
   private String get(Pattern ptn, boolean optional, boolean required) {
     Matcher match = ptn.matcher(line);
     int ndx = -1;
     int len = 0;
     if (match.find(spt)) {
       ndx = match.start();
       len = match.end()-ndx;
     }
     return get(ndx, len, optional, required);
   }

   private boolean isAllBlanks(String delim) {
     for (char chr : delim.toCharArray()) {
       if (chr != ' ') return false;
     }
     return true;
   }

   private int indexOf(String delim, boolean smart) {
     if (!smart) return line.indexOf(delim, spt);
     return smartIndexOf(delim);
   }

   private int indexOf(char delim, boolean smart) {
     if (!smart) return line.indexOf(delim, spt);
     return smartIndexOf(""+delim);
   }

   private int smartIndexOf(String delim) {
     int nest = 0;
     int tmp = spt;
     while (tmp < ept) {
       if (line.charAt(tmp) == '(') nest++;
       if (nest == 0 && line.substring(tmp).startsWith(delim)) return tmp;
       if (line.charAt(tmp) == ')') nest--;
       tmp++;
     }
     return -1;
   }

   private int lastIndexOf(String delim, boolean smart) {
     if (!smart) return line.lastIndexOf(delim, ept-delim.length());
     return smartLastIndexOf(delim);
   }

   private int lastIndexOf(char delim, boolean smart) {
     if (!smart) return line.lastIndexOf(delim, ept-1);
     return smartLastIndexOf(""+delim);
   }

   private int smartLastIndexOf(String delim) {
     int nest = 0;
     int tmp = ept-1;
     while (tmp >= spt) {
       if (line.charAt(tmp) == ')') nest++;
       if (nest == 0 && line.substring(tmp).startsWith(delim)) return tmp;
       if (line.charAt(tmp) == '(') nest--;
       tmp--;
     }
     return -1;
   }

   /**
    * Skip the start pointer over any leadning blanks
    * this is called with the delimiter contains nothing but blanks
    */
   private void skipBlanks() {
     while (spt < ept && line.charAt(spt)==' ') spt++;
   }

   /**
    * Skip the end pointer over any trailing blanks
    * this is called with the delimiter contains nothing but blanks
    */
   private void skipLastBlanks() {
     while (ept > spt && line.charAt(ept-1)==' ') ept--;
   }

   /**
    * @param npt index returned by indexof search
    * @param len length of delimiter searched for
    * @param optional true if empty string should be returned if deliminter not found
    * @param required true if null should be returned if delimiter not found
    * @return whatever was found
    */
   private String get(int npt, int len, boolean optional, boolean required) {
     if (npt < 0 || npt+len > ept) {
       if (optional) return "";
       if (required) return null;
       npt = ept;
       len = 0;
     }
     String result = line.substring(spt, npt).trim();
     spt = npt + len;
     return result;
   }

   /**
    * @param npt index returned by indexof search
    * @param len length of delimiter searched for
    * @param optional true if empty string should be returned if deliminter not found
    * @param required true if null should be returned if delimiter not found
    * @return whatever was found
    */
   private String getLast(int npt, int len, boolean optional, boolean required) {
     if (npt < 0 || npt<spt) {
       if (optional) return "";
       if (required) return null;
       npt = spt;
       len = 0;
     }
     String result = line.substring(npt+len, ept).trim();
     ept = npt;
     return result;
   }

   /**
    * Search for the next occurrance of a specific pattern and return the match result
    * @param ptn Pattern for which we are searching
    * @return a Match object matching the specified pattern if a match was found. Null
    * if no match was found
    */
   public Matcher getMatcher(Pattern ptn) {
     Matcher match = ptn.matcher(line);
     if (!match.find(spt)) return null;

     spt = match.end();
     return match;
   }

   /**
    * Convenience method to return first group of a pattern match
    * @param ptn Pattern to be searched for
    * @return First group value if match found, empty string otherwise
    */
   public String getMatcherValue(Pattern ptn) {
     Matcher match = getMatcher(ptn);
     return match == null ? "" : match.group(1);
   }
 }

 /**
  * Determine if message address matches address filter
  * @param sAddress message address
  * @param sFilter address filter
  * @return true if message address satisfies filter
  */
 public static boolean matchFilter(String sAddress, String sFilter) {

   if (sFilter == null) return true;

   // A filter with length of 0 or 1 is invalid and is always passed
   sFilter = sFilter.trim();
   if (sFilter.length() <= 1) return true;

   // Filter can consist of multiple address filters separated by comas
   for (String tFilter : FILTER_SPLIT.split(sFilter)) {
     tFilter = tFilter.trim();

     // A subfilter with length of 0 or 1 is invalid and is ignored
     // Doing otherwise makes it too difficult to determine whether or not
     // an active filter is in place
     if (tFilter.length() <= 1) continue;

     // If filter consists only of numeric digits, it needs to match the
     // beginning of what is presumably a phone number. Taking some pains
     // to eliminate the spurious +1 that sometimes gets added to the the
     // sender phonen number
     if (DIGITS.matcher(tFilter).matches()) {
       String tmp = sAddress;
       if (tmp.startsWith("+")) tmp = tmp.substring(1);
       if (tmp.startsWith("1") && ! tFilter.startsWith("1")) tmp = tmp.substring(1);
       if (tmp.startsWith(tFilter)) return true;
     }

     // Otherwise it can
     // match any substring of the sender address.  This last
     // check should be case insensitive, which we accomplish by downshifting
     // both the address and the filter
     else {
       if (sAddress.toLowerCase().contains(tFilter.toLowerCase())) return true;
     }
   }
   return false;
  }
  private static final Pattern DIGITS = Pattern.compile("\\d+");
  private static final Pattern FILTER_SPLIT = Pattern.compile("[\n,]");
}
