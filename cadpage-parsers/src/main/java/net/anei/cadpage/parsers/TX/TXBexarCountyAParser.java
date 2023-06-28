package net.anei.cadpage.parsers.TX;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Bexar County, TX
 */
public class TXBexarCountyAParser extends FieldProgramParser {

  public TXBexarCountyAParser() {
    super("BEXAR COUNTY", "TX",
          "( SELECT/3 CALL! INFO! INFO/N+ Units_Assigned:UNIT! INFO/N+ " +
          "| SELECT/2  ( INCIDENT_NOTIFICATION! CALL EMPTY ADDR EMPTY X EMPTY MAP EMPTY UNIT END " +
                      "| SRC INCIDENT_INFORMATION! DATE_&_TIME:DATETIME2! EMPTY! PROBLEM:CALL! EMPTY! ADDRESS:ADDRCITY! EMPTY! CHANNEL:CH! EMPTY! DIVISION:MAP! EMPTY! UNITS:UNIT! EMPTY! LOCATION_NAME:PLACE! EMPTY! LOCATION_TYPE:SKIP END " +
                      ") " +
          "| Problem:CALL! Address:ADDR1B! Cross:X! MapGrid:MAP! Case_Number:ID? Units:UNIT! Notes:INFO1 " +
          "| DATETIME1? CALL1 CALL1? ADDR1A X_APT+? MAP_ID_UNIT! MAP_ID_UNIT+? INFO1+ )");
  }

