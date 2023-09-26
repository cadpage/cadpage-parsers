package net.anei.cadpage.parsers.MO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MOScottCountyEParser extends FieldProgramParser {

  public MOScottCountyEParser() {
    super("SCOTT COUNTY", "MO",
          "EventNumber:ID! Date/Time:DATETIME! Category:CALL! Sub_Category:CALL/SDS! Address:EMPTY! ADDRCITYST! Business:PLACE! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  String times;

  @Override
  protected boolean parseMsg(String body, Data data) {
    times = "";
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(times, "\n", data.strSupp);
    }
    return true;
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yy hh:mm aa");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_ST_ZIP_PTN = Pattern.compile("(.*?)(?:, +([A-Z ]+?))?(?:[, ]+([A-Z]{2}))?(?:[, ]+(\\d{5}))?");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCity = getOptGroup(match.group(2));
        data.strState = getOptGroup(match.group(3));
        if (data.strCity.isEmpty()) data.strCity = getOptGroup(match.group(4));
      }
      for (String part : field.split(",")) {
        part = part.trim();
        if (data.strAddress.isEmpty()) {
          parseAddress(part, data);
        } else {
          data.strApt = append(data.strApt, "-", part);
        }
      }
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Not listed")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_TIMES_PTN = Pattern.compile("(.*) Date / Time(?:\\d\\d?/\\d\\d?/\\d{4} (\\d\\d?:\\d\\d:\\d\\d [AP]M))?");
  private static final DateFormat IN_TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static final DateFormat OUT_TIME_FMT = new SimpleDateFormat("HH:mm:ss");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_TIMES_PTN.matcher(field);
      if (match.matches()) {
        String type = match.group(1);
        String time = match.group(2);
        if (time == null) return;
        if (type.equals("Closed")) data.msgType = MsgType.RUN_REPORT;
        try {
          time = OUT_TIME_FMT.format(IN_TIME_FMT.parse(time));
        } catch (ParseException e) {
          return;
        }
        times = append(times, "\n", String.format("%1$-10s $2$s", type, time));
      }
    }
  }
}
