package net.anei.cadpage.parsers.CO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COLarimerCountyBParser extends FieldProgramParser {

  public COLarimerCountyBParser() {
    super("LARIMER COUNTY", "CO", "ID! CALL! ADDR! INFO+ NAME:NAME PH:PHONE SOURCE:SKIP");
  }
  
  private static Pattern TO_COMMA = Pattern.compile("\\.TEXT:|\\\\|\\.?,");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(TO_COMMA.split(body), data);
  }
  
  @Override
  public Field getField(String  name) { 
    if (name.equals("ID")) return new IdField("\\d{5}",true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField { 
    
    @Override 
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, ", ", field);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" R:");
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
  
}