  public String getFilter() {
    return "visinet.command@sanantonio.gov,commcenteraustin@Acadian.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern RR_DELIM_PTN = Pattern.compile(" *__ *| *_(?=Miles Traveled :)");
  private static final Pattern PREFIX_PTN = Pattern.compile("\\*{2,} *([A-Za-z0-9 ]+?) *\\*{2,} +[A-Z0-9]+(?=Problem)");
  private static final Pattern SIT_AWARENESS_PTN = Pattern.compile("BCLE SITUATIONAL AWARENESS ALERT\\.{5}(.*?) (00|Emergency|In Progress|Non-Emergency|Priority) *([A-Z0-9]+) +(.*?)w/ cross streets:(.*?)\\| Responding Units:(\\S+) +(.*)");
  private static final String MAP_PATTERN = "(\\d{3}[A-Z]\\d|SA\\d{3}(?:/[A-Z]\\d?)?|NOT FOUN)";
  private static final Pattern DASH_DELIM_PTN = Pattern.compile(" +- ");
  private static final Pattern PROTECT_KEYWORD = Pattern.compile("(?<=:)  +(?=[^ ])");
  private static final Pattern BLANK_DELIM_PTN = Pattern.compile(" {4,}");
  private static final Pattern SHORT_BLANK_DELIM_PTN = Pattern.compile("(?<![ -])  +(?![ -])");
  private static final Pattern MAP_BLANK_DELIM_PTN = Pattern.compile(" " + MAP_PATTERN + " +(?=[^ -])");
  private static final Pattern COLON_MAP_PTN = Pattern.compile(":( " + MAP_PATTERN + ")");
  private static final Pattern DELIM_PTN1 = Pattern.compile(" -(?= )| \\*\\*");
  private static final Pattern DELIM_PTN2 = Pattern.compile("(?=Address:|Cross:|MapGrid:|Case Number:|Units:|Notes:)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (body.startsWith("|")) {
      setSelectValue("2");
      if (!parseFields(body.substring(1).trim().split("\\|"), data)) return false;
    }

    else if (body.contains("__Units Assigned :")) {
      setSelectValue("3");
      data.msgType = MsgType.RUN_REPORT;
      if (!parseFields(RR_DELIM_PTN.split(body), data)) return false;
    }

    else {
      setSelectValue("1");
      Matcher match = PREFIX_PTN.matcher(body);
      String prefix = "";
      if (match.lookingAt()) {
        prefix = match.group(1);
        body = body.substring(match.end());
      }

      // Check for latest new page format
      if (body.startsWith("Problem:")) {
        body = body.replace("||** ", " Notes: **");
        body = body.replaceAll("Mapgrid:", "MapGrid:");
        int pt = body.indexOf("Notes:");
        if (pt < 0) return false;
        String[] flds1 = DELIM_PTN2.split(body.substring(0,pt).trim());
        String[] flds2 = new String[flds1.length+1];
        System.arraycopy(flds1, 0, flds2, 0, flds1.length);
        flds2[flds1.length] = body.substring(pt);
        if (!parseFields(flds2, data)) return false;
        data.strCall = append(prefix, " - ", data.strCall);
      }

      // Check for new Situation Awareness format
      else if ((match = SIT_AWARENESS_PTN.matcher(body)).matches()) {
        setFieldList("CALL PRI ADDR APT X UNIT INFO");
        data.strCall = match.group(1).trim();
        data.strPriority  = match.group(2).substring(0,1)+'-'+match.group(3);
        parseAddress(match.group(4).trim(), data);
        data.strCross = match.group(5).trim();
        data.strUnit = match.group(6).trim();
        data.strSupp = match.group(7).trim();
      }

      else {

        // The main format is usually dash delimited, but occasionally drops the dashes in favor on
        // long strings of blanks, which we will turn in regular dash delimiters
        // And then, they occasionally leave a single space delimiter after the Map field
        body = DASH_DELIM_PTN.matcher(body).replaceAll(" - ");
        body = PROTECT_KEYWORD.matcher(body).replaceAll(" ");
        body = BLANK_DELIM_PTN.matcher(body).replaceAll(" - ");
        body = MAP_BLANK_DELIM_PTN.matcher(body).replaceFirst("$0 - ");
        body = COLON_MAP_PTN.matcher(body).replaceFirst("$1");

        int pt = body.lastIndexOf(" - ");
        if (pt < 0) return false;
        body = SHORT_BLANK_DELIM_PTN.matcher(body.substring(0,pt)).replaceAll(" - ") + body.substring(pt);
        if (!parseFields(DELIM_PTN1.split(body), data)) return false;
      }
    }

    if (data.strCity.equals("UBC")) data.strCity = "SAN ANTONIO";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]+", true);
    if (name.equals("INCIDENT_NOTIFICATION")) return new SkipField("\\*\\*Incident Notification\\*\\*", true);
    if (name.equals("INCIDENT_INFORMATION")) return new SkipField("\\*\\*Incident Information\\*\\*", true);
    if (name.equals("DATETIME2")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d");
    if (name.equals("CH")) return new ChannelField("\\* *(.*)");

    if (name.equals("MAP")) return new MyMapField();

    if (name.startsWith("T") && name.length()==2) return new SkipField(name, true);
    if (name.equals("DATETIME1")) return new MyDateTime1Field();
    if (name.equals("CALL1")) return new MyCallField();
    if (name.equals("ADDR1A")) return new MyAddress1AField();
    if (name.equals("ADDR1B")) return new MyAddress1BField();
    if (name.equals("X_APT")) return new MyCrossAptField();
    if (name.equals("MAP_ID_UNIT")) return new MyMapIdUnitField();
    if (name.equals("INFO1")) return new MyInfo1Field();
    return super.getField(name);
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NOT FOUN")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d )?(\\d\\d:\\d\\d(?: [ap]m)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("KK:mm aa");
  private class MyDateTime1Field extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = getOptGroup(match.group(1));
      String time = match.group(2);
      if (time.endsWith("m")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
      return true;
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {

      // Very special case.  A numeric token coming in as the
      // second part of the address is probably the start of
      // a house number range and should go in the address
      if (data.strCall.length() > 0 && NUMERIC.matcher(field).matches()) {
        data.strAddress = field;
        return;
      }

      if (field.startsWith("*")) field = field.substring(1).trim();
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private static final Pattern CALL_DESC_PTN = Pattern.compile("[A-Za-z ]*");
  private static final Pattern SPEC_CALL_DESC_PTN =
      Pattern.compile("^(?:Witness?|Inspection Fol[a-z]*|Investigation Foll|Special Assignment|SELF INITIATED ACT|Assist Other Agenc|Inspection Fire Wa)(?=[A-Z0-9 ])");
  private static final Pattern IH_PTN = Pattern.compile("\\bIh\\b", Pattern.CASE_INSENSITIVE);
  private class MyAddress1AField extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {

      // The only time this should fail is if the real call field got
      // broken up because it contains dashes.

      // A numeric token is probably a house number starting a house number range
      // We reject it as an address, the call description logic will put it in the
      // address field
      if (NUMERIC.matcher(field).matches()) return false;

      // OK, check some exceptional cases of call descriptions
      // that look like addresses
      if (field.equals("Tree / Br")) return false;

      // Otherwise, if contains only alpha letters and blanks, it is a call description
      // otherwise it is an address
      // Not sure what EOC is, but treat it like an address
      if (!field.equalsIgnoreCase("EOC") && !field.endsWith(" COUNTY") && CALL_DESC_PTN.matcher(field).matches()) return false;

      // There (so far) one case where there was no delimiter beween the last part of
      // the call description and the address, so they need to be split apart
      Matcher match = SPEC_CALL_DESC_PTN.matcher(field);
      if (match.find()) {
        data.strCall = append(data.strCall, " - ", match.group());
        field = field.substring(match.end()).trim();
      }

      // A numeric field is assumed to be part of a street range that will
      // be prepended to the address that is coming up next
      if (NUMERIC.matcher(field).matches()) {
        data.strAddress = field;
        return true;
      }

      // Looks good, parse the field and return true status
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {

      // There (so far) one case where there was no delimiter beween the last part of
      // the call description and the address, so they need to be split apart
      Matcher match = SPEC_CALL_DESC_PTN.matcher(field);
      if (match.find()) {
        data.strCall = append(data.strCall, " - ", match.group());
        field = field.substring(match.end()).trim();
      }

      String prefix = data.strAddress;
      data.strAddress = "";
      field = IH_PTN.matcher(field).replaceAll("I");
      super.parse(field, data);
      data.strAddress = append(prefix, "-", data.strAddress);
    }
  }

  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");
  private class MyAddress1BField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (STATE_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY ST";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:Apt|RM|#) *(.*)|(SUITE.*)");
  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d:\\d\\d");
  private class MyCrossAptField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = match.group(2);
        data.strApt = append(data.strApt, " - ", apt);
      }
      else if (CITY_SET.contains(field)) {
        data.strCity = field;
      } else if (TIME_PTN.matcher(field).matches()) {
        if (data.strTime.length() == 0) data.strTime = field;
      } else if (field.length() > 5) {
        data.strCross = append(data.strCross, " & ", field);
      } else {
        data.strApt = append(data.strApt, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY X APT TIME";
    }
  }

  private static final Pattern MAP_ID_UNIT_PATTERN =
      Pattern.compile(MAP_PATTERN + "|Case|([A-Z]{3,4}-\\d{4}-\\d{6,}|\\d{4}-(?:\\d{3}|[A-Z]{2})-\\d+|\\d{4}-\\d{7}-[A-Z]{3,4})(?: +Dept[ -](.*))?|Dept[ -]+([^ \\*]+?)([ \\*].*|)");
  private class MyMapIdUnitField extends MyInfo1Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MAP_ID_UNIT_PATTERN.matcher(field);
      if (!match.matches()) return false;

      field = match.group(1);
      if (field != null) {
        if (!field.equals("NOT FOUN")) data.strMap = field;
        return true;
      }

      field = match.group(2);
      if (field != null) {
        data.strCallId = field;
        field = match.group(3);
        if (field != null) data.strUnit = field;
        return true;
      }

      field = match.group(4);
      if (field != null) {
        data.strUnit = field;
        super.parse(match.group(5), data);
        return true;
      }

      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "MAP UNIT ID " + super.getFieldNames();
    }
  }

