package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COBroomfieldCountyParser extends FieldProgramParser {

  public COBroomfieldCountyParser() {
    super("BROOMFIELD COUNTY", "CO", 
          "ADDRCITY X CALL INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "sbarnes@broomfield.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Email Copy Message From Hiplink")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      field = stripFieldStart(field, "&");
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
}
