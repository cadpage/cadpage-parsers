package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class ALJeffersonCountyIParser extends DispatchH05Parser {

  public ALJeffersonCountyIParser() {
    super("JEFFERSON COUNTY", "AL",
          "( SELECT/2 ADDRCITY CALL SKIP! X:X! Units:UNIT! Created:DATETIME2! Pri_Inc:ID! END " +
          "| SELECT/H1  ( CALL:CALL! X/Y:GPS! ADDR1:ADDRCITY! ID:ID! DATE/TIME:DATETIME! CALL_TAKER:SKIP! " +
                              "GRID2640:MAP! GOOGLE_MAP:EMPTY! MAP:MAP/L! XSTREETS:X! UNITS:UNIT! NARR:EMPTY INFO_BLK+ " +
                       "| Address:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! https:SKIP! Call_Date/Time:DATETIME! " +
                              "Call_Type:CALL_PRI! Fire_Area:MAP! Alerts:ALERT! Incident_Numbers:ID! Units:UNIT! Times:EMPTY! TIMES+ " +
                       ") " +
          "| CALL:CALL! ADDR:GPS! ADDR1:ADDR! ID:ID! EMPTY+? ( GRID2640:MAP! ( Date/Time:DATETIME! MAP:SKIP! UNITS:UNIT! " +
                                                                            "| MAP:SKIP! UNITS:UNIT! Date/Time:DATETIME! " +
                                                                            ") " +
                                                            "| Date/Time:DATETIME GRID2640:MAP? MAP:SKIP! UNITS:UNIT! " +
                                                            "| MAP:SKIP! UNITS:UNIT! Date/Time:DATETIME! " +
                                                            "| DATE:DATETIME! MAP:SKIP! UNIT:UNIT! " +
                                                            ") INFO/N+ " +
         ")");
  }

  @Override
  public String getFilter() {
    return "FireDesk@JeffCoal911.org,dispatcher@jeffcoal911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public MapPageStatus getMapPageStatus() {
    return MapPageStatus.ANY;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (subject.startsWith("Automatic R&R Notification:")) {
      setSelectValue("H1");
      return super.parseHtmlMsg(subject, body, data);
    } else {
      return parseMsg(subject, body, data);
    }
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("D I S P A T C H")) {
      if (body.startsWith("CALL:")) {
        subject = "ACTIVE 9-1-1";
      } else {
        setSelectValue("2");
        return parseFields(body.split("\n"), data);
      }
    }
    if (subject.equals("ACTIVE 9-1-1")) {
      setSelectValue("1");
      body = body.replace("GRID2640:", "\nGRID2640:");
      return parseFields(body.split("\n"), data);
    }

    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("CALL_PRI")) return new MyCallPriorityField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("DATETIME2")) return new DateTimeField("(\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d) #\\d+", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO"))  return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern MAP_URL_PTN = Pattern.compile("https:.*/MapPage_(.*)\\.pdf");
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_URL_PTN.matcher(field);
      if (match.matches()) {
        data.mapPageURL = field;
        data.strMap = match.group(1);
      } else {
        data.strMap = field;
      }
    }
  }

  private static final Pattern CALL_PRIORITY_PTN = Pattern.compile("([^,]+)[, ]+Call Priority: *([^,]*)[, ]*");
  private class MyCallPriorityField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PRIORITY_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1).trim();
      data.strPriority = match.group(2).trim();
    }

    @Override
    public String getFieldNames() {
      return "CALL PRI";
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
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "NARR:");
      super.parse(field, data);
    }
  }
}
