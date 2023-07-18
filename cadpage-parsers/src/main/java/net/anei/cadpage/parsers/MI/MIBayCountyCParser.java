package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MIBayCountyCParser extends HtmlProgramParser {

  public MIBayCountyCParser() {
    super("BAY COUNTY", "MI",
          "CALL:SKIP! PLACE:PLACE! ADDR:ADDRCITY/S6! CROSS_ST:X! ID:ID! PRI:PRI! DATE:DATETIME! MAP:MAP! UNIT:UNIT! INFO:INFO/N+ TIMES:TIMES+ LAT/LONG:GPS");
  }

  @Override
  public String getFilter() {
    return "@baycounty.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private String times;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    do {
      if (subject.startsWith("Automatic R&R Notification: ")) {
        data.strCall = subject.substring(28).trim();
        break;
      }

      if (subject.equals("Automatic R&R Notification")) {
        data.strCall = "ALERT";
        break;
      }

      return false;

    } while (false);

    times = "";
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    data.strCity = data.strCity.replace(".", "");
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
    return true;
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private static final Pattern PLACE_PHONE_PTN = Pattern.compile("(.*) (\\d{3}-\\d{4})");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPhone = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE PHONE";
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
      if (data.strApt.length() > 0) {
        data.strCity = stripFieldEnd(data.strCity, data.strApt);
      }
    }
  }

  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}|\\d\\d?:\\d\\d:\\d\\d");
  private static final Pattern INFO_PREFIX_PTN = Pattern.compile("^[a-z]+ - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_DATE_TIME_PTN.matcher(field).matches()) return;
      field = INFO_PREFIX_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
    }
  }

  private static final Pattern TIMES_JUNK_PTN = Pattern.compile("\\d{5}: .*|Assigned Station:.*");
  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (TIMES_JUNK_PTN.matcher(field).matches()) return;
      if (field.startsWith("Cleared:")) data.msgType = MsgType.RUN_REPORT;
      times = append(times, "\n", field);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (!field.contains(",")) field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }
}
