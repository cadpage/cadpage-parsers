package net.anei.cadpage.parsers.NY;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NYBroomeCountyParser extends FieldProgramParser {

  private static final String SRC_PTN_STR = "(?:[0-9/]+|[A-Z]{3,4})";
  private static Pattern NEW_FMT_MARKER = Pattern.compile(SRC_PTN_STR + " *\n");
  private static Pattern BAD_EDIT_PTN = Pattern.compile("(\\d\\d)(?=[A-Z0-9])");

  private static Pattern NEW_FMT_MARKER2 = Pattern.compile(SRC_PTN_STR + ": ?:");

  private static Pattern PREFIX = Pattern.compile("^\\.{4} \\(.*?\\) ");
  private static Pattern LEADER = Pattern.compile("^([A-Z0-9/]+)[\\-:]");
  private static Pattern TRAILER = Pattern.compile(" V/Endicott? *$");
  private static Pattern KEYWORD_PAT = Pattern.compile(" (|Cross Sts|Caller|Phone):");
  private static Pattern DATE_TIME_PAT = Pattern.compile("\\b(\\d\\d:\\d\\d) (\\d\\d/\\d\\d/\\d{4}) ");
  private static Pattern TRAIL_COMMA_PAT = Pattern.compile("[ ,]+$");

  public NYBroomeCountyParser() {
    super("BROOME COUNTY", "NY",
          "SRC ( SELECT/2 EMPTY CALL ADDRCITY2 APT INFO+? X2 INFO+ " +
              "| CALL ADDR1/S6XP! INFO+ Cross_Sts:X1 Caller:NAME2 Phone:PHONE )");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,messaging@emergencysmc.com,mplus@co.broome.ny.us,9300,Dispatch@co.broome.ny.us,dispatch@co.broomecounty.org";
  }

  private static final Pattern APT_PREFIX_PTN = Pattern.compile("(?:APT|RM|ROOM) *(\\S*)", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Fix up some clever IAR edits :(
    if (NEW_FMT_MARKER.matcher(body).lookingAt()) {
      body = body.replace("\n Cross Sts", " Cross Sts");
      body = body.replace('\n', ':');
    }

    // And some  not so clever ones
    Matcher match = BAD_EDIT_PTN.matcher(body);
    if (match.lookingAt()) body = match.group(1)+':'+body.substring(match.end());

    // Removed unbalanced parenthesis from subject
    if (body.startsWith(")")) {
      body = body.substring(1).trim();
      if (body.startsWith(",")) body = body.substring(1).trim();
    }
    body = body.trim().replaceAll("  +", " ");
    match = PREFIX.matcher(body);
    if (match.find()) body = body.substring(match.end()).trim();
    match = TRAILER.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();

    // Fix up leading field separator
    if (body.startsWith(")")) body = body.substring(1).trim();

    // New format is a lot easier to parse
    String[] flds;
    if (NEW_FMT_MARKER2.matcher(body).lookingAt()) {
      setSelectValue("2");
      body = body.replace('\n', ' ');
      flds = body.split(":");
    }

    // Using a colon as both a field separator and a keyword indicator makes life complicated :(
    else {
      setSelectValue("1");
      body = LEADER.matcher(body).replaceFirst("$1 :");
      List<String>fldList = new ArrayList<String>();
      match = KEYWORD_PAT.matcher(body);
      int st = 0;
      while (match.find()) {
        int end = match.start();
        if (end > st) fldList.add(body.substring(st, end).trim());
        st = end+1;
        end = match.end();
        if (end == st + 1) st = end;
      }
      int end = body.length();
      if (end > st) fldList.add(body.substring(st, end).trim());
      flds = fldList.toArray(new String[fldList.size()]);
    }

    if (!parseFields(flds, data)) return false;


    match = APT_PREFIX_PTN.matcher(data.strApt);
    if (match.matches()) data.strApt = match.group(1);

    int pt = data.strCity.indexOf('/');
    if (pt >= 0) data.strCity = data.strCity.substring(0,pt).trim();
    return true;
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile(" *<([A-Z0-9]+)>$");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.find()) {
        data.strCode = match.group(1);
        field = field.substring(0,match.start());
      }
      super.parse(field, data);
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("SRC")) return new SourceField(SRC_PTN_STR, true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITY2")) return new MyAddressCity2Field();
    if (name.equals("X2")) return new MyCross2Field("Cross Sts-(.*)", true);
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("X1")) return new MyCross1Field();
    if (name.equals("NAME2")) return new MyName2Field();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY2_PTN = Pattern.compile("([^*]*?)\\*([^,]+)(?:,(.*))?\\*");
  private class MyAddressCity2Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY2_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPlace = match.group(1).trim();
      parseAddress(match.group(2).trim(), data);
      data.strCity = getOptGroup(match.group(3));
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }

  private class MyCross2Field extends CrossField {
    public MyCross2Field(String pattern, boolean hard) {
      super(pattern, hard);
    }

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }


  private class MyAddress1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      // An asterisk can be used to separate the address and place field
      // but can not be relied on
      int pt = field.indexOf('*');
      if (pt >= 0) {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field.substring(0,pt), data);
        data.strPlace = field.substring(pt+1).trim();
      }

      // Otherwise parse normally
      else {
        super.parse(field, data);
      }
    }
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    return apt.indexOf('/') >= 0 || apt.indexOf('&') >= 0;
  }

  // Cross street field needs to parse time, date, and ID data from field
  private class MyCross1Field extends CrossField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PAT.matcher(field);
      if (match.find()) {
        data.strTime = match.group(1);
        data.strDate = match.group(2);
        data.strCallId = field.substring(match.end()).trim();
        field = field.substring(0, match.start()).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "X TIME DATE ID";
    }
  }

  // Name field needs to remove trailing commas
  private class MyName2Field extends NameField {

    @Override
    public void parse(String field, Data data) {

      Matcher match = TRAIL_COMMA_PAT.matcher(field);
      if (match.find()) {
        field = field.substring(0, match.start());
      }
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("ProQA Paramount Medical")) return;
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = PK_PTN.matcher(addr).replaceAll("PARK");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern PK_PTN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);
}