  // Info field tries to clean up some of the more useless information
  private static final Pattern INFO_CH_APT_PTN = Pattern.compile("_Channel: *(\\S*) *_ Apt # if avail:");
  private static final Pattern INFO_APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE) *(.*)");
  private static final Pattern INFO_DEPT_PTN = Pattern.compile("Dept[ -]+([^ \\*]+?)(?:[ \\*]|$)");
  private static final Pattern ACADIAN_PTN = Pattern.compile("(?:\\bACADIAN:|\\[ProQA Script\\]) *");
  private static final Pattern SPEC_INFO_PTN = Pattern.compile("(?<=^|,) *Unit: *([^ ]+)\\b|" +                                // Unit:
                                                                  "(?<=^|,) *Dispatch code: *([^ ]+)\\b|" +                       // Dispatch code:
                                                                  "(?:(?:^|,)[^,]*?)?\\b([A-Z]{3,4}-\\d{4}-\\d{6,})\\b[^,]*");    // Call ID
  private static final Pattern TRASH_PTN = Pattern.compile("(?:^|,) *(?:A cellular re-bid |check the ANI/ALI |Invalid address received:|Automatic Case |\\[ProQA Session Aborted\\]|Transferred incident:|Acknowledgement Received |Reference Number:|status change to |This incident [-A-Z0-9]+ has been sent to |ACADIAN HAS CHANGED )[^,]*");
  private static final Pattern TRIM_PTN = Pattern.compile("^[, \\.]+|[, \\.]+$");
  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_CH_APT_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strChannel = match.group(1);
        int pt1 = match.end();
        int pt2 = pt1 + 12;
        String apt = substring(field, pt1, pt2);
        field = substring(field, pt2);
        match = INFO_APT_PTN.matcher(apt);
        if (match.matches()) apt = match.group(1);
        if (!apt.equals(data.strApt)) data.strApt = append(data.strApt, "-", apt);
      }
      else if (field.startsWith("Channel:")) {
        int pt2 = field.indexOf(' ', 8);
        if (pt2 >= 0) {
          data.strChannel = field.substring(8, pt2);
          field = field.substring(pt2+1).trim();
        } else {
          data.strChannel = field.substring(8);
          return;
        }
      }
      else if ((match = INFO_DEPT_PTN.matcher(field)).lookingAt()) {
        data.strUnit = append(data.strUnit, " ", match.group(1));
        field = field.substring(match.end()).trim();
      }
      field = ACADIAN_PTN.matcher(field).replaceAll("");
      match = SPEC_INFO_PTN.matcher(field);
      StringBuffer sb = new StringBuffer();
      while (match.find()) {
        if (match.group(1) != null) {
          data.strUnit = append(data.strUnit, " ", match.group(1));
        } else if (match.group(2) != null) {
          data.strCode = match.group(2);
        } else {
          if (data.strCallId.length() == 0) data.strCallId = match.group(3);
        }
        match.appendReplacement(sb, "");
      }
      match.appendTail(sb);
      field = sb.toString();
      field = TRASH_PTN.matcher(field).replaceAll("");
      field = field.replace("[Shared]", "");
      field = TRIM_PTN.matcher(field).replaceAll("");
      data.strSupp = append(data.strSupp, " - ", field);
    }

    @Override
    public String getFieldNames() {
      return "CH APT UNIT CODE ID INFO";
    }
  }

  private static final Set<String> CITY_SET = new HashSet<String>(Arrays.asList(new String[]{
      "ALAMO HEIGHTS",
      "BALCONES HEIGHTS",
      "CASTLE HILLS",
      "CIBOLO",
      "CHINA GROVE",
      "CONVERSE",
      "CROSS MOUNTAIN",
      "ELMENDORF",
      "FAIR OAKS RANCH",
      "GREY FOREST",
      "HELOTES",
      "HILL COUNTRY VILLAGE",
      "HOLLYWOOD PARK",
      "KIRBY",
      "LEON SPRINGS",
      "LEON VALLEY",
      "LIVE OAK",
      "LYTLE",
      "OLMOS PARK",
      "SAN ANTONIO",
      "SCHERTZ",
      "SCENIC OAKS",
      "SELMA",
      "SHAVANO PARK",
      "SOMERSET",
      "ST. HEDWIG",
      "TERRELL HILLS",
      "TIMBERWOOD PARK",
      "UNIVERSAL CITY",
      "VON ORMY",
      "WINDCREST"
  }));
}
