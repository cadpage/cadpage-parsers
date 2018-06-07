package net.anei.cadpage.parsers.MA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MASussexCountyParser extends FieldProgramParser {
  
  public MASussexCountyParser() {
    super("SUSSEX COUNTY", "MA", 
          "CALL DATETIME! MAP! PLACE? ADDR/Z CITY_ST! END");
  }
  
  @Override
  public String getFilter() {
    return "paging.oem@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Chelsea 911")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("MAP")) return new MapField("District: *(.*)");
    if (name.equals("CITY_ST")) return new MyCityStateField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d[AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mmaa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }
  
  private static final Pattern CITY_ST_PTN = Pattern.compile("([ A-Z]+), *([A-Z]{2})");
  private class MyCityStateField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_ST_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCity = match.group(1).trim();
      data.strState = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

}
