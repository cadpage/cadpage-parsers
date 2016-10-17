package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Madison County (Alexandria), IN
 */
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA26Parser extends FieldProgramParser {
  
  public DispatchA26Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }
  
  public DispatchA26Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "Inc#:ID Unit:UNIT UnitSts:SKIP Known_as:PLACE Loc:ADDR! Venue:CITY Between:X Desc:CALL Inc:CODE! Date:DATE! Time:TIME Addtl:INFO Caller:NAME");
  }
  
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) {
        data.strCall = field;
      } else {
        data.strCode = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }
  
  private class MyTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get(' '), data);
      data.strSupp = p.get();
    }
    
    @Override
    public String getFieldNames() {
      return "TIME INFO";
    }
  }

  private static final Pattern NAME_COMMAS_PTN = Pattern.compile(" *,+$");
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_COMMAS_PTN.matcher(field);
      if (match.find()) field = field.substring(0,match.start());
      super.parse(field, data);
      
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
}
