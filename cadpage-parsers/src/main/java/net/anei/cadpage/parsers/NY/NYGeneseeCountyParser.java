package net.anei.cadpage.parsers.NY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;




public class NYGeneseeCountyParser extends FieldProgramParser {

  public NYGeneseeCountyParser() {
    super(CITY_LIST, "GENESEE COUNTY", "NY",
          "( SELECT/3 CODE CALL ADDR3 X DATETIME! Dispatched_Units:UNIT! Stand-By_Units:SKIP? CFS:ID INFO/N+ END " +
          "| CALL1 PLACE ADDR1 APT? CALL2! X DATETIME ID% INFO+ )");
  }

  @Override
  public String getFilter() {
    return "911center@co.genesee.ny.us,777,888";
  }

  private static final Pattern DELIM3 = Pattern.compile("\\* *\\n");
  private static final Pattern UNIT_PREFIX_PTN = Pattern.compile("^(?:Unit: *([A-Z0-9]+) +)?Status: *(?:Dispatched|At Scene) *(?:\\*\\* +)?");
  private static final Pattern MUT_AID_CITY_PTN = Pattern.compile(".*\\bMUTUAL AID\\b.* TO ([^ ]+(?: +[^ ]+)?)", Pattern.CASE_INSENSITIVE);
  private static final Pattern DELIM1 = Pattern.compile("(?<= )\\*\\*(?= |$)");

  private boolean skipApt;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {

      if (body.startsWith("GENESEE COUNTY DISPATCH ")) {
        body = body.substring(24).trim();
        break;
      }

      if (subject.equals("Dispatch")) break;

      return false;
    } while (false);

    skipApt = false;

    int pt = body.indexOf("\n-- \n");
    if (pt < 0) pt = body.indexOf("\n\nNOTICE:");
    if (pt >= 0) {
      body = body.substring(0,pt).trim();
      body = stripFieldEnd(body, "*");
    }

    String[] flds = DELIM3.split(body);
    if (flds.length >= 5) {
      setSelectValue("3");
      return parseFields(flds, data);
    }

    setSelectValue("1");
    pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();

    body = body.replace("\n", "");
    String saveBody = body;

    // We have to try old format first, because new format is a subset of it
    Matcher match = UNIT_PREFIX_PTN.matcher(body);
    if (match.find()) {
      data.strUnit = match.group(1);
      body = body.substring(match.end());
    } else if (body.startsWith("** ")) {
      body = body.substring(3).trim();
    }

    if (!parseFields(DELIM1.split(body), 4, data)) {
      data.initialize(this);
      if (!parseNewFormat(saveBody, data)) {
        setFieldList("INFO");
        return data.parseGeneralAlert(this, saveBody);
      }
    }

    // If successful, and no city was specified, see if we can extract one from a mutual aid description
    if (data.strCity.length() == 0) {
      match = MUT_AID_CITY_PTN.matcher(data.strCall);
      if (match.matches()) data.strCity = match.group(1);
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "UNIT? " + super.getProgram();
  }

  private static final Pattern UNIT_PTN1 = Pattern.compile("(?:(?:\\d{3}|[A-Z]+\\d+) )+ ");
  private static final Pattern UNIT_PTN2 = Pattern.compile("([A-Z]+\\d+) +");
  private static final Pattern ID_PTN = Pattern.compile(" +(\\d{4}-\\d{8})$");
  private static final Pattern APT_PTN = Pattern.compile("([A-D](?: ?\\d+)?) +(.*)");

  private boolean parseNewFormat(String body, Data data) {
    setFieldList("UNIT ADDR APT PLACE CITY CALL ID");
    Matcher match = UNIT_PTN1.matcher(body);
    if (match.lookingAt()) {
      data.strUnit = match.group().trim();
      body = body.substring(match.end()).trim();
    }
    match = UNIT_PTN2.matcher(body);
    if (match.lookingAt()) {
      data.strUnit = append(data.strUnit, " ", match.group(1));
      body = body.substring(match.end());
    }
    if (data.strUnit.length() == 0) return false;

    match = ID_PTN.matcher(body);
    if (!match.find()) return false;
    data.strCallId = match.group(1);
    body = body.substring(0,match.start()).trim();

    parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD, body, data);
    if (data.strCity.length() == 0) return false;

