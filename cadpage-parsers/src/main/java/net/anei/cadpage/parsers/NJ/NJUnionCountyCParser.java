package net.anei.cadpage.parsers.NJ;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJUnionCountyCParser extends FieldProgramParser {

  public NJUnionCountyCParser() {
    super("UNION COUNTY", "NJ",
          "CAD_No:ID! Time_of_Call:DATETIME! Location:ADDRCITY! Incident_Type:CALL! Notes:EMPTY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "LSDoNotReply@Lawsoft-inc.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Union Township Police Dispatch - Fire Page")) return false;
    int pt = body.indexOf("\n?\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d(?::\\d\\d)? [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static final DateFormat TIME_FMT2 = new SimpleDateFormat("hh:mm aa");

  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      if (! setTime(TIME_FMT, match.group(2), data)) {
        setTime(TIME_FMT2, match.group(2), data);
      }
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("Latitude: *(.*?) +Logitude: *(.*)");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames();
    }
  }
}
