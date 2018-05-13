package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALJeffersonCountyJParser extends FieldProgramParser {
    
  public ALJeffersonCountyJParser() {
    super("JEFFERSON COUNTY", "AL", 
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! CITY:CITY! XY:GPS? ID:ID! PRI:PRI! DATE:DATE? TIME:TIME! X:X? UNIT:UNIT? INFO:INFO/N+"); 
  }
  
  @Override
  public String getFilter() {
    return "administrator@trussville.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@',  '/');
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
