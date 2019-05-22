package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAFranklinCountyCParser extends FieldProgramParser {

  public VAFranklinCountyCParser() {
    super("FRANKLIN COUNTY", "VA", 
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! CITY:CITY! XY:GPS? ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! X:X? INFO:INFO/N+");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d.\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" ", "");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '/');
      super.parse(field, data);
    }
  }
}
