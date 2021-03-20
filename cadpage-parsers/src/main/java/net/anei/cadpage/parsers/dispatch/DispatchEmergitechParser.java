package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchEmergitechParser extends FieldProgramParser {

  // Flag indicating that there will never be place name leading the address
  public static final int EMG_FLG_NO_PLACE = 1;

  public enum TrailAddrType {
    NONE,
    NAME,
    PLACE,
    INFO,
    PLACE_INFO(true);

    private boolean special;

    TrailAddrType() {
      this (false);
    }

    TrailAddrType(boolean special) {
      this.special = special;
    }

    public boolean isSpecial() {
      return special;
    }

    public TrailField getTrailField() {
      switch (this) {

      case NONE: return null;

      case NAME:
        return new TrailField(){
          @Override
          public void parse(String field, Data data) {
            data.strName = field;
          }
          @Override
          public String getFieldNames() {
            return "NAME";
          }
        };

      case PLACE:
      case PLACE_INFO:
        return new TrailField(){
          @Override
          public void parse(String field, Data data) {
            data.strPlace = append(data.strPlace, " - ", field);
          }
          @Override
          public String getFieldNames() {
            return "PLACE";
          }
        };

      case INFO:
        return new TrailField(){
          @Override
          public void parse(String field, Data data) {
            data.strSupp = field;
          }
          @Override
          public String getFieldNames() {
            return "INFO";
          }
        };
      }
      return null;
    }
  }

  private interface TrailField {
    void parse(String field, Data data);
    String getFieldNames();
  }

  private static final Pattern UNIT_PTN = Pattern.compile("D?\\[([- A-Z0-9]+)\\] *-+ *");
  private static final Pattern ID_PTN = Pattern.compile("Call: *([- 0-9]+)\\b");
  private static final Pattern HOUSE_DECIMAL_PTN = Pattern.compile("\\b(\\d+)\\.0{1,2}(?= )");
  private static final Pattern COMMENTS_PTN = Pattern.compile("\\bC ?O ?M ?M ?E ?N ?T ?S ?:");
  private static final Pattern BETWEEN_PTN = Pattern.compile("\\bB ?E ?T ?W ?E ?E ?N\\b");

  private String[] prefixList = null;
  private int[] extraSpacePosList;
  private boolean optUnit;
  private boolean noPlace;
  private boolean special;
  private TrailField trailField;

  private Set<String> specialWordSet = new HashSet<String>(Arrays.asList(new String[]{
      "APPLE",
      "EAST",
      "ELECTION",
      "MAIN",
      "MARKET",
      "NORTH",
      "SECOND",
      "SHORE",
      "SOUTH",
      "WEST"
  }));

  /**
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(String[] cityList, String defCity, String defState) {
    this((String[])null, false, (int[])null, cityList, defCity, defState, 0, null);
  }

  /**
   * @param optUnit true if unit information is optional
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(boolean optUnit, String[] cityList, String defCity, String defState) {
    this((String[])null, optUnit, (int[])null, cityList, defCity, defState, 0, null);
  }

  /**
   * @param optUnit true if unit information is optional
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param taType trailing address type
   */
  public DispatchEmergitechParser(boolean optUnit, String[] cityList, String defCity, String defState, TrailAddrType taType) {
    this((String[])null, optUnit, (int[])null, cityList, defCity, defState, 0, taType);
  }

  /**
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param flags parser flags
   * @param taType trailing address type
   */
  public DispatchEmergitechParser(String[] cityList, String defCity, String defState, int flags, TrailAddrType taType) {
    this((String[])null, false, (int[])null, cityList, defCity, defState, flags, taType);
  }

  /**
   * @param optUnit true if unit information is optional
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param flags parser flags
   * @param taType trailing address type
   */
  public DispatchEmergitechParser(boolean optUnit, String[] cityList, String defCity, String defState, int flags, TrailAddrType taType) {
    this((String[])null, optUnit, (int[])null, cityList, defCity, defState, flags, taType);
  }

  /**
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param flags parser flags
   */
  public DispatchEmergitechParser(String[] cityList, String defCity, String defState, TrailAddrType taType) {
    this((String[])null, false, (int[])null, cityList, defCity, defState, 0, taType);
  }

  /**
   * @param extraSpacePos Single extra blank column<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(int extraSpacePos, String[] cityList, String defCity, String defState) {
    this((String[])null, false, new int[]{extraSpacePos}, cityList, defCity, defState, 0, null);
  }

  /**
   * @param extraSpacePos Single extra blank column<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param optUnit true if unit is optional
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(boolean optUnit, int extraSpacePos, String[] cityList, String defCity, String defState) {
    this((String[])null, optUnit, new int[]{extraSpacePos}, cityList, defCity, defState, 0, null);
  }

  /**
   * Constructor
   * @param prefix Prefix that must be found at beginning of text page<br/>
   * An empty string means that no prefix value is expected<br/>
   * A null string means no prefix value is expected, and the following unit field is optional
   * @param extraSpacePos Single extra blank column<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(String prefix, int extraSpacePos,
                                   String[] cityList, String defCity, String defState) {
    this(prefix, new int[]{extraSpacePos}, cityList, defCity, defState, null);
  }

  /**
   * Constructor
   * @param prefix Prefix that must be found at beginning of text page<br/>
   * An empty string means that no prefix value is expected<br/>
   * A null string means no prefix value is expected, and the following unit field is optional
   * @param extraSpacePos Single extra blank column<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(String prefix, int extraSpacePos,
                                   String[] cityList, String defCity, String defState,
                                   TrailAddrType taType) {
    this(prefix, new int[]{extraSpacePos}, cityList, defCity, defState, taType);
  }

  /**
   * Constructor
   * @param prefix Prefix that must be found at beginning of text page<br/>
   * An empty string means that no prefix value is expected<br/>
   * A null string means no prefix value is expected, and the following unit field is optional
   * @param extraSpacePos1 Single extra blank column<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param extraSpacePos2 Single extra blank column<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(String prefix, int extraSpacePos1, int extraSpacePos2,
                                   String[] cityList, String defCity, String defState) {
    this(prefix, new int[]{extraSpacePos1, extraSpacePos2}, cityList, defCity, defState, null);
  }

  /**
   * Constructor
   * @param prefix Prefix that must be found at beginning of text page<br/>
   * An empty string means that no prefix value is expected<br/>
   * A null string means no prefix value is expected, and the following unit field is optional
   * @param extraSpacePosList Array of extra blank columns<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(String prefix, String[] cityList, String defCity, String defState) {

    // An empty prefix just means no prefix is expected
    // a null prefix means no prefix is expected and the entire unit block is optional
    this(prefix, null, cityList, defCity, defState, null);
  }

  /**
   * Constructor
   * @param prefix Prefix that must be found at beginning of text page<br/>
   * An empty string means that no prefix value is expected<br/>
   * A null string means no prefix value is expected, and the following unit field is optional
   * @param extraSpacePosList Array of extra blank columns<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param taType trailing address field type
   */
  public DispatchEmergitechParser(String prefix, String[] cityList, String defCity, String defState, TrailAddrType taType) {

    // An empty prefix just means no prefix is expected
    // a null prefix means no prefix is expected and the entire unit block is optional
    this(prefix, null, cityList, defCity, defState, taType);
  }

  /**
   * Constructor
   * @param prefix Prefix that must be found at beginning of text page
   * @param optUnit true if unit is optional
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(String prefix, boolean optUnit, String[] cityList, String defCity, String defState) {

    // An empty prefix just means no prefix is expected
    // a null prefix means no prefix is expected and the entire unit block is optional
    this(prefix, optUnit, null, cityList, defCity, defState, null);
  }

  /**
   * Constructor
   * @param prefix Prefix that must be found at beginning of text page
   * @param optUnit true if unit is optional
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param taType trailing address data type
   */
  public DispatchEmergitechParser(String prefix, boolean optUnit, String[] cityList, String defCity, String defState, TrailAddrType taType) {

    // An empty prefix just means no prefix is expected
    // a null prefix means no prefix is expected and the entire unit block is optional
    this(prefix, optUnit, null, cityList, defCity, defState, taType);
  }

  /**
   * Constructor
   * @param prefix Prefix that must be found at beginning of text page<br/>
   * An empty string means that no prefix value is expected<br/>
   * A null string means no prefix value is expected, and the following unit field is optional
   * @param extraSpacePosList Array of extra blank columns<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(String prefix, int[] extraSpacePosList,
                                 String[] cityList, String defCity, String defState) {

    // An empty prefix just means no prefix is expected
    // a null prefix means no prefix is expected and the entire unit block is optional
    this(prefix, extraSpacePosList, cityList, defCity, defState, null);
  }

  /**
   * Constructor
   * @param prefix Prefix that must be found at beginning of text page<br/>
   * An empty string means that no prefix value is expected<br/>
   * A null string means no prefix value is expected, and the following unit field is optional
   * @param extraSpacePosList Array of extra blank columns<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param taType trailing address data type
   */
  public DispatchEmergitechParser(String prefix, boolean optUnit, int[] extraSpacePosList,
                                  String[] cityList, String defCity, String defState, TrailAddrType taType) {

    this((prefix != null && prefix.length() > 0 ? new String[]{prefix} : null),
         optUnit, extraSpacePosList, cityList, defCity, defState, 0, taType);
  }

  /**
   * Constructor
   * @param prefix Prefix that must be found at beginning of text page<br/>
   * An empty string means that no prefix value is expected<br/>
   * A null string means no prefix value is expected, and the following unit field is optional
   * @param extraSpacePosList Array of extra blank columns<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param taType trailing address data type
   */
  public DispatchEmergitechParser(String prefix, int[] extraSpacePosList,
                                  String[] cityList, String defCity, String defState, TrailAddrType taType) {

    // An empty prefix just means no prefix is expected
    // a null prefix means no prefix is expected and the entire unit block is optional
    this((prefix != null && prefix.length() > 0 ? new String[]{prefix} : null),
         prefix == null, extraSpacePosList, cityList, defCity, defState, 0, taType);
  }

  /**
   * @param prefixList List of possible prefix values that must be found at start of text.  The
   * first is the primary value index offsets will be calculated with.  Any others are considered
   * earlier version that will be replaced with the primary value before the space adjustment is made
   * @param extraSpacePos Single extra blank column<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(String[] prefixList, String[] cityList, String defCity, String defState) {
    this(prefixList, false, (int[])null, cityList, defCity, defState, 0, null);
  }

  /**
   * @param prefixList List of possible prefix values that must be found at start of text.  The
   * first is the primary value index offsets will be calculated with.  Any others are considered
   * earlier version that will be replaced with the primary value before the space adjustment is made
   * @param extraSpacePos Single extra blank column<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param taType trail address data type
   */
  public DispatchEmergitechParser(String[] prefixList, String[] cityList,
                                  String defCity, String defState, TrailAddrType taType) {
    this(prefixList, false, (int[])null, cityList, defCity, defState, 0, taType);
  }

  /**
   * @param prefixList List of possible prefix values that must be found at start of text.  The
   * first is the primary value index offsets will be calculated with.  Any others are considered
   * earlier version that will be replaced with the primary value before the space adjustment is made
   * @param extraSpacePos Single extra blank column<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(String[] prefixList, int extraSpacePos,
                                  String[] cityList, String defCity, String defState) {
    this(prefixList, false, new int[]{extraSpacePos}, cityList, defCity, defState, 0, null);
  }

  /**
   * Primary constructor
   * @param prefixList List of possible prefix values that must be found at start of text.  The
   * first is the primary value index offsets will be calculated with.  Any others are considered
   * earlier version that will be replaced with the primary value before the space adjustment is made
   * @param optUnit True if unit field following prefix is optional
   * @param extraSpacePosList Array of extra blank columns<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   */
  public DispatchEmergitechParser(String[] prefixList, boolean optUnit, int[] extraSpacePosList,
                                  String[] cityList, String defCity, String defState) {
    this(prefixList, optUnit, extraSpacePosList, cityList, defCity, defState, 0, null);
  }

  /**
   * Primary constructor
   * @param prefix Prefix that must be found at beginning of text page
   * @param optUnit True if unit field following prefix is optional
   * @param extraSpacePosList Array of extra blank columns<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param taType trailing address data type
   */
  public DispatchEmergitechParser(String[] prefixList, boolean optUnit, int[] extraSpacePosList,
                                  String[] cityList, String defCity, String defState, TrailAddrType taType) {
    this(prefixList, optUnit, extraSpacePosList, cityList, defCity, defState, 0, taType);
  }

  /**
   * Primary constructor
   * @param prefix Prefix that must be found at beginning of text page
   * @param optUnit True if unit field following prefix is optional
   * @param extraSpacePosList Array of extra blank columns<br/>
   * Positive values of offsets from beginning of message<br/>
   * Negative values are offsets from beginning of address field
   * @param cityList list of cities
   * @param defCity default city
   * @param defState default state
   * @param flags parser flags
   * @param taType trailing address data type
   */
  public DispatchEmergitechParser(String[] prefixList, boolean optUnit, int[] extraSpacePosList,
                                  String[] cityList, String defCity, String defState, int flags, TrailAddrType taType) {
    super(cityList, defCity, defState,
          "( SELECT/3 Call:ID! NATURE:CALL! PLACE:ADDRCITY! AKA:PLACE? COMMENTS:INFO3! INFO3/N+ LAT:GPS1 LONG:GPS2 " +
          "| SELECT/2 CALL:CALL PLACE:PLACE ADDR:ADDR! BETWEEN:X CITY:CITY! ID:ID! DATE:DATE2! TIME:TIME2! INFO:INFO " +
          "| Nature:CALL Location:ADDR/S2! Comments:INFO " +
          "| ( CALL:ID NATURE:CALL | ID NATURE:CALL | NATURE:CALL | CALL ) CALL/SDS+ ( LOCATION:ADDR2! | PLACE:ADDR2! | ) BETWEEN:X? CALL:SKIP? NATURE:SKIP? COMMENTS:INFO INFO/N+ )");
    this.extraSpacePosList = extraSpacePosList;
    this.prefixList = prefixList;
    this.optUnit = optUnit;
    this.noPlace = (flags & EMG_FLG_NO_PLACE) != 0;
    if (taType == null) taType = TrailAddrType.NONE;
    this.special = taType.isSpecial();
    this.trailField = taType.getTrailField();
  }

  /**
   * Add words and names to special list of words that we do not always recognize
   * when they are split by an extraneous blank.  This happens one one of the terms
   * on either side of the split happens to be a recognizable word.  Usually one
   * of the ordinal directions
   * @param words list of words to be added
   */
  protected void addSpecialWords(String ... words) {
    for (String word : words) {
      specialWordSet.add(word);
    }
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("\\[[^\\]]+\\]- *(?:CALL|NATURE|LOCATION)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() > 0) {
      if (SUBJECT_PTN.matcher(subject).matches()) {
        String prefix = checkPrefix(body);
        subject = subject + ':';
        if (prefix != null) {
          subject = prefix + subject;
          if (!body.startsWith(subject)) {
            body = subject + body.substring(prefix.length());
          }
        } else {
          body = subject + body;
        }
      }
      else if (prefixList == null && body.startsWith("-")){
        body = '[' + subject + ']' + body;
      }
    }
    return parseMsg(body, data);
  }

  private static Pattern MISSING_LOC_BLANK_PTN = Pattern.compile("(?<! |^)(?=LOCATION:|PLACE:)", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (prefixList != null) {
      String prefix = checkPrefix(body);
      if (prefix == null) return false;
      body = body.substring(prefix.length()).trim();
    }

    int st = 0;
    Matcher match = UNIT_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strUnit = match.group(1);
      st = match.end();
    } else {
      if (!optUnit) return false;
    }

    // An ADDR: keyword identifies new format
    if (body.contains(" ADDR:")) {
      setSelectValue("2");
      body = body.substring(st).trim();
      int pt = body.indexOf(" BETWEEN ");
      if (pt >= 0) {
        pt += 8;
        body = body.substring(0,pt) + ':' + body.substring(pt);
      }
      return super.parseMsg(body, data);
    }


    // See if this is the new fangled dash delimited format.  Makes things so much easier
    if (body.contains(";PLACE:")) {
      setSelectValue("3");
      return parseFields(body.split(";"), data);
    }

    setSelectValue("1");
    if (body.contains(" - LOCATION:") || body.contains(" - PLACE:") || body.contains(" - NATURE:")) {
      if (body.endsWith("-")) body += ' ';
      int pt = body.indexOf(" BETWEEN ");
      if (pt >= 0) {
        body = body.substring(0,pt) + " - BETWEEN: " + body.substring(pt+9);
      }
      return parseFields(body.substring(st).split(" - "), data);
    }
    String tmp = body.substring(st);
    if (tmp.contains(" - Location:")) {

      // Check for a labeled call ID field
      match = ID_PTN.matcher(tmp);
      if (match.lookingAt()) {
        data.strCallId = match.group(1).trim();
        tmp = tmp.substring(match.end());
      }
      int pt = tmp.indexOf("Nature:");
      if (pt > 0) tmp = tmp.substring(pt);
      if (tmp.endsWith(" -")) tmp = tmp + ' ';
      tmp = HOUSE_DECIMAL_PTN.matcher(tmp).replaceFirst("$1");
      return parseFields(tmp.split(" - "), data);
    }

    // There are usually 2 extraneous blanks.  The first one tends to fall in the
    // address field and we will spend a lot of time trying to excise it.  The
    // second tends to fall in the cross street or comment fields, where an extra
    // blank isn't that critical.  We will, however, try to rebuild a COMMENTS:
    // keyword that has been split
    if (extraSpacePosList != null) {
      body = COMMENTS_PTN.matcher(body).replaceFirst("COMMENTS:");

      // Ditto for BETWEEN
      body = BETWEEN_PTN.matcher(body).replaceFirst("BETWEEN");
    }

    body = body.replace("/COMMENTS:", " COMMENTS:");
    body = body.replace("/LOCATION:", " LOCATION:");
    body = MISSING_LOC_BLANK_PTN.matcher(body).replaceFirst(" ");

    // If extraSpacePos is positive, the extraneous blank is found in a fixed
    // position relative to the message text.  Also check for keywords that
    // might get split with one side looking like a real word
    if (extraSpacePosList != null) {
      for (int extraSpacePos : extraSpacePosList) {
        int oldLen = body.length();
        if (extraSpacePos > 0) {
          body = removeBlank(extraSpacePos, body);
        } else {
          int ndx = body.indexOf(" LOCATION:");
          if (ndx >= 0) {
            ndx += 10;
            while (ndx < body.length() && body.charAt(ndx) == ' ') ndx++;
            body = removeBlank(ndx - extraSpacePos, body);
          }
        }
        if (body.length() != oldLen) break;
      }
    }

    // Strip off any duplicate NATURE: keywords beyond the
    // first COMMENTS: keyword
    int pt = body.indexOf("COMMENTS:");
    if (pt >= 0) {
      pt += 8;
      int pt2 = body.indexOf("NATURE:", pt);
      if (pt2 >= 0) body = body.substring(0,pt2).trim();
    }

    // Carry on with more normal adjustments
    body = body.substring(st).trim().replace(" BETWEEN ", " BETWEEN: ");

    if (!super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) {
      data.strCall = data.strSupp;
      data.strSupp = "";
      if (data.strCall.length() == 0) data.strCall = "ALERT";
    }
    return data.strAddress.length() > 0 || data.strGPSLoc.length() > 0
           || data.strCallId.length() > 0 || data.strSupp.length() > 0;
  }

  private String checkPrefix(String body) {
    if (prefixList == null) return null;
    for (String prefix : prefixList) {
      if (body.startsWith(prefix)) return prefix;
    }
    return null;
  }

  @Override
  public String getProgram() {
    return "UNIT ID? " + super.getProgram() + " CALL";
  }

  /**
   * This method has the unenviable job of remove a extraneous blank that the
   * message service insists on putting in a a fixed position in the string
   * @param pos fixed field position where blank may be inserted
   * @param field field which has the inserted blank
   * @return field with extraneous blank removed
   */
  private String removeBlank(int pos, String field) {

    // If field doesn't extend position, or character in that position is
    // not a blank, we don't have to do anything.
    if (field.length() <= pos || field.charAt(pos) != ' ') return field;

    // Get the words in front of and behind the blank
    int pt = field.lastIndexOf(' ', pos-1);
    if (pt < 0) pt = -1;
    String word1 = field.substring(pt+1,pos);

    pt = field.indexOf(' ', pos+1);
    if (pt < 0) pt = field.length();
    String word2 = field.substring(pos+1,pt);

    // Next we are going to make a number of tests to confirm that the space
    // should or should not be removed
    // But first see if the combined workd is in our special word set.  if it
    // is, we are going to change it and can skip the other checking
    if (!isWord(word1+word2)) {

      // If we did not find it there, see with either the  least or trail word
      // is a recognized dictionary word.  If it is, don't change anything
      if (isWord(word1) || isWord(word2)) return field;

      // if one, but not both, of the words contain only numeric digits
      // don't change anything
      if (NUMERIC.matcher(word1).matches() ^ NUMERIC.matcher(word2).matches()) return field;
    }

    // Otherwise, assume this is an extraneous blank and remove it
    field = field.substring(0,pos) + field.substring(pos+1);
    return field;
  }

  /**
   * Determine if word is a recognized word, meaning it in either our special word list
   * or the smart address dictionary.
   * @param word word to be checked
   * @return true if word is a recognized word
   */
  private boolean isWord(String word) {
    return specialWordSet.contains(word) || isDictionaryWord(word);
  }

  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mmaa");

  @Override
  public Field getField(String name) {
    if (name.equals("DATE2")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME2")) return new TimeField(TIME_FMT, true);
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("ADDR2")) return new BaseAddressField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("INFO3")) return new BaseInfo3Field();
    return super.getField(name);
  }

  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*), *([A-Z]{2})(?: +(\\d{5}))?");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        zip = match.group(3);
      }
      super.parse(field, data);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?\\b");
  private static final Pattern ADDR_BRK_PTN = Pattern.compile(" - |\n");
  protected class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int flags = FLAG_AT_SIGN_ONLY;
      String zip = null;
      if (trailField == null) {
        flags |= FLAG_ANCHOR_END;
        Parser p = new Parser(field);
        String city = p.getLastOptional(',');
        Matcher match = ST_ZIP_PTN.matcher(city);
        if (match.matches()) {
          city = p.getLastOptional(',');
          data.strState = match.group(1);
          zip = match.group(2);
        }
        data.strCity = city;
        if (!data.strCity.isEmpty()) flags |= FLAG_NO_CITY;
        field = p.get();
      }
      flags |= getExtraParseAddressFlags();
      StartType st = noPlace ? StartType.START_ADDR : StartType.START_PLACE;
      parseAddress(st, flags, field, data);
      if (data.strAddress.length() == 0) {
        parseAddress(data.strPlace, data);
        data.strPlace = "";
      }
      if (zip != null && data.strCity.isEmpty()) data.strCity = zip;
      if (trailField != null) {
        String left = getLeft();
        if (left.startsWith("/")) {
          if (data.strCity.length() == 0) {
            data.strAddress = append(data.strAddress, " & ", left.substring(1).trim());
            left = "";
          } else {
            left = left.substring(1).trim();
          }
        }
        if (left.length() == 0 && data.strApt.length() == 0) {
          Matcher match = ADDR_BRK_PTN.matcher(data.strAddress);
          if (match.find()) {
            left = data.strAddress.substring(match.end()).trim();
            data.strAddress = data.strAddress.substring(0,match.start()).trim();
          }
        }
        left = stripFieldStart(left, "-");

        if (special && isLastField()) {
          data.strSupp = left;
        } else {
          trailField.parse(left, data);
        }
      }
    }

    @Override
    public String getFieldNames() {
      String names = "PLACE ADDR APT CITY ST";
      if (trailField != null) names = names + ' ' + trailField.getFieldNames();
      if (special) names += " INFO";
      names += " X?";
      return names;
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "*");

      // If there is no comment field, see if this
      // field contains extra info
      if (isLastField()) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_NO_CITY, field, data);
        String left = getLeft();
        if (left.startsWith("/")) {
          data.strCross = append(data.strCross, " ", left);
        } else {
          data.strSupp = append(data.strSupp, " / ", left);
        }
        left = stripFieldStart(left, "-");
      }

      // Otherwise parse normally
      else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "X INFO";
    }
  }

  private static final Pattern INFO_GPS_PTN1 = Pattern.compile("((?:LON:)?[-+]?\\d{1,3}\\.\\d{5,6}) *(?:LAT:)?([-+]?\\d{1,3}\\.\\d{5,6})(?:CO?F[:=]\\d+%?)?(?:CPF:[A-Z0-9]*)?(?:CALLBK=(\\(\\d{3}\\)\\d{3}-\\d{4}))?|CNF=\\d*UNC=\\d*");
  private static final Pattern INFO_GPS_PTN2 = Pattern.compile("ALT#=([- 0-9]+) X=([-+]?\\d+\\.\\d+) Y=([-+]?\\d+\\.\\d+) (?:AT&T )?[A-Z]+ *");
  private static final Pattern INFO_GPS_PTN3 = Pattern.compile("CF=\\d+%([-+]\\d{1,3}\\.\\d{6,})([-+]\\d{1,3}\\.\\d{6,})CO=[A-Z]+\n");
  private static final Pattern INFO_GPS_PTN4 = Pattern.compile("X([-+]?(?:\\d+\\.\\d+|0))?Y([-+]?(?:\\d+\\.\\d+|0))?(?:CFO?\\d*%?)?(?:(?:[ZU]*ZUNC|UNC\\d*\\.?\\d*)Z?)?");
  private static final Pattern INFO_GPS_PTN5 = Pattern.compile("CPF:[A-Z]+([-+]?\\d{1,3}\\.\\d{5,6})([-+]?\\d{1,3}\\.\\d{5,6})COF:\\d+COP:\\d+(?:CALLBK=(\\(740\\)\\d{3}-\\d{4}))?");
  private static final Pattern INFO_GPS_PTN6 = Pattern.compile("ALT#(\\d{3}-\\d{3}-\\d{4})([-+]\\d{1,3}\\.\\d{5,7})([-+]\\d{1,3}\\.\\d{5,7})");
  private static final Pattern INFO_GPS_PTN7 = Pattern.compile("X=([+-]?[0-9\\.]+)Y=([+-]?[0-9\\.]+)CF=\\d+%UF=\\d+MZ=[0-9\\.]*M");
  private static final Pattern INFO_GPS_PTN8 = Pattern.compile("=([+-]?[0-9\\.]+) Y=([+-]?[0-9\\.]+) F= *\\d+% UF= *\\d*\\b *");
  private static final Pattern INFO_GPS_PTN9 = Pattern.compile("(?:(\\(\\d{3}\\)\\d{3}-\\d{4}))?LAT:([-+]?\\d{2,3}\\.\\d{5,7}|)LON:([-+]?\\d{2,3}\\.\\d{5,7}|)(?:ELV(?::\\d*|))?(?:COF:(?:\\d*|))?(?:COP:(?:0+|\\d{2}|))?\\.?");
  private static final Pattern INFO_GPS_PTN10 = Pattern.compile("(\\(\\d{3}\\)\\d{3}-\\d{4})([-+]\\d{3}\\.\\d{5,6})([-+]\\d{3}\\.\\d{5,6})\\b");
  private static final Pattern INFO_GPS_PTN11 = Pattern.compile("CALLBK=(\\(\\d{3}\\)\\d{3}-\\d{4})([-+]\\d{3}\\.\\d{6})([-+]\\d{3}\\.\\d{6})(?:CF=\\d{1,3}%)?");
  private static final Pattern INFO_GPS_TRUNC = Pattern.compile("ALT#[=(]|CF=|X=CALLBK=");

  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      // What should be a simple pattern check for GPS coordinates gets complicated
      // because random blanks get inserted anywhere.  We solve this by squeezing
      // all blanks out of the field first, then doing our pattern check, then
      // counting how many non-blank characters need to be removed from the front
      // of the field :(
      boolean found = false;
      String compField = field.replace(" ", "");
      Matcher match = INFO_GPS_PTN1.matcher(compField);
      if (match.lookingAt()) {
        found = true;
        if (match.group(1) != null) {
          setGPSLoc(match.group(1)+','+match.group(2), data);
          data.strPhone = getOptGroup(match.group(3));
        }
      }

      else if ((match = INFO_GPS_PTN3.matcher(compField)).lookingAt()) {
        found = true;
        setGPSLoc(match.group(1)+','+match.group(2), data);
      }

      else if ((match = INFO_GPS_PTN4.matcher(compField)).lookingAt()) {
        found = true;
        setGPSLoc(getOptGroup(match.group(1))+','+getOptGroup(match.group(2)), data);
      }

      else if ((match = INFO_GPS_PTN5.matcher(compField)).lookingAt()) {
        found = true;
        setGPSLoc(match.group(1)+','+match.group(2), data);
      }
      else if ((match = INFO_GPS_PTN6.matcher(compField)).lookingAt()) {
        found = true;
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2)+','+match.group(3), data);
      }
      else if ((match = INFO_GPS_PTN7.matcher(compField)).lookingAt()) {
        found = true;
        setGPSLoc(match.group(1)+','+match.group(2), data);
      }
      else if ((match = INFO_GPS_PTN9.matcher(compField)).lookingAt()) {
        found = true;
        data.strPhone = getOptGroup(match.group(1));
        setGPSLoc(match.group(2)+','+match.group(3), data);
      }
      else if ((match = INFO_GPS_PTN10.matcher(compField)).lookingAt()) {
        found = true;
        data.strPhone = getOptGroup(match.group(1));
        setGPSLoc(match.group(2)+','+match.group(3), data);
      }
      else if ((match = INFO_GPS_PTN11.matcher(compField)).lookingAt()) {
        found = true;
        data.strPhone = getOptGroup(match.group(1));
        setGPSLoc(match.group(2)+','+match.group(3), data);
      }

      if (found) {
        int pos = 0;
        for (int ii = 0; ii<match.end(); ii++) {
          while (field.charAt(pos) == ' ') pos++;
          pos++;
        }
        while (pos < field.length() && field.charAt(pos) == ' ') pos++;
        field = field.substring(pos).trim();
      }

      // Alas, version 2 & 8 require space terminators so we can't get a way with the compressed search trick.
      // Thankfully, the extra space logic seems to be fading a way.  Haven't seen it in any recent Emergitech formats
      // and with a bit of luck, we never will.  So we will just try to do without it
      else if ((match = INFO_GPS_PTN2.matcher(field)).lookingAt()) {
        data.strPhone = match.group(1).trim();
        setGPSLoc(match.group(2)+','+match.group(3), data);
        field = field.substring(match.end());
      }
      else if ((match = INFO_GPS_PTN8.matcher(field)).lookingAt()) {
        found = true;
        setGPSLoc(match.group(1)+','+match.group(2), data);
        field = field.substring(match.end());
      }
      else if (INFO_GPS_TRUNC.matcher(field).lookingAt()) {
        return;
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "GPS PHONE INFO";
    }
  }

  private static final Pattern INFO3_HEADER_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - +(.*)");
  private class BaseInfo3Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = INFO3_HEADER_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
}
