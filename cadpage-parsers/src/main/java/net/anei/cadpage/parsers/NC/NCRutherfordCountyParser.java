package net.anei.cadpage.parsers.NC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class NCRutherfordCountyParser extends FieldProgramParser {

  public NCRutherfordCountyParser() {
    super(CITY_CODES, "RUTHERFORD COUNTY", "NC",
          "( ( UNIT Prob:CALL! | Prob:CALL! ) Addr:ADDR! ( City:CITY! CrossSt:X! | CrossSt:X! City:CITY ) Comments:INFO Unit:UNIT" +
          "| UNIT Run_Number:ID! Address:ADDR! Dispatch:TIMES! Unit:UNIT! " +
          "| Location:ADDR! APT/ROOM:APT? City:CITY! Call_Type:CALL! Line11:INFO? Units:UNIT! DATETIME1 " +
          "| City:CITY! Call_Type:CALL! Units:UNIT! " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "paging@rutherfordcountync.gov,8284295922";
  }

  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<=\\S)(?=Prob:|Addr:|City:|CrossSt:|Run Number:|Unit:)");
  private static final Pattern MISSING_COLON_PTN = Pattern.compile("(<=/City)(?!:)");
  private static final Pattern PREFIX_PTN = Pattern.compile("To - (\\w+)\\s+");
  private static final Pattern KEYWORD_DELIM = Pattern.compile("(?<=Location|APT/ROOM|City|Call Type|Line11|Units)[^*]");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    // Strip off extraneous msg headers
    if (body.startsWith("X-ASG-Debug-ID:")) {
      int pt = body.indexOf("\n\n");
      if (pt < 0) return false;
      String headers = body.substring(0,pt).trim();
      body = body.substring(pt).trim();

      pt = headers.indexOf("\nThread-Topic:");
      if (pt >= 0) {
        pt += 14;
        int pt2 = headers.indexOf('\n', pt);
        if (pt2 < 0) pt2 = headers.length();
        subject = headers.substring(pt, pt2).trim();
      }

      body = body.replace("=\n", "");
    }

    // Parse new page format
    if (subject.equals("911 Paging")) {
      int pt = body.indexOf("\n\n____");
      if (pt >= 0) body = body.substring(0,pt).trim();

      if (parseRunReport(body, data)) return true;

      body = stripFieldStart(body, "Units:");
      body = body.replace("/Addr:", " Addr:").replace("/City:", " City:");
      body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
      body = MISSING_COLON_PTN.matcher(body).replaceAll(":");
      body = body.replace("Cross St:", "CrossSt:");
      return parseMsg(body,  data);
    }

    // page old page format
    String[] parts = subject.split("\\|");
    subject = parts[parts.length-1];
    do {
      if (subject.equals("Paging")) break;

      if (body.startsWith("Paging:")) {
        body = body.substring(7).trim();
        break;
      }
      if (subject.endsWith("PageGate")) break;

      Matcher match = PREFIX_PTN.matcher(body);
      if (match.lookingAt()) {
        data.strSource = match.group(1);
        body = body.substring(match.end()).replace("\n", "");
        break;
      }

      if (body.startsWith("City=")) break;

      return false;
    } while (false);

    body = body.replaceAll("\n", "");
    body = KEYWORD_DELIM.matcher(body).replaceAll(":");
    return super.parseFields(body.split("\\*"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("Unit on Call:(\\S+) *Incident #:(\\S+) *(.*)");
  private static final Pattern RUN_REPORT_TIME_PTN = Pattern.compile("[- A-Za-z]+ Time:");

  private boolean parseRunReport(String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (!match.matches()) return false;
    setFieldList("UNIT ID INFO");
    data.msgType = MsgType.RUN_REPORT;
    data.strUnit = match.group(1);
    data.strCallId = match.group(2);
    body = match.group(3);

    String label = null;
    int lastPt = 0;
    StringBuilder sb = new StringBuilder();
    match = RUN_REPORT_TIME_PTN.matcher(body);
    while (true) {
      boolean done = !match.find();
      int pt = done ? body.length() : match.start();
      String value = body.substring(lastPt, pt).trim();
      if (label != null && !value.isEmpty()) {
        if (sb.length() > 0) sb.append('\n');
        sb.append(label);
        sb.append(value);
      }
      if (done) break;
      label = match.group();
      lastPt = match.end();
    }
    data.strSupp = sb.toString();
    return true;
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MMMdd,hh:mmaa");

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("DATETIME1")) return new DateTimeField(DATE_TIME_FMT, false);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("Not Found", "");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern TIMES_BRK_PTN = Pattern.compile("(?<=\\d\\d:\\d\\d:\\d\\d)\\s*|\\s+");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.msgType = MsgType.RUN_REPORT;
      field = getRelativeField(0);
      field = TIMES_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(",?\\[\\d{1,2}\\]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_BRK_PTN.split(field)) {
        line = line.trim();
        if (line.startsWith("A cellular re-bid")) continue;
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ALEXANDER MI",   "ALEXANDER MILLS",
      "RUTHERFORDTO",   "RUTHERFORDTON",
      "SULPHUR SPRI",   "SULPHUR SPRINGS"
  });
}
