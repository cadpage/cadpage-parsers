package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COElPasoCountyBParser extends FieldProgramParser {

  public COElPasoCountyBParser() {
    super("EL PASO COUNTY", "CO",
          "CODE_UNIT ADDR PLACE? MAP_TIME CALL ID!");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("77777711*")) return false;
    body = body.substring(9).trim();
    return super.parseFields(body.split("\n"), 5, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_UNIT")) return new MyCodeUnitField();
    if (name.equals("MAP_TIME")) return new MyMapTimeField();
    if (name.equals("ID")) return new IdField("Report +(.*)", true);
    return super.getField(name);
  }
  
  private static final Pattern CODE_UNIT_PTN = Pattern.compile("([^ ]+) ([^ ]+)");
  private class MyCodeUnitField extends Field {
    @Override 
    public void parse(String field, Data data) {
      Matcher  match = CODE_UNIT_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strUnit = match.group(2);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE UNIT";
    }
  }
  
  private static final Pattern MAP_TIME_PTN = Pattern.compile("Map ([A-Z]\\d{1,2}) (\\d\\d:\\d\\d:\\d\\d)");
  private class MyMapTimeField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override 
    public boolean checkParse(String field, Data data) {
      Matcher  match = MAP_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strMap = match.group(1);
      data.strTime = match.group(2);
      return true;
    }
    
    @Override 
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "MAP TIME";
    }
  }
  
}
