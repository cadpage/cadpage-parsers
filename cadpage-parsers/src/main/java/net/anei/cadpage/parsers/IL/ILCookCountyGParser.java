package net.anei.cadpage.parsers.IL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILCookCountyGParser extends FieldProgramParser {
  
  public ILCookCountyGParser() {
    super("COOK COUNTY", "IL", 
          "CALL:CALL! ADDR:ADDR! CITY:CITY! UNITS:UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "cad@orlandfire.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String field, Data data) {
    if (!subject.equals("CAD")) return false;
    return super.parseMsg(field, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+) *- +(.*)");
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strCall = match.group(2);
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
