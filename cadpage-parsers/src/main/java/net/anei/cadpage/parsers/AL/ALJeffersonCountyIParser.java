package net.anei.cadpage.parsers.AL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser.MapPageStatus;

public class ALJeffersonCountyIParser extends FieldProgramParser {

  public ALJeffersonCountyIParser() {
    super("JEFFERSON COUNTY", "AL",
          "CALL:CALL! ADDR:GPS! ADDR1:ADDR! ID:ID! GRID2640:MAP? ( Date/Time:DATETIME MAP:SKIP! UNITS:UNIT! INFO/N+ " +
                                                                "| MAP:SKIP! UNITS:UNIT! ( Date/Time:DATETIME! INFO/N+ " +
                                                                                        "| INFO/N+? Date/Time:DATETIME! END ) )");
  }

  @Override
  public String getFilter() {
    return "FireDesk@JeffCoal911.org";
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
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("ACTIVE 9-1-1")) return false;
    body = body.replace(" GRID2640:", "\nGRID2640:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
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
