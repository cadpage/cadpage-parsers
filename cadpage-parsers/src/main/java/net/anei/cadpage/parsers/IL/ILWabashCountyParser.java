package net.anei.cadpage.parsers.IL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILWabashCountyParser extends FieldProgramParser {

  public ILWabashCountyParser() {
    super("WABASH COUNTY", "IL",
          "DATE:DATETIME! CALL:CALL! ADDR:ADDR! CITY:CITY! EMPTY! Comments:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "dispatch1@wabash911.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d \\S+");
  private static final Pattern INFO_GPS_PTN = Pattern.compile(".*\\bPhone: (\\S*?), (?:UNC: \\S*?, )?Lat: (\\S*?), Long: ?(\\S*?)(?:,.*)?");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      if (INFO_JUNK_PTN.matcher(field).matches()) return;

      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2)+','+match.group(3), data);
        return;
      }

      if (field.startsWith("ProQA Code:")) {
        data.strCode = field.substring(11).trim();
        return;
      }

      if (field.startsWith("http:") || field.startsWith("https:")) {
        data.strInfoURL = field;
        return;
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PHONE GPS CODE URL";
    }
  }

}
