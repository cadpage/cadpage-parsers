package net.anei.cadpage.parsers.MO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOAndrewCountyParser extends FieldProgramParser {
  
  public MOAndrewCountyParser() {
    super("ANDREW COUNTY", "MO", 
          "CALL:CALL! ID:ID! PLACE:PLACE! ADDR:ADDR! CITY:CITY! UNIT:UNIT! INFO:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "cad@academs.org";
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\[Paged (\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)\\]$");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss hh");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD ALERT PROGRAM")) return false;
    
    Matcher match = DATE_TIME_PTN.matcher(body);
    if (!match.find()) return false;
    data.strDate = match.group(1);
    setTime(TIME_FMT, match.group(2), data);
    
    int pt = body.lastIndexOf("\n[PSAP");
    if (pt < 0) return false;
    body = body.substring(0,pt).trim();
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Scene")) return;
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("DUTY")) return;
      super.parse(field, data);
    }
  }
}
