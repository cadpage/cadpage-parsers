package net.anei.cadpage.parsers.dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * This class parses messages from OSSI CAD software
 *
 * Format always starts with "CAD:" and generally consists of a series of
 * semicolon delimited fields which will be processed according to a program
 * passed from the subclass.
 *
 * There are, of course, some exception
 * The text may contain a leading numeric call ID and a colon before the CAD:
 * marker.  Subclasses will indicate this by inserting an ID: term as the
 * first field term in the program
 *
 * The text may contain some text in square brackets.  This comes in two flavors
 *
 * [mm/dd/yy hh:mm:ss xxxxxx] appears to be an subsequent update marker, it will
 * be treated as field delimiter
 *
 * [Medical Priority Info] or [Fire Priority Info]
 * The field that follows this may one of the two following formats
 * RESPONSE: xxxxxx RESPONDER SCRIPT: xxxxxx
 * PROBLEM: xxxxx # PATS: n AGE:  SEX:  xxxxxxx
 *
 * In this case we will skip all text up to and including the keyword
 * "RESPONDER SCRIPT: or "PROBLEM:"
 *
 * In addition, the last field in the string may be a date/time indicator
 * or a truncated piece of a date/time indicator.  If this is the case
 * drop it from further consideration
 **/

public class DispatchOSSIParser extends FieldProgramParser {

