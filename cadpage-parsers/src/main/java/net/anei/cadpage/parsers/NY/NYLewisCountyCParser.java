package net.anei.cadpage.parsers.NY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYLewisCountyCParser extends FieldProgramParser {

  public NYLewisCountyCParser() {
    super("LEWIS COUNTY", "NY",
          "NATURE:CALL! PRIORITY:PRI! ADDRESS:ADDR! CITY:CITY! CROSS_STREETS:X! CURRENT_TIME:DATETIME! TIME_REPORTED:SKIP! " +
          "RESPONDING_UNITS:UNIT! COMMENTS:INFO! INFO/N+ CONTACT:NAME! PHONE:PHONE! END");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Rapid Notification")) return false;
    return parseFields(body.split("\n"), data);
  }

  private static final DateFormat DATETIME_FMT = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATETIME_FMT, true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d .*:");
  private static final Pattern INFO_GPS_PTN = Pattern.compile("WPH2 data.*\\bPhone: *(\\S*),.* Lat: *(\\S*), Long: *(\\S*),.*");

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

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PHONE GPS";
    }
  }
}
