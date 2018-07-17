package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * E Feliciana Parish, LA
 */
public class LAEastFelicianaParishParser extends FieldProgramParser {
  
  
  public LAEastFelicianaParishParser() {
    super("EAST FELICIANA PARISH", "LA",
          "( SELECT/2 SRC! dispatched:CALL! Address:ADDR! | Address:ADDR! Type:CALL!  ) Subtype:CALL/SDS? Incident_ID:ID! Time/Date:DATETIME!");
  }
  
  @Override
  public String getFilter() {
    return "noreply@emergencycallworx.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("E911 Unit Dispatched Notification")) {
      setSelectValue("2");
    }
    else if (subject.equals("E911 Station Dispatch Notification")) {
      setSelectValue("1");
    } else {
      return false;
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  
  private class MyDateTimeField extends DateTimeField {
    public MyDateTimeField() {
      super("\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace('-', '/');
      super.parse(field, data);
    }
  }
}
