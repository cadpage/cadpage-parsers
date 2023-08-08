package net.anei.cadpage.parsers.CO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class COMesaCountyAParser extends FieldProgramParser {

  public COMesaCountyAParser() {
    super("MESA COUNTY", "CO",
          "ID Call_Type:CALL! Address:ADDRCITY! ( Additional_address_info:PLACE! | Common_Name:PLACE! ) Closest_Intersection:X? Call_Time:DATETIME! Narrative:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "aegisadmin@gjcity.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern ID_JUNK_PTN = Pattern.compile(" \\([A-Z0-9]+\\)");
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("****CAREFLIGHT****")) return;
      if (!field.startsWith("INC#")) abort();
      field = field.substring(4).trim();
      field = ID_JUNK_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      field = field.replace("UNKNOWN", "").trim();
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, match.group(2), data);
      } else {
        data.strTime = time;
      }
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = addr.replace("HWY 6 & 50", "HWY 50");
    addr = addr.replace("MILLER CANYON RANCH RD", "E S 5/10 RD");
    return super.adjustMapAddress(addr);
  }

}
