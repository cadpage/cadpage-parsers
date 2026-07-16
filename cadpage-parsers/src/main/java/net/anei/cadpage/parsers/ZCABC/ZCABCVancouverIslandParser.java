package net.anei.cadpage.parsers.ZCABC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZCABCVancouverIslandParser extends FieldProgramParser {

  public ZCABCVancouverIslandParser() {
    this("", "BC");
  }

  public ZCABCVancouverIslandParser(String defCity, String defState) {
    super(defCity, defState,
          "SRC CALL ADDR! Unit#:APT! CITY ST DATETIME UNIT! X-ST:X! Alarm_Level:PRI! ID! EMPTY! END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "paging@ni911.ca";
  }

  @Override
  public String getLocName() {
    if (this.getClass() == ZCABCVancouverIslandParser.class) return "Vancouver Island, BC";
    return super.getLocName();
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if(!subject.equals("Fire Dispatch")) return false;
    return parseFields(body.split(",", -1), data);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [ap]m)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME"))  return new MyDateTimeField();
    return super.getField(name);
  }

  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("m")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }

  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "HMCS QUADRA",            "+49.662330,-124.914198",
      "10696 TAYLOR ARM DR",    "+49.275430,-124.985284"

  });
}
