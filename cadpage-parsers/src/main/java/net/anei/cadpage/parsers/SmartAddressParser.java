package net.anei.cadpage.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract message parser class that adds support for "smart" address parsing.
 * That is parsing logic that can be used for text formats that do not clearly
 * diliminate where the the address portion of the message can be found.
 *
 */
public abstract class SmartAddressParser extends MsgParser {

  /**
   * Type code indicating what kind of information might precede the address
   * portion of this message
   */
  public  enum StartType {START_ADDR, START_CALL, START_CALL_PLACE, START_PLACE, START_OTHER};

  /**
   * Flag indicating that the start field (either CALL, PLACE, or SKIP) must
   * contain at least one token
   */
  public static final int FLAG_START_FLD_REQ = 0x0001;

  /**
   * Flag indicating that an @ mark will be used to mark the start of a
   * place name following the normal address instead of an address
   */
  public static final int FLAG_AT_PLACE = 0x0002;

  /**
   * Flag indicating that an @ mark can be used to mark either the start of
   * an address or the start of a place name following an address
   */
  public static final int FLAG_AT_BOTH = 0x0004;

  /**
   * Flag indicating that address should extend to end of parsed data field
   */
  public static final int FLAG_ANCHOR_END = 0x0008;

  /**
   * Flag indicating that status should always be calculated, even if
   * both beginning and end of the address have been identified
   */
  public static final int FLAG_CHECK_STATUS = 0x0010;

  /**
   * Flag indicating that the & or / between street names for an intersection
   * type address may not be present and should be implied
   */
  public static final int FLAG_IMPLIED_INTERSECT = 0x0020;

  /**
   * Flag indicating the presence of some kind of pad field between the
   * address proper and the city field.   The field can be retrieved later
   * with a getPadField() method
   */
  public static final int FLAG_PAD_FIELD = 0x0040;

  /**
   * Flag indicating that @ and AT tokens should be ignored
   */
  public static final int FLAG_IGNORE_AT = 0x0080;

  /**
   * Flag indicating that we only want to parse a cross street
   */
  public static final int FLAG_ONLY_CROSS = 0x0100;

  /**
   * Flag indicating that we only want to parse a city
   */
  public static final int FLAG_ONLY_CITY = 0x0200;

  /**
   * Flag indicating that a numeric field following the address should NOT
   * be considered an apartment number.  Use when dispatch never includes
   * a trailing apt.  And the following field might legitimately start
   * with a number as "123 BLACKSTONE DR 76 YO FEMALE
   */
  public static final int FLAG_NO_IMPLIED_APT =0x0400;

  /**
   * Flag indicating there is cross street information following the address
   * which means logic to identify a city followed by a street suffix as as
   * street name instead of a city should be suppressed.
   * Normally 123 BLACKSTONE DR PHILOMATH MAIN ST
   * would not terminate at PHILOMATH because PHILOMATH MAIN ST looks like
   * a valid street name.  Turning on this flag changes that.  There have
   * been cases were turning this on fixed problems with following data that
   * was not really a cross street, but looked like it could be a cross street
   */
  public static final int FLAG_CROSS_FOLLOWS = 0x0800;

  /**
   * Flag indicating that there my not be a blank delimiter between the
   * address and the data field in front of it. As in
   * STRUCTURE FIRE100 BLACK ST
   * Things are pretty bad when you have to use this
   */
  public static final int FLAG_START_FLD_NO_DELIM = 0x1000;

  /**
   * Flag indicating that we will accept street names with non street suffix.
   * Only use in cases where dispatch routinely drops streets suffixes.  When
   * you have to use this, things are going to go badly and a lot of addresses
   * are not going to parser properly.  But this is the best we can do.
   */
  public static final int FLAG_OPT_STREET_SFX = 0x2000;

  /**
   * Flag indicating that @ signs should be processed as at markers
   * but the word "AT" should be ignored.  This should probably be
   * used more often than it is.  Without it, things got south when
   * following data inocently contains the word AT.
   */
  public static final int FLAG_AT_SIGN_ONLY = 0x4000;

  /**
   * Flag indicating the presence of some kind of PAD field between
   * the address and city name.  Similar to the FLAG_PAD_FIELD flag
   * except that this pad field may not contain a legitimate city name.
   * The pad field can later be retrieved with the getPadField() method
   */
  public static final int FLAG_PAD_FIELD_EXCL_CITY = 0x8000;

  /**
   * Flag indicating that this call should not find a city.  That any predefined city lists
   * or codes should be ignored.  Basically tells the parser to ignore any city codes or lists.
   * Obviously not needed when there are no city codes/lists.  But can be useful when
   * you have an address line that might or might not contain a city.  A parser can
   * work with a line that might contain either
   * STRUCTURE FIRE 1035 MAIN ST, PHILOMATH or
   * STRUCTURE FIRE 1035 MAIN ST PHILOMATH
   * by checking for the comma.  If found, extract city name from after the comma and turn
   * on the FLAG_NO_CITY flag
   */
  public static final int FLAG_NO_CITY = 0x10000;

  /**
   * Flag indicating that a NEAR clause should extend to the end of the input string.
   * I'm really not sure about this one.  But here is what I think happens.
   * Normally an address like 1035 MAIN ST NEAR THE PHILOMATH FIRE STATION
   * would parser NEAR THE as the place name and PHILOMATH as the city.  With
   * this flag set, NEAR THE PHILOMATH FIRE STATION goes into the place name.
   */
  public static final int FLAG_NEAR_TO_END = 0x20000;

  /**
   * Flag indicating a city with no address is an acceptable result
   */
  public static final int FLAG_EMPTY_ADDR_OK = 0x40000;

  /**
   * Flag indicating that entire line should be assigned to address in
   * the event of a full parser failure
   */
  public static final int FLAG_FALLBACK_ADDR = 0x80000;

  /**
   * Flag indicating that extra effort should be expended to identify
   * implied apt fields at the end of the address field.  Normally only
   * numeric tokens following the address are considered apartments.  This
   * triggers some aggressive logic to identify non-numeric information
   * following the address and assigning it to the apt field.
   */
  public static final int FLAG_RECHECK_APT = 0x100000;

  /**
   * Flag indication that there is absolutely, positively, no street suffix
   * in this address.  Used only in the incredibly weird case where search for
   * a street suffix will mistakenly pick up a following cross street.
   */
  public static final int FLAG_NO_STREET_SFX = 0x200000;

  /**
   * Flag indicating that the word "AND" should not be considered as
   * connecting two street names at an intersection
   */
  public static final int FLAG_AND_NOT_CONNECTOR = 0x400000;

  /**
   * Flag indicating that simple directions generally follow rather
   * than lead the street name.  This only really matters when
   * a direction is found between two street names
   */
  public static final int FLAG_PREF_TRAILING_SIMPLE_DIR = 0x800000;

  /**
   * Flag indicating that @ and at should be treated as cross street markers
   */
  public static final int FLAG_AT_MEANS_CROSS = 0x1000000;

  /**
   * Flag indicating city name may occur before @ sign.  This is only significant
   * if FLAG_AT_PLACE or FLAG_AT_BOTH have been specified
   */
  public static final int FLAG_AT_INCL_CITY = 0x2000000;

  /**
   * Flag indicating we are looking for anything in this line *EXCEPT* an address
   * this is used internally by the @ mark processing.  It is not expected to
   * ever be used in the real world.
   */
  public static final int FLAG_NO_ADDRESS = 0x4000000;

  /**
   * Normally, a street name that should be ended with a street suffix will be
   * considered safely terminated by a cross street or apt marker.  This flag
   * requires that street names end in proper street suffixes to be accepted
   */
  public static final int FLAG_STRICT_SUFFIX = 0x8000000;

  /**
   * Allow street names to have common direction qualifiers at both ends
   */
  public static final int FLAG_ALLOW_DUAL_DIRECTIONS = 0x10000000;

  /**
   * Flag indicating that bound directions generally follow rather
   * than lead the street name.  This only really matters when
   * a bound direction is found between two street names
   */
  public static final int FLAG_PREF_TRAILING_BOUND = 0x20000000;

  /**
   * Flag indicating the commas should be accepted in the address field
   */
  public static final int FLAG_ACCEPT_COMMA = 0x40000000;

  // Flag combination that indicates we are processing some kind of pad field
  // Rechecking the apt is treated as a flavor of pad field
  private static final int FLAG_ANY_PAD_FIELD = FLAG_PAD_FIELD | FLAG_PAD_FIELD_EXCL_CITY | FLAG_RECHECK_APT;

  /**
   * Flag combination indicating that all direction constructs generally follow rather
   * than lead the street name.  This only really matters when
   * a direction is found between two street names
   */
  public static final int FLAG_PREF_TRAILING_DIR = FLAG_PREF_TRAILING_SIMPLE_DIR | FLAG_PREF_TRAILING_BOUND;

  // Status codes that might be returned
  public static final int STATUS_TRIVIAL = 5;
  public static final int STATUS_FULL_ADDRESS = 4;
  public static final int STATUS_INTERSECTION = 3;
  public static final int STATUS_STREET_NAME = 2;
  public static final int STATUS_MARGINAL = 1;
  public static final int STATUS_NOTHING = 0;
  public static final int STATUS_EMPTY = -1;
  public static final int STATUS_REJECT = -2;

  // Pattern searching for characters that are not allowed in addresses
  private Pattern badCharPtn = null;

  private Properties cityCodes = null;

  // Main dictionary maps words to a bitmap indicating what is important about that word
  private final Map<String, Long> dictionary = new HashMap<String, Long>();

  // Bitmask indicating dictionary word is a road suffix
  private static final long ID_ROAD_SFX = 1L;

  // Bitmask bit indicating dictionary word is a route number prefix
  private static final long ID_ROUTE_PFX_PFX = 2L;

  // Bitmask bit indicating dictionary word is a direction modifier
  private static final long ID_DIRECTION = 4L;

  // Bitmask bit indicating dictionary work is an intersection connector
  private static final long ID_CONNECTOR = 8L;

  // Bitmask bit indicating dictionary word is a cross street selector
  private static final long ID_CROSS_STREET = 0x10L;

  // Bitmask bit indicating dictionary word is a city name/code
  private static final long ID_CITY = 0x100L;

  // Bitmask bit indicating numeric token
  private static final long ID_NUMBER = 0x200L;

  // Bitmask bit indicating start of multiword token
  private static final long ID_MULTIWORD = 0x400L;

  // Bitmask bit indicating a start address marker
  private static final long ID_AT_MARKER = 0x800L;

  // Bitmask bit indicating token had a preceding @ character
  // private static final long ID_INCL_AT_MARKER = 0x1000L;    *** AVAILABLE ***

  // Bitmask bit indicating token is an apartment selector
  private static final long ID_APT = 0x2000L;

  // Bitmask bit indicating token is a complete single word token
  // both this and the ID_MULTIWORD flag may be set if a token is both
  // a single complete token and the start of a multiword token
  private static final long ID_COMPLETE = 0x4000L;

  // Bitmask bit indicating token is a route prefix that can be extended
  // by another normal route prefix
  private static final long ID_ROUTE_PFX_EXT =  0x8000L;

  // Bitmask bit indicating token is an ambiguous road suffix
  private static final long ID_AMBIG_ROAD_SFX = 0x10000L;

  // Bitmask bit indicating token cannot be part of an address
  private static final long ID_NOT_ADDRESS = 0x20000L;

  // Bitmask bit indicating token can be an alpha route or highway number
  private static final long ID_ALPHA_ROUTE = 0x40000L;

  // Bitmask bit indicating token is an optional extra road prefix (OLD, NEW)
  private static final long ID_OPT_ROAD_PFX = 0x80000L;

  // Bitmask bit indicating token is a road suffix commonly associated with
  // numbered road names
  private static final long ID_NUMBERED_ROAD_SFX = 0x100000L;

  // Bitmask bit indicating token is a word that is commonly used to qualify
  // other highway names
  private static final long ID_ROAD_QUALIFIER = 0x200000L;

  // Bitmask indicating dictionary word should be considered as starting a multi-word
  // street name
  private static final long ID_STREET_NAME_PREFIX = 0x400000L;

  private static final long ID_SPEC_CROSS_FWD = 0x0800000L;
  private static final long ID_SPEC_CROSS_REV = 0x1000000L;

  private static final long ID_RELATION = 0x2000000L;

  private static final long ID_NEAR = 0x4000000L;

  private static final long ID_SINGLE_WORD_ROAD = 0x8000000L;

  private static final long ID_BYPASS = 0x10000000L;

  private static final long ID_BLOCK = 0x20000000L;

  private static final long ID_AND_CONNECTOR = 0x40000000L;

  private static final long ID_FLOOR = 0x80000000L;

  private static final long ID_DR = 0x100000000L;

  private static final long ID_DOCTOR = 0x200000000L;

  private static final long ID_APT_SOFT = 0x400000000L;

  private static final long ID_NUMBER_SUFFIX = 0x800000000L;

  private static final long ID_ST = 0x1000000000L;

  private static final long ID_SAINT = 0x2000000000L;

  private static final long ID_OF = 0x4000000000L;

  private static final long ID_TO = 0x8000000000L;

  private static final long ID_ROUTE_PFX = 0x10000000000L;

  private static final long ID_MILE_MARKER = 0x20000000000L;

  private static final long ID_PURE_DIRECTION = 0x40000000000L;

  private static final long ID_NOT_STREET_NAME = 0x80000000000L;

  private static final long ID_SPECIAL_STREET_FWD = 0x100000000000L;

  private static final long ID_SPECIAL_STREET_FWD_END = 0x200000000000L;

  private static final long ID_SPECIAL_STREET_REV = 0x400000000000L;

  private static final long ID_SPECIAL_STREET_REV_END = 0x800000000000L;

  private static final long ID_YEAR_OLD_NOT_ADDRESS = 0x1000000000000L;

  private static final long ID_JUST = 0x2000000000000L;

  private static final Pattern PAT_HOUSE_NUMBER = Pattern.compile("\\d+(?:-[A-Z]?[0-9/]+|\\.\\d)?(?:-?(?:[A-Z]|BLK))?", Pattern.CASE_INSENSITIVE);

  // Permanent parsing  flags
  private int permParseFlags = 0;

  // List of multiple word cities
  private MultiWordList mWordCities = null;

  // List of multiple word street names
  // We keep two of these in case we have to search forward or backward
  private MultiWordList mWordStreetsFwd = null;
  private MultiWordList mWordStreetsRev = null;

  // List of special street names
  private MultiWordList mWordSpecialFwd = null;
  private MultiWordList mWordSpecialRev = null;

  // List of special cross street names
  private MultiWordList mWordCrossStreetsFwd = null;
  private MultiWordList mWordCrossStreetsRev = null;

  // Call lookup table
  private CodeSet callDictionary = null;

  // Permanent address flags
  private boolean allowDirectionHwyNames = false;

  // Special Place detection algorithm
  private Pattern placeAddressPtn = null;
  private boolean placeAddressPtnExcl = false;

  // Special reject address pattern
  private Pattern rejectAddressPtn = null;

  public SmartAddressParser(String[] cities, String defCity, String defState) {
    this(cities, defCity, defState, CountryCode.US);
  }

  public SmartAddressParser(String[] cities, String defCity, String defState,
                            CountryCode code) {
    this(defCity, defState, code);
    if (cities != null) setupCities(cities);
  }

  public SmartAddressParser(Properties cityCodes, String defCity, String defState) {
    this(cityCodes, defCity, defState, CountryCode.US);
  }

  public SmartAddressParser(Properties cityCodes, String defCity, String defState,
                            CountryCode code) {
    this(defCity, defState, code);
    if (cityCodes != null) setupCities(cityCodes);
    this.cityCodes = cityCodes;
  }

  public SmartAddressParser(String defCity, String defState) {
    this(defCity, defState, CountryCode.US);
  }

  public SmartAddressParser(String defCity, String defState, CountryCode code) {
    super(defCity, defState, code);
    setupDictionary(defState);
    allowBadChars("");
  }