    String place = getPadField();
    match = APT_PTN.matcher(place);
    if (match.matches()) {
      data.strApt = append(data.strApt, "-", match.group(1));
      place = match.group(2);
    }
    data.strPlace = place;

    data.strCall = getLeft();
    return (data.strCall.length() > 0);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CALL1")) return new CallField("[^:]*", true);
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("ADDR3")) return new MyAddress3Field();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("UNIT3")) return new UnitField("Dispatched(?: Units:)? *(.*)", true);
    return super.getField(name);
  }

  private class MyAddress1Field extends AddressField {

    @Override
    public void parse(String field, Data data) {

      // remove leading asterisk
      if (field.startsWith("*")) field = field.substring(1).trim();

      // If field contains comma, parse as address and cross / city
      String sApt = "";
      skipApt = true;
      String sCity = null;
      int pt = field.indexOf(',');
      if (pt >= 0) {
        skipApt = false;
        sCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        int pt2 = sCity.indexOf('-');
        if (pt2 >= 0) {
          skipApt = true;
          sApt = sCity.substring(pt2+1).trim();
          if (sApt.startsWith("APT ")) sApt = sApt.substring(4);
          sCity = sCity.substring(0,pt2).trim();
        }
      }
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      if (sCity != null) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_ONLY_CITY | FLAG_ANCHOR_END, sCity, data);
        if (data.strCity.equals("TONAWANDA IR")) data.strCity = "TONAWANDA INDIAN RESERVATION";
        data.strAddress = append(data.strAddress, " & ", data.strCross);
        data.strCross = "";
        data.strApt = append(data.strApt, "-", sApt);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
    }
  }

  private static final Pattern ADDRESS3_PTN = Pattern.compile("([^*]*?)\\*([^,]*?),(?: ([^,]*?),)?(?: (.*))?");
  private class MyAddress3Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDRESS3_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPlace = match.group(1).trim();
      parseAddress(match.group(2).trim(), data);
      data.strCity = getOptGroup(match.group(3));
      data.strApt = append(data.strApt, "-", getOptGroup(match.group(4)));
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR CITY APT";
    }

  }

  private class MyAptField extends AptField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // Older formats included apt in address field
      // if this is one of those, skip this field
      if (skipApt) return false;
      parse(field, data);
      return true;
    }
  }

  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d\\d(?:\\d\\d)?) (\\d\\d?:\\d\\d(?::\\d\\d)?(?: [AP]M)?).*");
  private static final Pattern TRUNC_DATE_TIME_PTN = Pattern.compile("\\d[ \\d/:]*");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) {
        if (!isLastField() || !TRUNC_DATE_TIME_PTN.matcher(field).matches()) abort();
        return;
      }
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }

  private static final String[] CITY_LIST = new String[]{
    "ALABAMA",
    "ALEXANDER",
    "ATTICA",
    "BATAVIA",
    "BERGEN",
    "BERGEN",
    "BETHANY",
    "BYRON",
    "CORFU",
    "DARIEN",
    "ELBA",
    "LE ROY",
    "OAKFIELD",
    "PAVILION",
    "PEMBROKE",
    "STAFFORD",

    "TONAWANDA IR",

    // Erie County
    "ALDEN",
    "AKRON",

    // Wyoming County
    "ATTICA",
    "BENNINGTON",
    "WARSAW",

    // Erie County,
    "CLARENCE",
    "NEWSTEAD",

    // Orleans County
    "BARRE",
    "CLARENDON",
    "SHELBY",

    // Livingston County
    "CALEDONIA",
    "YORK",

    // Monroe County
    "RIGA"
  };
}
