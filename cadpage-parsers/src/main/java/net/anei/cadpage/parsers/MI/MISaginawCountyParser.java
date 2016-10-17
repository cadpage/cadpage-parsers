package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MISaginawCountyParser extends FieldProgramParser {
  
  public MISaginawCountyParser() {
    super("SAGINAW COUNTY", "MI",
           "LOC:ADDR! DESC:INFO! APT:APT? TYP:CALL!");
  }
  
  @Override
  public String getFilter() {
    return "27538,34292";
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("LAT: <([+-]?[\\d\\.]+)> +LONG: <([+-]?[\\d\\.]+)>");
  private class MyInfoField extends InfoField {
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.find()) {
        setGPSLoc(match.group(1) + "," + match.group(2), data);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "INFO GPS";
    }
  }
  
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("*M*")) field = field.substring(3).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
}
