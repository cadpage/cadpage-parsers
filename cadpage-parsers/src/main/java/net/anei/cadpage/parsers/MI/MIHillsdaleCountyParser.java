package net.anei.cadpage.parsers.MI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Hillsdale County, MI
 */
public class MIHillsdaleCountyParser extends FieldProgramParser {
  
  public MIHillsdaleCountyParser() {
    super("HILLSDALE COUNTY", "MI", 
          "CALL ADDR ( X | CITY ST_ZIP? X ) DATETIME!");
  }
  
  @Override
  public String getFilter() {
    return "hccd@co.hillsdale.mi.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split(","), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ST_ZIP")) return new StateField("([A-Z]{2})(?: +\\d{5})?", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    
    public MyCrossField() {
      super(".*//.*", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d [AP]M)");
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
}
