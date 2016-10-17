package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTHartfordCountyEastGranbyParser extends FieldProgramParser {

  public CTHartfordCountyEastGranbyParser() {
    super("HARTFORD COUNTY", "CT", 
          "CALL CALL ADDR CITY ST DATETIME ID!");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ST")) return new MyStateField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override 
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }
  
  private class MyStateField extends StateField {
    @Override 
    public void parse(String field, Data data) {
      if (!field.equals("CT")) data.strState = field;
    }
  }
  
  private class MyDateTimeField extends DateTimeField {
    @Override 
    public void parse(String field, Data data) {
      super.parse(field.replace("-",  "/"), data);
    }
  }
}