  /**
   * Set up base dictionary common to all countries
   * @param defState default state
   */
  private void setupDictionary(String defState) {

    setupDictionary(ID_ROAD_SFX,
        "AVENUE", "AV", "AVE",
        "STREET", "ST",
        "PLACE", "PL",
        "ROAD", "RD",
        "LANE", "LN",
        "DRIVE", "DR",
        "SQUARE", "SQ",
        "BLVD", "BL", "BLV", "BVD", "BV", "BD",
        "PARKWAY", "WAY", "PKWAY", "PKWY", "PKY", "PWY", "PK", "PY", "FWY", "WY", "HW", "EXPW", "EXPY", "PW", "PKW",
        "CIRCLE", "CIR", "CL", "CI", "CR",
        "TRAIL", "TRL", "TR", "TL",
        "PATH",
        "PIKE", "PKE",
        "COURT", "CT",
        "TER", "TERR",
        "HWY", "HY", "HW",
        "MALL",
        "GR",
        "GTWY", "GATEWAY",
        "HOLW",
        "PLAZ", "PLAZA",
        "TURNPIKE", "TURNPKE", "TRNPK", "TPKE", "TRPK", "TPK", "TP",
        "PASS",
        "EST",
        "RUN",
        "GRN",
        "GRV",
        "LOOP",
        "TERRACE", "TRC", "TRCE", "TE",
        "ESTATES", "ESTS",
        "WALK",
        "CUTOFF",
        "RCH", "REACH",
        "ARCH",
        "MNR",
        "BYPASS", "BYP",
        "ALLEY", "ALY",
        "FREEWAY",
        "HT", "HTS", "HEIGHTS",
        "BND", "BEND",
        "CV", "COVE",
        "THOROUGHFARE",
        "KNOLL",
        "XING");
    if ((getMapFlags() & MAP_FLG_SUPPR_LA) == 0)  setupDictionary(ID_ROAD_SFX, "LA");

    setupDictionary(ID_BYPASS, "BYPASS", "BYP", "BUSINESS", "BUS", "ALT");

    setupDictionary(ID_AMBIG_ROAD_SFX,
        "PLACE", "TRAIL", "PATH", "PIKE", "COURT", "MALL", "TURNPIKE", "PASS",
        "RUN", "LANE", "PARK", "POINT", "RIDGE", "CREEK", "MILL", "BRIDGE", "HILLS",
        "HILL", "TRACE", "MILE", "BAY", "NOTCH", "END", "LOOP", "ESTATES",
        "SQUARE", "WALK", "CIRCLE", "GROVE", "HT", "HTS", "HEIGHTS", "BEND", "VALLEY",
        "WAY", "GATEWAY", "KNOLL", "COVE", "ARCH", "BYPASS", "ESTS", "ESTATES", "CUTOFF",
        "TERRACE", "PLAZA", "LANE", "PARKWAY", "REACH", "THOROUGHFARE", "STREET");


    setupDictionary(ID_NUMBERED_ROAD_SFX,
        "AVENUE", "AV", "AVE",
        "STREET", "ST",
        "PLACE", "PL",
        "ROAD", "RD",
        "HWY",
        "MILE");

    setupDictionary(ID_ROAD_QUALIFIER, "OLD");

    setupDictionary(ID_ROUTE_PFX_PFX, "STATE", "ST", "SR", "SRT", "US", "FS", "INTERSTATE", "I", "IH", "STHWY", "STHY", "SH", "USHWY", "USHY", "CO", "CR", "CORD", "COUNTY", "CTY", "FM", "FR", "TWP", "PVT", "PRIVATE", "COUNTY");
    setupDictionary(ID_ROUTE_PFX_EXT, "RT", "RTE", "ROUTE", "HW", "HY", "HWY", "HIGHWAY", "ROAD", "RD");
    setupDictionary(ID_ROUTE_PFX, "STATE", "ST", "SR", "SRT", "US", "FS", "INTERSTATE", "I", "IH", "STHWY", "STHY", "SH", "USHWY", "USHY", "CO", "CR", "CORD", "COUNTY", "CTY", "FM", "FR", "RT", "RTE", "ROUTE", "HW", "HY", "HWY", "HIGHWAY", "TWP");
    setupDictionary(ID_ROUTE_PFX_PFX, defState);
    setupDictionary(ID_ROUTE_PFX, defState);
    if (defState.equals("MI")) {
      setupDictionary(ID_ROUTE_PFX_PFX, "M");
      setupDictionary(ID_ROUTE_PFX, "M");
    }
    setupDictionary(ID_PURE_DIRECTION, "N", "NE", "E", "SE", "S", "SW", "W", "NW");
    setupDictionary(ID_DIRECTION, "N", "NE", "E", "SE", "S", "SW", "W", "NW", "NB", "EB", "SB", "WB", "EXT", "EXTENSION",
                                  "NORTHBOUND", "EASTBOUND", "SOUTHBOUND", "WESTBOUND", "BUS");
    setupDictionary(ID_OPT_ROAD_PFX, "OLD", "NEW", "UPPER", "LOWER", "MC");
    setupDictionary(ID_CONNECTOR, "AND", "/", "&");
    setupDictionary(ID_AND_CONNECTOR, "AND");
    setupDictionary(ID_AT_MARKER, "@", "AT");
    setupDictionary(ID_OF, "OF");
    setupDictionary(ID_TO, "TO");
    setupDictionary(ID_DR, "DR");
    setupDictionary(ID_ST, "ST");
    setupDictionary(ID_MILE_MARKER, "MM", "MP");

    setupDictionary(ID_CROSS_STREET, "XS:", "X:", "C/S:", "C/S", "XSTR", "X");    //  Warning!  Must match CROSS_MARK_PTN
    setupDictionary(ID_NEAR, "BESIDE", "NEAR", "ACROSS");
    setupDictionary(ID_APT, "APARTMENT", "APT:", "APT", "#APT", "#", "SP", "RM", "SUITE", "SUIT", "STE", "SUITE:", "ROOM", "ROOM:", "LOT", "#LOT", "UNIT");
    setupDictionary(ID_APT_SOFT, "APT", "APTS", "SUITE", "ROOM", "LOT", "UNIT");
    setupDictionary(ID_FLOOR, "FLOOR", "FLR", "FL", "BLDG");
    setupDictionary(ID_STREET_NAME_PREFIX, "HIDDEN", "LAKE", "MT", "MOUNT", "SUNKEN");
    setupDictionary(ID_NOT_ADDRESS, "ENTRAPED", "ENTRAPPED", "SECTOR", "YOM", "YOF", "YOA", "YO", "Y/O", "-");
    setupDictionary(ID_YEAR_OLD_NOT_ADDRESS, "YOM", "YOF", "YOA", "YO", "Y/O");
    setupDictionary(ID_SINGLE_WORD_ROAD, "TURNPIKE");
    setupDictionary(ID_BLOCK, "BLK", "BLOCK");
    setupDictionary(ID_NUMBER_SUFFIX, "ND", "RD", "TH");
    setupDictionary(ID_NOT_STREET_NAME, "ON", "NO", "IN", "AT", "THE", "-");
    setupDictionary(ID_JUST, "JUST");

    // Set up special cross street names
    addCrossStreetNames(
        "ALLEY",
        "CITY LIMIT",
        "CITY LIMITS",
        "COUNTY LINE",
        "DEADEND",
        "DEAD END",
        "DEAD ENDS",
        "RAILROAD CROSSI",
        "RR",
        "RR TRACKS",
        "TRAILER PARK",
        "UNKNOWN",
        "UNNAMED STREET"
    );

    // Add any country specific words
    switch (getCountryCode()) {
    case UK:
      setupDictionary(ID_ROAD_SFX,
          "CLOSE",
          "GREEN",
          "CRESCENT");
      break;

    case NZ:
      setupDictionary(ID_ROAD_SFX,
          "CRES", "CRESCENT",
          "TCE", "TERRACE");
      break;

    default:
      break;
    }
  }

  protected void setupParseAddressFlags(int flags) {
    permParseFlags = flags;
  }

  /**
   * Allow otherwise prohibited characters in address
   * @param charList list of characters that will be allowed
   */
  protected void allowBadChars(String charList) {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    for (char chr : "()[],".toCharArray()) {
      if (charList.indexOf(chr) < 0) {
        sb.append('\\');
        sb.append(chr);
      }
    }
    sb.append(']');
    badCharPtn = Pattern.compile(sb.toString());
  }

  /**
   * Add full length names of directions to direction dictionary tables
   */
  protected void addExtendedDirections() {
    setupDictionary(ID_DIRECTION, "NORTH", "SOUTH", "EAST", "WEST");
  }

  /**
   * Add vocabulary to identify nautical locations
   */
  protected void addNauticalTerms() {
    setupDictionary(ID_ROAD_SFX | ID_AMBIG_ROAD_SFX,
        "POINT", "PT",
        "BAY",
        "MARINA",
        "STRAIGHT",
        "ISLAND", "ISLE");
  }

  /**
   * Add additional road suffix terms.  It is the callers responsibility
   * to ensure that all new suffix terms are properly handled when the
   * map address is generated
   * @param terms new road suffix terms
   */
  protected void addRoadSuffixTerms(String ... terms) {
    setupDictionary(ID_ROAD_SFX, terms);
  }

  /**
   * Add additional words that should not be allowed in address fields
   * @param words words to be added to list of invalid address words
   */
  protected void addInvalidWords(String ... words) {
    setupDictionary(ID_NOT_ADDRESS, words);
  }

  /**
   * Remove words from dictionary that are causing confusion
   * @param words words to be removed
   */
  protected void removeWords(String ... words) {
    for (String word : words) dictionary.remove(word);
  }

  protected static String[] getKeywords(Properties table) {
    String[] result = new String[table.size()];
    int ndx = 0;
    for (Enumeration<?> e = table.propertyNames(); e.hasMoreElements(); ) {
      result[ndx++] = (String)e.nextElement();
    }
    return result;
  }

  protected static String[] getValues(Properties table) {
    String[] result = new String[table.size()];
    int ndx = 0;
    for (Enumeration<?> e = table.propertyNames(); e.hasMoreElements(); ) {
      result[ndx++] = table.getProperty((String)e.nextElement());
    }
    return result;
  }

  /**
   * Set up predefined city code tables or lists
   */
  protected void setupCities(String ... cities) {
    setupCities(Arrays.asList(cities));
  }

  protected void setupCities(Properties cityCodes) {
    setupCities(getKeywords(cityCodes));
  }

  protected void setupCityValues(Properties cityCodes) {
    setupCities(getValues(cityCodes));
  }

  protected void setupCities(Collection<String> cities) {
    if (cities == null) return;
    if (mWordCities ==  null) {
      mWordCities = new MultiWordList(+1, ID_CITY, ID_MULTIWORD, ID_COMPLETE, cities);
    } else {
      mWordCities.addNames(+1, cities);
    }
  }

  private void setupDictionary(long bitMask, String ... args) {
    for (String arg : args) {
      long newMask = bitMask;
      Long oldMask = dictionary.get(arg);
      if (oldMask != null) newMask |= oldMask;
      dictionary.put(arg, newMask);
    }
  }

  /**
   * Look up a word on our internal dictionary
   * @param word word to be checked
   * @return true if word is found in dictionary and should
   * be expected in a street
   */
  public boolean isDictionaryWord(String word) {
    Long flags = dictionary.get(word);
    return (flags != null && (flags & (ID_NOT_ADDRESS|ID_NOT_STREET_NAME)) == 0);
  }

  /**
   * See if word is a legitimate street suffix (ie not a state)
   * @param word word to be checked
   * @return true if word is found in dictionary and should
   * be expected in a street
   */
  public boolean isStreetSuffix(String word) {
    if (word.equals("CT")) return false;
    Long flags = dictionary.get(word);
    return (flags != null && (flags & ID_ROAD_SFX) != 0);
  }

  /**
   * Setup predefined call list
   * @param callList list of predefined calls
   */
  protected void setupCallList(String ... callList) {
    setupCallList(new CodeSet(callList));
  }

  /**
   * Set up predefined call list
   * @param callDictionary CodeSet containing expected call values
   */
  protected void setupCallList(CodeSet callDictionary) {
    this.callDictionary = callDictionary;
  }

  @Override
  public CodeSet getCallList() {
    return callDictionary;
  }

  protected void setAllowDirectionHwyNames() {
    setAllowDirectionHwyNames(true);
  }

  /**
   * Setup up pattern to be used to detect place names in front of address
   * information
   * @param placeAddressPtn Pattern to be used to identify place names leading
   * the address.  Pattern may specify groups, in which case the first group
   * that returns a non-null result will become the place name.
   * @param placeAddressPtnExcl true if the pattern match should be the only way
   * to identify a place name.  False if regular logic will be used should the
   * pattern match fail.
   */
  protected void setupPlaceAddressPtn(Pattern placeAddressPtn, boolean placeAddressPtnExcl) {
    this.placeAddressPtn = placeAddressPtn;
    this.placeAddressPtnExcl = placeAddressPtnExcl;
  }

  protected void setupRejectAddressPattern(Pattern rejectAddressPtn) {
    this.rejectAddressPtn = rejectAddressPtn;
  }

  public void setAllowDirectionHwyNames(boolean allowDirectionHwyNames) {
    this.allowDirectionHwyNames = allowDirectionHwyNames;
  }

  /**
   * Set up preloaded multi-word street names
   * @param names list of multi-word street names.  with no street suffixes
   */
  protected void setupMultiWordStreets(String ... names) {
    mWordStreetsFwd = new MultiWordList(+1, 0, ID_MULTIWORD, 0, names);
    mWordStreetsRev = new MultiWordList(-1, 0, ID_MULTIWORD, 0, names);
  }

  /**
   * Set up preloaded special street names.  These are names of legitimate streets that lack a normal
   * street suffix
   * @param names special street names
   */
  protected void setupSpecialStreets(String ... names) {
    mWordSpecialFwd = new MultiWordList(+1, ID_SPECIAL_STREET_FWD, ID_MULTIWORD, ID_SPECIAL_STREET_FWD_END, names);
    mWordSpecialRev = new MultiWordList(-1, ID_SPECIAL_STREET_REV, ID_MULTIWORD, ID_SPECIAL_STREET_REV_END, names);
  }

  /**
   * Add additional names to the list of special cross street names
   * @param names cross street names to be added
   */
  protected void addCrossStreetNames(String ... names) {
    if (mWordCrossStreetsFwd == null) {
      mWordCrossStreetsRev = new MultiWordList(-1, ID_SPEC_CROSS_FWD, ID_MULTIWORD, ID_COMPLETE, names);
      mWordCrossStreetsFwd = new MultiWordList(+1, ID_SPEC_CROSS_REV, ID_MULTIWORD, ID_COMPLETE, names);
    } else {
      mWordCrossStreetsRev.addNames(-1, names);
      mWordCrossStreetsFwd.addNames(+1, names);
    }
  }

  protected void setupDoctorNames(String ... names) {
    for (String name : names) {
      setupDictionary(ID_DOCTOR, name, name+"S", name+"'S");
    }
  }

  protected void setupSaintNames(String ... names) {
    setupDictionary(ID_SAINT, names);
  }


  // Parser working variables
  private int flags;
  private String[] tokens;
  private long[] tokenType;
  private int[] tokenPos;
  private int startAddress = -1;
  private int lastCity = -1;
  private int startNdx = 0;
  private String saveAddress;

  // Have to save last result for backward compatibility with some calls
  private Result lastResult = null;

  /**
   * Determine if a string looks like a valid address
   * @param address Address string to be checked
   * @return zero if string is not recognized as valid address, otherwise a
   * numeric value in which higher values indicate better addresses
   */
  protected int checkAddress(String address) {
    return checkAddress(address, 0);
  }

