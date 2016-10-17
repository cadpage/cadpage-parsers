package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COChaffeeCountyParser extends FieldProgramParser {
  
  
  public COChaffeeCountyParser() {
    super("CHAFFEE COUNTY", "CO",
          "Call:ID! Event:CALL! Location:ADDR! City:CITY! Caller:NAME Phone:PHONE");
  }
  
  @Override
  public String getFilter() {
    return "noreply@chaffeesheriff.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch CAD Call")) return false;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private static final Pattern CODE_CALL_PTN = Pattern.compile("([-A-Z]+)-(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2).trim();
      }
      data.strCall = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
