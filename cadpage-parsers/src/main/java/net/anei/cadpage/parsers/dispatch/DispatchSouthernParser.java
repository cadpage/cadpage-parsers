package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchSouthernParser extends FieldProgramParser {

  /*
   * This does get complicated....
   * The field sequence can be delimited by commas, semicolons, or
   * blanks.  Generally the parser will work the same field sequence regardless of
   * the field delimiters, though obviously a lot of work has to go into processing
   * alerts with blank field delimiters
   *
   * The field sequence is always the same, fields may be always present, always absent,
   * or optional depending on what flags are passed to the constructor
   *
   * The basic field sequence is
   * Dispatcher ID
   * Address, apt, city and possibly place
   * State
   * one of
   *   misused call field or
   *   misused place field or
   *    cross street
   *    name
   *    phone number
   * code
   * Unit 1
   * Call ID
   * Time
   * Call
   * Info
   *
   * There are two sets of constructor flags.  The old set evolved slowly over time
   * and became increasingly unwieldy.  It is in the process of being replaced by the
   * new constructor flag sequence.  Some flag values are shared between the two sets
   * the presence of an DSFLG_ADDR uniquely identifies a set of new constructor flags
   */


  // New Constructor flags.  With these flags, fields are only present if specifically flagged
  // except for the call description and info fields which are always present.  The different
  // Address place fields needs some further explanation
  // DSFLG_ADDR_LEAD_PLACE place can precede address information
  // DSFLG_ADDR_TRAIL_PLACE place information can follow address field
  // DSFLG_ADDR_TRAIL_PLACE2 place information can follow address field
  // In the non delimited alert format, information following the address
  // will be considered a place information rather than name information
  public static final long DSFLG_DISP_ID =             0x200000000L;
  public static final long DSFLG_OPT_DISP_ID =         0x100000000L;

  public static final long DSFLG_ADDR =                0x80000000L;
  public static final long DSFLG_ADDR_LEAD_PLACE =     0x40000000L;
  public static final long DSFLG_ADDR_TRAIL_PLACE =    0x20000000L;
  public static final long DSFLG_ADDR_TRAIL_PLACE2 =   0x10000000L;
  public static final long DSFLG_ADDR_NO_IMPLIED_APT = 0x08000000L;

  public static final long DSFLG_APT =                 0x04000000L;
  public static final long DSFLG_OPT_APT =             0x02000000L;

//  public static final long DSFLG_STATE =               0x01000000L;
  public static final long DSFLG_OPT_STATE =           0x00800000L;

  public static final long DSFLG_BAD_CALL =            0x00400000L;
//  public static final long DSFLG_OPT_BAD_CALL =        0x00200000L;

  public static final long DSFLG_BAD_PLACE =           0x00100000L;
  public static final long DSFLG_OPT_BAD_PLACE =       0x00080000L;

  public static final long DSFLG_X =                   0x00040000L;
  public static final long DSFLG_OPT_X =               0x00020000L;

  public static final long DSFLG_NAME =                0x00010000L;
  public static final long DSFLG_OPT_NAME =            0x00008000L;

  public static final long DSFLG_PHONE =               0x00004000L;
  public static final long DSFLG_OPT_PHONE =           0x00002000L;

  public static final long DSFLG_CODE =                0x00001000L;
  public static final long DSFLG_OPT_CODE =            0x00000800L;

  public static final long DSFLG_UNIT1 =               0x00000400L;
  public static final long DSFLG_OPT_UNIT1 =           0x00000200L;

  public static final long DSFLG_ID =                  0x00000100L;
  public static final long DSFLG_OPT_ID =              0x00000080L;

  public static final long DSFLG_TIME =                0x00000040L;
  public static final long DSFLG_OPT_TIME =            0x00000020L;

  // Some special cases
  // Process empty delimited fields instead of ignoring them
  // this should be the default, but is not for historical reasons
  public static final long DSFLG_PROC_EMPTY_FLDS =     0x00000002L;

  // In undelimited format, not info fileds, evertyhing goes into call description
  public static final long DSFLG_NO_INFO =             0x00000001L;

  // Old flags are a complicated mess, which is why they are depreciated

  // Flag indicating  a leading dispatch name is required
  public static final long DSFLAG_DISPATCH_ID = 0x01L;

  // Flag indicating a leading dispatch name is optional
  public static final long DSFLAG_OPT_DISPATCH_ID = 0x02L;


  // Flag indicating that the call ID is optional
  public static final long DSFLAG_ID_OPTIONAL = 0x08L;

  // Flag indicating a place name may precede the address
  // And Name/Phone number follows address
  public static final long DSFLAG_LEAD_PLACE = 0x010L;

  // Flag indicating cross street information follows the address instead of
  // the usual name & phone
  public static final long DSFLAG_FOLLOW_CROSS = 0x20L;

  // Flag indicating address will be followed by cross street, and then the usual
  // name & phone
  public static final long DSFLAG_CROSS_NAME_PHONE = 0x40L;

  // Flag indicating there is no name and phone following the address
  public static final long DSFLAG_NO_NAME_PHONE = 0x80L;

  // Flag indicating we should not check for implied non-numeric apartments
  public static final long DSFLAG_NO_IMPLIED_APT = 0x100L;

  // Flag indicating call description follows address
  public static final long DSFLAG_FOLLOW_CALL = 0x200L;

  // Flag indicating optional unit designation precedes call ID
  public static final long DSFLAG_LEAD_UNIT = 0x400L;

  // Flag indicating place name can be in front of and behind the address
  public static final long  DSFLAG_BOTH_PLACE = 0x800L;

  // Flag indicating there is no place name
  public static final long DSFLAG_NO_PLACE = 0x1000L;

  // Flag indicating a place follows the address, even in field delimited mode
  // This is the default behavior in space delimited mode
  public static final long DSFLAG_TRAIL_PLACE = 0x2000L;

  // Flag indicating a state may follow the address
  public static final long DSFLAG_STATE = 0x4000L;

  // Flag indicating time is optional :(
  public static final long DSFLAG_TIME_OPTIONAL = 0x8000L;

  // Flag indicating place field following address is a place field
  public static final long DSFLAG_PLACE_FOLLOWS = 0x10000L;

  // Flag indicating that we will never have a call ID field
  public static final long DSFLAG_NO_ID = 0x20000L;


  private boolean parseFieldOnly;

  private long flags;

  private Pattern callCodePtn = null;
  private Pattern callPtn = null;
  private CodeSet callSet = null;
  private Pattern unitPtn;

  private String defaultFieldList;

  public DispatchSouthernParser(String[] cityList, String defCity, String defState) {
    this(null, cityList, defCity, defState, DSFLAG_DISPATCH_ID, null);
  }

  public DispatchSouthernParser(String[] cityList, String defCity, String defState, long flags) {
    this(null, cityList, defCity, defState, flags, null);
  }

  public DispatchSouthernParser(String[] cityList, String defCity, String defState, long flags, String unitPtnStr) {
    this(null, cityList, defCity, defState, flags, unitPtnStr);
  }

  public DispatchSouthernParser(CodeSet callSet, String[] cityList, String defCity, String defState, long flags) {
    this(callSet, cityList, defCity, defState, flags, null);
  }

  public DispatchSouthernParser(CodeSet callSet, String[] cityList, String defCity, String defState, long flags, String unitPtnStr) {
    super(cityList, defCity, defState, "");
    this.parseFieldOnly = false;
    setupCallList(callSet);

    this.flags = convertFlags(flags);

    this.unitPtn = (unitPtnStr == null ? null : Pattern.compile(unitPtnStr));

    // Program string needs to be built at run time
    StringBuilder sb = new StringBuilder();
    sb.append("ADDR/S2");
    if (!chkFlag(DSFLG_ADDR_NO_IMPLIED_APT)) sb.append("6");
    sb.append(chkFlag(DSFLG_ADDR_LEAD_PLACE) ? 'P' : 'X');
    if (chkFlag(DSFLG_ADDR_TRAIL_PLACE|DSFLG_ADDR_TRAIL_PLACE2)) sb.append("P");
    appendTerm(sb, "APT", DSFLG_APT, DSFLG_OPT_APT);
    if (chkFlag(DSFLG_OPT_STATE)) sb.append(" ST?");
    if (chkFlag(DSFLG_BAD_CALL)) sb.append(" CALL!");
    else if (chkFlag(DSFLG_BAD_PLACE | DSFLG_OPT_BAD_PLACE)) {
      appendTerm(sb, "PLACE", DSFLG_BAD_PLACE, DSFLG_OPT_BAD_PLACE);
    }

    appendTerm(sb, "X", DSFLG_X, DSFLG_OPT_X);

    if (chkFlag(DSFLG_NAME)) sb.append(" NAME NAME+?");
    else if (chkFlag(DSFLG_OPT_NAME)) sb.append(" NAME+?");

    appendTerm(sb, "PHONE", DSFLG_PHONE, DSFLG_OPT_PHONE);

    if (chkFlag(DSFLG_CODE)) sb.append(" CODE! CODE+? PARTCODE?");
    else if (chkFlag(DSFLG_OPT_CODE)) {
      if (chkFlag(DSFLG_OPT_BAD_PLACE)) {
        sb.append(" CODE?");
      } else {
        sb.append(" CODE+? PARTCODE?");
      }
    }

    appendTerm(sb, "UNIT", DSFLG_UNIT1, DSFLG_OPT_UNIT1);
    appendTerm(sb, "ID", DSFLG_ID, DSFLG_OPT_ID);
    appendTerm(sb, "TIME", DSFLG_TIME,  DSFLG_OPT_TIME);
    sb.append(" INFO+ OCA:ID2");
    String program = sb.toString();
    program = fixProgram(program);
//    System.out.println(program);
    setProgram(program, 0);

    // For non-delimited alerts, build a list of field terms
    sb = new StringBuilder();
    if (chkFlag(DSFLG_ADDR_LEAD_PLACE)) sb.append("PLACE ");
    sb.append("ADDR X? APT CITY");
    if (chkFlag(DSFLG_ADDR_TRAIL_PLACE | DSFLG_ADDR_TRAIL_PLACE2 | DSFLG_BAD_PLACE | DSFLG_OPT_BAD_PLACE)) sb.append(" PLACE");
    if (chkFlag(DSFLG_BAD_CALL)) sb.append(" CALL");
    if (chkFlag(DSFLG_X | DSFLG_OPT_X)) sb.append(" X");
    if (chkFlag(DSFLG_NAME | DSFLG_OPT_NAME)) sb.append(" NAME");
    if (chkFlag(DSFLG_PHONE | DSFLG_OPT_PHONE)) sb.append(" PHONE");
    if (chkFlag(DSFLG_UNIT1 | DSFLG_OPT_UNIT1)) sb.append(" UNIT");
    if (chkFlag(DSFLG_CODE | DSFLG_OPT_CODE)) sb.append(" CODE");
    if (chkFlag(DSFLG_ID | DSFLG_OPT_ID)) sb.append(" ID");
    if (chkFlag(DSFLG_TIME | DSFLG_OPT_TIME)) sb.append(" TIME");
    sb.append(" CODE CALL PRI INFO ID");
    defaultFieldList = sb.toString();
  }

  /**
   * Convert old deprecated flags to new flags
   * @param flags flags to be converted
   * @return properly converted flags
   */
  private static long convertFlags(long flags) {

    // If new constructor flags, nothing needs to be done
    if ((flags & DSFLG_ADDR) != 0) return flags;

    // Otherwise, we have to go to work
    int nflgs = 0;
    if (chkFlag(flags, DSFLAG_DISPATCH_ID)) nflgs |= DSFLG_DISP_ID;
    if (chkFlag(flags, DSFLAG_OPT_DISPATCH_ID)) nflgs |= DSFLG_OPT_DISP_ID;

    nflgs |= DSFLG_ADDR;
    if (chkFlag(flags, DSFLAG_LEAD_PLACE)) nflgs |= DSFLG_ADDR_LEAD_PLACE;
    if (chkFlag(flags, DSFLAG_TRAIL_PLACE)) nflgs |= DSFLG_ADDR_TRAIL_PLACE;
    if (chkFlag(flags, DSFLAG_BOTH_PLACE)) nflgs |= DSFLG_ADDR_LEAD_PLACE|DSFLG_ADDR_TRAIL_PLACE;
    if (!chkFlag(flags, (DSFLAG_LEAD_PLACE|DSFLAG_TRAIL_PLACE|DSFLAG_NO_PLACE|DSFLAG_PLACE_FOLLOWS|DSFLAG_FOLLOW_CROSS|DSFLAG_CROSS_NAME_PHONE))) nflgs |= DSFLG_ADDR_TRAIL_PLACE;
    if (chkFlag(flags, DSFLAG_NO_IMPLIED_APT)) nflgs |= DSFLG_ADDR_NO_IMPLIED_APT;

    if (chkFlag(flags, DSFLAG_STATE)) nflgs |= DSFLG_OPT_STATE;

    if (chkFlag(flags, DSFLAG_FOLLOW_CALL)) nflgs |= DSFLG_BAD_CALL;
    if (chkFlag(flags, DSFLAG_PLACE_FOLLOWS)) nflgs |= DSFLG_OPT_BAD_PLACE;

    if (chkFlag(flags, DSFLAG_FOLLOW_CROSS)) {
      nflgs |= DSFLG_OPT_X;
    } else if (chkFlag(flags, DSFLAG_CROSS_NAME_PHONE)) {
      nflgs |= DSFLG_OPT_X|DSFLG_OPT_NAME|DSFLG_OPT_PHONE;
    } else if (!chkFlag(flags, DSFLAG_NO_NAME_PHONE)) {
      nflgs |= DSFLG_OPT_NAME|DSFLG_OPT_PHONE;
    }

    nflgs |= DSFLG_OPT_CODE;

    if (chkFlag(flags, DSFLAG_LEAD_UNIT)) nflgs |= DSFLG_OPT_UNIT1;

    if (!chkFlag(flags, DSFLAG_NO_ID)) {
      nflgs |= (chkFlag(flags, DSFLAG_ID_OPTIONAL)) ? DSFLG_OPT_ID : DSFLG_ID;
    }

    nflgs |= (chkFlag(flags, DSFLAG_TIME_OPTIONAL)) ? DSFLG_OPT_TIME : DSFLG_TIME;

    return nflgs;
  }

  private void appendTerm(StringBuilder sb, String term, long reqFlag, long optFlag) {
    if (chkFlag(reqFlag | optFlag)) {
      sb.append(' ');
      sb.append(term);
      sb.append(chkFlag(optFlag) ? '?' : '!');
    }
  }

  private boolean chkFlag(long mask) {
    return chkFlag(flags, mask);
  }

  private static boolean chkFlag(long flags, long mask) {
    return ((flags & mask) != 0);
  }

  private static final Pattern OPT_PLACE_PTN = Pattern.compile(" PLACE\\? ((?:[A-Z]+\\? )+[A-Z]+!)");

  /**
   * Fix some basic constructs in constructed program string.
   * @param program  Constructed program string
   * @return fixed program string
   */
  private static String fixProgram(String program) {

    // An optional place name followed by an optional field does not work.
    // But if that is followed by a required field, we can use that required field
    // to identify when a place field exists.
    Matcher match = OPT_PLACE_PTN.matcher(program);
    if (match.find()) {

      // fldList contains the string of optional fields following the PLACE? field, plus
      // a trailing required field.  That sequence needs to be turned into one big
      // conditional branch statement
      String fldList = match.group(1);

      // Start building the conditional branch statement
      StringBuilder sb = new StringBuilder(program.substring(0,match.start()));
      String connect = " ( ";
      int spt = 0;

      // Looping through each term in fldList
      do {

        // For each term, we build a tList list which consists of
        // 1) the current term from the field list, stripped of
        //    the trailing question mark
        // 2) All of the following terms from the field list
        int ept = fldList.indexOf(' ', spt);
        String tList;
        if (ept < 0) {
          ept = fldList.length();
          tList = fldList.substring(spt);
        } else {
          tList = fldList.substring(spt, ept-1) + fldList.substring(ept);
        }

        // Bumpt spt to the next field term
        spt = ept + 1;

        // Next we add two branches to are conditional branch statement
        // The first consist of the constructed tList
        // The second consists of a PLACE term followed by the constructted tList
        sb.append(connect);
        connect = " | ";

        sb.append(tList);
        sb.append(connect);
        sb.append("PLACE ");
        sb.append(tList);
      } while (spt < fldList.length());

      // Finish off by closing the conditional branch construct and appendig the
      // rest of the additional statement
      sb.append(" )");
      sb.append(program.substring(match.end()));
      program = sb.toString();
    }

    // Fix PHONE - ID combination
    program = program.replace(" PHONE? ID! ", " ( PHONE/Z ID! | ID! ) ");
    return program;
  }

  public DispatchSouthernParser(String[] cityList, String defState, String defCity, String program) {
    super(cityList, defState, defCity, program);
    this.parseFieldOnly = true;
  }

  @Override
  protected void setupCallList(CodeSet callSet) {
    this.callSet = callSet;
    super.setupCallList(callSet);
  }

  protected void setCallCodePtn(String callCodePtn) {
    setCallCodePtn(callCodePtn == null ? null : Pattern.compile(callCodePtn));
  }

  protected void setCallCodePtn(Pattern callCodePtn) {
    this.callCodePtn = callCodePtn;
  }

  protected void setCallPtn(String callPtn) {
    setCallPtn(callPtn == null ? null : Pattern.compile(callPtn));
  }

  protected void setCallPtn(Pattern callPtn) {
    this.callPtn = callPtn;
  }

  private static final Pattern RUN_REPORT_PTN1 = Pattern.compile("(?:[A-Z\\.]+: *)?(\\d{8,10}|[A-Z]?\\d{2}-\\d+|\\d{4}-\\d{5,7}|\\d{4}-\\d{2}-\\d{5})(?: ([^;]+))?[ ;,] *([- _A-Z0-9]+)\\(.*\\)\\d\\d:\\d\\d:\\d\\d([\\| ])");
  private static final Pattern RUN_REPORT_DELIM_PTN = Pattern.compile("(?<=\\d\\d:\\d\\d:\\d\\d) ");
  private static final Pattern RUN_REPORT_PTN2 = Pattern.compile("(?:[A-Z]+:)?CFS: *(\\S+?)[;, ](?:([^;]+)[;, ])?(?: [;, ])? *Unit: *([^,;]+?)[;, ] *(Status:.*?)[;,]?(?: Note: *(.*))?");
  private static final Pattern RUN_REPORT_PTN3 = Pattern.compile("CFS Closed:;(\\d{4}-\\d{6});(\\d\\d:\\d\\d:\\d\\d);(.*)");
  private static final Pattern LEAD_PTN = Pattern.compile("^[\\w\\.@]+:");
  private static final Pattern NAKED_TIME_PTN = Pattern.compile("([ ,;]) *(\\d\\d:\\d\\d:\\d\\d)(?:\\1|$)");
  private static final Pattern OCA_TRAIL_PTN = Pattern.compile("\\bOCA: *([-A-Z0-9]+)$");
  private static final Pattern ID_PTN = Pattern.compile("\\b\\d{2,4}-?(?:\\d\\d-)?\\d{4,8}$");
  private static final Pattern PHONE_PTN = Pattern.compile("\\b\\d{10}\\b");
  private static final Pattern EXTRA_CROSS_PTN = Pattern.compile("(?:AND +|[/&] *|X +)(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern CALL_BRK_PTN = Pattern.compile(" +/+ *");
  private static final Pattern VERIFY_X_PTN = Pattern.compile(" *\\(Verify\\) *");

  @Override
  protected boolean parseMsg(String body, Data data) {

    Matcher match = RUN_REPORT_PTN1.matcher(body);
    if (match.lookingAt()) {
      setFieldList("ID CALL INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strCall = getOptGroup(match.group(2));
      String info = body.substring(match.start(3)).trim();
      if (match.group(4).equals("|")) {
        info = info.replace('|', '\n').trim();
      } else {
        info = RUN_REPORT_DELIM_PTN.matcher(info).replaceAll("\n");
      }
      data.strSupp = info;
      return true;
    }

    match = RUN_REPORT_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("ID CALL UNIT INFO");
      data.msgType= MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strCall = getOptGroup(match.group(2));
      data.strUnit = match.group(3);
      data.strSupp = match.group(4).replaceAll(", +", "\n");
      data.strSupp = append(data.strSupp, "\n", getOptGroup(match.group(5)));
      return true;
    }

    match = RUN_REPORT_PTN3.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2) + ' ' + match.group(3).replace(';', '\n');
      return true;
    }

    // Message must always start with dispatcher ID, which we promptly discard
    if (chkFlag(DSFLG_DISP_ID | DSFLG_OPT_DISP_ID)) {
      match = LEAD_PTN.matcher(body);
      if (match.find()) {
        body = body.substring(match.end()).trim();
      } else if (!chkFlag(DSFLG_OPT_DISP_ID)) return false;
    }

    boolean procEmptyFields = chkFlag(DSFLG_PROC_EMPTY_FLDS);

    if (parseFieldOnly || !chkFlag(DSFLG_TIME | DSFLG_OPT_TIME)) {
      if (!parseDelimitedFields(procEmptyFields || parseFieldOnly, body, data)) return false;
      if (!parseFieldOnly) {
        if (data.strCallId.length() == 0 && data.strTime.length() == 0 && data.strCode.length() == 0 && data.strGPSLoc.length() == 0) return false;
      }
    }

    else {

      // See if this looks like one of the new comma delimited page formats
      // If it is, let FieldProgramParser handle it.
      match = NAKED_TIME_PTN.matcher(body);
      if (!match.find()) {
        if (!chkFlag(DSFLG_OPT_TIME)) return false;
        if (!parseDelimitedFields(procEmptyFields, body, data)) return false;
        if (data.strCallId.length() == 0 && data.strTime.length() == 0 && data.strCode.length() == 0 && data.strGPSLoc.length() == 0) return false;
      } else {
        String delim = match.group(1);
        if (delim.charAt(0) != ' ') {
          body = body.replace(" OCA:", delim + "OCA:");
          if (!procEmptyFields) delim += '+';
          if (!parseFields(body.split(delim), data)) return false;
        }

        // Blank delimited fields get complicated
        // We already found a time field.  Use that to split the message
        // into and address and extra versions
        else {
          setFieldList(defaultFieldList);
          data.strTime = match.group(2);
          String sAddr = body.substring(0,match.start()).trim();
          String sExtra = body.substring(match.end()).trim();

          // See if there is an ID field immediate in front of the time field
          if (chkFlag(DSFLG_ID | DSFLG_OPT_ID)) {
            match = ID_PTN.matcher(sAddr);
            if (match.find()) {
              data.strCallId = match.group();
              sAddr = sAddr.substring(0,match.start()).trim();
            } else if (chkFlag(DSFLG_ID)) return false;
          }

          // See if there is a labeled OCA field at the end of the extra block
          match = OCA_TRAIL_PTN.matcher(sExtra);
          if (match.find()) {
            data.strCallId = match.group(1).trim();
            sExtra = sExtra.substring(0,match.start()).trim();
          }

          if (sAddr.length() > 0) {
            parseMain(sAddr, data);
            parseExtra(sExtra, data);
          } else {
            parseExtra2(sExtra, data);
          }
        }
      }
    }

    // set an call description if we do not have one
    if (data.strCall.length() == 0 && data.strSupp.length() == 0) data.strCall= "ALERT";

    // Remove any asterisks or verify markers from cross street info
    data.strCross = data.strCross.replace("*", "");
    data.strCross = VERIFY_X_PTN.matcher(data.strCross).replaceAll(" ").trim();

    // Apparently there is no way to not enter a street number, entering 0 or 1 is the accepted
    // workaround.
    if (data.strAddress.startsWith("1 ") || data.strAddress.startsWith("0 ")) {
      data.strAddress = data.strAddress.substring(2).trim();
      String cross = data.strCross;
      if (!cross.contains("/") && !cross.contains("&")) {
        data.strAddress = append(data.strAddress, " & ", cross);
        data.strCross = "";
      }
    }

    //  Occasional implied intersections end up in the apt field
    if (data.strApt.length() > 0) {
      int status = checkAddress(data.strApt);
      if (status == STATUS_STREET_NAME) {
        data.strAddress = append(data.strAddress, " & ", data.strApt);
        data.strApt = "";
      } else if (status == STATUS_INTERSECTION) {
        data.strCross = append(data.strApt, " / ", data.strCross);
        data.strApt = "";
      }
    }

    return true;
  }

  private boolean parseDelimitedFields(boolean singleDelim, String body, Data data) {
    String ocaField = null;
    int pt = body.lastIndexOf(" OCA:");
    if (pt >= 0) {
      ocaField = body.substring(pt+1);
      body = body.substring(0,pt);
    }
    String[] flds = body.split(singleDelim ? ";" : ";+");
    String[] flds2 = body.split(singleDelim ? "," : ",+");
    if (flds2.length > flds.length) flds = flds2;
    if (ocaField != null) {
      flds2 = flds;
      flds = new String[flds2.length+1];
      System.arraycopy(flds2, 0, flds, 0, flds2.length);
      flds[flds2.length] = ocaField;
    }
    return parseFields(flds, data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " CALL";
  }

  private static final Pattern MM_MARK_PTN = Pattern.compile("MM(?: +\\d+)?\\b *");

  protected void parseMain(String sAddr, Data data) {
    // First half contains address, optional place/name, and possibly an MDL call code
    Parser p = new Parser(sAddr);
    data.strCode = p.getLastOptional(" MDL ");
    if (data.strCode.length() == 0) data.strCode = p.getLastOptional(" FDL ");
    if (data.strCode.length() == 0) data.strCode = p.getLastOptional(" LDL ");
    sAddr = p.get();
    StartType st = (chkFlag(DSFLG_ADDR_LEAD_PLACE) ? StartType.START_PLACE : StartType.START_ADDR);
    int flags = FLAG_AT_SIGN_ONLY;
    if (!chkFlag(DSFLG_ADDR_NO_IMPLIED_APT)) flags |= FLAG_RECHECK_APT;
    if (chkFlag(DSFLG_X | DSFLG_OPT_X)) flags |= FLAG_CROSS_FOLLOWS;
    if (!chkFlag(DSFLG_ADDR_TRAIL_PLACE | DSFLG_ADDR_TRAIL_PLACE2 | DSFLG_X | DSFLG_OPT_X | DSFLG_NAME | DSFLG_OPT_NAME | DSFLG_PHONE | DSFLG_OPT_PHONE)) flags |= FLAG_ANCHOR_END;
    boolean leadOne = sAddr.startsWith("1 ");
    if (leadOne) {
      sAddr = sAddr.substring(2).trim();
      st = StartType.START_ADDR;
    }
    sAddr = sAddr.replace('@', '/');
    flags |= getExtraParseAddressFlags();
    parseAddress(st, flags, sAddr, data);
    if (leadOne) data.strAddress = append("1", " ", data.strAddress);
    String sLeft = getLeft();

    Matcher match = MM_MARK_PTN.matcher(sLeft);
    if (match.lookingAt()) {
      data.strAddress = append(data.strAddress, " ", match.group());
      sLeft = sLeft.substring(match.end());
    }

    // If everything went to place, move it back to address
    if (st == StartType.START_PLACE && data.strAddress.length() == 0) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }

    // Processing what is left gets complicated
    // First strip anything that looks like a trailing phone number
    if (chkFlag(DSFLG_PHONE | DSFLG_OPT_PHONE)) {
      match = PHONE_PTN.matcher(sLeft);
      if (match.find()) {
        data.strPhone = match.group();
        sLeft = sLeft.substring(0,match.start()).trim();
      }
    }

    // If this is a (misused) call description, make it so
    if (chkFlag(DSFLG_BAD_CALL)) {
      data.strCall = stripCallCode(sLeft, data);
      return;
    }

    // Ditto for a misused place name
    if (chkFlag(DSFLG_BAD_PLACE | DSFLG_OPT_BAD_PLACE)) {
      data.strPlace = sLeft;
      return;
    }

    // if cross street information follows the address, process that
    if (chkFlag(DSFLG_X | DSFLG_OPT_X) && !chkFlag(DSFLG_NAME | DSFLG_OPT_NAME)) {
      match = EXTRA_CROSS_PTN.matcher(sLeft);
      if (match.matches()) {
        sLeft = match.group(1).trim();
        if (data.strCity.length() == 0) {
          parseAddress(sLeft, data);
          return;
        }
      }
      sLeft = stripLeadPlace(sLeft, data, false);
      sLeft = stripFieldStart(sLeft, "AT ");
      sLeft = sLeft.replace(" X ", " / ");
      sLeft = stripFieldEnd(sLeft, " X");
      data.strCross = append(data.strCross, " & ", sLeft);
      return;
    }

    if (chkFlag(DSFLG_X | DSFLG_OPT_X)) {
      if (sLeft.startsWith("X ")) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, sLeft.substring(2).trim(), data);
        data.strName = cleanWirelessCarrier(getLeft());
        sLeft = null;
      } else {
        sLeft = stripLeadPlace(sLeft, data, true);
        int pt = sLeft.indexOf(" X ");
        if (pt >= 0) {
          String save = sLeft.substring(0,pt).trim();
          sLeft = sLeft.substring(pt+3).trim();
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, sLeft, data);
          data.strCross = save + " / " + data.strCross;
          data.strName = cleanWirelessCarrier(getLeft());
          sLeft = null;
        }
      }
    }

    // Otherwise, if the place name isn't located in front of the address
    // assume whatever follows it is a place name
    if (sLeft != null) {
      if (!chkFlag(DSFLG_NAME|DSFLG_OPT_NAME) || chkFlag(DSFLG_ADDR_TRAIL_PLACE2)) {
        if (sLeft.startsWith("/")) {
          sLeft = sLeft.substring(1).trim();
          if (data.strCity.length() == 0 && isCity(sLeft)) {
            data.strCity = sLeft;
          } else {
            data.strAddress = data.strAddress + " & " + sLeft;
          }
        } else {
          data.strPlace = append(data.strPlace, " - ", sLeft);
        }
      }

      // Otherwise assume it is a name followed by an optional phone number
      else {
        data.strName = cleanWirelessCarrier(sLeft);
      }
    }
  }

  private String stripCallCode(String field, Data data) {
    if (callCodePtn != null) {
      Parser p = new Parser(field);
      String code = p.get(' ');
      if (callCodePtn.matcher(code).matches()) {
        if (data.strCode.length() == 0) data.strCode = code;
        field = p.get();
      }
    }
    return field;
  }

  private String stripLeadPlace(String field, Data data, boolean trailName) {

    if (!chkFlag(DSFLG_ADDR_TRAIL_PLACE|DSFLG_ADDR_TRAIL_PLACE2)) return field;

    if (field.length() == 0 || Character.isDigit(field.charAt(0))) return field;

    boolean anchorEnd = !trailName;
    int pt = field.indexOf(" X ");
    if (pt < 0) pt = field.indexOf('/');
    boolean crossMark = (pt >= 0);
    if (crossMark) {
      anchorEnd = true;
    } else {
      pt = field.length();
    }

    String part = field.substring(0,pt).trim();
    pt = part.indexOf('@');
    if (pt < 0) pt = part.indexOf('*');
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", field.substring(0,pt).trim());
      return field.substring(pt+1).trim();
    }

    int flags = FLAG_ONLY_CROSS | FLAG_IGNORE_AT;
    if (anchorEnd) flags |= FLAG_ANCHOR_END;
    Result res = parseAddress(StartType.START_PLACE, flags, part);
    if (res.isValid()) {
      Data tmpData = new Data(this);
      res.getData(tmpData);
      data.strPlace = append(data.strPlace, " - ", tmpData.strPlace);
      field = field.substring(tmpData.strPlace.length()).trim();
      return field;
    }

    if (field.startsWith("X ") || field.startsWith("/")) {
      return field.substring(1).trim();
    }

    if (crossMark || trailName) return field;

    data.strPlace = append(data.strPlace, " - ", field);
    return "";
  }

  private static final Pattern CALL_ALERT_INFO_PTN = Pattern.compile("(.*?) AL:(\\d)\\b *(.*)");
  private static final Pattern CALL_PTN = Pattern.compile("^([A-Z0-9\\- /()]+)\\b[ \\.,-]*");

  protected void parseExtra(String sExtra, Data data) {

    if (chkFlag(DSFLG_BAD_CALL)) {
      data.strSupp = sExtra;
      return;
    }

    sExtra = stripCallCode(sExtra, data);

    if (chkFlag(DSFLG_NO_INFO)) {
      data.strCall = sExtra;
      return;
    }

    if (callSet != null) {
      String call = callSet.getCode(sExtra, true);
      if (call != null) {
        data.strCall = call;
        data.strSupp = stripFieldStart(sExtra.substring(call.length()).trim(), "/");
        return;
      }
    }

    if (callPtn != null) {
      Parser p = new Parser(sExtra);
      String call = p.get(' ');
      Matcher match = callPtn.matcher(call);
      if (match.matches()) {
        data.strCall = call;
        data.strSupp = p.get();
        return;
      }
    }

    Matcher match = CALL_ALERT_INFO_PTN.matcher(sExtra);
    if (match.matches()) {
      data.strCall = match.group(1).trim();
      data.strPriority= match.group(2);
      data.strSupp = match.group(3);
      return;
    }

    match = CALL_PTN.matcher(sExtra);
    if (match.find() && match.end() > 0 && match.end() < sExtra.length()) {
      String sCall = match.group(1).trim();
      if (sCall.length() <= 30) {
        sExtra = sExtra.substring(match.end()).trim();
        if (sExtra.startsWith("y")) {
          int pt = sCall.length();
          while (pt > 0 && Character.isDigit(sCall.charAt(pt-1))) pt--;
          if (pt > 0 && pt < sCall.length()) {
            sExtra = sCall.substring(pt) + ' ' + sExtra;
            sCall = sCall.substring(0,pt).trim();
          }
        }
        data.strCall = sCall;
        data.strSupp = sExtra;
        return;
      }
    }

    if (sExtra.length() <= 30) {
      data.strCall = sExtra;
      return;
    }

    match = CALL_BRK_PTN.matcher(sExtra);
    if (match.find() && match.start() <= 30) {
      data.strCall = sExtra.substring(0,match.start()).trim();
      data.strSupp = sExtra.substring(match.end()).trim();
      return;
    }

    data.strSupp = sExtra;
  }

  protected void parseExtra2(String sExtra, Data data) {
    // First half contains address, optional place/name, and possibly an MDL call code
    Parser p = new Parser(sExtra);
    data.strCode = p.getLastOptional(" MDL ");
    if (data.strCode.length() == 0) data.strCode =p.getLastOptional(" FDL ");
    sExtra = p.get();
    parseAddress(StartType.START_CALL_PLACE, FLAG_AT_SIGN_ONLY | FLAG_RECHECK_APT, sExtra, data);
    data.strSupp = getLeft();
  }

  //  Classes for handling the new comma delimited format

  private class BaseCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(" X ") && !field.startsWith("X ") && !field.endsWith(" X")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace(" X ", " / ");
      field = stripFieldStart(field, "X ");
      field = stripFieldEnd(field, " X");
      field = stripFieldStart(field, "*");
      field = field.replace("/ *", "/ ");
      super.parse(field, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("CODE"))  return new BaseCodeField();
    if (name.equals("PARTCODE")) return new SkipField("[MFL]D?");
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("ID")) return new IdField("[A-Z]?\\d\\d(?:\\d\\d)?-?\\d{4,8}|\\d{8}-\\d{5}|\\d{4}-\\d{2}-\\d{5}|\\d{7}-\\d{2}", true);
    if (name.equals("NAME")) return new BaseNameField();
    if (name.equals("PHONE")) return new PhoneField("\\d{10}");
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("ID2")) return new IdField("\\d{6}-\\d{2,4}");
    return super.getField(name);
  }

  private static final Pattern ADDR_CNCT_PTN = Pattern.compile("(?:[/&]|AND\\b) *(.*)", Pattern.CASE_INSENSITIVE);
  protected class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      if (field.startsWith("1 ")) {
        field = field.substring(2).trim();
        int flags = FLAG_AT_SIGN_ONLY;
        if (!chkFlag(DSFLG_ADDR_TRAIL_PLACE|DSFLG_ADDR_TRAIL_PLACE2)) flags |= FLAG_ANCHOR_END;
        if (!chkFlag(DSFLG_ADDR_NO_IMPLIED_APT)) flags |= FLAG_RECHECK_APT;
        flags |= getExtraParseAddressFlags();
        parseAddress(StartType.START_ADDR, flags, field, data);
        if (chkFlag(DSFLG_ADDR_TRAIL_PLACE|DSFLG_ADDR_TRAIL_PLACE2)) {
          String left = getLeft();
          Matcher match = ADDR_CNCT_PTN.matcher(left);
          if (match.matches()) {
            data.strAddress = append(data.strAddress, " & ", match.group(1));
          } else {
            data.strPlace = append(data.strPlace, " - ", left);
          }
        }
        data.strAddress = append("1", " ", data.strAddress);
      } else {
        super.parse(field, data);

        // If we pulled a place name from the front fo the string, see if
        // it looks like a legitimate address that got mangled by something
        // further back
        if (data.strPlace.length() > 0 && field.startsWith(data.strPlace)) {
          if (data.strPlace.endsWith("/")) {
            String place = data.strPlace.substring(0,data.strPlace.length()-1).trim().replace('/', '&');
            data.strAddress = append(place, " & ", data.strAddress);
            data.strPlace = "";
          } else {
            int flags = FLAG_CHECK_STATUS | FLAG_AT_SIGN_ONLY | FLAG_ANCHOR_END | getExtraParseAddressFlags();
            if (!chkFlag(DSFLG_ADDR_NO_IMPLIED_APT)) flags |= FLAG_RECHECK_APT;
            Result res = parseAddress(StartType.START_ADDR, flags, field);
            if (res.isValid() && res.getStatus() >= getStatus()) {
              data.strPlace = data.strAddress = data.strApt = data.strCity = "";
              res.getData(data);
            }
          }
        }
      }
    }
  }

  // Name field continues until it finds a phone number, call number, or time
  private static final Pattern NOT_NAME_PTN = Pattern.compile("|\\d{10}|\\d\\d(?:\\d\\d)?-?\\d{5,8}|\\d\\d:\\d\\d:\\d\\d|[FML]DL.*");
  class BaseNameField extends NameField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (NOT_NAME_PTN.matcher(field).matches()) return false;
      if (chkFlag(DSFLG_UNIT1) && NOT_NAME_PTN.matcher(getRelativeField(+1)).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      data.strName = append(data.strName, ", ", cleanWirelessCarrier(field));
    }
  }

  private class BaseCodeField extends CodeField {
    public BaseCodeField() {
      super("[FML]DL *(.*)|", true);
    }

    @Override
    public void parse(String field, Data data) {
      if (data.strCode.length() == 0) super.parse(field, data);
    }
  }

  private class BaseUnitField extends UnitField {
    public BaseUnitField() {
      setPattern(unitPtn, true);
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("(?:geo:|https://maps.google.com/\\?q=|https://www.ssmap.link/cad\\?)(.*?)(?:&.*)?");
  private static final Pattern INFO_PRI_PTN = Pattern.compile("(?:AL:|P)?(\\d)");
  protected class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1), data);
        return;
      }

      if (data.strCall.isEmpty()) {
        data.strCall = stripCallCode(field, data);
        return;
      }

      if (data.strSupp.isEmpty() && data.strPriority.isEmpty() &&
          (match = INFO_PRI_PTN.matcher(field)).matches()) {
        data.strPriority = match.group(1);
        return;
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "UNIT CODE CALL PRI GPS INFO";
    }
  }
}