  /**
   * Determine if a string looks like a valid address
   * @param address Address string to be checked
   * @param extra number of extra tokens (presumably city names) that can
   * be ignored at the end of the line
   * @return zero if string is not recognized as valid address, otherwise a
   * numeric value in which higher values indicate better addresses
   */
  protected int checkAddress(String address, int extra) {
    return parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_STRICT_SUFFIX | FLAG_NO_CITY, address).getStatus(extra);
  }

  /**
   * Determine if a string looks like a valid address
   * @param address Address string to be checked
   * @return true if valid address
   */
  protected boolean isValidAddress(String address) {
    return isValidAddress(address, 0);
  }

  /**
   * Determine if a string looks like a valid address
   * @param address Address string to be checked
   * @param extra number of extra tokens (presumably city names) that can
   * be ignored at the end of the line
   * @return true if valid address
   */
  protected boolean isValidAddress(String address, int extra) {
    return parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_STRICT_SUFFIX | FLAG_ALLOW_DUAL_DIRECTIONS | FLAG_NO_CITY, address).isValid(extra);
  }

  /**
   * Determine if a string looks like a cross street
   * @param address Address string to be checked
   * @return true if valid cross street
   */
  protected boolean isValidCrossStreet(String address) {
    return isValidCrossStreet(address, 0);
  }

  /**
   * Determine if a string looks like a valid address
   * @param address Address string to be checked
   * @param extra number of extra tokens (presumably city names) that can
   * be ignored at the end of the line
   * @return true if valid cross street
   */
  protected boolean isValidCrossStreet(String address, int extra) {
    int status = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ONLY_CROSS | FLAG_ALLOW_DUAL_DIRECTIONS | FLAG_NO_CITY, address).getStatus(extra);
    return status == STATUS_STREET_NAME || status == STATUS_INTERSECTION;
  }

  /**
   * Determine if string contains a city name or code.
   * @param address string to be checked for city name
   * @return true if recognized as city, false otherwise
   */
  protected boolean isCity(String address) {
    return parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY | FLAG_CHECK_STATUS | FLAG_ANCHOR_END, address).getStatus() > 0;
  }

  /**
   * Parse address line
   * @param sType indicates what we now about start of address field
   *         START_ADDR - field starts with the address
   *         START_CALL - field starts with call description followed by address
   *         START_CALL_PLACE - field starts will call description possibly followed by place name
   *         START_PLACE - field starts with a place name
   *         START_SKIP - field starts with something we aren't interested in,
   *                      followed by address
   * @param address address field to be parsed
   * @param data data object to be filled with information from parsed address field.
   */
  protected void parseAddress(StartType sType, String address, MsgInfo.Data data) {
    parseAddress(sType, 0, address, data);
  }

  /**
   * Parse address line
   * @param sType indicates what we now about start of address field
   *         START_ADDR - field starts with the address
   *         START_CALL - field starts with call description followed by address
   *         START_CALL_PLACE - field starts will call description possibly followed by place name
   *         START_PLACE - field starts with a place name
   *         START_SKIP - field starts with something we aren't interested in,
   *                      followed by address
   * @param flags - Special processing flags
   * @param address address field to be parsed
   * @param data data object to be filled with information from parsed address field.
   */
  protected void parseAddress(StartType sType, int flags, String address, MsgInfo.Data data) {
    lastResult = parseAddress(sType, flags, address);
    lastResult.getData(data);
  }

  /**
   * Determine if the string that we just parsed looks like an address
   * @return zero if string was not recognized as valid address, otherwise a
   * numeric value in which higher values indicate better addresses
   */
  protected int getStatus() {
    return lastResult.getStatus();
  }

  /**
   * Determine if the string that we just parsed looks like an address
   * @return zero if string was not recognized as valid address, otherwise a
   * numeric value in which higher values indicate better addresses
   */
  protected boolean isValidAddress() {
    return lastResult.isValid();
  }

  /**
   * parse address and return a result object that can be used to build
   * the parsed data fields at a later time
   * @param sType indicates what we now about start of address field
   *         START_ADDR - field starts with the address
   *         START_CALL - field starts with call description followed by address
   *         START_CALL_PLACE - field starts will call description possibly followed by place name
   *         START_PLACE - field starts with a place name
   *         START_SKIP - field starts with something we aren't interested in,
   *                      followed by address
   * @param address address field to be parsed
   * @return integer indicating how good this address is, higher number mean better fit
   */
  protected Result parseAddress(StartType sType, String address) {
    return parseAddress(sType, 0, address);
  }

  /**
   * parse address and return a result object that can be used to build
   * the parsed data fields at a later time
   * @param sType indicates what we now about start of address field
   *         START_ADDR - field starts with the address
   *         START_CALL - field starts with call description followed by address
   *         START_CALL_PLACE - field starts will call description possibly followed by place name
   *         START_PLACE - field starts with a place name
   *         START_SKIP - field starts with something we aren't interested in,
   *                      followed by address
   * @param flags - Special processing flags
   * @param address address field to be parsed
   * @return integer indicating how good this address is, higher number mean better fit
   */
  protected Result parseAddress(StartType sType, int flags, String address) {
    address = address.trim();
    flags |= permParseFlags;
    this.flags = flags;

    // Pad fields and Apt recheck logic do not work well together
    if (isFlagSet(FLAG_RECHECK_APT) && isFlagSet(FLAG_PAD_FIELD | FLAG_PAD_FIELD_EXCL_CITY)) {
      throw new RuntimeException("Unsupported flag combination");
    }

    // If we have a call dictionary, and address starts with a call, search
    // the dictionary to see if address line starts with matching call.  This logic
    // should be suppressed if we are starting with a call description and the address
    // contains an @
    String callPrefix = null;
    if ((sType == StartType.START_CALL && (isFlagSet(FLAG_AT_PLACE|FLAG_AT_BOTH|FLAG_IGNORE_AT) || !address.contains("@")) ||
         sType == StartType.START_CALL_PLACE)
         && callDictionary != null) {
      String call = callDictionary.getCode(address.toUpperCase(), !isFlagSet(FLAG_START_FLD_NO_DELIM));
      if (call != null) {

        // We have a match.  Store the call (without the trailing space)
        // in the result call prefix.  Remove the call prefix from the address
        // line, and set the start type to start with the address
        callPrefix = address.substring(0,call.length());
        address = address.substring(call.length()).trim();
        int pt = call.length();
        while (pt < address.length() && CodeSet.DELIMS.indexOf(address.charAt(pt)) >= 0) pt++;
        Matcher match = ADDR_START_PTN.matcher(address);
        if (match.find()) address = address.substring(match.end()).trim();
        sType = (sType == StartType.START_CALL_PLACE ? StartType.START_PLACE : StartType.START_ADDR);
        flags &= ~FLAG_START_FLD_REQ;
        this.flags = flags;
      }
    }

    // Convert any protected names in address line
    address = protectNames(address);

    // A start type of START_ADDR is incompatible with the FLAG_AT_BOTH flag.  If both are
    // set, switch flag to FLAG_AT_PLACE
//    if (sType == StartType.START_ADDR && isFlagSet(FLAG_AT_BOTH)) {
//      flags &= ~FLAG_AT_BOTH;
//      flags |= FLAG_AT_PLACE;
//      this.flags = flags;
//    }

    // If we are not ignoring at signs, see if we have one
    String prefix = null;
    String specPlace = null;
    Result result = null;
    if (!isFlagSet(FLAG_IGNORE_AT | FLAG_AT_MEANS_CROSS | FLAG_ONLY_CITY)  &&
        (sType != StartType.START_ADDR || isFlagSet(FLAG_AT_PLACE | FLAG_AT_BOTH))) {
      Pattern ptn = (isFlagSet(FLAG_AT_SIGN_ONLY) ? AT2_PTN : AT1_PTN);
      Matcher match = ptn.matcher(address);
      if (match.find()) {

        // Find the first cross street marker
        int crossPos = Integer.MAX_VALUE;
        Matcher mat2 = CROSS_MARK_PTN.matcher(address);
        if (mat2.find()) crossPos = mat2.start();

        boolean atPlace = isFlagSet(FLAG_AT_PLACE);
        boolean atBoth = isFlagSet(FLAG_AT_BOTH);
        boolean atIncCity = isFlagSet(FLAG_AT_INCL_CITY);

        // It makes a difference if there is a second AT mark in the field so lets
        // look ahead and see if we have one
        int atStart = match.start();
        int atEnd = match.end();
        int nextStart = -1;
        int nextEnd = -1;
        boolean atMult = match.find();
        if (atMult) {
          nextStart = match.start();
          nextEnd = match.end();
        }

        // Go into loop checking results at different break points.
        do {

          // @ markers beyond the first cross street marker do not count
          if (atStart >= crossPos) break;

          // We got one.  Use it to split address into two pieces
          String part1 = address.substring(0,atStart).trim();
          String part2 = address.substring(atEnd).trim();

          // Try one or both pieces as an address
          Result res1 = null, res2 = null;
          if (atPlace || atBoth) {
            StartType st = sType == StartType.START_PLACE ? StartType.START_ADDR : sType;
            int tFlags = flags | FLAG_ANCHOR_END;
            if (!atIncCity) tFlags |= FLAG_NO_CITY;
            if (atBoth || atMult) tFlags |= FLAG_CHECK_STATUS;
            res1 = parseAddressInternal1(st, tFlags, part1);
          }
          if (!atPlace || atBoth) {
            int tFlags = flags;
            if (atBoth || atMult) tFlags |= FLAG_CHECK_STATUS;
            res2 = parseAddressInternal1(StartType.START_ADDR, tFlags, part2);
          }

          // If FLAG_AT_BOTH was set, we tried both pieces and need to determine which one is better
          if (res1 != null && res2 != null) {
            if (res1.getStatus() >  res2.getStatus()) {
              res2 = null;
            } else {
              res1  = null;
            }
          }

          // AT this point, one and only one of res1 and res2 will be non-null.  Use it to
          // determine the final result
          String tmpPrefix, tmpSpecPlace;
          Result tmpResult;
          if (res1 != null) {
            tmpPrefix = null;
            tmpResult = res1;
            tmpSpecPlace = part2;
          } else {
            if (sType == StartType.START_ADDR) sType = StartType.START_PLACE;
            tmpPrefix = part1;
            tmpResult = res2;
            tmpSpecPlace = null;
          }

          // OK, not the final result, now we compare the previous loop results to determine
          // the real final result
          if (result == null || tmpResult.getStatus() > result.getStatus()) {
            prefix = tmpPrefix;
            result = tmpResult;
            specPlace = tmpSpecPlace;
          }

          // Continue checking further @ breaks until we get a valid address or
          // run out of breaks
          atStart = nextStart;
          atEnd = nextEnd;
          if (atStart < 0 || result.isValid()) break;
          if (match.find()) {
            nextStart = match.start();
            nextEnd = match.end();
          } else {
            nextStart = nextEnd = -1;
          }
        } while (true);
      }
    }


    if (result == null) result = parseAddressInternal1(sType, flags, address);

    result.startType = sType;
    result.flags = flags;
    result.callPrefix = callPrefix;

    // Some consistency checks
    if (prefix != null) {
      if (result.startField != null) throw new RuntimeException("Inconsistent prefix parse result");
      result.stdPrefix = prefix;
    }
    if (specPlace != null) {
      if (result.placePrefix != null || result.placeField != null) throw new RuntimeException("Inconsistent place parse result");
      result.trailPlace = specPlace;
    }

    // If we were really parsing cross streets, switch address index to cross street index
    if (isFlagSet(FLAG_ONLY_CROSS)) {
      result.crossField = result.addressField;
      result.addressField = null;
    }

    // Clean up any protected names in any fields
    if (result.callPrefix != null) result.callPrefix = unprotectNames(result.callPrefix);
    if (result.placePrefix != null) result.placePrefix = unprotectNames(result.placePrefix);
    if (result.stdPrefix != null) result.stdPrefix = unprotectNames(result.stdPrefix);
    if (result.trailPlace != null) result.trailPlace = unprotectNames(result.trailPlace);
    if (result.startFld != null) result.startFld = unprotectNames(result.startFld);
    if (result.left != null) result.left = unprotectNames(result.left);
    if (result.tokens != null) {
      for (int ii = 0; ii<result.tokens.length; ii++) {
        result.tokens[ii] = unprotectNames(result.tokens[ii]);
      }
    }
    return result;
  }
  private static Pattern CROSS_MARK_PTN = Pattern.compile("\\b(?:XS?:|C/S:|C/S\\b|XSTR\\b|X\\b)", Pattern.CASE_INSENSITIVE);
  private static Pattern AT1_PTN = Pattern.compile("@|(?: REPORTED *)?\\bAT\\b(?!&T)", Pattern.CASE_INSENSITIVE);
  private static Pattern AT2_PTN = Pattern.compile("@");


  protected Result parseAddressInternal1(StartType sType, int flags, String address) {

    Result result = parseAddressInternal2(sType, flags, address);

    // Now is where we use the result to compute the true prefix and leftover segments
    result.left = "";
    result.mBlankLeft = false;
    result.commaLeft = false;

    if (result.startField != null) {
      int tmp = result.startField.fldEnd;
      if (tmp > 0) {
        if (tmp >= tokenPos.length) {
          result.stdPrefix = saveAddress;
        } else {
          result.stdPrefix = stripFieldEnd(saveAddress.substring(0,tokenPos[tmp]).trim(), ",");
        }
      }
    }

    if (result.endAll >= 0 && result.endAll < tokenPos.length) {
      int tmp = result.endAll;
      while (tmp < tokens.length && tokens[tmp].equals(",")) {
        tmp++;
        result.commaLeft = true;
      }
      if (tmp < tokenPos.length){
        int pt = tokenPos[tmp];
        result.mBlankLeft = (pt >= 2 && saveAddress.substring(pt-2, pt).equals("  "));
        result.left = saveAddress.substring(pt);
      }
    }

    return result;
  }

  private Result parseAddressInternal2(StartType sType, int flags, String address) {
    lastCity = -1;
    this.flags = flags;
    Result result = new Result(this, flags);

    // If address fits the reject pattern return a reject status
    if (rejectAddressPtn != null && rejectAddressPtn.matcher(address).matches()) {
      result.status = STATUS_REJECT;
      result.left = address;
      return result;
    }

    // If a place pattern has been specified, we use it instead of the
    // standard start address logic to identify the place prefix
    if (sType == StartType.START_PLACE && placeAddressPtn != null) {
      Matcher match = placeAddressPtn.matcher(address);
      if (match.lookingAt()) {
        sType = StartType.START_ADDR;
        result.placePrefix = null;
        for (int ii = 1; ii<= match.groupCount(); ii++) {
          result.placePrefix = match.group(ii);
          if (result.placePrefix != null) break;
        }
        if (result.placePrefix == null) result.placePrefix = match.group();
        result.placePrefix = result.placePrefix.trim();
        address = address.substring(match.end()).trim();
      } else {
        if (placeAddressPtnExcl) sType = StartType.START_ADDR;
      }
    }

    // Save the result address type
    result.startType = sType;

    // Check for null string
    result.status = STATUS_EMPTY;
    address = address.trim();
    if (address.length() == 0) return result;

    // Check some stuff that will not apply to city only parse requests
    String gpsCoords = null;
    boolean onlyCity = isFlagSet(FLAG_ONLY_CITY);
    if (!onlyCity) {

      // Look for and compress any US symbols
      address = US_PTN.matcher(address).replaceAll("$1$2");

      // Strip leading zeros from starting numeric tokens
      address = stripLeadingZero(address);

      // Identify and protect any GPS coordinates
      Matcher match = MsgParser.GPS_PATTERN.matcher(address);
      if (match.find()) gpsCoords = match.group().trim();
    }

    // Set up token list and types
    setTokenTypes(sType, address, gpsCoords, result);

    // Null token list should return total failure status
    if (tokens.length == 0) {
      result.status = 0;
      return result;
    }

    // If we are looking for a city and nothing else, parseToCity can find it
    // If the city has to start and end the field, check that that start index is zero
    boolean noAddress = isFlagSet(FLAG_NO_ADDRESS);
    if (noAddress || onlyCity && ! isFlagSet(FLAG_ONLY_CROSS)) {
      int req = 0;
      if (isFlagSet(FLAG_START_FLD_REQ) && sType != StartType.START_ADDR) req++;
      if (parseStartToCity(req, noAddress, result)) {
        if (result.cityField == null || sType != StartType.START_ADDR || result.cityField.fldStart == 0) {
          result.status = STATUS_TRIVIAL;
          return result;
        }
        result.cityField = null;
      }

      // Otherwise assign entire field to start field if there is one
      if (result.startField != null) {
        result.startField.end(tokens.length);
      } else {
        result.endAll = 0;
      }
      result.status = STATUS_NOTHING;
      return result;
    }

    // Now comes the hard part.

    // We have a number of basic patters that we will recognize
    // Try each one until we find one that works
    result.status = STATUS_TRIVIAL;
    if (parseTrivialAddress(result, false, false)) return result;
    if (!isFlagSet(FLAG_ONLY_CROSS)) {
      if (parseGPSCoords(result, gpsCoords)) return result;
      result.status = STATUS_FULL_ADDRESS;
      if (parseSimpleAddress(result)) return result;
    }
    result.status = STATUS_INTERSECTION;
    if (parseIntersection(result)) return result;
    result.status = STATUS_STREET_NAME;
    if (parseNakedRoad(result)) {
      // If the naked road pattern detected an implied intersection symbol
      // promote the result status to intersection
      if (result.insertAmp >= 0) result.status++;
      return result;
    }

    result.status = STATUS_TRIVIAL;
    if (parseTrivialAddress(result, true, false)) return result;

    // If all else fails, try the trivial parser in override status mode
    // if that fails, use the fallback parser
    if (!parseTrivialAddress(result, true, true)) {
      parseFallback(sType, result);
    }
    result.status = STATUS_NOTHING;
    if (result.addressField != null && isHouseNumber(result.addressField.fldStart) ||
        MARGINAL_INTERSECT_PTN.matcher(address).matches()) result.status = STATUS_MARGINAL;
    return result;
  }
  private static final Pattern US_PTN = Pattern.compile("\\b(U) (S)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_START_PTN = Pattern.compile("^(?:@|AT |REPORTED AT )", Pattern.CASE_INSENSITIVE);
  private static final Pattern MARGINAL_INTERSECT_PTN = Pattern.compile(".*&.*|.*/(?!B\\b).*");

  /**
   * Called after address has been parsed
   * @return the part of the line in front of the address
   */
  public String getStart() {
    return lastResult.getStart();
  }

  /**
   * Called after address has been parsed with FLAG_PAD_FIELD or
   * FLAG_PAD_FIELD_EXCL_CITY flag
   * @return pad field between address and city
   */
  public String getPadField() {
    return lastResult.getPadField();
  }

  /**
   * Called after address has been parsed
   * @return the part of the line after the address
   */
  public String getLeft() {
    return lastResult.getLeft();
  }

  /**
   * Called after address has been parsed
   * @return true if multple blanks occurred before the getLeft() result
   */
  public boolean isMBlankLeft() {
    return lastResult.isMBlankLeft();
  }

  /**
   * Called after address has been parsed
   * @return true if there was a comma before the getLeft() result
   */
  public boolean isCommaLeft() {
    return lastResult.isCommaLeft();
  }

  /**
   * We handle the dead simple case where the address starts at the
   * beginning of the text and we have found a city to mark the end
   * of the address (would that life were always this simple
   * @param result returns the parsed results if successful
   * @param skipPad true if processing should continue when pad field or implied intersection has been requested
   * @param overrideCheckStatus true if processing should continue when check status has been requested
   */
  private boolean parseTrivialAddress(Result result, boolean skipPad, boolean overrideCheckStatus) {

    // If start address is not fixed somewhere, this cannot possibly work
    if (startAddress < 0) return false;

    // If we were called to specifically check address status, we always fail
    // because this short circuits the entire address validation logic
    if (isFlagSet(FLAG_CHECK_STATUS)) return false;

    // We end up getting called twice. The first time skipPad is false, and we are
    // still hoping to find an implied intersection or pad field, which won't happen if we
    // return a trivial result here

    // We are called a second time if nothing else has worked, in which case we
    // will ahead and process this if we skipped processing the first time around
    if (isFlagSet(FLAG_IMPLIED_INTERSECT|FLAG_ANY_PAD_FIELD) != skipPad) return false;

    // OK, we have to have at least 1 items before the city
    // Unless we are parsing a cross street instead of a real address, in which
    // case we allow it to be empty
    int reserve = (isFlagSet(FLAG_ONLY_CROSS | FLAG_EMPTY_ADDR_OK) ? 0 : isHouseNumber(startAddress) ? 2 : 1);
    return parseAddressToCity(startAddress, startAddress+reserve, result);
  }

  /**
   * Handle special case where the prospective address contains a token
   * that contains valid GPS coordinates
   */
  private boolean parseGPSCoords(Result result, String gpsCoords) {

    // If no GPS coordinates were found, return false
    if (gpsCoords == null) return false;

    // We have already packed the GPS coordinates into a single token
    // now find that token
    int gpsNdx = 0;
    for (; gpsNdx < tokens.length; gpsNdx++) {
      if (tokens[gpsNdx].contains(gpsCoords)) break;
    }
    if (gpsNdx >= tokens.length) {
      throw new RuntimeException("GPS coordinates not found");
    }

    // Expand address to beginning or end of token string if required by
    // constraints
    int start = (result.startType == StartType.START_ADDR ? 0 : gpsNdx);
    int end  = (isFlagSet(FLAG_ANCHOR_END) ? tokens.length : gpsNdx+1);
    if (result.startField != null) result.startField.end(start);
    result.addressField = new FieldSpec(start, end);
    result.endAll = end;
    return true;
  }

  /**
   * Look for the basic address looking like
   *     <number> <street name> <street suffix>
   */
  private boolean parseSimpleAddress(Result result) {

    result.reset(startAddress);
    boolean padField = isFlagSet(FLAG_ANY_PAD_FIELD);

    // Start looking through the address for a starting house number
    int start = startAddress >= 0 ? startAddress : startNdx;
    int sAddr = start;
    int sEnd = -1;
    boolean locked = false;
    while (true) {
      while (true) {
        if (sAddr >= tokens.length) return false;
        if (isType(sAddr, ID_CROSS_STREET)) return false;

        // See if this token looks like a house number, and is not followed by an invalid address token or a connector
        if (isHouseNumber(sAddr) && !isType(sAddr+1, ID_NOT_ADDRESS | ID_NUMBER_SUFFIX | ID_CROSS_STREET | ID_APT) && findConnector(sAddr+1)<0) {

          // And is not followed by another number, unless that number is followed by a numbered road suffix
          if (!isHouseNumber(sAddr+1) || isType(sAddr+2, ID_NUMBERED_ROAD_SFX)) {

            // And is not preceded by a relational operator
            // or preceded or followed by a mile marker symbol
            if (!isType(sAddr+1, ID_MILE_MARKER) && (sAddr == 0 || ! isType(sAddr-1, ID_RELATION | ID_MILE_MARKER))) {

              // Getting closer...
              // if the house number appears to be part of a numbered highway, bail out
              // Unless the previous word is "CO" which might be abbreviation for "COMPANY in the prefix data
              if (findRevNumberedHwy(start, sAddr) < 0 || tokens[sAddr-1].equalsIgnoreCase("CO")) {

                // If the house numbered is followed by a numbered road suffix
                // this starts looking chancy.  But more analysis is needed.
                if (isType(sAddr+1, ID_NUMBERED_ROAD_SFX)) {

                  // If the numbered road suffix is actually the start of a numbered highway name, we are good to go
                  sEnd = findNumberedHwy(sAddr+1);
                  if (sEnd >= 0) {
                    sEnd++;
                    sEnd = bumpTrailingDir(sAddr+1, sEnd);
                    break;
                  }

                  // If the numbered road suffix is ST, see if it is actually the start
                  // of a street name named after saint.  if it is, we are good to go
                  if (isType(sAddr+1, ID_ST)) {
                    if (!isType(sAddr+2, ID_DIRECTION)) {
                      sEnd = findRoadEnd(sAddr+1, 1);
                      if (sEnd > 0) {

                        // Unless the implied intesection flag is set, in which case
                        // this only works if the street name is a defined saint name
                        if (!isFlagSet(FLAG_IMPLIED_INTERSECT) ||
                            isType(sAddr+2, ID_SAINT)) break;
                      }
                    }
                  }

                  // Last chance, see if this is an identified special street name
                  sEnd = findSpecialRoadEnd(sAddr+1);
                  if (sEnd > 0) break;

                  // Otherwise, this has been confirmed as a legitimate numbered road suffix following
                  // the house number which is a show stopper
                  return false;
                }
                break;
              }
            }
          }
        }

        // Not a house  number.  If the start address field is locked, bail out
        if (startAddress >= 0 || locked) return false;

        // Otherwise move on to the next token
        // If we encounter a flexible @ sign, lock in the start of the address field
        sAddr++;
      }

      // OK, we found a house number!!
      // sAddr points to the house number
      // sEnd points to end of street name if identified, or -1 if not.

      // If we found a city beyond this start point, just use that as the terminator
      int sTmp = sEnd;
      if (sTmp < 0) {
        sTmp = sAddr+2;
        if (isType(sTmp-1, ID_DIRECTION)) sTmp++;
      }
      if (! padField && parseAddressToCity(sAddr, sTmp, result)) {

        // if FLAG_ANCHOR_END is set, an apt number in front of the trailing city name
        // will be mistakenly interpreted as a regular address.  So we need to check to
        // make sure that is not accepted
        if (sEnd < 0 && result.cityField == null &&
            findEndCity(sAddr+1) == tokens.length) return false;
        if (locked && result.startField != null) result.startField.fldEnd--;
        return true;
      }

      // Otherwise, see if we can find a road starting from the next token
      // In general, we would search for a non-strict road end here.  But doing so may
      // preempt better matches from some of the other parsers.  So will make a strict
      // check here, and catch non-strict road termination results in the fallback parser
      if (sEnd < 0) sEnd = findRoadEnd(sAddr+1, 2, true);
      if (sEnd > 0) {

        // We found one.  But before accepting it lets check for one
        // particular special case.  If the start address position is not locked
        // and the road suffix that ended this address also happens to be a
        // numbered route prefix, the see if we can get a better address
        // match treating treating that as a number route prefix
        if (startAddress < 0 && isType(sEnd-1, ID_ROUTE_PFX)) {
          int tmp = sEnd-2;
          while (tmp > 0 && isType(tmp, ID_DIRECTION)) tmp--;
          if (tmp > sAddr) {
            int saveStartAddress =  startAddress;
            startAddress = tmp;
            if (parseSimpleAddress(result)) return true;
            startAddress = saveStartAddress;
          }
        }

        // Not problem, this will be the end of the address
        break;
      }

      // This isn't what we are looking for
      // Increment the search index and look for something else
      sAddr++;
    }

    // We have found what we need to have found and we are going
    // to be successful
    result.addressField = new FieldSpec(sAddr, sEnd);
    if (result.startField != null) result.startField.optionalEnd(locked ? sAddr-1 : sAddr);
    result.endAll = sEnd;

    // Special case - simple address can have intersection marker followed by
    // cross stream info.  But it has to be a real 1 token marker.  We do
    // not accept "N OF" type connectors here
    int tmp = findConnector(sEnd);
    if (tmp == sEnd+1) {
      findCrossStreetEnd(tmp, false, result);
    }

    return parseTrailingData(result);
  }

  /**
   * Look for intersection adddress with the basic form of
   *    <roadname> <road sfx> <connector> <roadname> <road sfx>
   * @return true if successful
   */
  private boolean parseIntersection(Result result) {

    result.reset(startAddress);
    boolean padField = isFlagSet(FLAG_ANY_PAD_FIELD);

    // First lets figure out where the address starts
    int sAddr = -1;
    int ndx = -1;

    // At least one of the two street names must be pretty solid
    // we can allow some slop for the other one.
    boolean good = true;

    // If address has a known start point
    if (startAddress >= 0) {

      // See if we can identify a street name starting here
      int tmp = findRoadEnd(startAddress, 2, true, true);
      if (tmp >= 0) {

        // If this is followed by a connector, we are good to go.
        tmp = findConnector(tmp);
        if (tmp >= 0) {
          sAddr = startAddress;
          ndx = tmp;
        }

        // Legal street name not followed by a connector.  Normally we would drop
        // down and see if we can find a connector followed by a good street name.
        // But if there might be a cross street following this address, that might
        // end up merging a naked street name with the first cross street, which we
        // don't wan to do.
        else if (isFlagSet(FLAG_CROSS_FOLLOWS)) return false;
      }
    }

    // We either do not have a known starting place, or could not identify
    // a street name at the start of the field.  Next step is to start
    // scanning forward looking for a connector
    if (sAddr < 0) {
      int start = startAddress >= 0 ? startAddress : startNdx;
      ndx = start;
      while (true) {
        ndx++;

        // If we reach the end of the string, all is lost
        if (ndx >= tokens.length) return false;

        // If we started from a solid address start point, stop searching before things
        // get too ridiculous
        if (startAddress >= 0 && ndx-startAddress > 5) return false;

        // Likewise if we encounter a cross street marker
        if (isType(ndx, ID_CROSS_STREET)) return false;

        // If field starts with an address, any invalid token is a reject
        if (startAddress >= 0 && isType(ndx, ID_NOT_ADDRESS)) return false;

        // Now look for a connector, but it can not be the first element
        if (ndx-start >= 1) {
          int tmp = findConnector(ndx);
          if (tmp >= 0) {

            // Found one
            sAddr = ndx-1;
            ndx = tmp;

            // Next identify a street in front of the connector
            // If start of address has been locked, that takes care of that
            // But, we do not have a confirmed good street name and will
            // require higher standards for the following street name
            if (startAddress >= 0) {
              sAddr = startAddress;
              good = false;
              break;
            }

            // If cross street search, check for special cross street name
            if (isFlagSet(FLAG_ONLY_CROSS)) {
              tmp = mWordCrossStreetsRev.findEndSequence(sAddr);
              if (tmp >= 0) {
                sAddr = tmp;
                break;
              }
            }

            // skip over direction and bypass
            if (sAddr > start && isType(sAddr, ID_DIRECTION)) sAddr--;

            // Check for special street names
            if (mWordSpecialRev != null) {
              tmp = mWordSpecialRev.findEndSequence(sAddr);
              if (tmp >= 0) {
                sAddr = tmp;
                break;
              }
            }

            // See if this is a multi word street name without a suffix
            if (mWordStreetsRev != null && isFlagSet(FLAG_OPT_STREET_SFX)) {
              int save = sAddr;
              sAddr = mWordStreetsRev.findEndSequence(sAddr);
              if (sAddr != save) break;
            }

            // Skip over bypass
            if (sAddr > start && isType(sAddr, ID_BYPASS)) sAddr--;

            if (sAddr >= start && isRoadToken(sAddr)) break;

            if (sAddr > start && isRoadSuffix(sAddr)) {
              sAddr--;
              if (isType(sAddr,ID_NOT_ADDRESS|ID_NOT_STREET_NAME)) continue;
              sAddr = findRoadStart(start, sAddr);
              break;
            }

            // Check for a route prefix followed by a numeric or alpha route number
            // We check two positions for this sequence to catch the odd case where an alpha route number
            // has previously been mistaken for a direction
            // Good old St Louis County, MO. Thank your for creating a HWY W :(
            tmp = findRevNumberedHwy(start, sAddr);
            if (tmp < 0) tmp = findRevNumberedHwy(start, sAddr+1);
            if (tmp >= 0) {
              sAddr = tmp;
              break;
            }

            if (isFlagSet(FLAG_OPT_STREET_SFX) && !isType(sAddr,ID_NOT_ADDRESS)) break;

            // One last chance.  If we skipped over a "BYPASS" token to get here
            // see if we can treat it as a suffix as well
            if (sAddr > start && isType(sAddr+1, ID_BYPASS)) {
              if (isType(sAddr,ID_NOT_ADDRESS)) continue;
              sAddr = findRoadStart(start, sAddr);
              break;
            }
          }
        }
      }

      // If road is preceded by a direction, include that
      sAddr = stretchRoadPrefix(start, sAddr);
    }

    // When we get here,
    // good true if we have a good solid first street name
    // saddr points to beginning of address
    // ndx points to the token following the connector

    // If there is a city terminating the address, just parse up to it
    if (good && !padField && parseAddressToCity(sAddr, ndx+1, result)) return true;

    // Otherwise find end of second road
    if (good) {
      ndx = findRoadEnd(ndx, 2);
    } else {
      ndx = findRoadEnd(ndx, 0, true);
    }
    if (ndx < 0) return false;

    // If we did not try to parse to a trailing city previously because
    // we did not have a solid first street name, we can try again now
    // that we have a solid second street name
    if (!good && parseAddressToCity(sAddr, ndx, result)) return true;

    // If we found that, we have a successful intersection parse
    result.addressField = new FieldSpec(sAddr, ndx);
    if (startAddress < 0) result.startField.end(sAddr);
    result.endAll = ndx;

    // But there might be some additional cross street info we can parse
    return parseTrailingData(result);
  }

  /**
   * Find beginning of street name once we have identified a legitimate street suffix
   * @param start hard beginning of possible street name
   * @param sAddr tentative beginning of possible street name
   * @return
   */
  private int findRoadStart(int start, int sAddr) {

    // Two things to check.  First is to see if we have a recognized multi word street name
    int save = sAddr;
    if (mWordStreetsRev != null) sAddr = mWordStreetsRev.findEndSequence(sAddr);

    // If not, see if the first word is an ambiguous street suffix, and if it is, include the
    // previous word,
    if (sAddr == save && isAmbigRoadSuffix(sAddr)) sAddr--;

    // Not allowed to precede hard start
    if (sAddr < start) sAddr = start;
    return sAddr;
  }

  /**
   * Look for simple road without a house number or intersection
   *    <roadname> <roadsfx>
   * @return true if found
   */
  private boolean parseNakedRoad(Result result) {

    result.reset(startAddress);
    boolean atStart = false;
    boolean padField = isFlagSet(FLAG_ANY_PAD_FIELD);

    // Normally we will consider the possiblilty that ST might be the
    // beginning of a street name starting with the abbrevation for Saint.
    // But don't try this if  there is a reason to expect a different
    // street following this one.
    boolean followingRoad = isFlagSet(FLAG_IMPLIED_INTERSECT|FLAG_CROSS_FOLLOWS);

    // If address starts at beginning of field, find end of address and
    // Don't have to look for city because we wouldn't be here if both startAddr
    // and city was found

    int sAddr, ndx;
    if (startAddress >= 0) {
      sAddr = ndx = startAddress;
      ndx = findRoadEnd(ndx, 0, true, followingRoad);
      if (ndx < 0) return false;

      // A number followed by a connector is considered to be a street
      // name in most contexts.  But not here
      if (ndx - sAddr == 1 && isType(sAddr, ID_NUMBER)) return false;
    }

    // Otherwise, scan forward looking for a <road-sfx>
    //            that isn't the start of a <route-pfx> <number> combination
    // or number preceded by a <route-pfx>
    // or an ambiguous <road-sfx> followed by a second <road-sfx>
    else {
      int start = startNdx;
      ndx = start;
      boolean found = false;
      int failStart = -1;
      int failEnd = -1;
      while (true) {

        // See if there is a naked cross street name here.  Finding one doesn't
        // immediately return that result, but it defines the fallback result
        // we will use if something better doesn't come along.
        if (failStart < 0 && isFlagSet(FLAG_ONLY_CROSS)) {
          failEnd = mWordCrossStreetsFwd.findEndSequence(ndx);
          if (failEnd >= 0) failStart = ndx;
        }

        // See if we have a special street name
        if (mWordSpecialFwd != null) {
          int tmp = mWordSpecialFwd.findEndSequence(ndx);
          if (tmp >= 0) {
            found = true;
            sAddr = ndx;
            ndx = tmp-1;
            break;
          }
        }

        // See if we have a multi-word street match
        if (mWordStreetsFwd != null) {
          int tmp = mWordStreetsFwd.findEndSequence(ndx);
          if (tmp > ndx+1) {

            // If followed by a road suffix, this is what we are looking for
            if (isRoadSuffix(tmp)) {
              found = true;
              sAddr = ndx;
              ndx = tmp;
              break;
            }

            // Otherwise, use it as the failure fallback
            else if (failStart < 0 && isFlagSet(FLAG_OPT_STREET_SFX|FLAG_NO_STREET_SFX)) {
              failStart = ndx;
              failEnd = tmp;
            }
          }
        }

        // The rest of the checks can only happen after we have passed at least
        // one token
        if (ndx > start) {

          sAddr = ndx - 1;
          if (ndx >= tokens.length) break;
          if (isType(ndx, ID_CROSS_STREET)) break;
          if (atStart) {
            start = sAddr = ndx;
            ndx = findRoadEnd(sAddr, 0, true);
            ndx--;
            if (ndx >= 0) found = true;
            break;
          }

          if (isRoadSuffix(ndx) && !isType(sAddr, ID_NOT_ADDRESS|ID_NOT_STREET_NAME) &&
              findConnector(sAddr)<0 && !isType(sAddr, ID_MILE_MARKER)) {
            boolean startHwy = checkNumberedHwy(ndx) ||
                               ndx > start && findNumberedHwy(ndx-1) >= 0 ||
                               (isType(ndx, ID_AMBIG_ROAD_SFX) && (isRoadSuffix(ndx+1)));

            // If street suffix was ST, see if it might be the beginning of a street name
            // Don't do this if we are looking for an implied intersection
            if (!startHwy && !followingRoad && isType(ndx, ID_ST)) {
              if (!isType(ndx+1, ID_DIRECTION)) {
                int sEnd = findRoadEnd(ndx, 0, true);
                if (sEnd > 0) {
                  found = true;
                  sAddr = ndx;
                  ndx = sEnd-1;
                  break;
                }
              }
            }

            // OK, this is a legitimate street suffix
            // Check the multiple word street list to find the start of the
            // street name.  If not found, assume a one word street name.
            if (!startHwy) {
              found = true;

              // Assume one word street name, unless first word is an ambiguous suffix
              // in which case we keep expanding the street name
              while (sAddr > start && findConnector(sAddr-1)<0 && isAmbigRoadSuffix(sAddr)) {
                sAddr--;
              }
              break;
            }
          }

          int tmp = findRevNumberedHwy(start, ndx);
          if (tmp >= 0) {
            sAddr = tmp;
            found = true;
            break;
          }
        }

        if (isRoadToken(ndx)) {
          sAddr = ndx;
          found = true;
          break;
        }
        ndx++;
      }

      // When we break out of this loop, see if we found a real street name
      // If we didn't, see if there is a fallback street name we can return
      // otherwise fail
      if (!found) {
        if (failStart < 0) return false;
        sAddr = failStart;
        ndx = failEnd;
      }

      else {

        // If the previous token is a direction, back up one more to include that.
        sAddr = stretchRoadPrefix(start, sAddr);

        // increment end pointer past the road terminator
        // and possibly over a trailing direction
        ndx++;
        ndx = bumpTrailingDir(sAddr, ndx, followingRoad);
      }
      result.startField.end(atStart ? sAddr-1 : sAddr);
    }

    // When we get here,
    // sAddr points to beginning of address
    // ndx points past the end of the road

    // We have a naked road parse
    result.addressField = new FieldSpec(sAddr, ndx);
    result.endAll = ndx;

    // If we are looking for implied intersection markers see if we can
    // find another street name immediately following this one.  If we can
    // then set the implied & column to the end of the first street and set
    // the end of the address field to the end of the second street.
    if (isFlagSet(FLAG_IMPLIED_INTERSECT)) {

      // if a street connector or mile marker follows the road name
      // do not insert an ampersand here
      int mark = result.endAll;
      if (!isType(mark, ID_MILE_MARKER) && findConnector(mark)<0) {

        if (!padField && parseAddressToCity(sAddr, ndx, result)) {
          if (ndx < result.addressField.fldEnd) {

            // We are treating the second part as a street name, and might or might
            // not have previously recognized it as such.  If we did not, we might have
            // attached an ambiguous direction to the first street when we really want
            // to attach it to the second street
            if (isType(mark-1, ID_DIRECTION) && !isType(mark, ID_DIRECTION)) {
              if (isFlagSet(FLAG_ALLOW_DUAL_DIRECTIONS) || !isType(result.addressField.fldEnd-1, ID_DIRECTION)) {
                if (!isFlagSet(isType(mark-1, ID_PURE_DIRECTION) ? FLAG_PREF_TRAILING_SIMPLE_DIR : FLAG_PREF_TRAILING_BOUND)) {
                  mark--;
                }
              }
            }

            // Set the ampersand insert point
            result.insertAmp = mark;
          }
          return true;
        }
        ndx = findRoadEnd(ndx, 0);
        if (ndx >= 0) {
          result.insertAmp = result.endAll;
          result.addressField.end(ndx);
          result.endAll = ndx;
        }
      }
    }

    // See if we can parse out to a city
    if (!padField && parseAddressToCity(sAddr, result.endAll, result)) return true;

    // Nope, see if we can find some trailing data
    return parseTrailingData(result);
  }

  /**
   * Fallback parser used when nothing else works
   * @param sType
   */
  private void parseFallback(StartType sType, Result result) {

    result.reset(startAddress);
    boolean crossOnly = isFlagSet(FLAG_ONLY_CROSS);
    boolean padField = isFlagSet(FLAG_PAD_FIELD | FLAG_PAD_FIELD_EXCL_CITY);
    boolean startFldReq = isFlagSet(FLAG_START_FLD_REQ);
    boolean emptyAddrOK = isFlagSet(FLAG_EMPTY_ADDR_OK);

    // Determine tentative start and end of address information
    int stAddr = startAddress;
    int endAddr = padField || !isFlagSet(FLAG_ANCHOR_END) ? -1 : result.tokens.length;

    // Make a pass through the token string looking for interesting things
    // (apt indicates, cross street indicators, and invalid address tokens
    int iApt = -1;
    int iAptSt = -1;
    int iAptEnd = -1;

    int tmp;
    if (stAddr < 0) {
      tmp = startFldReq ? 1 : 0;
    } else {
      tmp = stAddr;
      if (!emptyAddrOK) tmp++;
    }
    for (int ndx = tmp; ndx < tokens.length; ndx++) {

      // Mark the location and type of the first apartment indicator
      // Not a hard limit yet, just want to know where the first one is.
      if (iApt < 0) {
        if (isType(ndx, ID_APT)) {
          iApt = ndx;
          iAptSt = ndx+1;
          iAptEnd = ndx+2;
        }
        else if (isType(ndx, ID_FLOOR)) {
          iApt = iAptSt = ndx;
          iAptEnd = ndx+2;
        }
        else if (isAptToken(tokens[ndx], false)) {
          iApt = iAptSt = ndx;
          iAptEnd = ndx+1;
        }
      }

      // Stuff we only look for in  real address searches
      if (!crossOnly) {

        // If we have not yet identified the start address point, see if
        // this looks like a house number, and if it does set the start
        // address point here.  Oh, and wipe out any previously found
        // apt indicators
        if (stAddr < 0 && (ndx > 0 || !startFldReq) && ndx < tokens.length-1) {
          if (isHouseNumber(ndx) && !isType(ndx+1, ID_NOT_ADDRESS)) {
            if (ndx == 0 || ! isType(ndx-1, ID_RELATION)) {
              stAddr = ndx;
              iApt = -1;
            }
          }
        }

        // A cross street indicator, marks a hard end of address
        if (isType(ndx, ID_CROSS_STREET)) {
          result.endAll = endAddr = ndx;
          findCrossStreetEnd(ndx+1, true, result);
          break;
        }
      }

      // An invalid address token works for all address searches.  It only
      // kicks in after we have identified an address start and if the end
      // address is not locked.  In which case it marks the end of the address
      // info
      if (stAddr >= 0 && endAddr < 0 && isType(ndx, ID_NOT_ADDRESS)) {

        // A YO type of not address also takes out a preceding numeric field
        if (isType(ndx, ID_YEAR_OLD_NOT_ADDRESS) && ndx > stAddr && isType(ndx-1, ID_NUMBER)) ndx--;
        result.endAll = endAddr = ndx;
        break;
      }
    }

    // If we still haven't found the end of the address, see if we
    // have found apt marker, if we have, set the end address 2 past
    // the apt marker.  We will get the actual apt field later.
    if (endAddr < 0 && iApt >= 0 && iAptEnd <= tokens.length) {
      result.endAll = endAddr = iAptEnd;
    }

    // If we found and end of the address, and are looking for
    // a pad field, try to extend the pad field out to the city
    // name or EOL
    boolean parseCity = false;
    if (endAddr >= 0 && padField && result.endAll < tokens.length) {
      parseCity = true;
      int tmpNdx = result.endAll;
      if (isComma(tmpNdx)) tmpNdx++;
      parsePadToCity(tmpNdx, result);
    }

    // If we still haven't found a recognizable end address, take everything
    if (endAddr < 0) {
      result.endAll = endAddr = tokens.length;
    }

    // However we identified the end of the address, see if it contains an
    // apt indicator, and if it does, use that to set up the apartment field
    if (iApt >= 0 && iAptEnd <= endAddr) {
      if (iApt == iAptSt && iAptEnd == iApt+1) result.aptToken = true;
      result.aptField = new FieldSpec(iAptSt, endAddr);
      endAddr = iApt;
    }

    // If we haven't found a start of address yet,
    // assign everything to the start field
    if (stAddr < 0) {

      if (result.startField != null) {

        // But first, if we are still parsing to the end of the field, see if we can
        // find a city at end of field
        if (endAddr == tokens.length) {
          if (parseStartToCity((emptyAddrOK ? 0 : 1), true, result)) return;
        }

        result.startField.end(endAddr);
        return;
      }
    }

    // Last step, see if we can parse out a city name
    // if we haven't already
    if (result.startField != null) result.startField.end(stAddr);
    if (!parseCity) {
      tmp = stAddr+1;
      if (isHouseNumber(stAddr)) tmp++;
      if (parseFallbackToCity(stAddr, tmp, result)) return;
    }

    // OK, we have an address
    result.addressField = new FieldSpec(stAddr, endAddr);
  }

  /**
   * Called when we found the end of a road name and are guessing at where
   * it might start
   * @param start Hard limit on where address can start
   * @param sAddr index of current start of tentative road name
   *
   * @return index of presumed road name
   */
  private int stretchRoadPrefix(int start, int sAddr) {

    // If we are backed up against hard start position, nothing to check
    if (sAddr == start) return sAddr;

    // Ditto if preceding token is not legitimate address
    if (isType(sAddr-1, ID_NOT_ADDRESS)) return sAddr;

    // If read name starts with a direction, back up one
    if (isType(sAddr, ID_DIRECTION)) sAddr--;

    // If road starts with a common street name prefix, back up one place
    if (sAddr > start && isType(sAddr-1, ID_STREET_NAME_PREFIX)) sAddr--;

    // Look up to 3 tokens back to see if we find a direction token
    // Stop search if we encounter a /, lest we confuse a W/INJURY
    // as part of a road name
    for (int j = 1; j<= 3; j++) {
      int ndx = sAddr - j;
      if (ndx < start) break;
      if (tokens[ndx].equals("/")) break;
      if (isType(ndx, ID_NOT_ADDRESS)) break;
      if (findConnector(ndx)>=0 || ndx>0 && findConnector(ndx-1)==ndx+1) break;
      if (isType(ndx, ID_DIRECTION)) return sAddr-j;
    }

    // No luck, see if the previous token is a possible road prefix
    if (sAddr > start && isType(sAddr-1, ID_OPT_ROAD_PFX)) sAddr--;
    return sAddr;
  }

  /**
   * Parse address field to city
   * @param stAddr prospective start of address field
   * @param srcNdx index to start searching
   * @param result Result object where results will be saved
   * @return true if city was found and all pertinent information was found.
   * False if not city was found and  nothing was changed in result
   */
  private boolean parseAddressToCity(int stAddr, int srcNdx, Result result) {
    FieldSpec addressField = new FieldSpec(stAddr);
    if (!parseToCity(addressField, true, false, 1, srcNdx, result)) return false;
    result.addressField = addressField;
    if (result.startField != null) result.startField.optionalEnd(stAddr);
    return true;
  }

  /**
   * Parse fallback field to city
   * @param stAddr prospective start of address field
   * @param srcNdx index to start searching
   * @param result Result object where results will be saved
   * @return true if city was found and all pertinent information was found.
   * False if not city was found and  nothing was changed in result
   */
  private boolean parseFallbackToCity(int stAddr, int srcNdx, Result result) {
    FieldSpec addressField = new FieldSpec(stAddr);
    if (!parseToCity(addressField, false, false, 1, srcNdx, result)) return false;
    result.addressField = addressField;
    if (result.startField != null) result.startField.optionalEnd(stAddr);
    return true;
  }

  /**
   * Parse pad field to city
   * @param start prospective start of pad field
   * @param result Result object where results will be saved
   * @return true if city was found and all pertinent information was found.
   * False if not city was found and  nothing was changed in result
   */
  private boolean parsePadToCity(int start, Result result) {
    boolean recheckApt = isFlagSet(FLAG_RECHECK_APT);
    boolean extraApt = recheckApt;
    if (recheckApt) {
      if (isType(start, ID_APT)) {
        start++;
        extraApt = false;
      } else if (isType(start, ID_FLOOR)) {
        extraApt = false;
      } else if (isAptToken(start)) {
        result.aptToken = true;
      }
    }
    FieldSpec padField = new FieldSpec(start);
    if (!parseToCity(padField, false, recheckApt, 1, start, result)) return false;
    if (recheckApt) {
      result.extraApt = extraApt;
      result.aptField = padField;
    } else {
      result.padField = padField;
    }
    return true;
  }

  /**
   * Parse start field to City
   * @param start prospective start of pad field
   * @param result Result object where results will be saved
   * @return true if city was found and all pertinent information was found.
   * False if not city was found and  nothing was changed in result
   */
  private boolean parseStartToCity(int start, boolean optCity, Result result) {
    return parseToCity(result.startField, false, false, optCity ? 3 : 1, start, result);
  }

  /**
   * See if we can parse an address from a known starting point to a city
   * @param fldSpec previous field being parsed
   * @param addrFld true if fldSpec is destined to be an address field spec
   * @param aptFld true if fldSPec is destined to be an apartment field
   * @param cityOpt search option<br>
   * 1 - succeed only if real city found, or at EOL if FLAG_ANCHOR_END is set
   * 2 - succeed if we find an apt or cross street indicator
   * 3 - always succeed
   * @param srcNdx start looking for city here
   * @return true if  city was identified and all fields have been set
   */
  private boolean parseToCity(FieldSpec fldSpec, boolean addrFld, boolean aptFld, int cityOpt, int srcNdx, Result result) {

    // If we are doing a cross only parse without a city, answer is always no
    boolean crossOnly = isFlagSet(FLAG_ONLY_CROSS);
    if (crossOnly && !isFlagSet(FLAG_ONLY_CITY | FLAG_ANCHOR_END)) return false;

    // If FLAG_ANCHOR_END is set, we are going to parse this to the
    // end of the line without looking for a city
    boolean anchorEnd = isFlagSet(FLAG_ANCHOR_END);
    boolean checkStatus = isFlagSet(FLAG_CHECK_STATUS);
    boolean parseToEnd = anchorEnd && ! checkStatus;
    boolean padField = isFlagSet(FLAG_PAD_FIELD | FLAG_PAD_FIELD_EXCL_CITY);
    boolean cityOnly = isFlagSet(FLAG_ONLY_CITY);
    boolean nearToEnd = isFlagSet(FLAG_NEAR_TO_END);

    // Starting at or after the end of the field is usually an automatic failure
    // But if the anchor end flag is set, and ending here will produce a
    // legitimate data field, close out that field
    if (srcNdx >= tokens.length) {
      if (srcNdx > tokens.length) return false;
      if (!anchorEnd) return false;
      if (fldSpec == null || fldSpec.fldStart >= tokens.length) return false;
      fldSpec.end(tokens.length);
      result.endAll = tokens.length;
      return true;
    }
    if ((cityOpt == 1 || checkStatus) && !parseToEnd && !nearToEnd && lastCity < srcNdx) return false;

    // Notice: If the FLAG_ANY_PAD_FIELD was set, some of these fields might have
    // been found before the PAD field, in which case we don't want to disturb
    // them.
    boolean foundCity = false;
    FieldSpec aptField = null;
    FieldSpec crossField = null;
    FieldSpec nearField = null;
    boolean aptToken = false;

    FieldSpec startField = new FieldSpec(0);

    FieldSpec lastField = startField;
    for (int ndx = srcNdx-1; ndx < tokens.length; ndx++) {

      // Skip over special street names
      if (mWordSpecialFwd != null && ndx >= 0) {
        int tmpndx = mWordSpecialFwd.findEndSequence(ndx);
        if (tmpndx >= 0) ndx = tmpndx;
      }

      // Skip over multiword  street names
      if (mWordStreetsFwd != null && ndx >= 0) {
        int tmpNdx = mWordStreetsFwd.findEndSequence(ndx);
        if (tmpNdx > ndx+1) {
          if (isFlagSet(FLAG_NO_STREET_SFX)) ndx = tmpNdx;
          else if (isRoadSuffix(tmpNdx)) ndx = tmpNdx + 1;
        }
      }
      if (ndx < srcNdx) continue;

      // Is there a city here?
      int tmpNdx = ndx;
      if (isComma(tmpNdx)) tmpNdx++;
      int endCity = findEndCity(tmpNdx);
      if (endCity >= 0) {

        // Close out the last field and save the
        // city location in results and jump out of the loop
        lastField.end(ndx);
        result.cityField = new FieldSpec(tmpNdx, endCity);
        foundCity = true;
        break;
      }

      // If we are only parsing a city field, skip all of the fancy stuff
      if (cityOnly) continue;

      // Only check for fun stuff if it isn't inside a pad field
      if (!padField) {

        // Special stuff not checked for in cross street only parse
        if (!crossOnly) {

        // Check for apartment marker
          if (!aptFld) {
            if (aptField == null) {
              if (tmpNdx+1 < tokens.length && isType(tmpNdx, ID_FLOOR | ID_APT) &&
                  (!isType(tmpNdx, ID_APT_SOFT) || !isRoadSuffix(tmpNdx+1))) {
                lastField.end(ndx);
                ndx = tmpNdx;
                int tmp = ndx;
                if (!isType(tmp, ID_FLOOR)) tmp++;
                lastField = aptField = new FieldSpec(tmp);
              }
              else if (tmpNdx < tokens.length && isAptToken(tokens[tmpNdx], false)) {
                aptToken = true;
                lastField.end(ndx);
                ndx = tmpNdx;
                lastField = aptField = new FieldSpec(ndx);
              }
            }
          }

          if (nearField == null && isType(tmpNdx, ID_NEAR)) {
            lastField.end(ndx);
            ndx = tmpNdx;
            lastField = nearField = new FieldSpec(ndx);
            if (nearToEnd && cityOpt == 1) cityOpt = 2;
          }

          // Check for cross street marker
          if (crossField == null && ndx < tokens.length-1 && isType(tmpNdx, ID_CROSS_STREET)) {
            lastField.end(ndx);
            ndx = tmpNdx;
            lastField = crossField = new FieldSpec(ndx+1);
          }
        }
      }

      // If we are processing an address field and we skipped over a comma
      // without finding anything, or encountered a non-address character
      // fail here
      if (addrFld && lastField == startField) {
        if (tmpNdx != ndx) {
            if (!isFlagSet(FLAG_ACCEPT_COMMA)) return false;
        } else {
          if (isType(ndx, ID_NOT_ADDRESS)) return false;
        }
      }
      ndx = tmpNdx;
    }

    // End of loop, check for different success conditions
    // Obviously finding a city is a successful outcome
    if (foundCity) {
      result.endAll = result.cityField.fldEnd;
    }

    // If we are parsing to end of field, return successful status
    // If we are still processing a NEAR field and the FLAG_NEAR_TO_END
    // flag was set, do likewise
    else if (parseToEnd ||
        nearToEnd && nearField != null && nearField == lastField) {
      lastField.end(tokens.length);
      result.endAll = tokens.length;
    }

    // If the city option is to always succeed and we have not found anything
    // put everything in the start field
    else if (cityOpt == 3 && lastField == startField) {
      startField.end(tokens.length);
      result.endAll = tokens.length;
    }

    // If we are checking address status, none of the remaining
    // success conditions should be recognized
    else if (cityOpt == 1 || checkStatus) return false;

    // Otherwise if we found an apartment or cross street indicator
    // try to close out the found field
    else if (lastField == aptField) {
      int ndx = lastField.fldStart;
      if (aptToken) {
        String apt = getAptValue(tokens[ndx]);
        ndx++;
        Long tokFlgs = dictionary.get(apt);
        if (tokFlgs != null && (tokFlgs.longValue() & ID_FLOOR) != 0) ndx++;
      }
      else {
        if (isType(ndx, ID_FLOOR)) ndx += 2;
        else ndx++;
      }
      lastField.end(ndx);
      result.endAll = ndx;
    }
    else if (lastField == crossField) {
      findCrossStreetEnd(lastField.fldStart, true, result);
      crossField  = result.crossField;
    }

    // Otherwise return failure
    else return false;

    // We are successful!!
    // Now is when we save all of the working variables back into the result object
    if (aptField != null) {
      result.aptToken = aptToken;
      result.aptField = aptField;
    }
    if (crossField != null) result.crossField = crossField;
    if (nearField != null && nearField.fldEnd-nearField.fldStart > 1) result.nearField = nearField;
    if (fldSpec != null) fldSpec.end(startField.fldEnd);

    // If we did find a city and there is a pad field, it might have the misfortune to be a
    // place name that includes a local city name.  So we will call
    // ourselves recursively in an attempt to find another city name
    // behind this one
    if (foundCity && isFlagSet(FLAG_PAD_FIELD) && result.cityField.fldEnd < tokens.length) {
      parseToCity(fldSpec, addrFld, aptFld, 1, result.cityField.fldEnd, result);
    }
    return true;
  }

  /**
   * Find the end of a city that starts at the current index
   * @param ndx current index
   * @return one past the last token in city if city was found,
   * -1 if this is not a city.
   */
  private int findEndCity(int ndx) {

    // If there is no city list, obviously there is no city
    if (mWordCities == null) return -1;

    // If this call should ignore any listed cities, return now
    if (isFlagSet(FLAG_NO_CITY)) return -1;

    // See if we can find the end of a city sequence
    // If not, then return -1;
    int endNdx = mWordCities.findEndSequence(ndx);
    if (endNdx < 0) return -1;

    // Under no circumstances will we accept a city ending with "CO" or "COUNTY"
    // that is followed by the word "LINE" or "LIN".
    if (endNdx < tokens.length) {
      String lastWord = tokens[endNdx-1].toUpperCase();
      if (lastWord.equals("CO") || lastWord.equals("COUNTY")) {
        lastWord = tokens[endNdx].toUpperCase();
        if (lastWord.equals("LINE") || lastWord.equals("LIN")) return -1;
      }
    }

    // We found a real city.  But....
    // Doesn't count if anchor end requested and this isn't the end of the tokens
    if (isFlagSet(FLAG_ANCHOR_END) &&  endNdx != tokens.length) return -1;

    // We don't want to return this as a city if it look it might by a street
    // named after that city :(

    // But we do not have to worry about that if we are only parsing a city name
    if (isFlagSet(FLAG_ONLY_CITY) && !isFlagSet(FLAG_ONLY_CROSS)) return endNdx;

    // OK, we have to do this....
    // If the city is followed by a road suffix, disqualify it
    // Unless the road suffix is followed by a road suffix
    if (!isType(endNdx, ID_NOT_ADDRESS | ID_CROSS_STREET | ID_NEAR)) {
      if (isRoadSuffix(endNdx) || isRoadSuffix(endNdx+1)) {

        // Or unless there might be a following cross street, and the street suffix
        // found after the city can start a valid road name in its own right
        if (!isFlagSet(FLAG_CROSS_FOLLOWS) || findRoadEnd(endNdx, 1, true) < 0) return -1;
      }
    }

    // Looks good, lets return this
    return endNdx;
  }

  /**
   * Determine if token is unambiguous street suffix
   * @param ndx - token index
   * @return true if token at index it unambiguous street suffix
   */
  private boolean isRealRoadSuffix(int ndx) {
    if (!isRoadSuffix(ndx)) return false;
    if (isType(ndx, ID_AMBIG_ROAD_SFX)) return false;
    return true;
  }

  /**
   * See if a token is a legitimate road suffix
   * @param ndx token index
   * @return true if token is a legitimate road suffix
   * considered a road suffix
   */
  private boolean isRoadSuffix(int ndx) {
    if (!isType(ndx, ID_ROAD_SFX)) return false;
    if (isType(ndx, ID_DR) && isType(ndx+1, ID_DOCTOR)) return false;
    if (isType(ndx, ID_ST) && isType(ndx+1, ID_SAINT)) return false;
    return true;
  }

  /**
   * Determine if token is a potential ambiguous road suffix, which means
   * road names should generally include the word in front of this
   * @param ndx token index
   * @return true if previous word should be included in road name
   */
  private boolean isAmbigRoadSuffix(int ndx) {

    // If this is the first token, obviusly we cannot include a previous word
    if (ndx == 0) return false;

    // Is this a potential ambiguous road suffix
    if (!isType(ndx, ID_AMBIG_ROAD_SFX)) return false;

    // If the previous word cannot be an address or is a direction, answer is no
    if (isType(ndx-1,ID_NOT_ADDRESS | ID_DIRECTION)) return false;

    // One more special case, if there might not be a delimiter
    // between the start field and address, and the previous word
    // looks like it might consist of a merged word and house number
    if (isFlagSet(FLAG_START_FLD_NO_DELIM) && MIXED_WORD_PTN.matcher(tokens[ndx-1]).matches()) return false;

    // Otherwise we are good to go
    return true;
  }
  private static final Pattern MIXED_WORD_PTN = Pattern.compile("[A-Z]+\\d+", Pattern.CASE_INSENSITIVE);

  /**
   * Parse apt and cross street information after we have parsed
   * a complete address
   * @param result returns result information
   * @return true if final address parse should be considered successful
   */
  private boolean parseTrailingData(Result result) {

    // Apt and cross street process is skipped if we are parsing cross streets
    if (!isFlagSet(FLAG_ONLY_CROSS)) {

      boolean recheckApt = isFlagSet(FLAG_RECHECK_APT);

      // If recheck Apt is requested, parsePdToCity does all of the work.
      // If and only it succeeds.
      if (recheckApt && parsePadToCity(result.endAll, result)) return true;

      // Otherwise, just look for a normal apt
      if (parseApt(result)) recheckApt = true;

      // If we found an apt, or are rechecking apt fields, look ahead for
      // cross street indicator.  If found, expand the end of the apt field
      // to that
      if (recheckApt) {
        int ndx = findAptEnd(result);
        if (ndx > result.endAll) {
          if (result.aptField == null) result.aptField = new FieldSpec(result.endAll, ndx);
          else result.aptField.fldEnd = ndx;
          result.endAll = ndx;
        }
      }

      // Always look for a cross street indicator
      if (isType(result.endAll, ID_CROSS_STREET)) {
        findCrossStreetEnd(result.endAll+1, true, result);
      }

      // If we are looking for a pad field between the address and city
      // now is when we try to find that city
      if (isFlagSet(FLAG_PAD_FIELD | FLAG_PAD_FIELD_EXCL_CITY)) {
        parsePadToCity(result.endAll, result);
      }
    }

    // If we have not yet found a NEAR field, see if there is one here
    if (result.nearField == null && isType(result.endAll, ID_NEAR) && !isFlagSet(FLAG_ONLY_CROSS)) {
      result.nearField = new FieldSpec(result.endAll, tokens.length);
      result.endAll = tokens.length;
    }

    // Return success unless we were requested to use the entire text string and
    // have failed to do so
    return (result.endAll == tokens.length || !isFlagSet(FLAG_ANCHOR_END));
  }

  /**
   * Find keyword that would indicate the end of an apt field
   * @param result parsed result object
   * @return index of cross street indicator found, otherwise -1
   */
  private int findAptEnd(Result result) {
    for (int ndx = result.endAll; ndx<tokens.length; ndx++) {
      if (isType(ndx, ID_CROSS_STREET | ID_NEAR)) return ndx;
    }
    return -1;
  }

  /**
   * Parse apt field from end of address
   * @param ndx
   * @param result
   * @return true if apt field was identified
   */
  private boolean parseApt(Result result) {

    int ndx = result.endAll;
    if (isComma(ndx)) ndx++;
    if (isType(ndx, ID_APT)) {
      if (++ndx < tokens.length) {
        result.aptField = new FieldSpec(ndx, ++ndx);
        result.endAll = ndx;
      } else {
        result.endAll = tokens.length;
      }
      return true;
    }

    else if (isType(ndx, ID_FLOOR)) {
      if (ndx+1 < tokens.length) {
        result.aptField = new FieldSpec(ndx, ndx+2);
        result.endAll = ndx+2;
      } else {
        result.endAll = tokens.length;
      }
      return true;
    }

    else if (isAptToken(ndx)) {
      result.aptToken = true;
      result.aptField = new FieldSpec(ndx, ++ndx);
      result.endAll = ndx;
      return true;
    }
    return false;
  }

  /**
   * Locate end of cross street field
   * @param sCross start of prospective cross street information
   * @param force true if cross street information is required
   * @param result parse results
   */
  private void findCrossStreetEnd(int sCross, boolean force, Result result) {

    int sEnd = sCross;
    int ndx = sEnd;
    while (true) {

      // And try to find another road
      ndx = findRoadEnd(ndx, 2);
      if (ndx < 0) break;
      sEnd = ndx;

      // If this road was terminated by a connector
      // Loop back and see if we can find another cross street
      int tmp = findConnector(sEnd);
      if (tmp < 0) break;
      ndx = tmp;
    }

    // If we did not find anything, grab everything that we can
    // up to the first invalid address token
    if (force && sEnd <= sCross) {
      sEnd = tokens.length;
      for (int ii = sCross; ii<tokens.length; ii++) {
        if (isType(ii, ID_NOT_ADDRESS)) {
          sEnd = ii;
          break;
        }
      }
    }

    // Save the cross street location if found
    if (sEnd > sCross) {
      result.crossField = new FieldSpec(sCross, sEnd);
      result.endAll = sEnd;
    }
  }

  /**
   * See if we can identify a road name starting at a given index
   * @param start starting index
   * @param option - option controlling how we will deal with a suffixless street search
   *                  0 - No suffixless street names accepted
   *                  1 - only multiword suffixless street names accepted
   *                  2 - any suffixless street name accepted
   *                  3 - Only identified multiword or numbered highway names accepted
   * @return index of token past end of road name if successful, -1 otherwise
   */
  private int findRoadEnd(int start, int option) {
    return findRoadEnd(start, option, isFlagSet(FLAG_ONLY_CROSS | FLAG_STRICT_SUFFIX), isFlagSet(FLAG_CROSS_FOLLOWS));
  }

  /**
   * See if we can identify a road name starting at a given index
   * @param start starting index
   * @param option - option controlling how we will deal with a suffixless street search
   *                  0 - No suffixless street names accepted
   *                  1 - only multiword suffixless street names accepted
   *                  2 - any suffixless street name accepted
   *                  3 - Only identified multiword or numbered highway names accepted
   * @param strict do not permit early termination wihtout a proper suffix
   * @return index of token past end of road name if successful, -1 otherwise
   */
  private int findRoadEnd(int start, int option, boolean strict) {
    return findRoadEnd(start, option, strict, isFlagSet(FLAG_CROSS_FOLLOWS));
  }

  /**
   * See if we can identify a road name starting at a given index
   * @param start starting index
   * @param option - option controlling how we will deal with a suffixless street search
   *                  0 - No suffixless street names accepted
   *                  1 - only multiword suffixless street names accepted
   *                  2 - any suffixless street name accepted
   *                  3 - Only identified multiword or numbered highway names accepted
   * @param strict do not permit early termination wihtout a proper suffix
   * @param followingRoad true if this may be followed by another street name
   * @return index of token past end of road name if successful, -1 otherwise
   */
  private int findRoadEnd(int start, int option, boolean strict, boolean followingRoad) {

    // Skip over BLOCK indicator
    if (isType(start, ID_BLOCK)) start++;

    // Dummy loop that we can break out of when we find a road end
    int streetStart = start;
    int end;
    do {

      // See if a special or multi-word street names starts here
      int mWordIndex = -1;
      int tmp = findSpecialRoadEnd(start);
      if (tmp > 0) {
        end = tmp;
        break;
      }
      else if (tmp < 0) mWordIndex = -tmp;

      // If this starts with a street direction,

      if (isType(start, ID_DIRECTION)) {

        // If the direction starts a valid city name, bail out
        if (findEndCity(start) >= 0) return -1;

        // Otherwise bump the start search index and make another check
        // for special or multi-word street names
        start++;
        tmp = findSpecialRoadEnd(start);
        if (tmp > 0) {
          end = tmp;
          break;
        }
        else if (tmp < 0) mWordIndex = -tmp;
      }

      // Ditto for a street prefix
      // But save the original start position, some checks will use this
      // original start position and some will used the prefix adjusted start
      // position
      int origStart = start;
      while (isType(start, ID_OPT_ROAD_PFX)) start++;

      // If we are out of tokens, the answer is no
      if (start >= tokens.length) return -1;

      // Compute the failure index that we return if we fail to find a proper road end.
      // If we are processing cross streets, check for a special cross street name and
      // set the failure index to the end of that
      int failIndex = -1;
      if (isFlagSet(FLAG_ONLY_CROSS)) {
        failIndex = mWordCrossStreetsFwd.findEndSequence(origStart);
      }

      // if we are accepting roads without a street suffix, we will compute the
      // default value assuming this is a suffixless street name.  If not, the failure return
      // is always -1;
      if (failIndex < 0 && option > 0 && isFlagSet(FLAG_OPT_STREET_SFX|FLAG_NO_STREET_SFX)) {
        if (isType(start, ID_NOT_ADDRESS|ID_NOT_STREET_NAME) || findConnector(start)>=0) return -1;
        if (mWordIndex >= 0) {
          failIndex = mWordIndex;
        } else {
          failIndex = start+1;
        }
        if (option < 2 && failIndex - start < 2) failIndex = -1;
      }

      // We will accept a simple number as a street name in very restricted circumstances
      if (failIndex < 0 && option == 2 && !strict && isType(start, ID_NUMBER)) failIndex = start+1;

      // If non-address token found, we are done
      if (isType(start, ID_NOT_ADDRESS) || findConnector(start)>=0) return failIndex;

      // A stand alone road token can terminate the road search, but it must
      // be the first thing in the search sequence
      if (isRoadToken(start)) {
        end = start+1;
        break;
      }

      // See if this is a numbered highway
      end = findNumberedHwy(start);
      if (end >= 0) {

        // Yet another special case Texas FM number highways can be terminated
        // with a street suffix :(
        if (tokens[start].equalsIgnoreCase("FM")) {
          if (isRoadSuffix(end+1) && !isType(end+2, ID_ALPHA_ROUTE)) end++;
        }
        end++;

        // If this is a route prefix extension, (possibly following a direction)
        // we normally skip past it.
        // But do not do this if we are in implied intersections and it
        // looks like the beginning of another numbered hwy
        int tend = end;
        if (isType(tend, ID_DIRECTION)) tend++;
        if (isType(tend, ID_ROUTE_PFX_EXT)) {
          if (!isFlagSet(FLAG_IMPLIED_INTERSECT) || !isType(tend, ID_ROUTE_PFX) || !isType(tend+1, ID_ALPHA_ROUTE)) end = tend+1;
        }
        break;
      }

      // OK, OK, if we find a number followed by a connector, we will consider
      // it a numbered highway (sheesh)
      if (!strict && isType(start, ID_NUMBER) && findConnector(start+1)>=0) return start+1;

      // Still no luck,
      // If we are deliberately ignoring street suffixes, take what we have so far
      // Possibly incrementing the result over a road suffix that is right here.
      if (option > 0 && isFlagSet(FLAG_NO_STREET_SFX)) {
        if (failIndex > 0) {
          if (isType(failIndex-1, ID_NOT_STREET_NAME)) return -1;
          if (isRoadSuffix(failIndex)) failIndex++;
        }
        end = failIndex;
        break;
      }

      // If we only accept numbered highways, time to bail out
      if (option == 3) return -1;

      // A legitimate street suffix found here is never legal
      // Make an exception for ST and DR which might be the start
      // of roads named after saints or doctors.
      end = start;
      boolean good = false;
      do {
        if (!isType(end, ID_DR | ID_ST) && isRealRoadSuffix(end)) break;

        // Ditto for a NEAR indicator
        if (isType(end, ID_NEAR)) break;

        // start looking for a street suffix (or cross street indicator
        // If we have to pass more than two tokens before finding, give up
        boolean number = false;
        while (++end - start <= 3) {

          // Invalid token or mile marker rejects everything
          if (isType(end, ID_NOT_ADDRESS | ID_MILE_MARKER)) return failIndex;

          // An intersection marker marks the end of things
          if (findConnector(end)>=0) break;

          // See if this is a normal road suffix
          // Skip if it an ambiguous road suffix and a real road suffix follows it
          // Or if the road suffix is part of a two part highway number
          // Or if this is a TO route prefix phrase
          // Or is the start of a city name
          good = true;
          if (isRoadSuffix(end) && !isType(end-1, ID_NOT_STREET_NAME) &&
              (! (isType(end, ID_AMBIG_ROAD_SFX) && isRoadSuffix(end+1))) &&
              ( !checkNumberedHwy(end)) &&
              (findNumberedHwy(end-1) < 0) &&
              (! (isType(end, ID_ROUTE_PFX) && isType(end-1, ID_TO))) &&
              (findEndCity(end) < 0)) {
            end++;
            break;
          }

          // A cross street, apt keyword, or legitimate city name ends things
          // If a strict suffix search was requested, return failure, otherwise
          // return the current end location
          if ((isType(end, ID_CROSS_STREET|ID_APT|ID_FLOOR) && !isType(end, ID_APT_SOFT)) ||
              findEndCity(end) >= 0) {
            good = !strict;
            break;
          }

          // A numeric token is acceptable only if it is the last token in the street name
          good = false;
          if (number) break;
          if (isType(end, ID_NUMBER)) number = true;
        }
      } while (false);

      if (!good) end = failIndex;

    } while (false);

    // if we swapped in failIndex for end, it might be negative
    if (end < 0) return -1;

    // Bump bypass and trailing direction
    end = bumpTrailingDir(streetStart, end, followingRoad);

    return end;
  }

  /**
   * See if we can find a special street name or multi-word street name
   * at the specified index
   * @param ndx specified index
   * @return index past end of street name if found<br>
   * negative index past end of suffixless multi-word street name if found
   * zero otherwise
   *
   */
  private int findSpecialRoadEnd(int ndx) {

    // See if this is start of special street name
    if (mWordSpecialFwd != null) {
      int tmp = mWordSpecialFwd.findEndSequence(ndx);
      if (tmp >= 0) return tmp;
    }

    // See if this is the start of a multi word street name
    // terminated by a proper road suffix
    if (mWordStreetsFwd != null) {
      int mWordIndex = mWordStreetsFwd.findEndSequence(ndx);
      if (mWordIndex > ndx+1) {
        if (isRoadSuffix(mWordIndex)) {
          if (isType(mWordIndex, ID_AMBIG_ROAD_SFX) && isRoadSuffix(mWordIndex+1)) mWordIndex++;
          return mWordIndex+1;
        }
        if (!isFlagSet(FLAG_NO_STREET_SFX)) mWordIndex = -mWordIndex;
        return mWordIndex;
      }
    }
    return 0;
  }

  private void setTokenTypes(StartType sType, String address, String gpsCoords, Result result) {

    // Parse line into tokens and categorize each token
    // While we are doing this, identify the index of the last city
    // And see if we have a keyword flagging the start of the address
    startAddress =  -1;
    lastCity = -1;
    startNdx = isFlagSet(FLAG_START_FLD_REQ) ? 1 : 0;

    // A lot of special constructs need to be protected.  Which we will accomplish
    // by creating a second version of the address line that will be used to
    // identify the token positions with the line.  While the original address line
    // will be used to actually extract the token values
    String searchAddress = address;

    // GPS Coordinates can now contain blanks or colons, which we need to go to some trouble
    // to temporarily replace so they do not result in the coordinates being broken up
    if (gpsCoords != null) {
      String altGPSCoords = gpsCoords.replaceAll(".", "~").replace('\n', '~');
      searchAddress = searchAddress.replace(gpsCoords, altGPSCoords);
    }

    // The field delimiter pattern we use will break tokens before and after any / or &
    // characters.  But there are some special constructs that we do not want broken up.
    // These need to be protected

    Matcher match = PROTECTED_TOKEN_PTN.matcher(searchAddress);
    if (match.find()) {
      StringBuffer  sb = new StringBuffer();
      do {
        match.appendReplacement(sb, match.group().replace('/', '%').replace('&', '%'));
      } while (match.find());
      match.appendTail(sb);
      searchAddress = sb.toString();
    }

    // Now scan through the protected address line using the field token
    // delimiter pattern to identify the start and end of each token
    // Despite our best effort, there are case where the pattern may detect
    // back to back delimiters and we have to take steps to prevent that
    // from producing zero length tokens
    List<Integer> tokenStartList = new ArrayList<Integer>();
    List<Integer> tokenEndList = new ArrayList<Integer>();
    match = TOKEN_DELIM_PTN.matcher(searchAddress);
    int lastEnd = 0;
    while (match.find()) {
      int st = match.start();
      int end = match.end();
      if (st > lastEnd) {
        tokenStartList.add(lastEnd);
        tokenEndList.add(st);
      }
      lastEnd = end;
    }
    if (lastEnd < address.length()) {
      tokenStartList.add(lastEnd);
      tokenEndList.add(address.length());
    }

    int cnt = tokenStartList.size();
    tokenPos = new int[cnt];
    tokens = new String[cnt];
    for (int ndx = 0; ndx < cnt; ndx++) {
      int st = tokenStartList.get(ndx);
      tokenPos[ndx] = st;
      tokens[ndx] = address.substring(st, tokenEndList.get(ndx));
    }

    result.tokens = tokens;

    // Save the original address
    // When parsing is finished, we will use this to calculate the true prefix and leftover segments
    saveAddress = address;

    tokenType = new long[tokens.length];

    if (sType == StartType.START_ADDR) startAddress = 0;
    else result.startField = new FieldSpec(0);

    boolean pastAddr = false;
    for (int ndx = 0; ndx < tokens.length; ndx++) {
      setType(ndx, pastAddr);
      if (isType(ndx, ID_CROSS_STREET | ID_APT | ID_FLOOR)) {
        pastAddr = true;
      }
      if (isType(ndx, ID_CITY)) lastCity = ndx;
    }
  }

  // Sequence containing slashes that need to be protected
  private static final Pattern PROTECTED_TOKEN_PTN = Pattern.compile("\\bC/S:|\\b(?:\\d/\\d|AT&T|C/S|Y/O)\\b");

  // Token delimiter pattern should find field breaks
  // 1) for any sequence of one or more blanks
  // 2) Before or after any & or / or comma
  // 3) After any :
  // 4) For any period not followed by a space
  private static final Pattern TOKEN_DELIM_PTN =
      Pattern.compile("\\.?(?:\\s+|(?<! )(?=[/&,])|(?<=[/&,:])(?! )|(?<=\\.)(?![ \\d])|,)");

  // Identify token type
  private void setType(int ndx, boolean pastAddr) {
    String token = tokens[ndx];

    // If token contains any illegal characters, flag it as a non-address token
    // and bail out.  This is only a problem if we are still in the address proper
    // If we have passed the address and are now in apt or cross fields, illegal
    // character tokens are OK
    if (!pastAddr && badCharPtn.matcher(token).find()) {
      tokenType[ndx] |= ID_NOT_ADDRESS;
      return;
    }

    long mask = 0;

    // If token ends with a numeric comparison character, set the relation flag
    char chr = token.charAt(token.length()-1);
    if ("=><".indexOf(chr) >= 0) mask |= ID_RELATION;

    // If token is in dictionary, return the associated type code
    // City codes are not permitted to follow intersection connectors, cross
    // street markers, or at markers for fear they might be a street with the
    // same name as a city
    Long iType = dictionary.get(token.toUpperCase());
    if (iType != null) {
      mask = iType;

      // If @ is being used as a cross street marker, switch any tokens marked
      // as at signs to connectors
      if (isFlagSet(FLAG_AT_MEANS_CROSS)) {
        if ((mask & ID_AT_MARKER) != 0) {
          if (!isFlagSet(FLAG_AT_SIGN_ONLY) || token.equals("@")) mask |= ID_CONNECTOR;
        }
      }

      // Unless we are only looking for a city, cities are not allowed to follow
      // connectors or cross street indicators
      if (!isFlagSet(FLAG_ONLY_CITY) || isFlagSet(FLAG_ONLY_CROSS)) {
        if (ndx > 0 && isType(ndx-1, ID_CONNECTOR | ID_CROSS_STREET) ||
            ndx > 1 && isType(ndx-2, ID_CONNECTOR | ID_CROSS_STREET) && isType(ndx-1, ID_DIRECTION)) {
          mask &= ~ID_CITY;
        }
      }

      // Special case, the word LOT should not be considered an apt keyword if it
      // follows the word PARKING
      if ((mask & ID_APT) != 0 && ndx > 0 && token.equalsIgnoreCase("LOT") && tokens[ndx-1].equalsIgnoreCase("PARKING")) mask &= ~ID_APT;
    }

    // Numeric tokens 9 digits or longer are probably SSNs or phone numbers
    // which should be marked as non-address tokens
    if (NUMERIC.matcher(token).matches()) {
      if (token.length() >= 9) {
        tokenType[ndx] |= ID_NOT_ADDRESS;
        if (isFlagSet(FLAG_ANCHOR_END) && !isFlagSet(FLAG_ANY_PAD_FIELD)) startNdx = ndx+1;
        return;
      }
      else mask |= ID_NUMBER | ID_ALPHA_ROUTE;
    }

    // dull numbers separated by a dash might be a dual hwy number
    else if (DUAL_NUMBER_HWY_PTN.matcher(token).matches()) {
      mask |= ID_ALPHA_ROUTE;
    }

    // Some states use alpha route numbers.  This token is a candidate if
    // it hasn't been designated as anything else
    // it is one or two characters long
    // all of the characters are letters
    // it is not a common 2 letter word
    // The ordinal directions (NSEW) may be legitimate alpha routes (grumble)
    if (mask == 0) {
      if (ROUTE_NUMBER_PTN.matcher(token).matches()) mask |= ID_ALPHA_ROUTE;
    } else {
      if (allowDirectionHwyNames && token.length() == 1 && "NSEW".contains(token)) {
        mask |= ID_ALPHA_ROUTE;
      }
    }

    tokenType[ndx] =  mask;
  }
  private static final Pattern ROUTE_NUMBER_PTN = Pattern.compile("(?!IN|OF)[A-Z]{1,2}|\\d+[ABDHNSEW]?|\\d+[NSEW]B");
  private static final Pattern DUAL_NUMBER_HWY_PTN = Pattern.compile("\\d{1,3}-\\d{1,3}");

  private boolean isComma(int ndx) {
    if (ndx >= tokens.length) return false;
    return tokens[ndx].equals(",");
  }

  private int findConnector(int ndx) {
    if (isType(ndx, ID_JUST)) ndx++;
    if (isType(ndx, ID_CONNECTOR)) {
      if (isFlagSet(FLAG_AND_NOT_CONNECTOR) && isType(ndx, ID_AND_CONNECTOR)) return -1;
      return ndx+1;
    }
    if (isType(ndx, ID_DIRECTION) && isType(ndx+1, ID_OF)) return ndx+2;

    return -1;
  }

  /**
   * find end of number highway name
   * @param ndx possible start of numbered highway name
   * @return index of last token in numbered  highway name if found
   * -1 if not found
   */
  private int findNumberedHwy(int ndx) {
    return findNumberedHwy(ndx, false);
  }

  /**
   * Check a potential road suffix to confirm that is not the start
   * of a numbered highway.  A single route prefix and number will not
   * be considered since it is more likely to be a route suffix followed
   * by an apt number.
   * @param ndx index road suffix to be checked
   * @return true if this does look more like the start of a numbered
   * highway than an ending road suffix
   */
  private boolean checkNumberedHwy(int ndx) {
    return (findNumberedHwy(ndx, true) >= 0);
  }

  /**
   * find end of number highway name
   * @param ndx possible start of numbered highway name
   * @param boolean strict true if this should be a strict positive
   * identification check (ie.  Not a street suffix followed by an apt)
   * @return index of last token in numbered  highway name if found
   * -1 if not found
   */
  private int findNumberedHwy(int ndx, boolean strict) {
    if (isType(ndx, ID_ROUTE_PFX_PFX) && isType(ndx+1, ID_ROUTE_PFX_EXT) && isType(ndx+2, ID_ALPHA_ROUTE)) {

      // More special cases.  In a strict search, if a cross street might follow this address and
      // the token following this one starts a legitimate numbered highway name, then disgregard
      // this one
      if (strict && findNumberedHwy(ndx+1, false) >= 0) return -1;

      // Otherwise return the end of this numbered highway
      return ndx+2;
    }

    if (!strict && isType(ndx, ID_ROUTE_PFX) && isType(ndx+1, ID_ALPHA_ROUTE)) {

      // "IN A"  is never considered a numbered highway
      if (tokens[ndx].equalsIgnoreCase("IN") && tokens[ndx+1].equalsIgnoreCase("A")) return -1;

      return ndx+1;
    }
    return -1;
  }

  /**
   * find end of number highway name
   * @param start hard start of address field
   * @param ndx possible end of numbered highway name
   * @return one beginning index of numbered highway name if found
   * -1 if not found
   */
  private int findRevNumberedHwy(int start, int ndx) {
    if (ndx <= start) return -1;
    if (!isType(ndx, ID_ALPHA_ROUTE)) return -1;
    if (ndx > start+1 && isType(ndx-2, ID_ROUTE_PFX_PFX) && isType(ndx-1, ID_ROUTE_PFX_EXT)) return ndx-2;
    if (isType(ndx-1, ID_ROUTE_PFX)) return ndx-1;
    return -1;
  }

  /**
   * Bump index over trailing direction
   * @param stNdx street name start index
   * @param endNdx current end address index
   * @return adjusted end address
   */
  private int bumpTrailingDir(int stNdx, int endNdx) {
    return bumpTrailingDir(stNdx, endNdx, isFlagSet(FLAG_CROSS_FOLLOWS));
  }

  /**
   * Bump index over trailing direction
   * @param stNdx street name start index
   * @param endNdx current end address index
   * @param followingRoad true if we can expect a following road name
   * @return adjusted end address
   */
  private int bumpTrailingDir(int stNdx, int endNdx, boolean followingRoad) {

    // If we are not allowing
    // See what kind of directions were found at the start of the street name
    boolean leadDir = false;
    boolean leadBound = false;

    if (!isFlagSet(FLAG_ALLOW_DUAL_DIRECTIONS)) {
      int tmp = stNdx;
      while (true) {
        if (!isType(tmp, ID_DIRECTION)) break;
        if (isType(tmp, ID_PURE_DIRECTION)) leadDir = true;
        else leadBound = true;
        tmp++;
      }
    }

    // Bump pass bypass word
    if (isType(endNdx, ID_BYPASS)) endNdx++;

    // Is there a trailing direction that is not followed by OF
    if (isType(endNdx, ID_DIRECTION) && !isType(endNdx+1, ID_OF)) {

      // See if this is direction or bound and make sure we did not
      // encounter this beast at the start of the street name

      boolean pureDir = isType(endNdx, ID_PURE_DIRECTION);
      if (!(pureDir ? leadDir : leadBound)) {

        // If direction is part of a trailing city name, the answer is no.
        if (findEndCity(endNdx) < 0) {

          // Otherwise, if we are not concerned about the direction being part
          // of a following cross street name, we should include it
          boolean checkBound = false;
          if (!followingRoad || isFlagSet(pureDir ? FLAG_PREF_TRAILING_SIMPLE_DIR : FLAG_PREF_TRAILING_BOUND)) {
            endNdx++;
            checkBound = pureDir;
          }

          // Otherwise, see if what follows looks like a trailing street
          // name.  If it does not, or if the trailing street has its own
          // leading or trailing direction, bump over this direction
          else {
            boolean bump = false;
            int tmp = endNdx+1;
            while (isType(tmp, ID_DIRECTION)) {
              bump = true;
              tmp++;
            }
            while (isType(tmp, ID_OPT_ROAD_PFX)) tmp++;
            tmp = findRoadEnd(tmp, 0, true);
            if (tmp < 0) checkBound = bump = true;
            else if (isType(tmp-1, ID_DIRECTION)) bump = true;;
            if (bump) endNdx++;
          }

          // See if we should check for a trailing bound after incrementing over
          // a pure direction
          if (checkBound && isType(endNdx, ID_DIRECTION) && !isType(endNdx, ID_PURE_DIRECTION)) endNdx++;
        }
      }
    }
    return endNdx;
  }

  private boolean isType(int ndx, long mask) {
    if (ndx >= tokenType.length) return false;
    return (tokenType[ndx] & mask) != 0;
  }

  private boolean isFlagSet(int mask) {
    return (flags & mask) != 0;
  }

  // Determine if token is a single standalone road token
  // such as I-234, or US50, or RT250NB :(
  private boolean isRoadToken(int ndx) {

    // If reserved single word name, answer is yes
    // Unless it is also an optional street suffix followed by a
    // real street suffix
    if (isType(ndx, ID_SINGLE_WORD_ROAD)) {
      if (isType(ndx, ID_AMBIG_ROAD_SFX) && isRoadSuffix(ndx+1)) return false;
      return true;
    }

    // If illegal char, answer is no
    if (isType(ndx, ID_NOT_ADDRESS | ID_CONNECTOR)) return false;

    // Get the string token
    if (ndx >= tokenType.length) return false;
    String token = tokens[ndx];

    // See how many letters there are at start of token
    int pt = 0;
    while (pt < token.length() && Character.isLetter(token.charAt(pt))) pt++;

    // If zero, or if entire token is letters, the answer is no
    if (pt == 0 || pt == token.length()) return false;

    // If next character is a dash, skip over it
    int pta = pt;
    if (token.charAt(pt) == '-') pt++;

    // If next character is not a digit, answer is no
    if (pt >= token.length() || ! Character.isDigit(token.charAt(pt))) return false;

    // Shift the alpha portion upper case and see if what is left is a route prefix
    String pfx = token.substring(0, pta).toUpperCase();
    Long mask = dictionary.get(pfx);
    if (mask == null || (mask&ID_ROUTE_PFX) == 0) return false;

    // Looks promising.  Now scan over any digits and see what we have left
    do pt++; while (pt < token.length() && Character.isDigit(token.charAt(pt)));

    // If we are at end of string, answer is yes
    // Otherwise, the answer depends on if the remaining suffix looks like some
    // kind of highway qualifier (NB = north bound for example)
    if (pt != token.length()) {
      String sfx = token.substring(pt).toUpperCase();

      do {
        if (sfx.length() == 2 && sfx.charAt(1) == 'B' &&
            "NESW".indexOf(sfx.charAt(0)) >= 0) break;

        if (sfx.length() == 1 &&
            "NESW".indexOf(sfx.charAt(0)) >= 0) break;

        // Nothing recognized, return false
        return false;

      } while (false);
    }
    return true;
  }

  // Determine if token is a valid house number
  private boolean isHouseNumber(int ndx) {

    // If numeric token, answer is yes
    if (isType(ndx, ID_NUMBER)) return true;

    // If not address, return false
    if (isType(ndx, ID_NOT_ADDRESS)) return false;

    // Try it against the numeric street number pattern
    // which allows a trailing letter qualifier
    if (ndx >= tokens.length) return false;
    return isHouseNumber(tokens[ndx]);
  }

  /**
   * Determine if token is a house number
   * @param token token to be checked
   * @return true if this is a house number
   */
  protected boolean isHouseNumber(String token) {
    if (token.equals("7-11")) return false;
    if (PAT_HOUSE_NUMBER.matcher(token).matches()) return true;
    return false;
  }

  // Determine if token at index is a standalone apartment number
  private boolean isAptToken(int ndx) {
    if (ndx >= tokens.length) return false;
    if (isType(ndx+1, ID_YEAR_OLD_NOT_ADDRESS)) return false;
    return isAptToken(tokens[ndx], !isFlagSet(FLAG_NO_IMPLIED_APT));
  }

  /**
   * Determine if token is a valid single apartment token
   * @param token token to be checked
   * @param numberOK true if tokens that start with a digit should be considered valid
   * @return true if this is an apartment token
   */
  private boolean isAptToken(String token, boolean numberOK) {

    // Do not accept fractions
    if (token.contains("/")) return false;

    int pt = getAptBreak(token);
    if (pt < 0) return false;

    // Check for reserved things that look like street names
    if (isFlagSet(FLAG_CROSS_FOLLOWS) && token.length() >= 3) {
      String tail = token.substring(token.length()-3).toUpperCase();
      if (tail.equals("1ST") || tail.equals("2ND") || tail.equals("3RD") || tail.substring(1).equals("TH")) return false;
    }
    if (pt == 0) return numberOK & token.length()-pt <= 4;
    String prefix = token.substring(0,pt).toUpperCase();
    Long flags = dictionary.get(prefix);
    return flags != null && (flags & ID_APT) != 0;
  }

  /**
   * Extract the Apt value from a token that has previously been
   * identified as a single word apartment token
   * @param token token to be checked
   * @return apartment value
   */
  private static String getAptValue(String token) {
    int pt = getAptBreak(token);
    return token.substring(pt);
  }

  /**
   * Identify the break between the Apt keyword and value in a token
   * that we are trying to identify as a single word apt token
   * @param token Token to be checked
   * @return offset into token of start of apartment value
   */
  private static int getAptBreak(String token) {
    if (token.startsWith("#")) return 1;
    Matcher match = DIGIT.matcher(token);
    if (match.find()) return match.start();
    return -1;
  }
  private static final Pattern DIGIT = Pattern.compile("\\d");

  /**
   * This class contains a searchable list of multi word items.  It will be
   * used to keep two lists.  One of multi-word cities, and another of multi-word
   * streets.
   */
  private class MultiWordList {

    private int dir;
    private long idFlag;
    private long incompFlag;
    private long completeFlag;
    private List<String[]> wordList = null;

    /**
     * Create a multiword list
     * @param dir search direction +1 for forward search, -1 for backward search
     * @param idFlag token flag used to objects in this list.  If zero any
     * token will be considered to be at least a single word match
     * @param incompFlag token flag used to mark beginning of multiword sequences
     * @param completeFlag token flag used to mark complete single word sequence.
     * Not used if idFlag is zero
     * @param nameList list of possibly multiword names to be added to list
     */
    public MultiWordList(int dir, long idFlag, long incompFlag, long completeFlag, String[] nameList) {
      this(dir, idFlag, incompFlag, completeFlag, Arrays.asList(nameList));
    }

    /**
     * Create a multiword list
     * @param dir search direction +1 for forward search, -1 for backward search
     * @param idFlag token flag used to objects in this list.  If zero any
     * token will be considered to be at least a single word match
     * @param incompFlag token flag used to mark beginning of multiword sequences
     * @param completeFlag token flag used to mark complete single word sequence.
     * Not used if idFlag is zero
     * @param nameList list of possibly multiword names to be added to list
     */
    public MultiWordList(int dir, long idFlag, long incompFlag, long completeFlag, Collection<String> nameList) {
      this.dir = (dir < 0 ? -1 : 1);
      this.idFlag = idFlag;
      this.incompFlag = incompFlag;
      this.completeFlag = completeFlag;

      addNames(dir, nameList);
    }

    public void addNames(int dir, String[] nameList) {
      addNames(dir, Arrays.asList(nameList));
    }

    public void addNames(int dir, Collection<String> nameList) {

      long flags1 = idFlag | incompFlag;
      long flags2 = idFlag | completeFlag;
      // Run thorough the city list
      for (String name : nameList) {

        // If city name does not contain a blank, we are not interested in it
        if (name.contains(" ")) {

          // Break up city name into token list.
          // If this a backward search list, reverse the tokenList word order
          if (wordList == null) wordList = new ArrayList<String[]>();
          String[] tokenList = name.split(" +");
          if (dir < 0) {
            for (int ndx = 0; ndx < tokenList.length/2; ndx++) {
              int ndx2 = tokenList.length-ndx-1;
              String tmp = tokenList[ndx];
              tokenList[ndx] = tokenList[ndx2];
              tokenList[ndx2] = tmp;
            }
          }

          // Add the token list to the word list
          wordList.add(tokenList);

          // And add the first token to the dictionary as an incomplete city
          if (flags1 != 0) setupDictionary(flags1, tokenList[0]);

          // Add the rest of the tokens to the dictionary with no flags
          // Doesn't hurt the address parsing logic and the weird DispatchEmergitechParser
          // subclasses work better if these are in the dictionary
          for (int ndx = 1; ndx < tokenList.length; ndx++) setupDictionary(0, tokenList[ndx]);
        }

        // Otherwise, we just add this to dictionary as a normal city
        else {
          if (flags2 != 0) setupDictionary(flags2, name);
        }
      }
    }

    /**
     * Search for a single or multi-word sequence begining at specfied index
     * @param ndx specified token index
     * @return if no sequence found, return -1.
     * If search direction is forward returns one past the end of a found sequence
     * If search direction is backward, returns the beginning of found sequence
     */
    public int findEndSequence(int ndx) {

      // If there is an ID token flag, and isn't set for this index, look no further
      if (idFlag != 0 && !isType(ndx, idFlag)) return -1;

      // If this is flagged as the start of a multi-word entry, see if
      // we find a match in the muti-word list
      // If there are multiple matches, pick the longest
      int foundLen = 0;
      if (isType(ndx, incompFlag) && wordList != null) {
        for (String[] tokenList : wordList) {
          boolean match = true;
          for (int j = 0; j< tokenList.length; j++) {
            int jj = ndx + j*dir;
            if (jj < 0 || jj >= tokens.length ||
                ! tokenList[j].equalsIgnoreCase(tokens[jj])) {
              match = false;
              break;
            }
          }
          if (match) {
            if (tokenList.length > foundLen) foundLen = tokenList.length;
          }
        }
      }

      // If we didn't find a multiword match, see if this is flagged as a complete
      // single word match.  If there was no completeFlag, than any single word
      // item will be considered a match.  If no singleword match, return -1
      if (foundLen == 0) {
        if (completeFlag == 0 || isType(ndx, completeFlag)) foundLen = 1;
        else return -1;
      }

      // Compute the end (or beginning) index of what we have found and return it.
      int endNdx = ndx + foundLen*dir;
      if (dir < 0) endNdx++;
      return endNdx;
    }
  }

  private static class FieldSpec {
    int fldStart;
    int fldEnd;

    public FieldSpec(int fldStart) {
      this(fldStart, -1);
    }

    public FieldSpec(int fldStart, int fldEnd) {
      this.fldStart = fldStart;
      this.fldEnd = fldEnd;
    }

    public void end(int fldEnd) {
      this.fldEnd = fldEnd;
    }

    public void optionalEnd(int fldEnd) {
      if (this.fldEnd < 0) this.fldEnd = fldEnd;
    }
  }

  private static final Pattern NO_DELIM_ADDR_PTN1 = Pattern.compile("(?<!\\d)\\d+\\b");
  private static final Pattern NO_DELIM_ADDR_PTN2 = Pattern.compile("(?<!\\d)\\d+$");
  public static class Result {

    // We do need to access our parent object for global configuration settings
    // and to perform additional calculations.  But we must never refer to any
    // local variables used in calculating this result that may have been
    // overridden by a second calculation.  So, we declare Result as static to
    // avoid accidental references to local members, but save the parent object
    // so we can use it when we have determined that it is safe to do so.
    private SmartAddressParser parent;
    private int flags;

    private String[] tokens;
    private StartType startType;
    private int status = -1;
    private String callPrefix = null;
    private String placePrefix = null;
    private String stdPrefix = null;
    private String trailPlace = null;
    private FieldSpec startField = null;
    private FieldSpec addressField = null;
    private FieldSpec placeField = null;
    private boolean aptToken = false;
    private boolean extraApt = false;
    private FieldSpec aptField = null;
    private FieldSpec crossField = null;
    private FieldSpec padField = null;
    private FieldSpec cityField = null;
    private FieldSpec nearField = null;
    private int insertAmp = -1;
    int endAll = -1;
    private Result nearResult = null;

    private String startFld = null;
    private String left = null;
    private boolean mBlankLeft = false;
    private boolean commaLeft = false;

    public Result(SmartAddressParser parent, int flags) {
      this.parent = parent;
      this.flags = flags;
    }

    public void reset(int startAddress) {
      if (startAddress < 0) startField.end(-1);
      addressField = null;
      placeField = null;
      aptToken = extraApt = false;
      aptField = null;
      crossField = null;
      padField = null;
      cityField = null;
      insertAmp = -1;
      endAll = -1;
    }

    /**
     * Determine Determine how good an address this parse call found
     * @return zero if string is not recognized as valid address, otherwise a
     * numeric value in which higher values indicate better addresses
     */
    public int getStatus() {
      return status;
    }

    /**
     * Determine Determine how good an address this parse call found
     * @param extra number of extra tokens (presumably city names) that can
     * be ignored at the end of the line
     * @return zero if string is not recognized as valid address, otherwise a
     * numeric value in which higher values indicate better addresses
     */
    public int getStatus(int extra) {
      if (tokens == null) return 0;
      if (tokens.length-endAll > extra) return 0;
      return status;
    }

    /**
     * Determine Determine how good an address this parse call found
     * @return true if this is a valid address
     */
    public boolean isValid() {
      return status > STATUS_MARGINAL;
    }

    /**
     * Determine Determine how good an address this parse call found
     * @param extra number of extra tokens (presumably city names) that can
     * be ignored at the end of the line
     * @return true if this is a valid address
     */
    public boolean isValid(int extra) {
      if (tokens == null) return false;
      if (tokens.length-endAll > extra) return false;
      return isValid();
    }

    /**
     * Fill data object with information from parsed line
     */
    public void getData(MsgInfo.Data data) {

      // Reject is a special case
      if (status == STATUS_REJECT) {
        data.strAddress = left;
        return;
      }

      if (addressField != null) data.strAddress = stripLeadingZero(buildData(addressField, 2));
      if (placeField != null) data.strPlace = buildData(placeField, 0);
      if (aptField != null) {
        int ndx = aptField.fldStart;
        String apt = tokens[ndx];
        if (aptToken) {
          tokens[ndx] = getAptValue(apt);
        } else if (apt.startsWith("#")) tokens[ndx] = apt.substring(1).trim();
        data.strApt =  buildData(aptField, 0);
      }
      if (crossField != null) data.strCross = buildData(crossField, 1);
      if (cityField != null) {
        data.strCity = buildData(cityField, 0);
        if (parent.cityCodes != null) {
          String city = parent.cityCodes.getProperty(data.strCity.toUpperCase());
          if (city != null) data.strCity = city;
        }
      }

      // Before we figure out with to do with the leading start field, see if some of it
      // should be stripped off and added to the address
      // We use two pattern searches to do this.  If no address has been found, look for
      // any word ending in 1 or more digits.  If we found an address but the result
      // status indicates it did not find a street number, restrict the previos search
      // to the last word of the start field.  if we find a match for either pattern
      // move the digits and everything following them to the address field
      startFld = stdPrefix;
      if (startFld == null) startFld = "";
      if ((flags & FLAG_START_FLD_NO_DELIM) != 0) {
        Pattern searchPtn = null;
        if (data.strAddress.length() == 0) {
          searchPtn = NO_DELIM_ADDR_PTN1;
        } else if (status < STATUS_FULL_ADDRESS) {
          searchPtn = NO_DELIM_ADDR_PTN2;
        }
        if (searchPtn != null) {
          Matcher match = searchPtn.matcher(startFld);
          if (match.find()) {
            int pt = match.start();
            data.strAddress = append(startFld.substring(pt), " ", data.strAddress);
            startFld = startFld.substring(0,pt);
          }
        }
      }

      if (callPrefix != null) data.strCall = callPrefix;
      if (placePrefix != null) data.strPlace = placePrefix;

      // If no address field has been found, but we have a start field and caller
      // has requested fallback to put everything in the address field, make it so
      if (data.strAddress.length() == 0 && startFld.length() > 0 && (flags & FLAG_FALLBACK_ADDR) != 0) {
        parent.parseAddress(startFld, data);
        startFld = "";
      }

      // Otherwise, assign the start field to where it seems appropriate
      else {
        switch (startType) {
          case START_CALL:
          case START_CALL_PLACE:
            data.strCall = append(data.strCall, " / ", startFld);
            break;
          case START_PLACE:
            if (data.strPlace.length() == 0) data.strPlace = startFld;
            break;
        case START_ADDR:
          break;
        case START_OTHER:
          break;
        }
      }

      if (trailPlace != null) {
        Result res = parent.parseAddress(StartType.START_PLACE, flags | FLAG_START_FLD_REQ | FLAG_IGNORE_AT | FLAG_NO_ADDRESS, trailPlace);
        res.getData(data);
        left = res.getLeft();
      }

      // We do the NEAR field last, because it requires a lot of special treatment
      if (nearField != null) {

        // If there is no cross street and the near field content looks like an
        // address, stick it in the cross street.
        // Otherwise append the full term to the place field
        String field = buildData(nearField, 0);
        String field2 = null;
        int pt = field.indexOf(' ');
        if (pt >= 0) field2 = field.substring(pt+1);

        boolean nearPlace = true;
        if (field2 != null && data.strCross.length() == 0) {

          // If we found a city, the near field has a well defined end
          // and we can just call checkAddress to see if the near field is
          // an address or not
          if (data.strCity.length() > 0) {
            if (parent.isValidAddress(field2)) {
              data.strCross = field2;
              nearPlace = false;
            }
          }

          // If there is no city, things get complicated.  This could only have
          // happened if the FLAG_NEAR_TO_END flag was specified and we have
          // to make another call to parseAddress to see how much of the
          // near field looks like an address.  Whatever is left will be saved
          // and returned by a call to our getLeft() method.
          else {
            Result res = parent.parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, field2);
            if (res.isValid()) {
              res.getData(data);
              nearResult = res;
              nearPlace = false;
            }
          }
        }
        if (nearPlace) data.strPlace = append(data.strPlace, " ", field);
      }

      // Some corrective adjustments may be needed if the apt field was
      // collected from whatever was left after the end of the address
      if (extraApt) {

        // If parse says this is not an apt, append it to the address
        if (parent.isNotExtraApt(data.strApt)) {
          data.strAddress = append(data.strAddress, " ", data.strApt.replace('/', '&'));
          data.strApt = "";
        }

        // Otherwise if what we found starts with a place mark, move it to the
        // place field
        else {
          Matcher match = ADDR_PLACE_MARK.matcher(data.strApt);
          if (match.matches()) {
            data.strPlace = append(data.strPlace, " - ", match.group(1));
            data.strApt = "";
          }
        }
      }

      // If we are collecting trailing apts and did not find one
      // see if what we found ends with a floor indicator
      if ((flags & FLAG_RECHECK_APT) != 0 && data.strApt.length() == 0) {
        Matcher match = ADDR_TRAIL_FL.matcher(data.strAddress);
        if (match.find()) {
          data.strAddress = data.strAddress.substring(0,match.start());
          data.strApt = match.group(1);
        }
      }
    }

    /**
     * return the start field that is fond in front of the address
     */
    public String getStart() {
      if (startFld == null) {
        throw new RuntimeException("Invalid call to getStart()");
      }
      return startFld;
    }

    /**
     * @return the pad field (if any) that lies between the address property and
     * the city field
     */
    public String getPadField() {
      return buildData(padField, 0);
    }

    /**
     * Called after address has been parsed
     * @return the part of the line after the address
     */
    public String getLeft() {
      if (status == STATUS_REJECT) return "";
      if (nearResult != null) return nearResult.getLeft();
      return left;
    }

    /**
     * Determine there were multiple blanks directly in front of the string
     * returned by getLeft()
     * @return true if there were multiple blanks before the getLeft() result
     */
    public boolean isMBlankLeft() {
      if (nearResult != null) return nearResult.isMBlankLeft();
      return mBlankLeft;
    }

    /**
     * Determine there was a comma in front of the string returned by getLeft()
     * @return true if there was a comma before the getLeft() result
     */
    public boolean isCommaLeft() {
      if (nearResult != null) return nearResult.isCommaLeft();
      return commaLeft;
    }

    /**
     * @return whatever was identifying as being in front of the address
     */
    public String getAddressPrefix() {
      return buildData(startField, 0);
    }

    /**
     * @return everything from start of identified address to end of string
     */
    public String getFullAddress() {
      if (addressField ==  null) return null;
      return buildData(addressField.fldStart, endAll, 0);
    }

    /**
     * @return parsed city field
     */
    public String getCity() {
      return buildData(cityField, 0);
    }

    private String buildData(FieldSpec spec, int addrCode) {
      if (spec == null) return "";
      return buildData(spec.fldStart, spec.fldEnd, addrCode);
    }

    /**
     * Construct data field from the token sequence from given start and end position
     * @param start starting token index
     * @param end ending token index
     * @param addr code indicating what should be inserted between street names
     *         0 nothing or leave existing slash
     *         1 - slash
     *         2 - ampersand
     * @return Constructed data field.
     */
    private String buildData(int start, int end, int addrCode) {

      boolean atToCross = ((flags & FLAG_AT_MEANS_CROSS) != 0);
      StringBuilder sb = new StringBuilder();
      for (int ndx = start; ndx < end; ndx++) {

        // Comma is a special case.  Skip if first token in sequence
        // otherwise inhibit the usual blank placed between it and the
        // previous token
        String token = tokens[ndx];
        if (token.equals(",")) {
          if (ndx == start) continue;
        } else {
          if (sb.length() > 0) sb.append(' ');
        }

        // Insert & if required
        if (ndx == insertAmp) {
          sb.append(new String[]{"", "/ ", "& "}[addrCode]);
        }
        if (addrCode>1 &&
            (token.equals("/") ||
             (atToCross && token.equalsIgnoreCase("AT")))) token = "&";
        sb.append(token);
      }
      return sb.toString();
    }
  }
  private static final Pattern ADDR_PLACE_MARK = Pattern.compile("^(?:AT |@) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_TRAIL_FL = Pattern.compile(" +(\\d+ *FL)$", Pattern.CASE_INSENSITIVE);

  /**
   * Check a potential extra apartment field for constructs that do
   * not look like apartment fields
   * @param apt prospective extra apartment field
   * @return true if this does not look like a possible apartment field
   */
  protected boolean isNotExtraApt(String apt) {
    return NOT_APT_PTN.matcher(apt).matches();
  }
  private static final Pattern NOT_APT_PTN = Pattern.compile("(?:[&/]|(?:JUST )?(?:MM *\\d*|AND .*|EX|NORTH|SOUTH|EAST|WEST|PRIOR|BLK|MILE|BEFORE|AFTER|RUNAWAY|OFF|FROM|NEAR|OFF|CLOSE TO|PAST)\\b).*|.* MM|EXT|[NSEW]|[NS][EW]", Pattern.CASE_INSENSITIVE);

  /**
   * This is used by GenMultiWordStreetList.  When passed a previously passed
   * address it returns a simple street name stripped of house numbers prefixes
   * and suffixes.
   * @param address The parsed address
   * @return null if it does not contain a normal street name
   * (ie, with a regular street suffix).  Otherwise returns the naked
   * unadorned street name
   */
  protected String stripStreetName(String address) {

    // Do the normal split tokens processing
    Result res = new Result(this, 0);
    setTokenTypes(StartType.START_ADDR, address, null, res);

    // Strip off directions at end of address
    if (tokens.length == 0) return null;
    int sEnd = tokens.length-1;
    while (sEnd >= 0 && isType(sEnd, ID_DIRECTION)) sEnd--;

    // If no remaining tokens, or if last token is not a regular street
    // suffix, bail out
    if (sEnd < 0) return null;
    if (! isRoadSuffix(sEnd)) return null;

    // Not start from the beginning
    // If first token is a house number, skip it
    int sBeg = 0;
    if (isHouseNumber(tokens[sBeg])) sBeg++;

    // Skip over any directions, optional prefixes, and block indicators
    while (sBeg < sEnd && isType(sBeg, ID_DIRECTION | ID_OPT_ROAD_PFX | ID_BLOCK)) sBeg++;

    // If we have anything left, return it
    if (sBeg >= sEnd) return null;
    String result = "";
    for (int ii = sBeg; ii<sEnd; ii++) {
      result = append(result, " ", tokens[ii]);
    }
    return result;
  }
}
