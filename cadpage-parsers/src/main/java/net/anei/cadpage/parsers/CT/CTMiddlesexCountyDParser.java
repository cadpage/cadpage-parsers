package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTMiddlesexCountyDParser extends FieldProgramParser {

  public CTMiddlesexCountyDParser() {
    super("MIDDLESEX COUNTY", "CT",
          "CALL:CALL! ADDR:ADDR! ADDR2:APT! CITY:CITY! ID:ID! PRI:PRI! ZONE:MAP! PLACE:PLACE! GPS:GPS! X:X! DATE:DATE! TIME:TIME! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "noreply@oldsaybrook911.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('-');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

}