  private static final Pattern LEAD_ID_PTN = Pattern.compile("^(\\d+):");
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{2,4}) (\\d\\d:\\d\\d:\\d\\d)\\b");

  private boolean leadID = false;
  private boolean optLeadId = false;
  private boolean dateTime = false;
  private boolean dateTimeReq = false;

  // Pattern searching for a leading square bracket or semicolon
  private Pattern delimPattern = Pattern.compile("\\[|;");

  // Pattern searching for "PROBLEM: or "RESPONDER SCRIPT:"
  private static final Pattern KEYWORD = Pattern.compile("\\b(?:PROBLEM:|RESPONDER SCRIPT:)");

  public DispatchOSSIParser(String defCity, String defState, String program) {
    super(defCity, defState, "SKIP");
    setup(program);
  }

  public DispatchOSSIParser(Properties cityCodes, String defCity, String defState, String program) {
    super(cityCodes, defCity, defState, "SKIP");
    setup(program);
  }

  public DispatchOSSIParser(String[] cityList, String defCity, String defState, String program) {
    super(cityList, defCity, defState, "SKIP");
    setup(program);
  }

  private void setup(String program) {
    if (program.startsWith("ID:")) {
      leadID = true;
      program = program.substring(3).trim();
    } else if (program.startsWith("ID?:")) {
      leadID = true;
      optLeadId = true;
      program = program.substring(4).trim();
    }
    if (program.endsWith(" DATETIME")) {
      dateTime = true;
      program = program.substring(0,program.length()-9);
    }
    if (program.endsWith(" DATETIME!")) {
      dateTime = true;
      dateTimeReq = true;
      program = program.substring(0,program.length()-10);
    }
    setProgram(program, 0);
  }

  protected void setDelimiter(char delim) {
    delimPattern = Pattern.compile("\\[|" + delim);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Strip off leading /
    if (body.startsWith("/")) body = body.substring(1).trim();

    // If format has a leading ID, strip that off
    if (leadID) {
      Matcher match = LEAD_ID_PTN.matcher(body);
      if (match.find()) {
        data.strCallId = match.group(1);
        body = body.substring(match.end()).trim();
      } else if (!optLeadId) return false;
    }

    // Body must start with 'CAD:'
    if (!body.startsWith("CAD:")) return false;

    // Break down string into generally semicolon delimited fields
    // with some complications involving text in square brackets

    List<String> fields = new ArrayList<String>();
    Matcher match = delimPattern.matcher(body);
    int st = 4;
    if (st < body.length() && body.charAt(st) == ':') st++;

    boolean priInfo = false;
    while (st < body.length()) {

      // search for the next delimiter (either ; or [
      char delim;
      int pt;
      if (match.find(st)) {
        pt = match.start();
        delim = body.charAt(pt);
      } else {
        pt = body.length();
        delim = 0;
      }

      // The next field consists of everything from the starting point
      // up to the location of the delimiter
      // If the delimiter in front of this term was a square bracket term
      // Search for and delete any text up to including the keywords
      // "PROBLEM:" or "RESPONDER SCRIPT:"

      String field = body.substring(st,pt).trim();
      if (priInfo) {
        Matcher match2 = KEYWORD.matcher(field);
        if (!match2.find()) field = null;
        else field = field.substring(match2.end()).trim();
      }
      if (field != null) fields.add(field);

      // Find the start of the next field
      // if the delimiter was an open square bracket, the start of the
      // next field will follow the closing square bracket
      st = pt+1;
      priInfo = false;
      if (delim == '[') {
        pt = body.indexOf(']', st);
        if (pt < 0) pt = body.length();
        String content = body.substring(st,pt);
        priInfo = content.contains("Priority") && !content.contains("Message");
        if (data.strDate.length() == 0 && data.strTime.length() == 0) {
          Matcher match2 = DATE_TIME_PTN.matcher(content);
          if (match2.find()) {
            data.strDate = match2.group(1);
            data.strTime = match2.group(2);
          }
        }
        st = pt + 1;
      }
    }

    int ndx = fields.size()-1;
    if (ndx < 0) return false;

    // Almost there.  Check to see if the last term looks like a date/time stamp
    // or the truncated remains of a date/time stamp.  If it does, remove it
    String field = fields.get(ndx);
    if (dateTime) {
      boolean isDateTime = false;
      if (field.length()>0 && Character.isDigit(field.charAt(0))) {
        field = field.replaceAll("\\d", "N");
        if ("NN/NN/NNNN NN:NN:NN".startsWith(field)) isDateTime = true;
      }
      if (isDateTime) {
        field = fields.get(ndx);
        if (field.length() >= 10) {
          data.strDate = field.substring(0,10);
          field = field.substring(10).trim();
          if (field.length() == 8) data.strTime = field;
          else if (field.length() >= 5) data.strTime = field.substring(0,5);
        }

        fields.remove(ndx);
      }
      else if (dateTimeReq) return false;
    }

    // We have a nice clean array of data fields, pass it to the programmer
    // field processor to parse
    return parseFields(fields.toArray(new String[fields.size()]), data);
  }

  @Override
  public String getProgram() {
    return (leadID ? "ID " : "") + super.getProgram() + " DATE TIME";
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("FYI")) return new SkipField("FYI:|Update:", true);
    if (name.equals("CANCEL")) return new BaseCancelField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern UNIT_PFX_PTN = Pattern.compile("(?:\\{([A-Z0-9,]+)\\} *)(.*)");
  private static final Pattern CANCEL_PTN = Pattern.compile("(CANCEL\\b.*|.*\\bCOMMAND ESTABLISHED|CODE RED|CONFIRMED FIRE.*|CONFIRMED PIN IN|FIRE OUT|FIRE UNDER CONTROL|UNDER CONTROL|UNITS RESPOND CODE 1)");
  protected class BaseCancelField extends CallField {

    private Pattern extraPattern;

    public BaseCancelField() {
      this(null);
    }

    public BaseCancelField(String pattern) {
      this.extraPattern = (pattern == null ? null : Pattern.compile(pattern));
    }


    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = UNIT_PFX_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = match.group(1);
        data.strCall = match.group(2);
        return true;
      }
      match = CANCEL_PTN.matcher(field);
      if (!match.matches()) {
        if (extraPattern == null) return false;
        if (!extraPattern.matcher(field).matches()) return false;
      }
      data.strCall = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT? CALL";
    }
  }
}
