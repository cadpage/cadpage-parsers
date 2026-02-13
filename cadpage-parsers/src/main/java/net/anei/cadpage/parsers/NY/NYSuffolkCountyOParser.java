package net.anei.cadpage.parsers.NY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYSuffolkCountyOParser extends FieldProgramParser {

  public NYSuffolkCountyOParser() {
    super("SUFFOLK COUNTY", "NY",
          "DATETIME CALL ADDRCITYST END");
  }

  @Override
  public String getFilter() {
    return "infoshare@ehtpd.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("[^;]*;[^;]*(?:;(.*))?\\|CSI Active911 - CFS Notification");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSupp = getOptGroup(match.group(1));
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('\t');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL PLACE";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) +(\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }

  private static final Pattern ADDR_GPS_PTN = Pattern.compile("(.*) \\(([-+]?\\d\\d\\.\\d+, *[-+]?\\d\\d\\.\\d+)\\)");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        setGPSLoc(match.group(2), data);
      }
      super.parse(field, data);
      int pt = data.strAddress.indexOf(',');
      if (pt >= 0) {
        data.strApt = append(data.strApt, "-", data.strAddress.substring(pt+1).trim());
        data.strAddress = data.strAddress.substring(0,pt).trim();
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " GPS";
    }
  }
}
