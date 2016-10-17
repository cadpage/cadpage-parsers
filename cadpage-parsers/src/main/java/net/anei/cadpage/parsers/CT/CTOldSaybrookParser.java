package net.anei.cadpage.parsers.CT;


import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Old Saybrook, CT
 */
public class CTOldSaybrookParser extends FieldProgramParser {
  
  public CTOldSaybrookParser() {
    super("OLD SAYBROOK","CT",
          "CALL:CALL! ADDR:ADDR! ADDR2:APT! CITY:CITY! ID:ID! PRI:PRI! ZONE:MAP! PLACE:PLACE! GPS:GPS! X:X! DATE:DATE! TIME:TIME!");
  }
  
  @Override
  public String getFilter() {
    return "noreply@oldsaybrook911.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{9,}", true);
    if (name.equals("PRI")) return new PriorityField("(\\d*)-.*");
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
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
