package net.anei.cadpage.parsers.LA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * E Feliciana Parish, LA
 */
public class LAEastFelicianaParishParser extends FieldProgramParser {
  
  
  public LAEastFelicianaParishParser() {
    super("EAST FELICIANA PARISH", "LA",
          "Address:ADDR! Type:CALL! Subtype:CALL/SDS? Incident_ID:ID! Time/Date:DATETIME!");
  }
  
  @Override
  public String getFilter() {
    return "noreply@emergencycallworx.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("E911 Station Dispatch Notification")) return false;
    return parseFields(body.split("\n"), 4
        , data);
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
